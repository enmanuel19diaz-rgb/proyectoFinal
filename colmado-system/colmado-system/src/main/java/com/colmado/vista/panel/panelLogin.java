package com.colmado.vista.panel;

import com.colmado.modelo.Usuario;
import com.colmado.service.LoginService;
import com.colmado.vista.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class panelLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private LoginService logingService;

    //colores
    private final Color AZUL_OSCURO = new Color(13, 27, 42);
    private final Color AZUL_MEDIO = new Color(27,38,59);
    private final Color AZUL_ACENTO = new Color(65, 105, 225);
    private final Color BLANCO = Color.WHITE;
    private final Color GRIS_CLARO = new Color(200, 210, 220);

    public panelLogin(){
        logingService = new LoginService();
        inicializarUI();
    }

    private void inicializarUI(){
        setTitle("Colmado - Iniciar sección");
        setIconImage(new ImageIcon(getClass().getResource("/logo.png")).getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450,550);
        setLocationRelativeTo(null);
        setResizable(false);

        //panel principal con fondo degradado Azul oscuro

        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, AZUL_OSCURO,
                        0, getHeight(), AZUL_MEDIO
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelPrincipal.setLayout(new GridLayout());

        // card central

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS));
        card.setBackground(AZUL_MEDIO);
        card.setBorder(new EmptyBorder(40,40,40,40));
        card.setPreferredSize(new Dimension(340,400));

        //icono
        JLabel lblIcono = new JLabel("🏪", SwingConstants.CENTER);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        // titulo
        JLabel lblTitulo = new JLabel("Colmado System", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(BLANCO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Inicia sesión para continuar", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(GRIS_CLARO);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Label usuario
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setForeground(GRIS_CLARO);
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campo usuario
        txtUsuario = new JTextField();
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBackground(AZUL_OSCURO);
        txtUsuario.setForeground(BLANCO);
        txtUsuario.setCaretColor(BLANCO);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AZUL_ACENTO, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Label contraseña
        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setForeground(GRIS_CLARO);
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPassword.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campo contraseña
        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBackground(AZUL_OSCURO);
        txtPassword.setForeground(BLANCO);
        txtPassword.setCaretColor(BLANCO);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AZUL_ACENTO, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Botón ingresar
        btnIngresar = new JButton("Ingresar");
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setBackground(AZUL_ACENTO);
        btnIngresar.setForeground(BLANCO);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIngresar.addActionListener(e -> login());

        // Enter en contraseña también hace login
        txtPassword.addActionListener(e -> login());

        // Ensamblar card
        card.add(lblIcono);
        card.add(Box.createVerticalStrut(8));
        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(4));
        card.add(lblSubtitulo);
        card.add(Box.createVerticalStrut(30));
        card.add(lblUsuario);
        card.add(Box.createVerticalStrut(5));
        card.add(txtUsuario);
        card.add(Box.createVerticalStrut(15));
        card.add(lblPassword);
        card.add(Box.createVerticalStrut(5));
        card.add(txtPassword);
        card.add(Box.createVerticalStrut(25));
        card.add(btnIngresar);

        panelPrincipal.add(card);
        add(panelPrincipal);
    }

    private void login() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        try {
            Usuario usuarioAutenticado = LoginService.autenticar(usuario, password);
            dispose();
            new MainFrame(usuarioAutenticado).setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error de autenticación", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new panelLogin().setVisible(true));
    }
}








