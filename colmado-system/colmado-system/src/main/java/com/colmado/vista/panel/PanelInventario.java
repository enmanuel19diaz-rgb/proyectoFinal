package com.colmado.vista.panel;

import com.colmado.modelo.Producto;
import com.colmado.modelo.Usuario;
import com.colmado.service.InventarioService;
import com.colmado.dao.ProductoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PanelInventario extends JPanel {

    private InventarioService inventarioService;
    private ProductoDAO productoDAO;
    private Usuario usuario;

    private JTable tablaInventario;
    private DefaultTableModel modeloTabla;
    private JTextField txtIdProducto, txtCantidad;
    private JButton btnAgregar, btnDescontar, btnRefrescar;
    private JLabel lblMensaje;

    private final Color AZUL_OSCURO = new Color(13, 27, 42);
    private final Color AZUL_MEDIO  = new Color(27, 38, 59);
    private final Color AZUL_ACENTO = new Color(65, 105, 225);
    private final Color BLANCO      = Color.WHITE;
    private final Color GRIS_CLARO  = new Color(200, 210, 220);
    private final Color FONDO       = new Color(240, 242, 245);

    public PanelInventario(Usuario usuario) {
        this.usuario           = usuario;
        this.inventarioService = new InventarioService();
        this.productoDAO       = new ProductoDAO();
        initComponents();
        cargarInventario();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(FONDO);
        setBorder(new EmptyBorder(16, 20, 16, 20));

        // ── Título ──
        JLabel lblTitulo = new JLabel("🏪  Control de Inventario");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(AZUL_OSCURO);
        add(lblTitulo, BorderLayout.NORTH);

        // ── Panel de acciones ──
        JPanel panelAccion = new JPanel();
        panelAccion.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelAccion.setBackground(BLANCO);
        panelAccion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRIS_CLARO, 1),
                new EmptyBorder(12, 14, 12, 14)
        ));

        panelAccion.add(crearLabel("ID Producto:"));
        txtIdProducto = new JTextField(8); estilizarCampo(txtIdProducto);
        panelAccion.add(txtIdProducto);

        panelAccion.add(crearLabel("Cantidad:"));
        txtCantidad = new JTextField(6); estilizarCampo(txtCantidad);
        panelAccion.add(txtCantidad);

        btnAgregar   = crearBoton("+ Agregar stock",   new Color(34, 139, 34));
        btnDescontar = crearBoton("- Descontar stock", new Color(180, 40, 40));
        btnRefrescar = crearBoton("Refrescar",         new Color(100, 100, 100));

        panelAccion.add(btnAgregar);
        panelAccion.add(btnDescontar);
        panelAccion.add(btnRefrescar);

        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelAccion.add(lblMensaje);

        // ── Tabla ──
        String[] columnas = {"ID", "Nombre", "Categoría", "Precio", "Stock", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaInventario = new JTable(modeloTabla);
        tablaInventario.setRowHeight(26);
        tablaInventario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaInventario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaInventario.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaInventario.getTableHeader().setBackground(AZUL_MEDIO);
        tablaInventario.getTableHeader().setForeground(BLANCO);

        // Al seleccionar fila → llenar ID automáticamente
        tablaInventario.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaInventario.getSelectedRow();
                if (fila >= 0) txtIdProducto.setText(modeloTabla.getValueAt(fila, 0).toString());
            }
        });

        JScrollPane scroll = new JScrollPane(tablaInventario);
        scroll.setBorder(BorderFactory.createLineBorder(GRIS_CLARO, 1));

        JPanel panelCentral = new JPanel(new BorderLayout(0, 12));
        panelCentral.setOpaque(false);
        panelCentral.add(panelAccion, BorderLayout.NORTH);
        panelCentral.add(scroll,      BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);

        // ── Eventos ──
        btnAgregar.addActionListener(e -> {
            try {
                int id  = Integer.parseInt(txtIdProducto.getText().trim());
                int cant = Integer.parseInt(txtCantidad.getText().trim());
                if (inventarioService.agregarStock(id, cant)) {
                    mostrarMensaje("Stock agregado correctamente.", new Color(34, 139, 34));
                    cargarInventario();
                    limpiar();
                } else {
                    mostrarMensaje("No se pudo agregar stock.", new Color(180, 40, 40));
                }
            } catch (NumberFormatException ex) {
                mostrarMensaje("Ingresa valores numéricos válidos.", new Color(180, 40, 40));
            }
        });

        btnDescontar.addActionListener(e -> {
            try {
                int id   = Integer.parseInt(txtIdProducto.getText().trim());
                int cant = Integer.parseInt(txtCantidad.getText().trim());
                if (inventarioService.descontarStock(id, cant)) {
                    mostrarMensaje("Stock descontado correctamente.", new Color(34, 139, 34));
                    cargarInventario();
                    limpiar();
                } else {
                    mostrarMensaje("Stock insuficiente o producto no encontrado.", new Color(180, 40, 40));
                }
            } catch (NumberFormatException ex) {
                mostrarMensaje("Ingresa valores numéricos válidos.", new Color(180, 40, 40));
            }
        });

        btnRefrescar.addActionListener(e -> cargarInventario());
    }

    private void cargarInventario() {
        modeloTabla.setRowCount(0);
        List<Producto> lista = productoDAO.obtenerTodos();
        for (Producto p : lista) {
            String estado = p.getCantidad() == 0 ? "Sin stock"
                    : p.getCantidad() <= 5  ? "Stock bajo"
                    : "Disponible";
            modeloTabla.addRow(new Object[]{
                    p.getId_producto(),
                    p.getNombre(),
                    p.getCategoria(),
                    String.format("$%.2f", p.getPrecio()),
                    p.getCantidad(),
                    estado
            });
        }
    }

    private void limpiar() {
        txtIdProducto.setText("");
        txtCantidad.setText("");
        tablaInventario.clearSelection();
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

    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRIS_CLARO, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
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