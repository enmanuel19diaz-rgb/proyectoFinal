package com.colmado.service;

import com.colmado.util.ConexionDB;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReporteService {

    // ══════════════════════════════════════════════════════════════════════════
    //  DATOS PARA REPORTES
    // ══════════════════════════════════════════════════════════════════════════

    /** Ventas del día actual */
    public List<String[]> getVentasHoy() {
        List<String[]> filas = new ArrayList<>();
        String sql = """
                SELECT v.id, c.nombre AS cliente, v.total, v.fecha
                FROM ventas v
                LEFT JOIN clientes c ON v.id_cliente = c.id
                WHERE DATE(v.fecha) = CURDATE()
                ORDER BY v.fecha DESC
                """;
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                filas.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("cliente") != null ? rs.getString("cliente") : "Sin cliente",
                        String.format("RD$ %.2f", rs.getDouble("total")),
                        rs.getString("fecha")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas;
    }

    /** Productos más vendidos (top 10) */
    public List<String[]> getProductosMasVendidos() {
        List<String[]> filas = new ArrayList<>();
        String sql = """
                SELECT p.nombre, SUM(dv.cantidad) AS total_vendido,
                       SUM(dv.cantidad * dv.precio) AS ingreso
                FROM detalle_venta dv
                JOIN productos p ON dv.id_producto = p.id
                GROUP BY p.id, p.nombre
                ORDER BY total_vendido DESC
                LIMIT 10
                """;
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                filas.add(new String[]{
                        rs.getString("nombre"),
                        String.valueOf(rs.getInt("total_vendido")),
                        String.format("RD$ %.2f", rs.getDouble("ingreso"))
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filas;
    }

    /** Resumen: total vendido hoy y número de ventas */
    public double getTotalVentasHoy() {
        String sql = "SELECT COALESCE(SUM(total),0) FROM ventas WHERE DATE(fecha) = CURDATE()";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCantidadVentasHoy() {
        String sql = "SELECT COUNT(*) FROM ventas WHERE DATE(fecha) = CURDATE()";
        try (Connection con = ConexionDB.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  EXPORTAR CSV
    // ══════════════════════════════════════════════════════════════════════════

    public String exportarVentasHoyCSV(String rutaArchivo) {
        List<String[]> ventas = getVentasHoy();
        try (FileWriter fw = new FileWriter(rutaArchivo)) {

            fw.write("ID,Cliente,Total,Fecha\n");
            for (String[] fila : ventas) {
                fw.write(String.join(",", fila) + "\n");
            }
            return null; // éxito

        } catch (IOException e) {
            e.printStackTrace();
            return "Error al exportar CSV: " + e.getMessage();
        }
    }

    public String exportarProductosCSV(String rutaArchivo) {
        List<String[]> productos = getProductosMasVendidos();
        try (FileWriter fw = new FileWriter(rutaArchivo)) {

            fw.write("Producto,Cantidad Vendida,Ingreso Total\n");
            for (String[] fila : productos) {
                fw.write(String.join(",", fila) + "\n");
            }
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return "Error al exportar CSV: " + e.getMessage();
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  EXPORTAR PDF  (iTextPDF 5)
    // ══════════════════════════════════════════════════════════════════════════

    public String exportarReportePDF(String rutaArchivo) {
        try {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(rutaArchivo));
            doc.open();

            // ── Título ────────────────────────────────────────────────────────
            Font fTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
            Font fSub    = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY);
            Font fNormal = new Font(Font.FontFamily.HELVETICA, 10);
            Font fHeader = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);

            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            Paragraph titulo = new Paragraph("Colmado — Reporte del día " + fecha, fTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(10);
            doc.add(titulo);

            // ── Resumen ───────────────────────────────────────────────────────
            double total  = getTotalVentasHoy();
            int    cantidad = getCantidadVentasHoy();

            PdfPTable resumen = new PdfPTable(2);
            resumen.setWidthPercentage(60);
            resumen.setHorizontalAlignment(Element.ALIGN_CENTER);
            resumen.setSpacingAfter(15);
            agregarCeldaResumen(resumen, "Total ventas hoy:", fSub);
            agregarCeldaResumen(resumen, String.format("RD$ %.2f", total), fNormal);
            agregarCeldaResumen(resumen, "Número de ventas:", fSub);
            agregarCeldaResumen(resumen, String.valueOf(cantidad), fNormal);
            doc.add(resumen);

            // ── Ventas del día ────────────────────────────────────────────────
            doc.add(new Paragraph("Ventas del día", fSub));
            doc.add(new Paragraph(" "));

            String[] colsVentas = {"ID", "Cliente", "Total", "Fecha"};
            PdfPTable tablaVentas = crearTabla(colsVentas, fHeader);
            for (String[] fila : getVentasHoy()) {
                for (String celda : fila) {
                    tablaVentas.addCell(new PdfPCell(new Phrase(celda, fNormal)));
                }
            }
            doc.add(tablaVentas);
            doc.add(new Paragraph(" "));

            // ── Productos más vendidos ────────────────────────────────────────
            doc.add(new Paragraph("Productos más vendidos (Top 10)", fSub));
            doc.add(new Paragraph(" "));

            String[] colsProductos = {"Producto", "Cant. Vendida", "Ingreso Total"};
            PdfPTable tablaProductos = crearTabla(colsProductos, fHeader);
            for (String[] fila : getProductosMasVendidos()) {
                for (String celda : fila) {
                    tablaProductos.addCell(new PdfPCell(new Phrase(celda, fNormal)));
                }
            }
            doc.add(tablaProductos);

            doc.close();
            return null; // éxito

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al generar PDF: " + e.getMessage();
        }
    }

    // ── Helpers PDF ───────────────────────────────────────────────────────────
    private PdfPTable crearTabla(String[] headers, Font fHeader) throws Exception {
        PdfPTable tabla = new PdfPTable(headers.length);
        tabla.setWidthPercentage(100);
        tabla.setSpacingAfter(8);
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, fHeader));
            cell.setBackgroundColor(new BaseColor(52, 73, 94));
            cell.setPadding(5);
            tabla.addCell(cell);
        }
        return tabla;
    }

    private void agregarCeldaResumen(PdfPTable tabla, String texto, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(4);
        tabla.addCell(cell);
    }
}