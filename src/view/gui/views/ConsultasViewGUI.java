package view.gui.views;

import view.gui.GUIViewBase;
import controller.ConsultasController;
import controller.ProductoController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ConsultasViewGUI extends GUIViewBase {
    private final ConsultasController consultasCtrl;
    private final ProductoController productoCtrl;
    
    private JTable tableResultados;
    private DefaultTableModel modelResultados;

    public ConsultasViewGUI(ConsultasController consultasCtrl, ProductoController productoCtrl) {
        super("Consultas de Stock");
        this.consultasCtrl = consultasCtrl;
        this.productoCtrl = productoCtrl;
        setupUI();
    }

    // Configura la interfaz de la vista
    private void setupUI() {
        setSize(900, 700);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(COLOR_BACKGROUND);

        JLabel titleLabel = createTitleLabel("Consultas de Stock");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_NORMAL);

        tabbedPane.addTab("Stock por Producto", createStockProductoPanel());
        tabbedPane.addTab("Stock por Ubicación", createStockUbicacionPanel());
        tabbedPane.addTab("Stock Agrupado por Producto", createStockAgrupadoProductoPanel());
        tabbedPane.addTab("Stock Agrupado por Ubicación", createStockAgrupadoUbicacionPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        JButton btnCerrar = createButton("Cerrar", COLOR_ERROR);
        btnCerrar.addActionListener(e -> dispose());
        footerPanel.add(btnCerrar);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Crea el panel para la consulta de stock por producto
    private JPanel createStockProductoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setOpaque(false);
        controlPanel.add(createLabel("ID del producto:"));
        JTextField fieldId = createTextField(10);
        controlPanel.add(fieldId);
        JButton btnConsultar = createButton("Consultar", COLOR_PRIMARY);
        btnConsultar.addActionListener(e -> {
            try {
                int id = readIntFromField(fieldId);
                consultarStockProducto(id);
            } catch (NumberFormatException ex) {
                showErrorMessage("Ingrese un ID válido.");
            }
        });
        controlPanel.add(btnConsultar);

        JTextArea textArea = new JTextArea(10, 50);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createTitledBorder("Resultado"));
        JScrollPane scrollPane = new JScrollPane(textArea);

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.putClientProperty("textArea", textArea);

        return panel;
    }

    // Crea el panel para la consulta de stock por ubicación
    private JPanel createStockUbicacionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setOpaque(false);
        controlPanel.add(createLabel("Código ubicación (N-R-F-C):"));
        JTextField fieldUbic = createTextField(15);
        controlPanel.add(fieldUbic);
        JButton btnConsultar = createButton("Consultar", COLOR_PRIMARY);
        btnConsultar.addActionListener(e -> {
            try {
                String ubicacion = readStringFromField(fieldUbic);
                consultarStockUbicacion(ubicacion);
            } catch (Exception ex) {
                showErrorMessage("Error: " + ex.getMessage());
            }
        });
        controlPanel.add(btnConsultar);

        String[] columnNames = {"Producto", "Cantidad"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setFont(FONT_NORMAL);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Stock en la Ubicación"));

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.putClientProperty("tableModel", model);
        panel.putClientProperty("fieldUbic", fieldUbic);

        return panel;
    }

    // Crea el panel para la consulta de stock agrupado por producto
    private JPanel createStockAgrupadoProductoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        JButton btnConsultar = createButton("Consultar Stock Agrupado por Producto", COLOR_PRIMARY);
        btnConsultar.addActionListener(e -> consultarStockAgrupadoProducto());

        String[] columnNames = {"Producto", "Stock Total"};
        modelResultados = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableResultados = new JTable(modelResultados);
        tableResultados.setFont(FONT_NORMAL);
        tableResultados.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tableResultados);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Stock Agrupado por Producto"));

        panel.add(btnConsultar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Crea el panel para la consulta de stock agrupado por ubicación
    private JPanel createStockAgrupadoUbicacionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        JButton btnConsultar = createButton("Consultar Stock Agrupado por Ubicación", COLOR_PRIMARY);
        btnConsultar.addActionListener(e -> consultarStockAgrupadoUbicacion());

        String[] columnNames = {"Ubicación", "Detalle"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setFont(FONT_NORMAL);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Stock Agrupado por Ubicación"));

        panel.add(btnConsultar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.putClientProperty("tableModel", model);

        return panel;
    }

    // Consulta el stock de un producto
    private void consultarStockProducto(int idProd) {
        if (!productoCtrl.existeProducto(idProd)) {
            showErrorMessage("No existe un producto con ese ID.");
            return;
        }
        
        int stock = consultasCtrl.stockDeUnProducto(idProd);
        
        JTabbedPane tabbedPane = (JTabbedPane) getContentPane().getComponent(0);
        JPanel panel = (JPanel) tabbedPane.getComponent(0);
        JTextArea textArea = (JTextArea) ((JScrollPane) panel.getComponent(1)).getViewport().getView();
        
        if (stock > 0) {
            textArea.setText("Stock total de P" + idProd + ": " + stock);
        } else {
            textArea.setText("No hay stock de P" + idProd);
        }
    }

    // Consulta el stock de una ubicación
    private void consultarStockUbicacion(String ubicacion) {
        List<String> respuesta = consultasCtrl.stockDeUnaUbicacion(ubicacion);
        
        JTabbedPane tabbedPane = (JTabbedPane) getContentPane().getComponent(0);
        JPanel panel = (JPanel) tabbedPane.getComponent(1);
        DefaultTableModel model = (DefaultTableModel) panel.getClientProperty("tableModel");
        
        model.setRowCount(0);
        
        if (respuesta.isEmpty()) {
            showWarningMessage("No hay stock en esa ubicación.");
        } else {
            for (String stock : respuesta) {
                // Parsear formato "P1, Ubicación: 1-1-1-2, Cant 10"
                String[] parts = stock.split(",");
                if (parts.length >= 3) {
                    String producto = parts[0].trim();
                    String cantidad = parts[2].replaceAll("[^0-9]", "");
                    model.addRow(new Object[]{producto, cantidad});
                }
            }
        }
    }

    // Consulta el stock agrupado por producto
    private void consultarStockAgrupadoProducto() {
        List<String> respuesta = consultasCtrl.agrupadoPorProducto();
        modelResultados.setRowCount(0);
        
        if (respuesta.isEmpty()) {
            showWarningMessage("No hay stock de ningún producto");
        } else {
            for (String stockProducto : respuesta) {
                // Parsear formato según lo que devuelva el controller
                String[] parts = stockProducto.split(":");
                if (parts.length >= 2) {
                    modelResultados.addRow(new Object[]{
                        parts[0].trim(),
                        parts[1].trim()
                    });
                } else {
                    modelResultados.addRow(new Object[]{stockProducto, ""});
                }
            }
        }
    }

    // Consulta el stock agrupado por ubicación
    private void consultarStockAgrupadoUbicacion() {
        List<String> respuesta = consultasCtrl.agrupadoPorUbicacion();
        
        JTabbedPane tabbedPane = (JTabbedPane) getContentPane().getComponent(0);
        JPanel panel = (JPanel) tabbedPane.getComponent(3);
        DefaultTableModel model = (DefaultTableModel) panel.getClientProperty("tableModel");
        
        model.setRowCount(0);
        
        if (respuesta.isEmpty()) {
            showWarningMessage("No hay stock de ningún producto");
        } else {
            for (String stockUbic : respuesta) {
                // Parsear según formato del controller
                String[] parts = stockUbic.split(":");
                if (parts.length >= 2) {
                    model.addRow(new Object[]{
                        parts[0].trim(),
                        parts[1].trim()
                    });
                } else {
                    model.addRow(new Object[]{stockUbic, ""});
                }
            }
        }
    }
}

