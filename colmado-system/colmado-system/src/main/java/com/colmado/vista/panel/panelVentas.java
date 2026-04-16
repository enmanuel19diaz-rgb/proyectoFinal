package com.colmado.vista.panel;

import com.colmado.modelo.*;
import com.colmado.service.*;
import com.colmado.dao.ProductoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class panelVentas extends JPanel {
    private final VentaService ventaService;
    private final InventarioService inventarioService;
    private final ProductoDAO productoDAO;
    private Producto productoSeleccionado;

    private JTextField txtBuscador, txtCantidad, txtPagoCon;
    private JPopupMenu menuSugerencias;
    private JList<Producto> listaSugerencias;
    private DefaultListModel<Producto> modeloLista;
    private JLabel lblPrecioInfo, lblTotal, lblCambioRealTime;
    private JTable tablaCarrito;
    private DefaultTableModel modeloTabla;

    private final Color azulOscuro = new Color(13, 27, 42);
    private final Color azulAcento = new Color(65, 105, 225);
    private final Color Blanco = Color.WHITE;

    public panelVentas(Usuario u, panelFactura pf, CardLayout cl, JPanel pc) {
        this.ventaService = new VentaService();
        this.inventarioService = new InventarioService();
        this.productoDAO = new ProductoDAO();
        initComponents(pf, cl, pc);
    }

    private void initComponents(panelFactura pf, CardLayout cl, JPanel pc) {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));

        // --- BUSCADOR ---
        txtBuscador = new JTextField(20);
        modeloLista = new DefaultListModel<>();
        listaSugerencias = new JList<>(modeloLista);
        menuSugerencias = new JPopupMenu();
        JScrollPane scrollSugerencias = new JScrollPane(listaSugerencias);
        scrollSugerencias.setPreferredSize(new Dimension(250, 150));
        menuSugerencias.add(scrollSugerencias);
        menuSugerencias.setFocusable(false);

        JPanel panelEntrada = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelEntrada.setBackground(Blanco);
        lblPrecioInfo = new JLabel("Precio: RD$ 0.00");
        txtCantidad = new JTextField(5);
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(azulAcento); btnAgregar.setForeground(Blanco);

        panelEntrada.add(new JLabel("🔍 Producto:")); panelEntrada.add(txtBuscador);
        panelEntrada.add(new JLabel("Cant:")); panelEntrada.add(txtCantidad);
        panelEntrada.add(lblPrecioInfo); panelEntrada.add(btnAgregar);

        // --- TABLA ---
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Descripción", "Cantidad", "Precio", "Subtotal"}, 0);
        tablaCarrito = new JTable(modeloTabla);
        add(panelEntrada, BorderLayout.NORTH);
        add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        // --- PIE DE COBRO ---
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(azulOscuro);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblTotal = new JLabel("Total: RD$ 0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(Blanco);

        lblCambioRealTime = new JLabel("Cambio: RD$ 0.00");
        lblCambioRealTime.setForeground(new Color(255, 215, 0));

        JPanel pLabels = new JPanel(new GridLayout(2, 1));
        pLabels.setOpaque(false);
        pLabels.add(lblTotal); pLabels.add(lblCambioRealTime);

        txtPagoCon = new JTextField(8);
        JButton btnCobrar = new JButton("Cobrar y Facturar");
        btnCobrar.setBackground(new Color(34, 139, 34)); btnCobrar.setForeground(Blanco);

        JPanel pAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        pAcciones.setOpaque(false);
        pAcciones.add(new JLabel("<html><font color='white'>Paga con RD$:</font></html>"));
        pAcciones.add(txtPagoCon);
        pAcciones.add(btnCobrar);

        panelInferior.add(pLabels, BorderLayout.WEST);
        panelInferior.add(pAcciones, BorderLayout.EAST);
        add(panelInferior, BorderLayout.SOUTH);

        // --- EVENTOS ---
        txtBuscador.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String t = txtBuscador.getText().trim();
                if (t.length() >= 2) {
                    modeloLista.clear();
                    List<Producto> sug = productoDAO.obtenerTodos().stream()
                            .filter(p -> p.getNombre().toLowerCase().contains(t.toLowerCase()))
                            .collect(Collectors.toList());
                    sug.forEach(modeloLista::addElement);
                    if (!sug.isEmpty()) menuSugerencias.show(txtBuscador, 0, txtBuscador.getHeight());
                }
            }
        });

        listaSugerencias.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                productoSeleccionado = listaSugerencias.getSelectedValue();
                if (productoSeleccionado != null) {
                    txtBuscador.setText(productoSeleccionado.getNombre());
                    lblPrecioInfo.setText("Precio: RD$ " + productoSeleccionado.getPrecio());
                    menuSugerencias.setVisible(false);
                    txtCantidad.requestFocus();
                }
            }
        });

        btnAgregar.addActionListener(e -> {
            if (productoSeleccionado == null) return;
            try {
                int c = Integer.parseInt(txtCantidad.getText().trim());
                if (inventarioService.hayStock(productoSeleccionado.getId_producto(), c)) {
                    ventaService.agregarProducto(productoSeleccionado.getId_producto(), c, productoSeleccionado.getPrecio());
                    actualizarCarrito();
                    limpiarEntrada();
                } else { JOptionPane.showMessageDialog(null, "No hay stock."); }
            } catch (Exception ex) { JOptionPane.showMessageDialog(null, "Cantidad inválida."); }
        });

        txtPagoCon.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try {
                    double pago = txtPagoCon.getText().isEmpty() ? 0 : Double.parseDouble(txtPagoCon.getText());
                    lblCambioRealTime.setText(String.format("Cambio: RD$ %.2f", (pago - ventaService.getTotal())));
                } catch (Exception ex) { }
            }
        });

        btnCobrar.addActionListener(e -> {
            try {
                double pago = Double.parseDouble(txtPagoCon.getText().trim());
                double total = ventaService.getTotal();

                if (pago < total) {
                    JOptionPane.showMessageDialog(null, "Dinero insuficiente. Faltan RD$ " + (total - pago));
                    return;
                }

                Venta v = ventaService.confirmarVenta(1);
                if (v != null) {
                    pf.cargarVentaConCambio(v, pago, (pago - total));
                    cl.show(pc, "factura");
                    vaciarTodo();
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(null, "Monto de pago inválido."); }
        });
    }

    private void actualizarCarrito() {
        modeloTabla.setRowCount(0);
        ventaService.getCarrito().forEach(d -> {
            Producto p = productoDAO.obtenerPorId(d.getId_producto());
            modeloTabla.addRow(new Object[]{d.getId_producto(), p.getNombre(), d.getCantidad(), d.getPrecioUnit(), d.getSubtotal()});
        });
        lblTotal.setText(String.format("Total: RD$ %.2f", ventaService.getTotal()));
    }

    private void limpiarEntrada() {
        txtBuscador.setText(""); txtCantidad.setText("");
        lblPrecioInfo.setText("Precio: RD$ 0.00"); productoSeleccionado = null;
        txtBuscador.requestFocus();
    }

    private void vaciarTodo() {
        limpiarEntrada(); txtPagoCon.setText("");
        actualizarCarrito(); lblCambioRealTime.setText("Cambio: RD$ 0.00");
    }
}