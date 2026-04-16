package com.colmado.vista.panel;

import com.colmado.modelo.Producto;
import com.colmado.modelo.Usuario;
import com.colmado.dao.ProductoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class panelProductos extends JPanel {

    private ProductoDAO productoDAO;
    private Usuario usuario;

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtPrecio, txtCantidad, txtDescripcion, txtCategoria;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar;
    private JLabel lblMensaje;

    private final Color AZUL_OSCURO = new Color(13, 27, 42);
    private final Color AZUL_MEDIO  = new Color(27, 38, 59);
    private final Color AZUL_ACENTO = new Color(65, 105, 225);
    private final Color BLANCO      = Color.WHITE;
    private final Color GRIS_CLARO  = new Color(200, 210, 220);
    private final Color FONDO       = new Color(240, 242, 245);

    public panelProductos(Usuario usuario) {
        this.usuario    = usuario;
        this.productoDAO = new ProductoDAO();
        initComponents();
        cargarProductos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(FONDO);
        setBorder(new EmptyBorder(16, 20, 16, 20));

        // ── Título ──
        JLabel lblTitulo = new JLabel("📦  Gestión de Productos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(AZUL_OSCURO);
        add(lblTitulo, BorderLayout.NORTH);

        // ── Formulario ──
        JPanel formulario = new JPanel(new GridLayout(3, 4, 10, 10));
        formulario.setBackground(BLANCO);
        formulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRIS_CLARO, 1),
                new EmptyBorder(14, 14, 14, 14)
        ));

        txtNombre      = new JTextField();
        txtPrecio      = new JTextField();
        txtCantidad    = new JTextField();
        txtDescripcion = new JTextField();
        txtCategoria   = new JTextField();

        formulario.add(crearLabel("Nombre:"));       formulario.add(txtNombre);
        formulario.add(crearLabel("Precio:"));       formulario.add(txtPrecio);
        formulario.add(crearLabel("Cantidad:"));     formulario.add(txtCantidad);
        formulario.add(crearLabel("Descripción:"));  formulario.add(txtDescripcion);
        formulario.add(crearLabel("Categoría:"));    formulario.add(txtCategoria);

        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formulario.add(lblMensaje);

        // ── Botones ──
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelBotones.setOpaque(false);

        btnAgregar    = crearBoton("Agregar",    AZUL_ACENTO);
        btnActualizar = crearBoton("Actualizar", new Color(34, 139, 34));
        btnEliminar   = crearBoton("Eliminar",   new Color(180, 40, 40));
        btnLimpiar    = crearBoton("Limpiar",    new Color(100, 100, 100));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelSuperior = new JPanel(new BorderLayout(0, 10));
        panelSuperior.setOpaque(false);
        panelSuperior.add(formulario,    BorderLayout.CENTER);
        panelSuperior.add(panelBotones,  BorderLayout.SOUTH);

        // ── Tabla ──
        String[] columnas = {"ID", "Nombre", "Precio", "Cantidad", "Descripción", "Categoría"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(26);
        tablaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaProductos.getTableHeader().setBackground(AZUL_MEDIO);
        tablaProductos.getTableHeader().setForeground(BLANCO);

        // Al seleccionar fila → llenar formulario
        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarEnFormulario();
        });

        JScrollPane scroll = new JScrollPane(tablaProductos);
        scroll.setBorder(BorderFactory.createLineBorder(GRIS_CLARO, 1));

        JPanel panelCentral = new JPanel(new BorderLayout(0, 12));
        panelCentral.setOpaque(false);
        panelCentral.add(panelSuperior, BorderLayout.NORTH);
        panelCentral.add(scroll,        BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);

        // ── Eventos ──
        btnAgregar.addActionListener(e -> agregarProducto());
        btnActualizar.addActionListener(e -> actualizarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }

    private void agregarProducto() {
        try {
            Producto p = new Producto(
                    txtNombre.getText().trim(),
                    Double.parseDouble(txtPrecio.getText().trim()),
                    Integer.parseInt(txtCantidad.getText().trim()),
                    txtDescripcion.getText().trim(),
                    txtCategoria.getText().trim()
            );
            if (productoDAO.agregar(p)) {
                mostrarMensaje("Producto agregado.", new Color(34, 139, 34));
                cargarProductos();
                limpiarFormulario();
            }
        } catch (NumberFormatException ex) {
            mostrarMensaje("Precio y cantidad deben ser numéricos.", new Color(180, 40, 40));
        }
    }

    private void actualizarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) { mostrarMensaje("Selecciona un producto.", Color.ORANGE); return; }
        try {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            Producto p = new Producto(
                    txtNombre.getText().trim(),
                    Double.parseDouble(txtPrecio.getText().trim()),
                    Integer.parseInt(txtCantidad.getText().trim()),
                    txtDescripcion.getText().trim(),
                    txtCategoria.getText().trim()
            );
            p.setId_producto(id);
            if (productoDAO.actualizar(p)) {
                mostrarMensaje("Producto actualizado.", new Color(34, 139, 34));
                cargarProductos();
                limpiarFormulario();
            }
        } catch (NumberFormatException ex) {
            mostrarMensaje("Precio y cantidad deben ser numéricos.", new Color(180, 40, 40));
        }
    }

    private void eliminarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) { mostrarMensaje("Selecciona un producto.", Color.ORANGE); return; }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (productoDAO.eliminar(id)) {
                mostrarMensaje("Producto eliminado.", new Color(34, 139, 34));
                cargarProductos();
                limpiarFormulario();
            }
        }
    }

    private void cargarProductos() {
        modeloTabla.setRowCount(0);
        List<Producto> lista = productoDAO.obtenerTodos();
        for (Producto p : lista) {
            modeloTabla.addRow(new Object[]{
                    p.getId_producto(),
                    p.getNombre(),
                    String.format("$%.2f", p.getPrecio()),
                    p.getCantidad(),
                    p.getDescripcion(),
                    p.getCategoria()
            });
        }
    }

    private void cargarEnFormulario() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) return;
        txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
        txtPrecio.setText(modeloTabla.getValueAt(fila, 2).toString().replace("$", ""));
        txtCantidad.setText(modeloTabla.getValueAt(fila, 3).toString());
        txtDescripcion.setText((String) modeloTabla.getValueAt(fila, 4));
        txtCategoria.setText((String) modeloTabla.getValueAt(fila, 5));
    }

    private void limpiarFormulario() {
        txtNombre.setText(""); txtPrecio.setText("");
        txtCantidad.setText(""); txtDescripcion.setText("");
        txtCategoria.setText(""); tablaProductos.clearSelection();
        lblMensaje.setText(" ");
    }

    private void mostrarMensaje(String msg, Color color) {
        lblMensaje.setText(msg);
        lblMensaje.setForeground(color);
    }

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return lbl;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(color);
        btn.setForeground(BLANCO);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}