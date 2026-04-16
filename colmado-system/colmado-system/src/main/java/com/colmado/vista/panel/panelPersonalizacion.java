package com.colmado.vista.panel;

import com.colmado.modelo.Configuracion;
import com.colmado.dao.ConfiguracionDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class panelPersonalizacion extends JPanel {

    // Colores del sistema
    private final Color azulOscuro = new Color(13, 27, 42);
    private final Color azulAcento = new Color(65, 105, 225);
    private final Color Blanco = Color.WHITE;
    private final Color grisClaro = new Color(200, 210, 220);
    private final Color fondo = new Color(240, 242, 245);

    private JTextField txtNombreNegocio, txtRnc, txtDireccion, txtTelefono;

    public panelPersonalizacion() {
        initComponents();
        cargarDatosDesdeBD();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(fondo);

        // Contenedor con márgenes
        JPanel contenedor = new JPanel(new BorderLayout(0, 20));
        contenedor.setOpaque(false);
        contenedor.setBorder(new EmptyBorder(30, 50, 30, 50));

        // --- ENCABEZADO ---
        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setBackground(azulOscuro);
        encabezado.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel lblTitulo = new JLabel("⚙️ CONFIGURACIÓN GENERAL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Blanco);

        JLabel lblSub = new JLabel("Gestione los datos que aparecen en sus comprobantes de venta.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(grisClaro);

        encabezado.add(lblTitulo);
        encabezado.add(Box.createVerticalStrut(5));
        encabezado.add(lblSub);

        // --- FORMULARIO ---
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Blanco);
        formulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(grisClaro),
                new EmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;

        // Campos
        txtNombreNegocio = agregarCampo(formulario, "Nombre del Negocio:", 0);
        txtRnc = agregarCampo(formulario, "RNC:", 1);
        txtDireccion = agregarCampo(formulario, "Dirección:", 2);
        txtTelefono = agregarCampo(formulario, "Teléfono:", 3);

        // --- BOTÓN GUARDAR ---
        JButton btnGuardar = new JButton("💾 Guardar Cambios");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setBackground(azulAcento);
        btnGuardar.setForeground(Blanco);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(new EmptyBorder(10, 25, 10, 25));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnGuardar.addActionListener(e -> accionGuardar());

        JPanel pnlBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBoton.setOpaque(false);
        pnlBoton.add(btnGuardar);

        // Ensamblar
        contenedor.add(encabezado, BorderLayout.NORTH);
        contenedor.add(formulario, BorderLayout.CENTER);
        contenedor.add(pnlBoton, BorderLayout.SOUTH);

        add(contenedor, BorderLayout.CENTER);
    }

    private JTextField agregarCampo(JPanel pnl, String etiqueta, int fila) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnl.add(lbl, gbc);

        gbc.gridx = 1;
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(grisClaro),
                new EmptyBorder(5, 7, 5, 7)));
        pnl.add(txt, gbc);
        return txt;
    }

    private void cargarDatosDesdeBD() {
        Configuracion config = new ConfiguracionDAO().obtenerConfiguracion();
        if (config != null) {
            txtNombreNegocio.setText(config.getNombre());
            txtRnc.setText(config.getRnc());
            txtDireccion.setText(config.getDireccion());
            txtTelefono.setText(config.getTelefono());
        }
    }

    private void accionGuardar() {
        Configuracion nuevaConfig = new Configuracion();
        nuevaConfig.setNombre(txtNombreNegocio.getText().trim());
        nuevaConfig.setRnc(txtRnc.getText().trim());
        nuevaConfig.setDireccion(txtDireccion.getText().trim());
        nuevaConfig.setTelefono(txtTelefono.getText().trim());

        if (new ConfiguracionDAO().actualizarConfiguracion(nuevaConfig)) {
            JOptionPane.showMessageDialog(this, "Configuración actualizada con éxito.", "Sistema", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar los cambios.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}