package view.gui.views;

import view.gui.GUIViewBase;
import controller.ProductoController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductoViewGUI extends GUIViewBase {
    private final ProductoController productoCtrl;
    private JTable tableProductos;
    private DefaultTableModel modelProductos;
    
    // Campos del formulario
    private JTextField fieldDescripcion;
    private JTextField fieldPeso;
    private JTextField fieldCapacidad;
    private JComboBox<String> comboUnidadMedida;
    private JTextField fieldStockMin;
    private JComboBox<String> comboGrupo;
    private JTextField fieldIdBuscar;
    private JTextField fieldIdEliminar;

    public ProductoViewGUI(ProductoController productoCtrl) {
        super("Gestión de Productos");
        this.productoCtrl = productoCtrl;
        setupUI();
    }

    private void setupUI() {
        setSize(1000, 750);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(COLOR_BACKGROUND);

        // Título
        JLabel titleLabel = createTitleLabel("Gestión de Productos");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel central con tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_NORMAL);

        tabbedPane.addTab("Listar Productos", createListarPanel());
        tabbedPane.addTab("Agregar Producto", createAgregarPanel());
        tabbedPane.addTab("Buscar Producto", createBuscarPanel());
        tabbedPane.addTab("Eliminar Producto", createEliminarPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Botón cerrar
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        JButton btnCerrar = createButton("Cerrar", COLOR_ERROR);
        btnCerrar.addActionListener(e -> dispose());
        footerPanel.add(btnCerrar);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createListarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        JButton btnListar = createButton("Listar Todos los Productos", COLOR_PRIMARY);
        btnListar.addActionListener(e -> listarProductos());

        String[] columnNames = {"ID", "Descripción", "Unidad", "Peso (kg)", "Capacidad", "Grupo", "Stock Mín", "Código"};
        modelProductos = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableProductos = new JTable(modelProductos);
        tableProductos.setFont(FONT_NORMAL);
        tableProductos.setRowHeight(25);
        tableProductos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(tableProductos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Productos"));

        panel.add(btnListar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAgregarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Descripción
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        fieldDescripcion = createTextField(30);
        formPanel.add(fieldDescripcion, gbc);

        // Peso
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createLabel("Peso unitario (kg):"), gbc);
        gbc.gridx = 1;
        fieldPeso = createTextField(15);
        formPanel.add(fieldPeso, gbc);

        // Capacidad
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createLabel("Capacidad del contenedor:"), gbc);
        gbc.gridx = 1;
        fieldCapacidad = createTextField(15);
        formPanel.add(fieldCapacidad, gbc);

        // Unidad de medida
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createLabel("Unidad de medida:"), gbc);
        gbc.gridx = 1;
        comboUnidadMedida = new JComboBox<>(new String[]{"LITROS", "KILOS", "UNIDAD", "GRAMOS"});
        comboUnidadMedida.setFont(FONT_NORMAL);
        formPanel.add(comboUnidadMedida, gbc);

        // Stock mínimo
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createLabel("Stock mínimo:"), gbc);
        gbc.gridx = 1;
        fieldStockMin = createTextField(10);
        formPanel.add(fieldStockMin, gbc);

        // Grupo
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(createLabel("Grupo:"), gbc);
        gbc.gridx = 1;
        comboGrupo = new JComboBox<>(new String[]{"Materia prima", "Producto final"});
        comboGrupo.setFont(FONT_NORMAL);
        formPanel.add(comboGrupo, gbc);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        JButton btnAgregar = createButton("Agregar Producto", COLOR_SUCCESS);
        btnAgregar.addActionListener(e -> agregarProducto());
        JButton btnLimpiar = createButton("Limpiar", COLOR_WARNING);
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        buttonPanel.add(btnAgregar);
        buttonPanel.add(btnLimpiar);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBuscarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setOpaque(false);
        controlPanel.add(createLabel("ID del producto:"));
        fieldIdBuscar = createTextField(10);
        controlPanel.add(fieldIdBuscar);
        JButton btnBuscar = createButton("Buscar", COLOR_PRIMARY);
        btnBuscar.addActionListener(e -> buscarProducto());
        controlPanel.add(btnBuscar);

        JTextArea textArea = new JTextArea(10, 50);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createTitledBorder("Resultado"));
        JScrollPane scrollPane = new JScrollPane(textArea);

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.putClientProperty("textArea", textArea);

        return panel;
    }

    private JPanel createEliminarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setOpaque(false);
        controlPanel.add(createLabel("ID del producto a eliminar:"));
        fieldIdEliminar = createTextField(10);
        controlPanel.add(fieldIdEliminar);
        JButton btnEliminar = createButton("Eliminar", COLOR_ERROR);
        btnEliminar.addActionListener(e -> eliminarProducto());
        controlPanel.add(btnEliminar);

        JTextArea textArea = new JTextArea(10, 50);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createTitledBorder("Información del Producto"));
        JScrollPane scrollPane = new JScrollPane(textArea);

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.putClientProperty("textArea", textArea);

        return panel;
    }

    private void listarProductos() {
        List<String> productos = productoCtrl.listarProductos();
        modelProductos.setRowCount(0);
        
        if (productos.isEmpty()) {
            showWarningMessage("No se encontraron productos.");
        } else {
            // Parsear cada línea del formato de texto
            for (String producto : productos) {
                String[] parts = producto.split("\\|");
                if (parts.length >= 7) {
                    Object[] row = new Object[8];
                    for (int i = 0; i < parts.length && i < 8; i++) {
                        row[i] = parts[i].trim();
                    }
                    modelProductos.addRow(row);
                }
            }
        }
    }

    private void agregarProducto() {
        try {
            String descripcion = readStringFromField(fieldDescripcion);
            double peso = readDoubleFromField(fieldPeso);
            double capacidad = readDoubleFromField(fieldCapacidad);
            String unidadMedida = (String) comboUnidadMedida.getSelectedItem();
            int stockMin = readIntFromField(fieldStockMin);
            String grupo = (String) comboGrupo.getSelectedItem();

            String respuesta = productoCtrl.agregarProducto(descripcion, unidadMedida, peso, capacidad, stockMin, grupo);

            if (respuesta.equals("")) {
                showErrorMessage("Error al crear producto.");
            } else {
                showSuccessMessage(respuesta + "\nProducto registrado correctamente.");
                limpiarFormulario();
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Error en los datos numéricos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showErrorMessage(e.getMessage());
        }
    }

    private void buscarProducto() {
        try {
            int id = readIntFromField(fieldIdBuscar);
            String respuesta = productoCtrl.buscarProductoPorId(id);

            JPanel buscarPanel = (JPanel) ((JTabbedPane) ((JPanel) getContentPane().getComponent(0)).getComponent(1)).getComponent(2);
            JTextArea textArea = (JTextArea) ((JScrollPane) buscarPanel.getComponent(1)).getViewport().getView();

            if (respuesta.equals("")) {
                textArea.setText("No se encontró ningún producto con esa ID.");
            } else {
                textArea.setText(respuesta);
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Por favor ingrese un ID válido.");
        }
    }

    private void eliminarProducto() {
        try {
            int id = readIntFromField(fieldIdEliminar);
            String producto = productoCtrl.buscarProductoPorId(id);

            JPanel eliminarPanel = (JPanel) ((JTabbedPane) getContentPane().getComponent(0)).getComponent(3);
            JTextArea textArea = (JTextArea) ((JScrollPane) eliminarPanel.getComponent(1)).getViewport().getView();

            if (producto.equals("")) {
                textArea.setText("No se encontró ningún producto con esa ID.");
            } else {
                textArea.setText(producto);
                int confirm = showConfirmDialog("¿Está seguro que desea eliminar este producto?", "Confirmar eliminación");
                if (confirm == JOptionPane.YES_OPTION) {
                    productoCtrl.eliminarProducto(id);
                    showSuccessMessage("Producto eliminado correctamente.");
                    textArea.setText("");
                    fieldIdEliminar.setText("");
                }
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Por favor ingrese un ID válido.");
        }
    }

    private void limpiarFormulario() {
        fieldDescripcion.setText("");
        fieldPeso.setText("");
        fieldCapacidad.setText("");
        fieldStockMin.setText("");
        comboUnidadMedida.setSelectedIndex(0);
        comboGrupo.setSelectedIndex(0);
    }
}

