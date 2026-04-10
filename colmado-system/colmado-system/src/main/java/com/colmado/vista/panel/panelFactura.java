package com.colmado.vista.panel;

import com.colmado.modelo.Venta;
import com.colmado.modelo.DetalleVenta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class panelFactura extends JPanel {

    private Venta venta;

    private final Color azulOscuro = new Color(13, 27, 42);
    private final Color azulMedio = new Color(27,38,59);
    private final Color azulAcento = new Color(65,105,225);
    private final Color Blanco = Color.WHITE;
    private final Color grisClaro = new Color(200, 210, 220);
    private final Color fondo = new Color(240, 242,245);


    private JLabel lblNumFactura;
    private JLabel lblFecha;
    private JLabel lblCliente;
    private JLabel lblTotal;
    private JTable tablaItems;
    private DefaultTableModel modeloTabla;

    public panelFactura(){
        setLayout(new BorderLayout());
        setBackground(fondo);
        mostrarVacio();
    }

    public void cargarVenta(Venta venta){
        this.venta = venta;
        removeAll();
        initComponents();
        revalidate();
        repaint();
    }

    private void mostrarVacio(){
        setLayout(new GridBagLayout());
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);

        JLabel icono = new JLabel("🧾", SwingConstants.CENTER);
        icono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        icono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel("Sin Factura activa", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(60,60,60));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Completa una venta para ver la factura aqui.", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(Color.GRAY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        contenido.add(icono);
        contenido.add(Box.createVerticalStrut(10));
        contenido.add(titulo);
        contenido.add(Box.createVerticalStrut(6));
        contenido.add(sub);
        add(contenido);
    }

    private void initComponents() {
        setLayout(new BorderLayout(0,0));
        setBackground(fondo);

        JPanel contenedor = new JPanel(new BorderLayout(0, 16));
        contenedor.setOpaque(false);
        contenedor.setBorder(new EmptyBorder(20, 40, 20, 40));

        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setBackground(azulOscuro);
        encabezado.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel lblNegocio = new JLabel("COLMADO EL BUEN PRECIO");
        lblNegocio.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNegocio.setForeground(Blanco);
        lblNegocio.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDireccion = new JLabel("Santo Domingo, RD | Tel: 809-000-0000");
        lblDireccion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDireccion.setForeground(grisClaro);
        lblDireccion.setAlignmentX(Component.LEFT_ALIGNMENT);

        encabezado.add(lblNegocio);
        encabezado.add(Box.createVerticalStrut(4));
        encabezado.add(lblDireccion);


        JPanel infoPanel = new JPanel(new GridLayout(1,3,16,0));
        infoPanel.setOpaque(false);

        lblNumFactura = new JLabel();
        lblFecha = new JLabel();
        lblCliente = new JLabel();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        lblNumFactura.setText("Factura #: " + venta.getId_venta());
        lblFecha.setText("Fecha: " + venta.getFecha().format(fmt));
        lblCliente.setText(("Cliente ID: " + venta.getId_cliente()));

        for (JLabel lbl: new JLabel[] {lblNumFactura, lblFecha, lblCliente}){
            lbl.setFont((new Font("Segoe UI", Font.PLAIN, 13)));
            lbl.setForeground(new Color(40, 40,40));
            lbl.setBorder(new EmptyBorder(10, 14, 10, 14));
        }

        JPanel cardNum = crearCardInfo(lblNumFactura);
        JPanel cardFecha = crearCardInfo(lblFecha);
        JPanel cardCliente = crearCardInfo(lblCliente);

        infoPanel.add(cardNum);
        infoPanel.add(cardFecha);
        infoPanel.add(cardCliente);

        String[] columnas = {"Producto", "Cantidad", "Precio Unit", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        for (DetalleVenta d: venta.getDetalle()){
            modeloTabla.addRow(new Object[]{
                    "Producto #" + d.getId_producto(),
                    d.getCantidad(),
                    String.format("$%.2f", d.getPrecioUnit()),
                    String.format("$%.2f", d.getSubtotal())
            });
        }

        tablaItems = new JTable(modeloTabla);
        tablaItems.setEnabled(false);
        tablaItems.setRowHeight(28);
        tablaItems.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaItems.getTableHeader().setBackground(azulMedio);
        tablaItems.getTableHeader().setBackground(Blanco);

        JScrollPane scroll = new JScrollPane(tablaItems);
        scroll.setBorder(BorderFactory.createLineBorder(grisClaro, 1));

        JPanel panelTotal = new JPanel(new BorderLayout());
        panelTotal.setBackground(azulOscuro);
        panelTotal.setBorder(new EmptyBorder(12, 20, 12, 20));

        lblTotal = new JLabel(String.format("TOTAL:   $%.2f", venta.getTotal()));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotal.setForeground(Blanco);
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        panelTotal.add(lblTotal, BorderLayout.EAST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT,10, 0));
        panelBotones.setOpaque(false);

        JButton btnImprimir = new JButton("\uD83D\uDDA8 Imprimir");
        btnImprimir.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnImprimir.setBackground(azulAcento);
        btnImprimir.setForeground(Blanco);
        btnImprimir.setFocusPainted(false);
        btnImprimir.setBorderPainted(false);
        btnImprimir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnImprimir.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Función de impresión próximamente.\", \"Imprimir\"," +
                        " JOptionPane.INFORMATION_MESSAGE"));

        JButton btnNuevaVenta = new JButton("\uD83D\uDCB0  Nueva Venta");
        btnNuevaVenta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnNuevaVenta.setBackground(azulMedio);
        btnNuevaVenta.setForeground(Blanco);
        btnNuevaVenta.setFocusPainted(false);
        btnNuevaVenta.setBorderPainted(false);
        btnNuevaVenta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBotones.add(btnNuevaVenta);
        panelBotones.add(btnImprimir);

        //Ensamblar
        JPanel cuerpo = new JPanel(new BorderLayout(0, 12));
        cuerpo.setOpaque(false);
        cuerpo.add(infoPanel, BorderLayout.NORTH);
        cuerpo.add(scroll, BorderLayout.CENTER);
        cuerpo.add(panelTotal, BorderLayout.SOUTH);

        contenedor.add(encabezado, BorderLayout.NORTH);
        contenedor.add(cuerpo, BorderLayout.CENTER);
        contenedor.add(panelBotones, BorderLayout.SOUTH);

        add(contenedor, BorderLayout.CENTER);
    }

    private JPanel crearCardInfo(JLabel label) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Blanco);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(grisClaro, 1),
                new EmptyBorder(4, 8, 4, 8)));
        card.add(label, BorderLayout.CENTER);
        return card;
    }
}
