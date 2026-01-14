package view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.User;
import services.AuthService;

public class LoginGUI extends JDialog {
    private AuthService authService;
    private User usuarioAutenticado = null;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginGUI(JFrame parent, AuthService authService) {
        super(parent, "Login - Sistema WMS", true);
        this.authService = authService;
        setupUI();
    }

    private void setupUI() {
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(250, 248, 245));

        // Título
        JLabel titleLabel = new JLabel("Sistema de Gestión de Almacenes", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 37, 41));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(250, 248, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel userLabel = new JLabel("Usuario:");
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);

        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel passLabel = new JLabel("Contraseña:");
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(250, 248, 245));
        
        JButton loginButton = new JButton("Ingresar");
        loginButton.setPreferredSize(new Dimension(120, 35));
        loginButton.setBackground(new Color(88, 177, 159));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.setBackground(new Color(200, 68, 54));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.addActionListener(e -> {
            usuarioAutenticado = null;
            dispose();
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Permitir login con Enter
        passwordField.addActionListener(e -> realizarLogin());

        add(mainPanel);
    }

    private void realizarLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, complete todos los campos.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        User usuario = authService.login(username, password);

        if (usuario != null) {
            usuarioAutenticado = usuario;
            JOptionPane.showMessageDialog(this,
                "Bienvenido, " + usuario.getUsername() + " (" + usuario.getRole() + ")",
                "Login exitoso",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Credenciales incorrectas. Intente nuevamente.",
                "Error de autenticación",
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    public User mostrarLogin() {
        setVisible(true);
        return usuarioAutenticado;
    }
}

