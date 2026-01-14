package view.gui.views;

import controller.ProductoController;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import view.gui.GUIViewBase;

public class ProductoViewGUI extends GUIViewBase {
    private final ProductoController productoCtrl;
    private JTable tableProductos;
    private DefaultTableModel modelProductos;
    private JTable tableBuscarProducto;
    private DefaultTableModel modelBuscarProducto;
    private JTable tableEliminarProducto;
    private DefaultTableModel modelEliminarProducto;
    
    // Campos del formulario
    private JTextField fieldDescripcion;
    private JTextField fieldPeso;
    private JTextField fieldCapacidad;
    private JComboBox<String> comboUnidadMedida;
    private JTextField fieldStockMin;
    private JComboBox<String> comboGrupo;
    private JTextField fieldIdBuscar;
    private JTextField fieldIdEliminar;
    private JTextArea textAreaBuscar;
    private JTextArea textAreaEliminar;

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

        String[] columnNames = {"ID", "Descripción", "Unidad", "Peso (kg)", "Capacidad", "Grupo", "Stock Mín", "Código"};
        modelBuscarProducto = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableBuscarProducto = new JTable(modelBuscarProducto);
        tableBuscarProducto.setFont(FONT_NORMAL);
        tableBuscarProducto.setRowHeight(25);
        tableBuscarProducto.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(tableBuscarProducto);

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

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

        String[] columnNames = {"ID", "Descripción", "Unidad", "Peso (kg)", "Capacidad", "Grupo", "Stock Mín", "Código"};
        modelEliminarProducto = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableEliminarProducto = new JTable(modelEliminarProducto);
        tableEliminarProducto.setFont(FONT_NORMAL);
        tableEliminarProducto.setRowHeight(25);
        tableEliminarProducto.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(tableEliminarProducto);

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void generarLineaProducto(List<String> productos, DefaultTableModel modelProductos) {
        // Parsear cada línea del formato de texto
        for (String producto : productos) {
            String[] parts = producto.split("\\|");
            if (parts.length >= 7) {
                String[] row = new String[8];
                for (int i = 0; i < parts.length && i < 8; i++) {
                    row[i] = parts[i].trim();
                }
                row[7] = row[0];
                row[0] = row[0].substring(4);
                modelProductos.addRow(row);
            }
        }
    }

    private void listarProductos() {
        List<String> productos = productoCtrl.listarProductos();
        modelProductos.setRowCount(0);
        
        if (productos.isEmpty()) {
            showWarningMessage("No se encontraron productos.");
        } else {
            generarLineaProducto(productos, modelProductos);
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
            } else if (respuesta.startsWith("ERROR:")) {
                // Si hay un error (por ejemplo, de permisos), mostrar solo el mensaje de error
                showErrorMessage(respuesta);
            } else {
                // Solo mostrar mensaje de éxito si no hay errores
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
        modelBuscarProducto.setRowCount(0);
        try {
            int id = readIntFromField(fieldIdBuscar);
            String respuesta = productoCtrl.buscarProductoPorId(id);
            List<String> respuestaEnLista = new ArrayList<>();
            respuestaEnLista.add(respuesta);

            if (respuesta == null || respuesta.equals("")) {
                showErrorMessage("No se encontró ningún producto con esa ID.");
            } else {
                generarLineaProducto(respuestaEnLista, modelBuscarProducto);
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Por favor ingrese un ID válido.");
            textAreaBuscar.setText("Error: ID inválido.");
        } catch (Exception e) {
            showErrorMessage("Error al buscar producto: " + e.getMessage());
            textAreaBuscar.setText("Error: " + e.getMessage());
        }
    }

    private void eliminarProducto() {
        modelEliminarProducto.setRowCount(0);
        try {
            int id = readIntFromField(fieldIdEliminar);
            String producto = productoCtrl.buscarProductoPorId(id);
            List<String> productoEnLista = new ArrayList<>();
            productoEnLista.add(producto);

            if (respuesta == null || producto.equals("")) {
                showErrorMessage("No se encontró ningún producto con esa ID.");
            } else {
                generarLineaProducto(productoEnLista, modelEliminarProducto);
                int confirm = showConfirmDialog("¿Está seguro que desea eliminar este producto?", "Confirmar eliminación");
                if (confirm == JOptionPane.YES_OPTION) {
                    productoCtrl.eliminarProducto(id);
                    showSuccessMessage("Producto eliminado correctamente.");
                    modelEliminarProducto.setRowCount(0);
                    fieldIdEliminar.setText("");
                }
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Por favor ingrese un ID válido.");
            textAreaEliminar.setText("Error: ID inválido.");
        } catch (Exception e) {
            showErrorMessage("Error al eliminar producto: " + e.getMessage());
            textAreaEliminar.setText("Error: " + e.getMessage());
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

