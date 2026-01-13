package view.gui.views;

import controller.NaveController;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import view.gui.GUIViewBase;

public class NaveViewGUI extends GUIViewBase {
    private final NaveController naveCtrl;
    private JTable tableNaves;
    private JTable tableRacks;
    private DefaultTableModel modelNaves;
    private DefaultTableModel modelRacks;
    private JTextField fieldIdNave;
    private JTextField fieldIdRack;

    public NaveViewGUI(NaveController naveCtrl) {
        super("Gestión de Naves");
        this.naveCtrl = naveCtrl;
        setupUI();
    }

    // Configura la interfaz de la vista
    private void setupUI() {
        setSize(900, 700);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(COLOR_BACKGROUND);

        // Título
        JLabel titleLabel = createTitleLabel("Gestión de Naves y Racks");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel central con tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_NORMAL);

        // Tab Naves
        tabbedPane.addTab("Naves", createNavesPanel());
        
        // Tab Racks
        tabbedPane.addTab("Racks", createRacksPanel());
        
        // Tab Ubicaciones
        tabbedPane.addTab("Ubicaciones", createUbicacionesPanel());

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

    // Crea el panel para la gestión de naves
    private JPanel createNavesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        
        JButton btnCrearNave = createButton("Crear Nueva Nave", COLOR_SUCCESS);
        JButton btnListarNaves = createButton("Listar Naves", COLOR_PRIMARY);
        
        btnCrearNave.addActionListener(e -> crearNave());
        btnListarNaves.addActionListener(e -> listarNaves());
        
        buttonPanel.add(btnCrearNave);
        buttonPanel.add(btnListarNaves);

