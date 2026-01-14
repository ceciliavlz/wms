package view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import model.User;
import services.AuthService;
import services.StockService;
import services.TransformacionService;
import services.MovimientoService;
import services.NaveService;
import services.PermissionService;
import services.RackService;
import controller.ConsultasController;
import controller.HistorialController;
import controller.MovimientoController;
import controller.NaveController;
import controller.ProductoController;
import controller.TransformacionController;
import view.gui.views.*;

public class MainGUI extends JFrame {
    private StockService stockService;
    private RackService rackService;
    private NaveService naveService;
    private MovimientoService movService;
    private TransformacionService transfService;
    private AuthService authService;
    private PermissionService permissionService;
    private User usuarioActual;
    
    private HistorialController historialCtrl;
    private ProductoController productoCtrl;
    private MovimientoController movimientoCtrl;
    private NaveController naveCtrl;
    private ConsultasController consultasCtrl;
    private TransformacionController transfController;

    public MainGUI() {
        // Primero autenticar
        authService = new AuthService();
        LoginGUI loginGUI = new LoginGUI(null, authService);
        usuarioActual = loginGUI.mostrarLogin();
        
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(null,
                "No se pudo autenticar. Saliendo...",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return;
        }

        permissionService = new PermissionService(usuarioActual);
        initializeServices();
        initializeControllers();
        setupUI();
    }

    private void initializeServices() {
        stockService = new StockService();
        rackService = new RackService(stockService);
        naveService = new NaveService(rackService);
        movService = new MovimientoService(stockService);
        transfService = new TransformacionService(stockService);
        
        // Configurar usuario actual en servicios para auditoría
        movService.setUsuarioActual(usuarioActual.getUsername());
        transfService.setUsuarioActual(usuarioActual.getUsername());
    }

    private void initializeControllers() {
        historialCtrl = new HistorialController(movService, transfService);
        productoCtrl = new ProductoController(stockService);
        movimientoCtrl = new MovimientoController(movService);
        naveCtrl = new NaveController(naveService, stockService);
        consultasCtrl = new ConsultasController(stockService);
        transfController = new TransformacionController(transfService, stockService);
        
        // Configurar permisos en controladores
        productoCtrl.setPermissionService(permissionService);
        movimientoCtrl.setPermissionService(permissionService);
        naveCtrl.setPermissionService(permissionService);
        transfController.setPermissionService(permissionService);
    }

    private void setupUI() {
        setTitle("Sistema de Gestión de Almacenes (WMS) - Usuario: " + usuarioActual.getUsername() + " (" + usuarioActual.getRole() + ")");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Configura el cierre de la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                guardarDatosYSalir();
            }
        });

        // Panel principal!
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(250, 248, 245));

        // Título
        JLabel titleLabel = new JLabel("Sistema de Gestión de Almacenes", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel de botones con grid
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        buttonPanel.setOpaque(false);

        // Crea los botones de la interfaz
        JButton btnNaves = createMenuButton("NAVES", new Color(227, 98, 30));
        JButton btnProductos = createMenuButton("PRODUCTOS", new Color(88, 177, 159));
        JButton btnOrdenesMov = createMenuButton("ÓRDENES DE MOVIMIENTO", new Color(247, 183, 49));      
        JButton btnTransformaciones = createMenuButton("TRANSFORMACIONES", new Color(142, 97, 153));
        JButton btnHistorial = createMenuButton("HISTORIAL", new Color(200, 68, 54));
        JButton btnConsultas = createMenuButton("CONSULTAS", new Color(79, 70, 60));

        // Agrega los listeners a los botones
        btnNaves.addActionListener(e -> abrirNaveView());
        btnProductos.addActionListener(e -> abrirProductoView());
        btnOrdenesMov.addActionListener(e -> abrirOrdenesMovView());
        btnTransformaciones.addActionListener(e -> abrirTransformacionView());
        btnHistorial.addActionListener(e -> abrirHistorialView());
        btnConsultas.addActionListener(e -> abrirConsultasView());

        buttonPanel.add(btnNaves);
        buttonPanel.add(btnProductos);
        buttonPanel.add(btnOrdenesMov);
        buttonPanel.add(btnTransformaciones);
        buttonPanel.add(btnHistorial);
        buttonPanel.add(btnConsultas);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Botón salir en la parte inferior
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnSalir.setPreferredSize(new Dimension(120, 35));
        btnSalir.setOpaque(true); // Para que el botón tenga fondo en iOS 
        btnSalir.setContentAreaFilled(true);
        btnSalir.setBackground(new Color(200, 68, 54));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setBorderPainted(false);
        btnSalir.addActionListener(e -> guardarDatosYSalir());
        footerPanel.add(btnSalir);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createMenuButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(300, 80));
        button.setOpaque(true); // Para que el botón tenga fondo en iOS 
        button.setContentAreaFilled(true);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover para el botón
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

    private void abrirNaveView() {
        NaveViewGUI naveView = new NaveViewGUI(naveCtrl);
        naveView.setVisible(true);
    }

    private void abrirProductoView() {
        ProductoViewGUI productoView = new ProductoViewGUI(productoCtrl);
        productoView.setVisible(true);
    }

    private void abrirOrdenesMovView() {
        OrdenesMovViewGUI ordenesMovView = new OrdenesMovViewGUI(movimientoCtrl, productoCtrl);
        ordenesMovView.setVisible(true);
    }

    private void abrirTransformacionView() {
        TransformacionViewGUI transfView = new TransformacionViewGUI(transfController);
        transfView.setVisible(true);
    }

    private void abrirHistorialView() {
        HistorialViewGUI historialView = new HistorialViewGUI(historialCtrl);
        historialView.setVisible(true);
    }

    private void abrirConsultasView() {
        ConsultasViewGUI consultasView = new ConsultasViewGUI(consultasCtrl, productoCtrl);
        consultasView.setVisible(true);
    }

    private void guardarDatosYSalir() {
        int option = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro que desea salir?",
            "Confirmar salida",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            try {
                stockService.guardarEnArchivo();
                movService.guardarHistorial();
                JOptionPane.showMessageDialog(
                    this,
                    "Datos guardados correctamente.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
                );
                System.exit(0);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    public static void main(String[] args) {
        // Establecer Look and Feel que respete los colores personalizados
        try {
            // Intentar usar Nimbus (más moderno y respeta colores)
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Si Nimbus no está disponible, usar Metal (Look and Feel por defecto de Java)
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(() -> {
            MainGUI mainGUI = new MainGUI();
            mainGUI.setVisible(true);
        });
    }
}

