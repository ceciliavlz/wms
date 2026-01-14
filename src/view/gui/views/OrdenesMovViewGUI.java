package view.gui.views;

import controller.MovimientoController;
import controller.ProductoController;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import view.gui.GUIViewBase;

public class OrdenesMovViewGUI extends GUIViewBase {
    private final MovimientoController movimientoCtrl;
    private final ProductoController productoCtrl;
    
    private JTextField fieldIdProducto;
    private JTextField fieldUbicacion;
    private JTextField fieldCantidad;
    private JTextArea textAreaProducto;

    public OrdenesMovViewGUI(MovimientoController movimientoCtrl, ProductoController productoCtrl) {
        super("Órdenes de Movimiento");
        this.movimientoCtrl = movimientoCtrl;
        this.productoCtrl = productoCtrl;
        setupUI();
    }

    // Configura la interfaz de la vista
    private void setupUI() {
        setSize(900, 700);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(COLOR_BACKGROUND);

        JLabel titleLabel = createTitleLabel("Órdenes de Movimiento");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_NORMAL);

        tabbedPane.addTab("Ingreso", createIngresoPanel());
        tabbedPane.addTab("Egreso", createEgresoPanel());
        tabbedPane.addTab("Traslado Interno", createTrasladoPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        JButton btnCerrar = createButton("Cerrar", COLOR_ERROR);
        btnCerrar.addActionListener(e -> dispose());
        footerPanel.add(btnCerrar);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Crea el panel para la generación de ordenes de ingreso
    private JPanel createIngresoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints(); 
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // ID Producto
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("ID de producto:"), gbc);
        gbc.gridx = 1;
        fieldIdProducto = createTextField(15);
        formPanel.add(fieldIdProducto, gbc);
        gbc.gridx = 2;
        JButton btnBuscar = createButton("Buscar", COLOR_PRIMARY);
        btnBuscar.addActionListener(e -> buscarProducto("ingreso"));
        formPanel.add(btnBuscar, gbc);

        // Crea el área de información del producto
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        textAreaProducto = new JTextArea(3, 40);
        textAreaProducto.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textAreaProducto.setEditable(false);
        textAreaProducto.setBorder(BorderFactory.createTitledBorder("Información del Producto"));
        formPanel.add(new JScrollPane(textAreaProducto), gbc);

        // Crea el campo para la ubicación destino
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Ubicación destino (N-R-F-C):"), gbc);
        gbc.gridx = 1;
        fieldUbicacion = createTextField(20);
        formPanel.add(fieldUbicacion, gbc);

