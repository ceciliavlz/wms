package view.gui.views;

import view.gui.GUIViewBase;
import controller.HistorialController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistorialViewGUI extends GUIViewBase {
    private final HistorialController historialCtrl;
    private JTable tableHistorial;
    private DefaultTableModel modelHistorial;
    private JTextField fieldIdProducto;

    public HistorialViewGUI(HistorialController historialCtrl) {
        super("Historial de Movimientos");
        this.historialCtrl = historialCtrl;
        setupUI();
    }

    // Configura la interfaz de la vista
    private void setupUI() {
        setSize(900, 650);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(COLOR_BACKGROUND);

        JLabel titleLabel = createTitleLabel("Historial de Movimientos y Transformaciones");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_NORMAL);

        tabbedPane.addTab("Historial por Producto", createHistorialProductoPanel());
        tabbedPane.addTab("Historial de Transformaciones", createHistorialTransformacionesPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        JButton btnCerrar = createButton("Cerrar", COLOR_ERROR);
        btnCerrar.addActionListener(e -> dispose());
        footerPanel.add(btnCerrar);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Crea el panel para el historial de movimientos por producto
    private JPanel createHistorialProductoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setOpaque(false);
        controlPanel.add(createLabel("ID del producto:"));
        fieldIdProducto = createTextField(10);
        controlPanel.add(fieldIdProducto);
        JButton btnBuscar = createButton("Buscar Historial", COLOR_PRIMARY);
        btnBuscar.addActionListener(e -> buscarHistorialProducto());
        controlPanel.add(btnBuscar);

        String[] columnNames = {"Tipo", "Fecha", "Detalle"};
        modelHistorial = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableHistorial = new JTable(modelHistorial);
        tableHistorial.setFont(FONT_NORMAL);
        tableHistorial.setRowHeight(25);
        tableHistorial.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(tableHistorial);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Historial del Producto"));

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Crea el panel para el historial de transformaciones
    private JPanel createHistorialTransformacionesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        JButton btnCargar = createButton("Cargar Historial General", COLOR_PRIMARY);
        btnCargar.addActionListener(e -> cargarHistorialTransformaciones());

        String[] columnNames = {"Tipo", "Fecha", "Detalle"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setFont(FONT_NORMAL);
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Historial General de Transformaciones"));

        panel.add(btnCargar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.putClientProperty("tableModel", model);

        return panel;
    }

    // Busca el historial de movimientos por producto
    private void buscarHistorialProducto() {
        try {
            int id = readIntFromField(fieldIdProducto);
            List<String> movimientos = historialCtrl.verHistorialProducto(id);
            
            modelHistorial.setRowCount(0);
            
            if (movimientos.isEmpty()) {
                showWarningMessage("No se encontraron movimientos ni transformaciones para ese producto.");
            } else {
                for (String mov : movimientos) {
                    // Parsear el formato "TIPO | FECHA | DETALLE"
                    String[] parts = mov.split("\\|");
                    if (parts.length >= 3) {
                        modelHistorial.addRow(new Object[]{
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim()
                        });
                    } else {
                        modelHistorial.addRow(new Object[]{mov, "", ""});
                    }
                }
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Por favor ingrese un ID válido.");
        }
    }

    // Carga el historial de transformaciones
    private void cargarHistorialTransformaciones() {
        List<String> transformaciones = historialCtrl.verHistorialTransformaciones();
        
        JTabbedPane tabbedPane = (JTabbedPane) getContentPane().getComponent(0);
        JPanel panel = (JPanel) tabbedPane.getComponent(1);
        DefaultTableModel model = (DefaultTableModel) panel.getClientProperty("tableModel");
        
        model.setRowCount(0);
        
        if (transformaciones.isEmpty()) {
            showWarningMessage("No hay transformaciones registradas.");
        } else {
            for (String t : transformaciones) {
                // Parsear el formato "TIPO | FECHA | DETALLE"
                String[] parts = t.split("\\|");
                if (parts.length >= 3) {
                    model.addRow(new Object[]{
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim()
                    });
                } else {
                    model.addRow(new Object[]{t, "", ""});
                }
            }
        }
    }
}




