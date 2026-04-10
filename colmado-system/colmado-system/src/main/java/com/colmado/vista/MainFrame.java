package com.colmado.vista;

import com.colmado.util.ConexionDB;
import com.colmado.modelo.Usuario;
import com.colmado.vista.panel.panelLogin;
import com.colmado.vista.panel.panelFactura;
import com.colmado.vista.panel.panelVentas;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    private Usuario usuarioActual;
    private JPanel panelContenido;
    private CardLayout cardLayout;

    // Colores
    private final Color AZUL_OSCURO = new Color(13, 27, 42);
    private final Color AZUL_MEDIO = new Color(27, 38, 59);
    private final Color AZUL_ACENTO = new Color(65, 105, 225);
    private final Color BLANCO = Color.WHITE;
    private final Color GRIS_CLARO = new Color(200, 210, 220);

    public MainFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("Colmado System");
        setIconImage(new ImageIcon(getClass().getResource("/logo.png")).getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel izquierdo (menú lateral)
        add(crearMenuLateral(), BorderLayout.WEST);

        // Panel contenido principal con CardLayout
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(new Color(240, 242, 245));

        // Placeholders — se reemplazan cuando los compañeros entreguen sus paneles
        panelContenido.add(crearPlaceholder("📦", "Módulo de Productos",  "Persona 2 — en desarrollo"), "productos");
        panelContenido.add(crearPlaceholder("🏪", "Módulo de Inventario", "Persona 2 — en desarrollo"), "inventario");
        panelFactura vistaFactura = new panelFactura();
        panelVentas vistaVentas = new panelVentas(usuarioActual, vistaFactura, cardLayout, panelContenido);
        panelContenido.add(vistaVentas, "ventas");

        panelContenido.add(vistaFactura, "factura");
        panelContenido.add(crearPlaceholder("👥", "Módulo de Clientes",   "Persona 4 — en desarrollo"), "clientes");
        panelContenido.add(crearPlaceholder("📊", "Módulo de Reportes",   "Persona 4 — en desarrollo"), "reportes");

        add(panelContenido, BorderLayout.CENTER);

        // Panel superior
        add(crearPanelSuperior(), BorderLayout.NORTH);

        // Mostrar productos por defecto
        cardLayout.show(panelContenido, "productos");
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AZUL_OSCURO);
        panel.setPreferredSize(new Dimension(0, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel lblTitulo = new JLabel("🏪 Colmado System");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(BLANCO);

        JLabel lblUsuario = new JLabel("👤 " + usuarioActual.getUsername() +
                "  |  Rol: " + usuarioActual.getRol());
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUsuario.setForeground(GRIS_CLARO);

        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(lblUsuario, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearMenuLateral() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(AZUL_MEDIO);
        menu.setPreferredSize(new Dimension(200, 0));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Título menú
        JLabel lblMenu = new JLabel("MENÚ");
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblMenu.setForeground(GRIS_CLARO);
        lblMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMenu.setBorder(BorderFactory.createEmptyBorder(0, 8, 10, 0));

        menu.add(lblMenu);
        menu.add(crearBotonMenu("📦  Productos",  "productos"));
        menu.add(Box.createVerticalStrut(5));
        menu.add(crearBotonMenu("🏪  Inventario", "inventario"));
        menu.add(Box.createVerticalStrut(5));
        menu.add(crearBotonMenu("💰  Ventas",     "ventas"));
        menu.add(Box.createVerticalStrut(5));
        menu.add(crearBotonMenu("🧾  Factura",    "factura"));

        // Clientes y Reportes solo para ADMIN
        if (usuarioActual.getRol().toString().equals("ADMIN")) {
            menu.add(Box.createVerticalStrut(5));
            menu.add(crearBotonMenu("👥  Clientes",  "clientes"));
            menu.add(Box.createVerticalStrut(5));
            menu.add(crearBotonMenu("📊  Reportes",  "reportes"));
        }

        // Espaciador para empujar cerrar sesión al fondo
        menu.add(Box.createVerticalGlue());

        // Botón cerrar sesión
        JButton btnCerrar = crearBotonMenu("🚪  Cerrar Sesión", "");
        btnCerrar.setBackground(new Color(180, 40, 40));
        btnCerrar.addActionListener(e -> cerrarSesion());
        menu.add(btnCerrar);

        return menu;
    }

    private JButton crearBotonMenu(String texto, String panel) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBackground(AZUL_OSCURO);
        btn.setForeground(BLANCO);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (!panel.isEmpty()) {
            btn.addActionListener(e -> cardLayout.show(panelContenido, panel));
        }

        return btn;
    }

    private JPanel crearPlaceholder(String icono, String titulo, String subtitulo) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 242, 245));

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);

        JLabel lblIcono = new JLabel(icono, SwingConstants.CENTER);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(60, 60, 60));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel(subtitulo, SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        contenido.add(lblIcono);
        contenido.add(Box.createVerticalStrut(10));
        contenido.add(lblTitulo);
        contenido.add(Box.createVerticalStrut(6));
        contenido.add(lblSubtitulo);

        panel.add(contenido);
        return panel;
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Deseas cerrar sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new panelLogin().setVisible(true);
        }
    }
}

