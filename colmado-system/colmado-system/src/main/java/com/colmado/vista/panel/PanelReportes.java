package com.colmado.vista.panel;

import com.colmado.service.ReporteService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PanelReportes extends JPanel {

    private final ReporteService service = new ReporteService();

    // ── Resumen ───────────────────────────────────────────────────────────────
    private final JLabel lblTotalHoy      = new JLabel("RD$ 0.00");
    private final JLabel lblCantidadHoy   = new JLabel("0");

    // ── Tablas ────────────────────────────────────────────────────────────────
    private final DefaultTableModel modeloVentas    = new DefaultTableModel(
            new String[]{"ID", "Cliente", "Total", "Fecha"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final DefaultTableModel modeloProductos = new DefaultTableModel(
            new String[]{"Producto", "Cant. Vendida", "Ingreso Total"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };

    private final JTable tablaVentas    = new JTable(modeloVentas);
    private final JTable tablaProductos = new JTable(modeloProductos);

    // ═════════════════════════════════════════════════════════════════════════
    public PanelReportes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(panelResumen(),    BorderLayout.NORTH);
        add(panelTablas(),     BorderLayout.CENTER);
        add(panelBotones(),    BorderLayout.SOUTH);

        cargarDatos();
    }

    // ── Panel resumen ─────────────────────────────────────────────────────────
    private JPanel panelResumen() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        p.setBorder(BorderFactory.createTitledBorder("Resumen del día"));

        Font fBig = new Font("SansSerif", Font.BOLD, 18);
        lblTotalHoy.setFont(fBig);
        lblTotalHoy.setForeground(new Color(39, 174, 96));
        lblCantidadHoy.setFont(fBig);
        lblCantidadHoy.setForeground(new Color(52, 152, 219));

        p.add(new JLabel("Total vendido:"));
        p.add(lblTotalHoy);
        p.add(Box.createHorizontalStrut(30));
        p.add(new JLabel("Número de ventas:"));
        p.add(lblCantidadHoy);

        JButton btnRefrescar = new JButton("🔄 Actualizar");
        btnRefrescar.addActionListener(e -> cargarDatos());
        p.add(Box.createHorizontalStrut(30));
        p.add(btnRefrescar);
        return p;
    }

    // ── Panel tablas ──────────────────────────────────────────────────────────
    private JTabbedPane panelTablas() {
        JTabbedPane tabs = new JTabbedPane();

        tablaVentas.getTableHeader().setReorderingAllowed(false);
        tablaProductos.getTableHeader().setReorderingAllowed(false);

        tabs.addTab("📋 Ventas del día",          new JScrollPane(tablaVentas));
        tabs.addTab("🏆 Productos más vendidos",  new JScrollPane(tablaProductos));
        return tabs;
    }

    // ── Panel botones ─────────────────────────────────────────────────────────
    private JPanel panelBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));

        JButton btnExportarCSVVentas    = crearBoton("📥 CSV Ventas",    new Color(52, 152, 219));
        JButton btnExportarCSVProductos = crearBoton("📥 CSV Productos", new Color(52, 152, 219));
        JButton btnExportarPDF          = crearBoton("📄 Exportar PDF",  new Color(192, 57, 43));

        btnExportarCSVVentas   .addActionListener(e -> exportarCSV("ventas"));
        btnExportarCSVProductos.addActionListener(e -> exportarCSV("productos"));
        btnExportarPDF         .addActionListener(e -> exportarPDF());

        p.add(btnExportarCSVVentas);
        p.add(btnExportarCSVProductos);
        p.add(btnExportarPDF);
        return p;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD));
        btn.setPreferredSize(new Dimension(160, 34));
        return btn;
    }

    // ── Lógica ────────────────────────────────────────────────────────────────

    private void cargarDatos() {
        // Resumen
        lblTotalHoy.setText(String.format("RD$ %.2f", service.getTotalVentasHoy()));
        lblCantidadHoy.setText(String.valueOf(service.getCantidadVentasHoy()));

        // Ventas del día
        modeloVentas.setRowCount(0);
        for (String[] fila : service.getVentasHoy()) {
            modeloVentas.addRow(fila);
        }

        // Productos más vendidos
        modeloProductos.setRowCount(0);
        for (String[] fila : service.getProductosMasVendidos()) {
            modeloProductos.addRow(fila);
        }
    }

    private void exportarCSV(String tipo) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar CSV");
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));
        chooser.setSelectedFile(new java.io.File(tipo + "_reporte.csv"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        String ruta = chooser.getSelectedFile().getAbsolutePath();
        if (!ruta.endsWith(".csv")) ruta += ".csv";

        String error = tipo.equals("ventas")
                ? service.exportarVentasHoyCSV(ruta)
                : service.exportarProductosCSV(ruta);

        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Error al exportar", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Archivo exportado correctamente:\n" + ruta,
                    "Exportación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exportarPDF() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar PDF");
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf"));
        chooser.setSelectedFile(new java.io.File("reporte_dia.pdf"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        String ruta = chooser.getSelectedFile().getAbsolutePath();
        if (!ruta.endsWith(".pdf")) ruta += ".pdf";

        String error = service.exportarReportePDF(ruta);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Error al exportar", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "PDF generado correctamente:\n" + ruta,
                    "Exportación exitosa", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}