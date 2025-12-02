package view.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Clase base para las vistas GUI que proporciona métodos comunes para el diseño de las vistas
 * Los colores fueron elegidos en base a un sitio de diseño de interfaces gráficas: https://htmlcolorcodes.com/es/
 */
public abstract class GUIViewBase extends JFrame {
    
    protected static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    protected static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    protected static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    
    protected static final Color COLOR_BACKGROUND = new Color(250, 248, 245);
    protected static final Color COLOR_PRIMARY = new Color(227, 98, 30);
    protected static final Color COLOR_SUCCESS = new Color(88, 177, 159);
    protected static final Color COLOR_ERROR = new Color(200, 68, 54);
    protected static final Color COLOR_WARNING = new Color(247, 183, 49);

    public GUIViewBase(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    protected JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(FONT_NORMAL);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 35));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

    protected JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(FONT_NORMAL);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    protected JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_NORMAL);
        return label;
    }

    protected JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_TITLE);
        label.setForeground(new Color(33, 37, 41));
        return label;
    }

    protected void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    protected void showSuccessMessage(String message) {
        showMessage(message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void showErrorMessage(String message) {
        showMessage(message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void showWarningMessage(String message) {
        showMessage(message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    protected int showConfirmDialog(String message, String title) {
        return JOptionPane.showConfirmDialog(
            this,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
    }

    protected int readIntFromField(JTextField field) throws NumberFormatException {
        String text = field.getText().trim();
        if (text.isEmpty()) {
            throw new NumberFormatException("El campo no puede estar vacío");
        }
        return Integer.parseInt(text);
    }

    protected double readDoubleFromField(JTextField field) throws NumberFormatException {
        String text = field.getText().trim();
        if (text.isEmpty()) {
            throw new NumberFormatException("El campo no puede estar vacío");
        }
        return Double.parseDouble(text);
    }

    protected String readStringFromField(JTextField field) {
        String text = field.getText().trim();
        if (text.isEmpty()) {
            throw new IllegalArgumentException("El campo no puede estar vacío");
        }
        if (text.contains(",")) {
            throw new IllegalArgumentException("No se permiten comas en el texto");
        }
        return text;
    }
}




