package com.peripollos.view;

import com.peripollos.dao.UsuarioDAO;
import com.peripollos.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginView extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private JButton btnIngresar;
    private UsuarioDAO usuarioDAO;

    public LoginView() {
        usuarioDAO = new UsuarioDAO();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Peripollos - Login");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBackground(new Color(245, 245, 245));

        JPanel panelHeader = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(43, 146, 209), 0, getHeight(), new Color(28, 143, 185));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelHeader.setPreferredSize(new Dimension(450, 150));
        panelHeader.setLayout(new BorderLayout());

        JPanel contenedorTitulo = new JPanel();
        contenedorTitulo.setOpaque(false);
        contenedorTitulo.setLayout(new BoxLayout(contenedorTitulo, BoxLayout.Y_AXIS));

        JLabel lblIcono = new JLabel("🍗");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("PERIPOLLOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Sistema de Punto de Venta");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(255, 255, 255, 200));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        contenedorTitulo.add(Box.createVerticalStrut(20));
        contenedorTitulo.add(lblIcono);
        contenedorTitulo.add(Box.createVerticalStrut(5));
        contenedorTitulo.add(lblTitulo);
        contenedorTitulo.add(Box.createVerticalStrut(5));
        contenedorTitulo.add(lblSubtitulo);

        panelHeader.add(contenedorTitulo, BorderLayout.CENTER);

        JPanel panelFormulario = new JPanel();
        panelFormulario.setBackground(new Color(245, 245, 245));
        panelFormulario.setLayout(null);

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 13));
        lblUsuario.setForeground(new Color(60, 60, 60));
        lblUsuario.setBounds(50, 30, 350, 25);
        panelFormulario.add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsuario.setBounds(50, 55, 350, 45);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        panelFormulario.add(txtUsuario);

        JLabel lblClave = new JLabel("Contraseña");
        lblClave.setFont(new Font("Arial", Font.BOLD, 13));
        lblClave.setForeground(new Color(60, 60, 60));
        lblClave.setBounds(50, 120, 350, 25);
        panelFormulario.add(lblClave);

        txtClave = new JPasswordField();
        txtClave.setFont(new Font("Arial", Font.PLAIN, 14));
        txtClave.setBounds(50, 145, 350, 45);
        txtClave.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        panelFormulario.add(txtClave);

        btnIngresar = new JButton("INGRESAR") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(30, 140, 55));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(35, 150, 60));
                } else {
                    g2d.setColor(new Color(40, 167, 69));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2d.dispose();

                super.paintComponent(g);
            }
        };
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 15));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setBounds(50, 220, 350, 50);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setContentAreaFilled(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelFormulario.add(btnIngresar);

        JLabel lblInfo = new JLabel("<html><center>Usuario: Admin | Contraseña: 123<br>Usuario: Cajero1 | Contraseña: 123</center></html>");
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 11));
        lblInfo.setForeground(new Color(120, 120, 120));
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        lblInfo.setBounds(50, 290, 350, 40);
        panelFormulario.add(lblInfo);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        add(panelPrincipal);

        btnIngresar.addActionListener(e -> autenticar());
        txtClave.addActionListener(e -> autenticar());

        SwingUtilities.invokeLater(() -> txtUsuario.requestFocus());
    }

    private void autenticar() {
        String nombreUsuario = txtUsuario.getText().trim();
        String clave = new String(txtClave.getPassword());

        if (nombreUsuario.isEmpty() || clave.isEmpty()) {
            mostrarError("Por favor, complete todos los campos");
            return;
        }

        try {
            Usuario usuario = usuarioDAO.autenticar(nombreUsuario, clave);

            if (usuario != null) {
                this.dispose();
                new MainView(usuario).setVisible(true);
            } else {
                mostrarError("Usuario o contraseña incorrectos");
                txtClave.setText("");
                txtUsuario.requestFocus();
            }
        } catch (SQLException ex) {
            mostrarError("Error de conexión: " + ex.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}
