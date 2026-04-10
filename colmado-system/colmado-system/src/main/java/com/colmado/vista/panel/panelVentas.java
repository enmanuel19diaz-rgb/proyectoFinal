package com.colmado.vista.panel;

import com.colmado.modelo.Venta;
import com.colmado.modelo.DetalleVenta;
import com.colmado.modelo.Usuario;
import com.colmado.service.VentaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class panelVentas extends JPanel {

    private VentaService ventaService;
    private Usuario usuario;
    private panelFactura panelFactura;
    private CardLayout cardLayout;
    private JPanel panelContenido;

    // Componentes
    private JTextField txtIdProducto;
    private JTextField txtCantidad;
    private JTextField txtPrecio;
    private JButton btnAgregar;
    private JButton btnEliminar;
    private JButton btnCobrar;
    private JTable tablaCarrito;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;

    // Combinacion de colores para interface
    private final Color azulOscuro = new Color(13,27, 42);
    private final Color azulMedio = new Color(27,38,59);
    private final Color azulAcento = new Color(65,105,225);
    private final Color Blanco = Color.WHITE;
    private final Color grisClaro = new Color(200,210,220);
    private final Color Fondo = new Color(240,242,245);

    public panelVentas(Usuario usuario, panelFactura panelFactura, CardLayout cardLayout, JPanel panelContenido){
        this.usuario = usuario;
        this.panelFactura = panelFactura;
        this.cardLayout = cardLayout;
        this.panelContenido = panelContenido;
        this.ventaService = new VentaService();
        initComponents();
    }

    private void initComponents(){
        setLayout(new BorderLayout(10, 10));
        setBackground(Fondo);
        setBorder(BorderFactory.createEmptyBorder(16,20,16,20));


        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);

        JLabel lblTitulo = new JLabel("\uD83D\uDCB0  Nueva Venta");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD,20));
        lblTitulo.setForeground(azulOscuro);
        panelTitulo.add(lblTitulo, BorderLayout.WEST);
        add(panelTitulo, BorderLayout.NORTH);

        JPanel panelEntrada = new JPanel();
        panelEntrada.setLayout(new BoxLayout(panelEntrada, BoxLayout.X_AXIS));
        panelEntrada.setBackground(Blanco);
        panelEntrada.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(grisClaro, 1), (BorderFactory.createEmptyBorder(10, 14, 10, 14))
        ));

        panelEntrada.add(new JLabel("ID Producto: "));
        txtIdProducto = new JTextField(8);
        estilizarCampo(txtIdProducto);
        panelEntrada.add(txtIdProducto);

        panelEntrada.add(Box.createHorizontalStrut(10));
        panelEntrada.add(new JLabel("Cantidad: "));
        txtCantidad = new JTextField(5);
        estilizarCampo(txtCantidad);
        panelEntrada.add(txtCantidad);

        panelEntrada.add(Box.createHorizontalStrut(10));
        panelEntrada.add(new JLabel("Precio Unit: "));
        txtPrecio = new JTextField(7);
        estilizarCampo(txtPrecio);
        panelEntrada.add(txtPrecio);

        panelEntrada.add(Box.createHorizontalStrut(14));
        btnAgregar = crearBoton("Agregar", azulAcento);
        panelEntrada.add(btnAgregar);

        //Panel Central
        String[] columns = {"ID Producto", "Cantidad", "Precio Unit", "Subtotal"};
        modeloTabla = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tablaCarrito = new JTable(modeloTabla);
        tablaCarrito.setRowHeight(28);
        tablaCarrito.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCarrito.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaCarrito.getTableHeader().setBackground(azulMedio);
        tablaCarrito.getTableHeader().setForeground(Blanco);

        JScrollPane scroll = new JScrollPane(tablaCarrito);
        scroll.setBorder(BorderFactory.createLineBorder(grisClaro,1 ));

        JPanel panelCentro = new JPanel(new BorderLayout(0, 10));
       panelCentro.setOpaque(false);
       panelCentro.add(panelEntrada, BorderLayout.NORTH);
       panelCentro.add(scroll, BorderLayout.CENTER);
       add(panelCentro, BorderLayout.CENTER);

       JPanel panelInferior = new JPanel(new BorderLayout());
       panelInferior.setBackground(azulOscuro);
       panelInferior.setBorder(BorderFactory.createEmptyBorder(12,16,12,16));

       lblTotal = new JLabel("Total: $0.00");
       lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
       lblTotal.setForeground(Blanco);
       panelInferior.add(lblTotal, BorderLayout.WEST);

       JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
       panelBotones.setOpaque(false);
       btnEliminar = crearBoton("Eliminar", new Color(180, 40,40));
       btnCobrar = crearBoton("Cobrar", new Color(34,139,34));
       panelBotones.add(btnEliminar);
       panelBotones.add(btnCobrar);
       panelInferior.add(panelBotones, BorderLayout.EAST);
       add(panelInferior, BorderLayout.SOUTH);

       btnAgregar.addActionListener(e -> {
           try{
               int idProducto = Integer.parseInt(txtIdProducto.getText().trim());
               int cantidad = Integer.parseInt(txtCantidad.getText().trim());
               double precio = Double.parseDouble(txtPrecio.getText().trim());

               if (cantidad <= 0 || precio <= 0) {
                   JOptionPane.showMessageDialog(this,
                           "Cantidad y precio deben ser mayores a 0.", "Error", JOptionPane.ERROR_MESSAGE );
                   return;
               }

               ventaService.agregarProducto(idProducto, cantidad, precio);
               refrescarTabla();
               actualizarTotal();
               txtIdProducto.setText("");
               txtCantidad.setText("");
               txtPrecio.setText("");
               txtIdProducto.requestFocus();

           } catch(NumberFormatException ex){
               JOptionPane.showMessageDialog(this,
                       "Ingresa valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
           }
       });

       btnEliminar.addActionListener(e -> {
           int fila = tablaCarrito.getSelectedRow();
           if(fila >= 0) {
               ventaService.eliminarProducto(fila);
               refrescarTabla();
               actualizarTotal();
           } else {
               JOptionPane.showMessageDialog(this,
                       "Selecciona un producto para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
           }
       });

       btnCobrar.addActionListener(e -> {
           if (ventaService.getCarrito().isEmpty()) {
               JOptionPane.showMessageDialog(this,
                       "El carrito está vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
               return;
           }

           int idCliente = 1;
           Venta ventaConfirmada = ventaService.confirmarVenta(idCliente);

           if (ventaConfirmada != null) {
               panelFactura.cargarVenta(ventaConfirmada);
               cardLayout.show(panelContenido, "factura");
               refrescarTabla();
               actualizarTotal();
           } else {
               JOptionPane.showMessageDialog(this,
                       "Error al registrar la venta.", "Error", JOptionPane.ERROR_MESSAGE);
           }
       });
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        for(DetalleVenta d: ventaService.getCarrito()) {
            modeloTabla.addRow(new Object[]{
                    d.getId_producto(),
                    d.getCantidad(),
                    String.format("$%.2f", d.getPrecioUnit()),
                    String.format("$%.2f", d.getSubtotal())
            });
        }
    }

    private void actualizarTotal() {
        lblTotal.setText(String.format("Total: $%.2f", ventaService.getTotal()));
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(grisClaro, 1),
                BorderFactory.createEmptyBorder(4,8,4,8)
        ));
    }

    private JButton crearBoton(String texto, Color color){
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(color);
        btn.setForeground(Blanco);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