        // Crea el campo para la cantidad
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createLabel("Cantidad a ingresar:"), gbc);
        gbc.gridx = 1;
        fieldCantidad = createTextField(15);
        formPanel.add(fieldCantidad, gbc);

        // Crea el botón para la generación de ordenes de ingreso
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        JButton btnGenerar = createButton("Generar Orden de Ingreso", COLOR_SUCCESS);
        btnGenerar.addActionListener(e -> generarIngreso());
        buttonPanel.add(btnGenerar);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Crea el panel para la generación de ordenes de egreso
    private JPanel createEgresoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // ID Producto
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("ID de producto:"), gbc);
        gbc.gridx = 1;
        JTextField fieldIdProdEgreso = createTextField(15);
        formPanel.add(fieldIdProdEgreso, gbc);
        gbc.gridx = 2;
        JButton btnBuscar = createButton("Buscar", COLOR_PRIMARY);
        btnBuscar.addActionListener(e -> {
            try {
                int id = readIntFromField(fieldIdProdEgreso);
                buscarProductoYUbicaciones(id, "egreso");
            } catch (NumberFormatException ex) {
                showErrorMessage("Ingrese un ID válido.");
            }
        });
        formPanel.add(btnBuscar, gbc);

        // Área de información
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        JTextArea textAreaInfo = new JTextArea(5, 40);
        textAreaInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textAreaInfo.setEditable(false);
        textAreaInfo.setBorder(BorderFactory.createTitledBorder("Información del Producto y Ubicaciones"));
        formPanel.add(new JScrollPane(textAreaInfo), gbc);

        // Ubicación
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Ubicación (N-R-F-C):"), gbc);
        gbc.gridx = 1;
        JTextField fieldUbicacionEgreso = createTextField(20);
        formPanel.add(fieldUbicacionEgreso, gbc);

        // Cantidad
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createLabel("Cantidad a quitar:"), gbc);
        gbc.gridx = 1;
        JTextField fieldCantidadEgreso = createTextField(15);
        formPanel.add(fieldCantidadEgreso, gbc);

        // Botón
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        JButton btnGenerar = createButton("Generar Orden de Egreso", COLOR_ERROR);
        btnGenerar.addActionListener(e -> {
            try {
                int id = readIntFromField(fieldIdProdEgreso);
                String ubicacion = readStringFromField(fieldUbicacionEgreso);
                int cantidad = readIntFromField(fieldCantidadEgreso);
                generarEgreso(id, ubicacion, cantidad);
            } catch (Exception ex) {
                showErrorMessage("Error: " + ex.getMessage());
            }
        });
        buttonPanel.add(btnGenerar);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTrasladoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // ID Producto
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("ID de producto:"), gbc);
        gbc.gridx = 1;
        JTextField fieldIdProdTraslado = createTextField(15);
        formPanel.add(fieldIdProdTraslado, gbc);
        gbc.gridx = 2;
        JButton btnBuscar = createButton("Buscar", COLOR_PRIMARY);
        btnBuscar.addActionListener(e -> {
            try {
                int id = readIntFromField(fieldIdProdTraslado);
                buscarProductoYUbicaciones(id, "traslado");
            } catch (NumberFormatException ex) {
                showErrorMessage("Ingrese un ID válido.");
            }
        });
        formPanel.add(btnBuscar, gbc);

        // Área de información
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        JTextArea textAreaInfo = new JTextArea(5, 40);
        textAreaInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textAreaInfo.setEditable(false);
        textAreaInfo.setBorder(BorderFactory.createTitledBorder("Información del Producto y Ubicaciones"));
        formPanel.add(new JScrollPane(textAreaInfo), gbc);

        // Ubicación origen
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Ubicación origen (N-R-F-C):"), gbc);
        gbc.gridx = 1;
        JTextField fieldOrigen = createTextField(20);
        formPanel.add(fieldOrigen, gbc);

        // Ubicación destino
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createLabel("Ubicación destino (N-R-F-C):"), gbc);
        gbc.gridx = 1;
        JTextField fieldDestino = createTextField(20);
        formPanel.add(fieldDestino, gbc);

        // Cantidad
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createLabel("Cantidad a mover:"), gbc);
        gbc.gridx = 1;
        JTextField fieldCantidadTraslado = createTextField(15);
        formPanel.add(fieldCantidadTraslado, gbc);

        // Botón
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        JButton btnGenerar = createButton("Generar Orden de Traslado", COLOR_WARNING);
        btnGenerar.addActionListener(e -> {
            try {
                int id = readIntFromField(fieldIdProdTraslado);
                String origen = readStringFromField(fieldOrigen);
                String destino = readStringFromField(fieldDestino);
                int cantidad = readIntFromField(fieldCantidadTraslado);
                generarTraslado(id, origen, destino, cantidad);
            } catch (Exception ex) {
                showErrorMessage("Error: " + ex.getMessage());
            }
        });
        buttonPanel.add(btnGenerar);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void buscarProducto(String tipo) {
        try {
            int id = readIntFromField(fieldIdProducto);
            String producto = productoCtrl.buscarProductoPorId(id);
            if (producto.equals("")) {
                textAreaProducto.setText("No se encontró ningún producto con esa ID.");
            } else {
                textAreaProducto.setText(producto);
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Por favor ingrese un ID válido.");
        }
    }

    private void buscarProductoYUbicaciones(int id, String tipo) {
        String producto = productoCtrl.buscarProductoPorId(id);
        if (producto.equals("")) {
            showErrorMessage("No se encontró ningún producto con esa ID.");
            return;
        }
        
        List<String> ubicaciones = productoCtrl.mostrarUbicacionesDeProducto(id);
        StringBuilder sb = new StringBuilder();
        sb.append(producto).append("\n\nUbicado en:\n");
        for (String u : ubicaciones) {
            sb.append(u).append("\n");
        }
        
        // Actualizar el textArea correspondiente según el tab
        
        JTabbedPane tabbedPane = (JTabbedPane) ((JPanel) getContentPane().getComponent(0)).getComponent(1);
        int tabIndex = tipo.equals("egreso") ? 1 : 2;
        JPanel panel = (JPanel) tabbedPane.getComponent(tabIndex);
        JScrollPane scrollPane = (JScrollPane) ((JPanel) panel.getComponent(0)).getComponent(3);
        JTextArea textArea = (JTextArea) scrollPane.getViewport().getView();
        textArea.setText(sb.toString());
    }

    private void generarIngreso() {
        try {
            int id = readIntFromField(fieldIdProducto);
            String producto = productoCtrl.buscarProductoPorId(id);
            
            if (producto.equals("")) {
                showErrorMessage("No se encontró ningún producto con esa ID.");
                return;
            }
            
            String ubicacion = readStringFromField(fieldUbicacion);
            int cantidad = readIntFromField(fieldCantidad);
            
            String respuesta = movimientoCtrl.generarOrdenIngreso(id, ubicacion, cantidad);
            showSuccessMessage(respuesta);
            
            // Limpiar campos
            fieldUbicacion.setText("");
            fieldCantidad.setText("");
        } catch (Exception e) {
            showErrorMessage("Error: " + e.getMessage());
        }
    }

    private void generarEgreso(int id, String ubicacion, int cantidad) {
        String respuesta = movimientoCtrl.generarOrdenEgreso(id, ubicacion, cantidad);
        showSuccessMessage(respuesta);
    }

    private void generarTraslado(int id, String origen, String destino, int cantidad) {
        String respuesta = movimientoCtrl.generarOrdenMovInterno(id, origen, destino, cantidad);
        showSuccessMessage(respuesta);
    }
}

