package com.colmado.vista.panel;

import com.colmado.modelo.Cliente;
import com.colmado.service.ClienteService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelClientes extends JPanel {

    // ── Servicio ──────────────────────────────────────────────────────────────
    private final ClienteService service = new ClienteService();

    // ── Tabla ─────────────────────────────────────────────────────────────────
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;

    // ── Campos del formulario ─────────────────────────────────────────────────
    private final JTextField txtNombre    = new JTextField();
    private final JTextField txtTelefono  = new JTextField();
    private final JTextField txtEmail     = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final JTextField txtBuscar    = new JTextField();

    // ── Botones ───────────────────────────────────────────────────────────────
    private final JButton btnNuevo    = new JButton("Nuevo");
    private final JButton btnGuardar  = new JButton("Guardar");
    private final JButton btnEditar   = new JButton("Editar");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnLimpiar  = new JButton("Limpiar");

    // ── Estado interno ────────────────────────────────────────────────────────
    private int idSeleccionado = 0; // 0 = modo nuevo

    // ═════════════════════════════════════════════════════════════════════════
    public PanelClientes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabla
        String[] columnas = {"ID", "Nombre", "Teléfono", "Email", "Dirección"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);

        // Layout
        add(panelSuperior(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelFormulario(), BorderLayout.EAST);
        add(panelBotones(), BorderLayout.SOUTH);

        // Eventos
        registrarEventos();

        // Carga inicial
        cargarTabla();
        modoNuevo();
    }

    // ── Panels de UI ──────────────────────────────────────────────────────────

    private JPanel panelSuperior() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel("🔍 Buscar:"));
        txtBuscar.setPreferredSize(new Dimension(220, 28));
        p.add(txtBuscar);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscar());
        p.add(btnBuscar);
        JButton btnMostrarTodos = new JButton("Mostrar todos");
        btnMostrarTodos.addActionListener(e -> { txtBuscar.setText(""); cargarTabla(); });
        p.add(btnMostrarTodos);
        return p;
    }

    private JPanel panelFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        p.setPreferredSize(new Dimension(250, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(6, 6, 6, 6);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        agregarCampo(p, gbc, 0, "Nombre *",    txtNombre);
        agregarCampo(p, gbc, 1, "Teléfono",    txtTelefono);
        agregarCampo(p, gbc, 2, "Email",        txtEmail);
        agregarCampo(p, gbc, 3, "Dirección",    txtDireccion);

        // Relleno inferior
        gbc.gridy   = 8;
        gbc.weighty = 1;
        p.add(new JLabel(), gbc);
        return p;
    }

    private void agregarCampo(JPanel p, GridBagConstraints gbc, int fila, String label, JTextField campo) {
        gbc.gridy  = fila * 2;
        gbc.gridx  = 0;
        gbc.weighty = 0;
        p.add(new JLabel(label), gbc);
        gbc.gridy  = fila * 2 + 1;
        campo.setPreferredSize(new Dimension(0, 28));
        p.add(campo, gbc);
    }

    private JPanel panelBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));

        estilizarBoton(btnNuevo,    new Color(52, 152, 219));
        estilizarBoton(btnGuardar,  new Color(39, 174, 96));
        estilizarBoton(btnEditar,   new Color(243, 156, 18));
        estilizarBoton(btnEliminar, new Color(231, 76, 60));
        estilizarBoton(btnLimpiar,  new Color(149, 165, 166));

        p.add(btnNuevo);
        p.add(btnGuardar);
        p.add(btnEditar);
        p.add(btnEliminar);
        p.add(btnLimpiar);
        return p;
    }

    private void estilizarBoton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(100, 32));
        btn.setFont(btn.getFont().deriveFont(Font.BOLD));
    }

    // ── Eventos ───────────────────────────────────────────────────────────────

    private void registrarEventos() {
        btnNuevo   .addActionListener(e -> modoNuevo());
        btnGuardar .addActionListener(e -> guardar());
        btnEditar  .addActionListener(e -> cargarEnFormulario());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar .addActionListener(e -> limpiarFormulario());

        // Click en fila de la tabla
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) resaltarSeleccion();
        });
    }

    // ── Lógica de UI ─────────────────────────────────────────────────────────

    private void cargarTabla() {
        cargarTablaConLista(service.listarTodos());
    }

    private void cargarTablaConLista(List<Cliente> lista) {
        modeloTabla.setRowCount(0);
        for (Cliente c : lista) {
            modeloTabla.addRow(new Object[]{
                    c.getId(), c.getNombre(), c.getTelefono(), c.getEmail(), c.getDireccion()
            });
        }
    }

    private void buscar() {
        cargarTablaConLista(service.buscar(txtBuscar.getText()));
    }

    private void modoNuevo() {
        idSeleccionado = 0;
        limpiarFormulario();
        txtNombre.requestFocus();
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
        idSeleccionado = 0;
        tabla.clearSelection();
    }

    private void cargarEnFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { mostrarError("Selecciona un cliente de la tabla."); return; }

        idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
        txtNombre   .setText((String) modeloTabla.getValueAt(fila, 1));
        txtTelefono .setText(nullSafe(modeloTabla.getValueAt(fila, 2)));
        txtEmail    .setText(nullSafe(modeloTabla.getValueAt(fila, 3)));
        txtDireccion.setText(nullSafe(modeloTabla.getValueAt(fila, 4)));
        txtNombre.requestFocus();
    }

    private void resaltarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) cargarEnFormulario();
    }

    private void guardar() {
        Cliente c = buildCliente();
        String error = service.guardar(c);
        if (error != null) { mostrarError(error); return; }
        mostrarExito(idSeleccionado == 0 ? "Cliente registrado correctamente." : "Cliente actualizado correctamente.");
        cargarTabla();
        modoNuevo();
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { mostrarError("Selecciona un cliente de la tabla."); return; }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar al cliente \"" + nombre + "\"?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        String error = service.eliminar(id);
        if (error != null) { mostrarError(error); return; }
        mostrarExito("Cliente eliminado correctamente.");
        cargarTabla();
        modoNuevo();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Cliente buildCliente() {
        Cliente c = new Cliente();
        c.setId(idSeleccionado);
        c.setNombre(txtNombre.getText().trim());
        c.setTelefono(txtTelefono.getText().trim());
        c.setEmail(txtEmail.getText().trim());
        c.setDireccion(txtDireccion.getText().trim());
        return c;
    }

    private String nullSafe(Object o) {
        return o == null ? "" : o.toString();
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}