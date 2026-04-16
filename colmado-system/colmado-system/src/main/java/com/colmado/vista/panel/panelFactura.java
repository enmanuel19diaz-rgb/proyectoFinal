package com.colmado.vista.panel;

import com.colmado.modelo.*;
import com.colmado.dao.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.*;

public class panelFactura extends JPanel {

    // Labels del Negocio
    private JLabel lblNegocio, lblRnc, lblDir, lblTel;

    // Labels de la Venta
    private JLabel lblTotal, lblPago, lblCambio, lblFactura, lblFecha;

    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnPrint;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ConfiguracionDAO configDAO = new ConfiguracionDAO();

    // Colores corporativos
    private final Color azulOscuro = new Color(13, 27, 42);
    private final Color azulAcento = new Color(65, 105, 225);
    private final Color Blanco = Color.WHITE;

    public panelFactura() {
        setLayout(new BorderLayout(0, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // --- ENCABEZADO ---
        JPanel pnlHeader = new JPanel(new GridLayout(4, 1));
        pnlHeader.setBackground(azulOscuro);
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        lblNegocio = crearLblCentral("", 24, Font.BOLD);
        lblDir = crearLblCentral("", 14, Font.PLAIN);
        lblRnc = crearLblCentral("", 14, Font.PLAIN);
        lblTel = crearLblCentral("", 14, Font.PLAIN);

        pnlHeader.add(lblNegocio);
        pnlHeader.add(lblDir);
        pnlHeader.add(lblRnc);
        pnlHeader.add(lblTel);

        // --- INFORMACIÓN DE VENTA ---
        JPanel pnlInfoVenta = new JPanel(new BorderLayout());
        pnlInfoVenta.setOpaque(false);
        lblFactura = new JLabel("Factura #: ");
        lblFactura.setFont(new Font("Segoe UI", Font.BOLD, 14));

        lblFecha = new JLabel("Fecha: ");
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        pnlInfoVenta.add(lblFactura, BorderLayout.WEST);
        pnlInfoVenta.add(lblFecha, BorderLayout.EAST);

        JPanel norteContenedor = new JPanel(new BorderLayout(0, 15));
        norteContenedor.setOpaque(false);
        norteContenedor.add(pnlHeader, BorderLayout.NORTH);
        norteContenedor.add(pnlInfoVenta, BorderLayout.SOUTH);
        add(norteContenedor, BorderLayout.NORTH);

        // --- TABLA DE PRODUCTOS ---
        modelo = new DefaultTableModel(new String[]{"Producto", "Cant.", "Precio", "Subtotal"}, 0);
        tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(new Color(230, 230, 230));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        add(scroll, BorderLayout.CENTER);

        // --- PIE DE PÁGINA: TOTALES Y BOTÓN ---
        JPanel sur = new JPanel(new BorderLayout());
        sur.setBackground(azulOscuro);
        sur.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Panel de montos
        JPanel pnlTotales = new JPanel(new GridLayout(3, 1));
        pnlTotales.setOpaque(false);

        lblTotal = crearLblIzquierda("TOTAL: RD$ 0.00", 20, Font.BOLD);
        lblPago = crearLblIzquierda("RECIBIDO: RD$ 0.00", 14, Font.PLAIN);
        lblCambio = crearLblIzquierda("CAMBIO: RD$ 0.00", 16, Font.BOLD);
        lblCambio.setForeground(new Color(255, 235, 59)); // Amarillo vibrante

        pnlTotales.add(lblTotal);
        pnlTotales.add(lblPago);
        pnlTotales.add(lblCambio);

        // BOTÓN IMPRIMIR MODERNO
        btnPrint = new JButton("IMPRIMIR FACTURA");
        btnPrint.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnPrint.setBackground(azulAcento);
        btnPrint.setForeground(Blanco);
        btnPrint.setFocusPainted(false);
        btnPrint.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPrint.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));

        // Efecto Hover
        btnPrint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btnPrint.setBackground(new Color(30, 144, 255)); }
            public void mouseExited(java.awt.event.MouseEvent e) { btnPrint.setBackground(azulAcento); }
        });

        btnPrint.addActionListener(e -> imprimir());

        sur.add(pnlTotales, BorderLayout.WEST);
        sur.add(btnPrint, BorderLayout.EAST);
        add(sur, BorderLayout.SOUTH);
    }

    public void cargarVentaConCambio(Venta v, double pago, double cambio) {
        // Cargar datos actuales del negocio
        Configuracion config = configDAO.obtenerConfiguracion();
        if (config != null) {
            lblNegocio.setText(config.getNombre().toUpperCase());
            lblRnc.setText("RNC: " + config.getRnc());
            lblDir.setText(config.getDireccion());
            lblTel.setText("TEL: " + config.getTelefono());
        }

        lblFactura.setText("Factura #: " + v.getId_venta());
        lblFecha.setText("Fecha: " + v.getFecha());
        lblTotal.setText("TOTAL: RD$ " + String.format("%.2f", v.getTotal()));
        lblPago.setText("RECIBIDO: RD$ " + String.format("%.2f", pago));
        lblCambio.setText("CAMBIO: RD$ " + String.format("%.2f", cambio));

        modelo.setRowCount(0);
        v.getDetalle().forEach(d -> {
            Producto p = productoDAO.obtenerPorId(d.getId_producto());
            String nombre = (p != null) ? p.getNombre() : "Producto ID: " + d.getId_producto();
            modelo.addRow(new Object[]{nombre, d.getCantidad(), d.getPrecioUnit(), d.getSubtotal()});
        });
    }

    private void imprimir() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Factura_" + lblFactura.getText());

        job.setPrintable((Graphics g, PageFormat pf, int pi) -> {
            if (pi > 0) return Printable.NO_SUCH_PAGE;

            Graphics2D g2d = (Graphics2D) g;

            // --- CONFIGURACIÓN DE ALTA CALIDAD ---
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            g2d.translate(pf.getImageableX(), pf.getImageableY());

            // Escalar para que se vea nítido en el papel (ajustado al ancho)
            double scale = pf.getImageableWidth() / this.getWidth();
            g2d.scale(scale, scale);

            // Ocultar botón antes de imprimir para que la factura salga limpia
            btnPrint.setVisible(false);
            this.printAll(g2d);
            btnPrint.setVisible(true);

            return Printable.PAGE_EXISTS;
        });

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Error al imprimir: " + ex.getMessage());
            }
        }
    }

    // Métodos auxiliares para Labels
    private JLabel crearLblCentral(String t, int tam, int estilo) {
        JLabel l = new JLabel(t, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", estilo, tam));
        l.setForeground(Blanco);
        return l;
    }

    private JLabel crearLblIzquierda(String t, int tam, int estilo) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", estilo, tam));
        l.setForeground(Blanco);
        return l;
    }
}