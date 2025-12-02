package view.gui.views;

import view.gui.GUIViewBase;
import controller.TransformacionController;
import model.OrdenTransformacion;
import model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class TransformacionViewGUI extends GUIViewBase {
    private final TransformacionController transfCtrl;
    
    private JTextField fieldIdProdEntrada;
    private JTextField fieldUbicEntrada;
    private JTextField fieldCantEntrada;
    private JTextField fieldDescripcionTransformado;
    private JTextField fieldPesoTransformado;
    private JTextField fieldCapacidadTransformado;
    private JTextField fieldUbicSalida;
    private JTextField fieldStockMinimo;
    private JTable tableHistorial;
    private DefaultTableModel modelHistorial;

    public TransformacionViewGUI(TransformacionController transfCtrl) {
        super("Órdenes de Transformación");
        this.transfCtrl = transfCtrl;
        setupUI();
    }

    private void setupUI() {
        setSize(1000, 750);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(COLOR_BACKGROUND);

        JLabel titleLabel = createTitleLabel("Órdenes de Transformación");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_NORMAL);

        tabbedPane.addTab("Registrar Transformación", createRegistrarPanel());
        tabbedPane.addTab("Historial", createHistorialPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        JButton btnCerrar = createButton("Cerrar", COLOR_ERROR);
        btnCerrar.addActionListener(e -> dispose());
        footerPanel.add(btnCerrar);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createRegistrarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Producto de entrada
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("ID producto de entrada:"), gbc);
        gbc.gridx = 1;
        fieldIdProdEntrada = createTextField(15);
        formPanel.add(fieldIdProdEntrada, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createLabel("Ubicación producto entrada:"), gbc);
        gbc.gridx = 1;
        fieldUbicEntrada = createTextField(20);
        formPanel.add(fieldUbicEntrada, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createLabel("Cantidad unidades entrada:"), gbc);
        gbc.gridx = 1;
        fieldCantEntrada = createTextField(15);
        formPanel.add(fieldCantEntrada, gbc);

        // Separador
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        // Producto transformado
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createLabel("Nombre producto transformado:"), gbc);
        gbc.gridx = 1;
        fieldDescripcionTransformado = createTextField(30);
        formPanel.add(fieldDescripcionTransformado, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(createLabel("Peso unitario transformado (kg):"), gbc);
        gbc.gridx = 1;
        fieldPesoTransformado = createTextField(15);
        formPanel.add(fieldPesoTransformado, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(createLabel("Capacidad nuevo contenedor:"), gbc);
        gbc.gridx = 1;
        fieldCapacidadTransformado = createTextField(15);
        formPanel.add(fieldCapacidadTransformado, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(createLabel("Stock mínimo producto transformado:"), gbc);
        gbc.gridx = 1;
        fieldStockMinimo = createTextField(15);
        formPanel.add(fieldStockMinimo, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(createLabel("Ubicación de salida:"), gbc);
        gbc.gridx = 1;
        fieldUbicSalida = createTextField(20);
        formPanel.add(fieldUbicSalida, gbc);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        JButton btnRegistrar = createButton("Registrar Transformación", COLOR_SUCCESS);
        btnRegistrar.addActionListener(e -> registrarTransformacion());
        JButton btnLimpiar = createButton("Limpiar", COLOR_WARNING);
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        buttonPanel.add(btnRegistrar);
        buttonPanel.add(btnLimpiar);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createHistorialPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        JButton btnCargar = createButton("Cargar Historial", COLOR_PRIMARY);
        btnCargar.addActionListener(e -> cargarHistorial());

        String[] columnNames = {"ID Orden", "Fecha", "Prod Entrada", "Prod Salida", "Cant Entrada", "Cant Salida", "Ubic Entrada", "Ubic Salida"};
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
        scrollPane.setBorder(BorderFactory.createTitledBorder("Historial de Transformaciones"));

        panel.add(btnCargar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void registrarTransformacion() {
        try {
            int idProdEntrada = readIntFromField(fieldIdProdEntrada);
            String ubicEntrada = readStringFromField(fieldUbicEntrada);
            int cantEntrada = readIntFromField(fieldCantEntrada);
            String descripcionTransformado = readStringFromField(fieldDescripcionTransformado);
            double pesoTransformado = readDoubleFromField(fieldPesoTransformado);
            int capacidadTransformado = readIntFromField(fieldCapacidadTransformado);
            String ubicSalida = readStringFromField(fieldUbicSalida);
            int stockMinimo = readIntFromField(fieldStockMinimo);

            LocalDate fecha = LocalDate.now();

            Producto productoTransformadoTemporal = new Producto(0, descripcionTransformado, pesoTransformado, 
                capacidadTransformado, stockMinimo, "Producto reenvasado");

            String resultado = transfCtrl.procesarTransformacion(
                idProdEntrada, ubicEntrada, cantEntrada, productoTransformadoTemporal, 
                ubicSalida, fecha
            );

            showSuccessMessage("Resultado: " + resultado);
            limpiarFormulario();
        } catch (Exception e) {
            showErrorMessage("Error: " + e.getMessage());
        }
    }

    private void cargarHistorial() {
        List<OrdenTransformacion> historial = transfCtrl.obtenerHistorial();
        modelHistorial.setRowCount(0);

        if (historial.isEmpty()) {
            showWarningMessage("No hay transformaciones registradas.");
        } else {
            for (OrdenTransformacion o : historial) {
                modelHistorial.addRow(new Object[]{
                    o.getIdOrdenTransf(),
                    o.getFecha(),
                    "P" + o.getIdProductoEntrada(),
                    "P" + o.getIdProductoTransformado(),
                    o.getCantidadEntrada(),
                    o.getCantidadSalida(),
                    o.getUbicacionProdEntrada(),
                    o.getUbicacionSalida()
                });
            }
        }
    }

    private void limpiarFormulario() {
        fieldIdProdEntrada.setText("");
        fieldUbicEntrada.setText("");
        fieldCantEntrada.setText("");
        fieldDescripcionTransformado.setText("");
        fieldPesoTransformado.setText("");
        fieldCapacidadTransformado.setText("");
        fieldUbicSalida.setText("");
        fieldStockMinimo.setText("");
    }
}