        // Tabla de naves
        String[] columnNames = {"ID Nave"};
        modelNaves = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableNaves = new JTable(modelNaves);
        tableNaves.setFont(FONT_NORMAL);
        tableNaves.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tableNaves);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Naves"));

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Crea el panel para la gestión de racks
    private JPanel createRacksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        // Panel de controles
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(createLabel("ID Nave:"), gbc);
        gbc.gridx = 1;
        fieldIdNave = createTextField(10);
        controlPanel.add(fieldIdNave, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JButton btnCrearRack = createButton("Crear Rack", COLOR_SUCCESS);
        btnCrearRack.addActionListener(e -> crearRack());
        controlPanel.add(btnCrearRack, gbc);

        gbc.gridx = 1;
        JButton btnListarRacks = createButton("Listar Racks", COLOR_PRIMARY);
        btnListarRacks.addActionListener(e -> listarRacks());
        controlPanel.add(btnListarRacks, gbc);

        // Tabla de racks
        String[] columnNames = {"ID Nave", "ID Rack"};
        modelRacks = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableRacks = new JTable(modelRacks);
        tableRacks.setFont(FONT_NORMAL);
        tableRacks.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tableRacks);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Racks"));

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Crea el panel para la gestión de ubicaciones
    private JPanel createUbicacionesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(COLOR_BACKGROUND);

        // Panel de controles
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setOpaque(false);
        
        controlPanel.add(createLabel("ID Rack:"));
        fieldIdRack = createTextField(10);
        controlPanel.add(fieldIdRack);
        
        JButton btnListarUbicaciones = createButton("Listar Ubicaciones", COLOR_PRIMARY);
        btnListarUbicaciones.addActionListener(e -> listarUbicaciones());
        controlPanel.add(btnListarUbicaciones);
        
        JButton btnPesoRack = createButton("Ver Peso del Rack", COLOR_WARNING);
        btnPesoRack.addActionListener(e -> mostrarPesoRack());
        controlPanel.add(btnPesoRack);

        // Área de texto para resultados
        JTextArea textArea = new JTextArea(15, 50);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createTitledBorder("Resultados"));
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Guardar referencia al textArea para usarla en los métodos
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Guardar referencia para uso en métodos
        panel.putClientProperty("textArea", textArea);

        return panel;
    }

    // Crea una nueva nave
    private void crearNave() {
        int id = naveCtrl.crearNave();
        showSuccessMessage("Nave creada con ID " + id);
        listarNaves();
    }

    // Lista las naves
    private void listarNaves() {
        List<String> naves = naveCtrl.listarNaves();
        modelNaves.setRowCount(0);
        
        if (naves.isEmpty()) {
            showWarningMessage("No hay naves registradas.");
        } else {
            for (String nave : naves) {
                // Extraer ID de la cadena (formato: "Nave ID: X")
                String id = nave.replaceAll("[^0-9]", "");
                if (!id.isEmpty()) {
                    modelNaves.addRow(new Object[]{id});
                }
            }
        }
    }

    // Crea un nuevo rack
    private void crearRack() {
        try {
            int idNave = readIntFromField(fieldIdNave);
            int idRack = naveCtrl.crearRack(idNave);
            
            if (idRack == 0) {
                showErrorMessage("No se encontró nave con ID " + idNave);
            } else {
                showSuccessMessage("Rack de ID " + idRack + " creado con éxito.");
                fieldIdNave.setText("");
                listarRacks();
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Por favor ingrese un ID de nave válido.");
        }
    }

    // Lista los racks
    private void listarRacks() {
        try {
            int idNave = readIntFromField(fieldIdNave);
            List<String> racks = naveCtrl.listarRacks(idNave);
            modelRacks.setRowCount(0);
            
            if (racks.isEmpty()) {
                showWarningMessage("No hay racks registrados en la nave " + idNave);
            } else {
                for (String rack : racks) {
                    // Extraer IDs del formato "Rack ID: X"
                    String[] parts = rack.split(":");
                    if (parts.length > 1) {
                        String id = parts[1].trim().replaceAll("[^0-9]", "");
                        if (!id.isEmpty()) {
                            modelRacks.addRow(new Object[]{idNave, id});
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Por favor ingrese un ID de nave válido.");
        }
    }

    // Lista las ubicaciones
    private void listarUbicaciones() {
        try {
            int idRack = readIntFromField(fieldIdRack);
            List<String> ubicaciones = naveCtrl.listarUbicacionesRack(idRack);
            
            JPanel ubicacionesPanel = (JPanel) ((JTabbedPane) ((JPanel) getContentPane().getComponent(0)).getComponent(1)).getComponent(2);
            JTextArea textArea = (JTextArea) ((JScrollPane) ubicacionesPanel.getComponent(1)).getViewport().getView();
            
            if (ubicaciones.isEmpty()) {
                showErrorMessage("No hay rack con esa ID");
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("NAVE-RACK-FILA-COL:\n");
                sb.append("N-R-F-C\n");
                sb.append("--------------------\n");
                for (String u : ubicaciones) {
                    sb.append(u).append("\n");
                }
                textArea.setText(sb.toString());
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Por favor ingrese un ID de rack válido.");
        }
    }

    // Muestra el peso del rack
    private void mostrarPesoRack() {
        try {
            int idRack = readIntFromField(fieldIdRack);
            List<String> pesos = naveCtrl.calcularPesosRack(idRack);
            
            JPanel ubicacionesPanel = (JPanel) ((JTabbedPane) getContentPane().getComponent(0)).getComponent(2);
            JTextArea textArea = (JTextArea) ((JScrollPane) ubicacionesPanel.getComponent(1)).getViewport().getView();
            
            if (pesos.isEmpty()) {
                textArea.setText("No se encontró un rack con ese ID");
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("==== Peso de ubicaciones rack ").append(idRack).append(" ====\n");
                for (int i = pesos.size() - 1; i >= 0; i--) {
                    sb.append(pesos.get(i)).append("\n");
                }
                textArea.setText(sb.toString());
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Por favor ingrese un ID de rack válido.");
        }
    }
}

