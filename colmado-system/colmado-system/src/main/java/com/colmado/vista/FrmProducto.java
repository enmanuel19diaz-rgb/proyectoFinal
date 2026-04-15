package com.colmado.vista;

import com.colmado.dao.ProductoDAO;
import com.colmado.modelo.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;

public class FrmProducto extends JFrame {
    private JTextField txtId, txtCodigo, txtNombre, txtCategoria, txtPrecio, txtStock;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnGuardar, btnEditar, btnEliminar, btnNuevo;
    private ProductoDAO proDao = new ProductoDAO();

    public FrmProducto() {
        setTitle("Gestión de Productos - Anderson");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Campo ID oculto (para saber qué editar)
        txtId = new JTextField();
        txtId.setVisible(false);

        JLabel lb1 = new JLabel("Código:");
        lb1.setBounds(20, 30, 80, 25);
        add(lb1);
        txtCodigo = new JTextField();
        txtCodigo.setBounds(100, 30, 150, 25);
        add(txtCodigo);

        JLabel lb2 = new JLabel("Nombre:");
        lb2.setBounds(20, 70, 80, 25);
        add(lb2);
        txtNombre = new JTextField();
        txtNombre.setBounds(100, 70, 150, 25);
        add(txtNombre);

        JLabel lb3 = new JLabel("Precio:");
        lb3.setBounds(20, 110, 80, 25);
        add(lb3);
        txtPrecio = new JTextField();
        txtPrecio.setBounds(100, 110, 150, 25);
        add(txtPrecio);

        // Botones
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(20, 200, 110, 30);
        add(btnGuardar);

        btnEditar = new JButton("Editar");
        btnEditar.setBounds(140, 200, 110, 30);
        add(btnEditar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(20, 240, 110, 30);
        add(btnEliminar);

        btnNuevo = new JButton("Nuevo");
        btnNuevo.setBounds(140, 240, 110, 30);
        add(btnNuevo);

        // Tabla
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Cod");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Precio");
        
        tabla = new JTable(modeloTabla);
        JScrollPane sp = new JScrollPane(tabla);
        sp.setBounds(300, 30, 550, 400);
        add(sp);

        // Evento para pasar de la tabla a los campos
        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.rowAtPoint(e.getPoint());
                txtId.setText(tabla.getValueAt(fila, 0).toString());
                txtCodigo.setText(tabla.getValueAt(fila, 1).toString());
                txtNombre.setText(tabla.getValueAt(fila, 2).toString());
                txtPrecio.setText(tabla.getValueAt(fila, 3).toString());
            }
        });

        btnGuardar.addActionListener(e -> guardar());
        btnNuevo.addActionListener(e -> limpiar());
        
        listar(); // Cargar la tabla al abrir
    }

    private void listar() {
        List<Producto> lista = proDao.listarProductos();
        modeloTabla.setRowCount(0);
        for (Producto p : lista) {
            Object[] objeto = {p.getId(), p.getCodigo(), p.getNombre(), p.getPrecio()};
            modeloTabla.addRow(objeto);
        }
    }

    private void guardar() {
        Producto p = new Producto();
        p.setCodigo(txtCodigo.getText());
        p.setNombre(txtNombre.getText());
        p.setPrecio(Double.parseDouble(txtPrecio.getText()));
        if (proDao.registrar(p)) {
            JOptionPane.showMessageDialog(null, "Registrado");
            limpiar();
            listar();
        }
    }

    private void limpiar() {
        txtId.setText("");
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
    }

    public static void main(String[] args) {
        new FrmProducto().setVisible(true);
    }
}