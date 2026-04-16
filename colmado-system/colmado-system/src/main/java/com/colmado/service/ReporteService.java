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

    private Connection getCon() {
        return ConexionDB.getInstancia().getConexion();
    }

    public List<String[]> getVentasHoy() {
        List<String[]> filas = new ArrayList<>();
        // CORRECCIÓN: Nombres de columnas según tu DB
        String sql = """
                SELECT v.id_venta, c.nombre AS cliente, v.total, v.fecha
                FROM ventas v
                LEFT JOIN clientes c ON v.id_cliente = c.id_cliente
                WHERE DATE(v.fecha) = CURDATE()
                ORDER BY v.fecha DESC
                """;
        try (Statement st = getCon().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                filas.add(new String[]{
                        String.valueOf(rs.getInt(1)),
                        rs.getString("cliente") != null ? rs.getString("cliente") : "Sin cliente",
                        String.format("RD$ %.2f", rs.getDouble("total")),
                        rs.getString("fecha")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return filas;
    }

    public List<String[]> getProductosMasVendidos() {
        List<String[]> filas = new ArrayList<>();
        // CORRECCIÓN: p.id_producto y dv.precio_unit
        String sql = """
                SELECT p.nombre, SUM(dv.cantidad) AS total_vendido,
                       SUM(dv.cantidad * dv.precio_unit) AS ingreso
                FROM detalle_venta dv
                JOIN productos p ON dv.id_producto = p.id_producto
                GROUP BY p.id_producto, p.nombre
                ORDER BY total_vendido DESC
                LIMIT 10
                """;
        try (Statement st = getCon().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                filas.add(new String[]{
                        rs.getString("nombre"),
                        String.valueOf(rs.getInt("total_vendido")),
                        String.format("RD$ %.2f", rs.getDouble("ingreso"))
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return filas;
    }

    public double getTotalVentasHoy() {
        String sql = "SELECT COALESCE(SUM(total),0) FROM ventas WHERE DATE(fecha) = CURDATE()";
        try (Statement st = getCon().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public int getCantidadVentasHoy() {
        String sql = "SELECT COUNT(*) FROM ventas WHERE DATE(fecha) = CURDATE()";
        try (Statement st = getCon().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    // Métodos de exportación restaurados para el Panel
    public String exportarVentasHoyCSV(String r) { return exportarCSV(getVentasHoy(), r, "ID,Cliente,Total,Fecha"); }
    public String exportarProductosCSV(String r) { return exportarCSV(getProductosMasVendidos(), r, "Producto,Cant,Ingreso"); }

    private String exportarCSV(List<String[]> datos, String ruta, String header) {
        try (FileWriter fw = new FileWriter(ruta)) {
            fw.write(header + "\n");
            for (String[] f : datos) { fw.write(String.join(",", f) + "\n"); }
            return null;
        } catch (IOException e) { return e.getMessage(); }
    }

    public String exportarReportePDF(String ruta) {
        // ... (Aquí va tu código original de iTextPDF, asegurándote de no cerrar la conexión)
        return null;
    }
}