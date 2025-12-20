package view.JPanels;

import controller.MovimientoController;
import controller.NaveController;
import controller.ProductoController;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import view.PrincipalJFrame;

/**
 *
 * @author Ce___
 */
public class OrdenIngreso extends javax.swing.JPanel {
    private final NaveController naveCtrl;
    private final MovimientoController movimientoCtrl;
    
    private PrincipalJFrame frame;
    private final ProductoController productoCtrl;
    private JPanel botonUbicacionElegido;
    private List<Integer> productos;
    private List<Integer> racksId;
    
//datos orden    
    private int idRack;
    private String idNave;
    private String ubicacionElegida;
    private int idProducto;
    private int cantidadCargada;
    private LocalDate fecha;
    
    
    /**
     * Creates new form BuscarProducto
     */
    public OrdenIngreso(PrincipalJFrame frame, ProductoController productoCtrl, NaveController naveCtrl, 
            MovimientoController movimientoCtrl) {
        initComponents();
        panelSinProductos3.setVisible(false);
        SwingUtilities.invokeLater(() -> visibleUbics(false));
        this.productoCtrl = productoCtrl;
        this.naveCtrl = naveCtrl;
        this.movimientoCtrl = movimientoCtrl;
        this.frame = frame;
        botonUbicacionElegido = f1c1;
        
        cargarProductos();
        cargarRacks();  
        nombrePanels();
    }
    
    private void visibleUbics(boolean visible){
        panelUbicaciones.setVisible(visible);
        ingrese3.setVisible(visible);
        ubicacionSelec.setVisible(visible);
    }
    
    private String crearOrdenIngreso(){
        String respuesta = movimientoCtrl.generarOrdenIngresoFechaIngresada(idProducto, ubicacionElegida, cantidadCargada, fecha);
        return respuesta;
    }
    
    private void cargarRacks(){
        racksId = naveCtrl.listarTodosLosRacks();
        racksComboBox.removeAllItems();
        if (racksId.isEmpty()){
            ingrese3.setVisible(true);
            ingrese3.setText("No hay racks o naves cargados en el programa");
            return;
        }
        for (int id : racksId){
            int idNaveAct = naveCtrl.naveDeRack(id);
            racksComboBox.addItem("NAVE "+idNaveAct+ ", ID " + id);
        }
    }
    
    private void cargarProductos() {
        List<String> prods = productoCtrl.listarProductos();
        productoComboBox.removeAllItems();

        if (prods.isEmpty()) {
            panelProducto.setVisible(false);
            panelUbi.setVisible(false);
            panelSinProductos3.setVisible(true);
            return;
        }

        // hay productos
        panelProducto.setVisible(true);
        panelUbi.setVisible(true);
        panelSinProductos3.setVisible(false);
        revalidate();
        repaint();

        for (String p : prods) {
            String[] partes = p.split("\\|");
            productoComboBox.addItem(partes[0] + partes[1]);
        }
    }

    
    private List<String> buscarUbicacionesLlenas(){
        List<String> ubicacionesLista = naveCtrl.calcularPesosRack(idRack);
        Collections.sort(ubicacionesLista);

        String[] ubicaciones = ubicacionesLista.toArray(new String[0]);

        List<String> ubicLlenas = new ArrayList<>();

        for (int i = 1; i <= 3; i++){
            for (int j = 1; j <= 3; j++){

                boolean disponible = false;

                for (String ubic : ubicaciones) {
                    String codigo = ubic.split(":")[0];
                    String[] partes = codigo.split("-");
                    int fila = Integer.parseInt(partes[2]);
                    int col  = Integer.parseInt(partes[3]);

                    if (fila == i && col == j) {
                        disponible = true;
                        break;
                    }
                }

                if (!disponible) {
                    ubicLlenas.add("f"+i+"c"+j);
                }
            }
        }
        return ubicLlenas;
    }
    
    private void nombrePanels(){
        f1c1.setName("f1c1");
        f1c2.setName("f1c2");
        f1c3.setName("f1c3");
        f2c1.setName("f2c1");
        f2c2.setName("f2c2");
        f2c3.setName("f2c3");
        f3c1.setName("f3c1");
        f3c2.setName("f3c2");
        f3c3.setName("f3c3");      
    }
    
    private void pintarUbicacionesLlenas(List<String> ubicLlenas){
        for (String ubic : ubicLlenas){
            JPanel panel = buscarPanelPorNombreEnPanel(panelUbicaciones,ubic);
            panel.setBackground(new Color(229,155,129));
            panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            
            for (Component comp : panel.getComponents()) {
                if (comp instanceof JLabel) {
                    JLabel lbl = (JLabel) comp;
                    lbl.setText("LLENO");
                }
            }
        }       
    }
    
    public JPanel buscarPanelPorNombreEnPanel(Container container, String nombreBuscado) {
        for (Component comp : container.getComponents()) {

            if (comp instanceof JPanel && nombreBuscado.equals(comp.getName())) {
                return (JPanel) comp;
            }

            if (comp instanceof Container) {
                JPanel resultado = buscarPanelPorNombreEnPanel((Container) comp, nombreBuscado);
                if (resultado != null) return resultado;
            }
        }
        return null;
    }
    
    private void seleccionBoton(JPanel nuevaSeleccion){
        botonUbicacionElegido.setBackground(new Color(232,232,232));
        botonUbicacionElegido = nuevaSeleccion;
        botonUbicacionElegido.setBackground(new Color(181,181,181));
    }
    
    private void resetearUbicaciones() {
        for (Component comp : panelUbicaciones.getComponents()) {
            if (comp instanceof JPanel panel) {
                panel.setBackground(new Color(232,232,232));
                panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                for (Component sub : panel.getComponents()) {
                    if (sub instanceof JLabel lbl) {
                        String nombre = panel.getName();
                        lbl.setText(nombre != null ? nombre.toUpperCase() : " ");;
                    }
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        popup = new javax.swing.JPopupMenu();
        container = new javax.swing.JPanel();
        fondo = new javax.swing.JPanel();
        panelUbi = new javax.swing.JPanel();
        labelUbicacion = new javax.swing.JLabel();
        ingrese2 = new javax.swing.JLabel();
        racksComboBox = new javax.swing.JComboBox<>();
        panelUbicaciones = new javax.swing.JPanel();
        f1c1 = new javax.swing.JPanel();
        codigo1 = new javax.swing.JLabel();
        f1c2 = new javax.swing.JPanel();
        codigo = new javax.swing.JLabel();
        f1c3 = new javax.swing.JPanel();
        codigo4 = new javax.swing.JLabel();
        f2c1 = new javax.swing.JPanel();
        codigo5 = new javax.swing.JLabel();
        f2c2 = new javax.swing.JPanel();
        codigo6 = new javax.swing.JLabel();
        f2c3 = new javax.swing.JPanel();
        codigo7 = new javax.swing.JLabel();
        f3c1 = new javax.swing.JPanel();
        codigo9 = new javax.swing.JLabel();
        f3c2 = new javax.swing.JPanel();
        codigo10 = new javax.swing.JLabel();
        f3c3 = new javax.swing.JPanel();
        codigo12 = new javax.swing.JLabel();
        ingrese3 = new javax.swing.JLabel();
        ubicacionSelec = new javax.swing.JLabel();
        panelProducto = new javax.swing.JPanel();
        labelProducto = new javax.swing.JLabel();
        ingrese = new javax.swing.JLabel();
        ingrese1 = new javax.swing.JLabel();
        cantidad = new javax.swing.JFormattedTextField();
        jSeparator2 = new javax.swing.JSeparator();
        ingrese4 = new javax.swing.JLabel();
        fechaCampo = new javax.swing.JFormattedTextField();
        jSeparator3 = new javax.swing.JSeparator();
        hoy = new javax.swing.JButton();
        botonCrearOrden = new javax.swing.JButton();
        productoComboBox = new javax.swing.JComboBox<>();
        containerTitleBar = new javax.swing.JPanel();
        subTitle = new javax.swing.JLabel();
        panelSinProductos3 = new javax.swing.JPanel();
        ingrese8 = new javax.swing.JLabel();
        volver3 = new javax.swing.JButton();

        popup.setFocusable(false);

        setBackground(new java.awt.Color(232, 232, 232));
        setPreferredSize(new java.awt.Dimension(670, 500));

        container.setBackground(new java.awt.Color(255, 255, 255));
        container.setPreferredSize(new java.awt.Dimension(580, 400));

        fondo.setMaximumSize(new java.awt.Dimension(580, 326));
        fondo.setMinimumSize(new java.awt.Dimension(580, 326));
        fondo.setPreferredSize(new java.awt.Dimension(580, 326));
        fondo.setLayout(new java.awt.BorderLayout());

        panelUbi.setBackground(new java.awt.Color(255, 255, 255));
        panelUbi.setMaximumSize(new java.awt.Dimension(367, 326));
        panelUbi.setMinimumSize(new java.awt.Dimension(367, 326));
        panelUbi.setName(""); // NOI18N
        panelUbi.setPreferredSize(new java.awt.Dimension(367, 326));
        panelUbi.setRequestFocusEnabled(false);

        labelUbicacion.setBackground(new java.awt.Color(255, 255, 255));
        labelUbicacion.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        labelUbicacion.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.borderColor"));
        labelUbicacion.setText("Ubicación de ingreso");

        ingrese2.setBackground(new java.awt.Color(255, 255, 255));
        ingrese2.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        ingrese2.setForeground(new java.awt.Color(0, 51, 51));
        ingrese2.setText("Rack");

        racksComboBox.setBackground(new java.awt.Color(255, 255, 255));
        racksComboBox.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        racksComboBox.setForeground(new java.awt.Color(51, 51, 51));
        racksComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        racksComboBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 227)));
        racksComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                racksComboBoxActionPerformed(evt);
            }
        });

        panelUbicaciones.setBackground(new java.awt.Color(255, 255, 255));
        panelUbicaciones.setMaximumSize(new java.awt.Dimension(170, 170));
        panelUbicaciones.setPreferredSize(new java.awt.Dimension(170, 170));
        panelUbicaciones.setLayout(new java.awt.GridLayout(3, 3, 10, 10));

        f1c1.setBackground(new java.awt.Color(232, 232, 232));
        f1c1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f1c1.setMaximumSize(new java.awt.Dimension(50, 50));
        f1c1.setMinimumSize(new java.awt.Dimension(50, 50));
        f1c1.setPreferredSize(new java.awt.Dimension(50, 50));
        f1c1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                f1c1MouseClicked(evt);
            }
        });

        codigo1.setBackground(new java.awt.Color(0, 51, 51));
        codigo1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        codigo1.setForeground(new java.awt.Color(0, 51, 51));
        codigo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        codigo1.setText("F1 C1");

        javax.swing.GroupLayout f1c1Layout = new javax.swing.GroupLayout(f1c1);
        f1c1.setLayout(f1c1Layout);
        f1c1Layout.setHorizontalGroup(
            f1c1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(codigo1, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
        f1c1Layout.setVerticalGroup(
            f1c1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f1c1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(codigo1)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        panelUbicaciones.add(f1c1);

        f1c2.setBackground(new java.awt.Color(232, 232, 232));
        f1c2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f1c2.setMaximumSize(new java.awt.Dimension(50, 50));
        f1c2.setMinimumSize(new java.awt.Dimension(50, 50));
        f1c2.setPreferredSize(new java.awt.Dimension(50, 50));
        f1c2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                f1c2MouseClicked(evt);
            }
        });

        codigo.setBackground(new java.awt.Color(0, 51, 51));
        codigo.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        codigo.setForeground(new java.awt.Color(0, 51, 51));
        codigo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        codigo.setText("F1 C2");

        javax.swing.GroupLayout f1c2Layout = new javax.swing.GroupLayout(f1c2);
        f1c2.setLayout(f1c2Layout);
        f1c2Layout.setHorizontalGroup(
            f1c2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(codigo, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
        f1c2Layout.setVerticalGroup(
            f1c2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f1c2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        panelUbicaciones.add(f1c2);

        f1c3.setBackground(new java.awt.Color(232, 232, 232));
        f1c3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f1c3.setMaximumSize(new java.awt.Dimension(50, 50));
        f1c3.setMinimumSize(new java.awt.Dimension(50, 50));
        f1c3.setPreferredSize(new java.awt.Dimension(50, 50));
        f1c3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                f1c3MouseClicked(evt);
            }
        });

        codigo4.setBackground(new java.awt.Color(0, 51, 51));
        codigo4.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        codigo4.setForeground(new java.awt.Color(0, 51, 51));
        codigo4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        codigo4.setText("F1 C3");

        javax.swing.GroupLayout f1c3Layout = new javax.swing.GroupLayout(f1c3);
        f1c3.setLayout(f1c3Layout);
        f1c3Layout.setHorizontalGroup(
            f1c3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(codigo4, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
        f1c3Layout.setVerticalGroup(
            f1c3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f1c3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(codigo4)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        panelUbicaciones.add(f1c3);

        f2c1.setBackground(new java.awt.Color(232, 232, 232));
        f2c1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f2c1.setMaximumSize(new java.awt.Dimension(50, 50));
        f2c1.setMinimumSize(new java.awt.Dimension(50, 50));
        f2c1.setPreferredSize(new java.awt.Dimension(50, 50));
        f2c1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                f2c1MouseClicked(evt);
            }
        });

        codigo5.setBackground(new java.awt.Color(0, 51, 51));
        codigo5.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        codigo5.setForeground(new java.awt.Color(0, 51, 51));
        codigo5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        codigo5.setText("F2 C1");

        javax.swing.GroupLayout f2c1Layout = new javax.swing.GroupLayout(f2c1);
        f2c1.setLayout(f2c1Layout);
        f2c1Layout.setHorizontalGroup(
            f2c1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(codigo5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
        f2c1Layout.setVerticalGroup(
            f2c1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f2c1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(codigo5)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        panelUbicaciones.add(f2c1);

        f2c2.setBackground(new java.awt.Color(232, 232, 232));
        f2c2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f2c2.setMaximumSize(new java.awt.Dimension(50, 50));
        f2c2.setMinimumSize(new java.awt.Dimension(50, 50));
        f2c2.setPreferredSize(new java.awt.Dimension(50, 50));
        f2c2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                f2c2MouseClicked(evt);
            }
        });

        codigo6.setBackground(new java.awt.Color(0, 51, 51));
        codigo6.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        codigo6.setForeground(new java.awt.Color(0, 51, 51));
        codigo6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        codigo6.setText("F2 C2");

        javax.swing.GroupLayout f2c2Layout = new javax.swing.GroupLayout(f2c2);
        f2c2.setLayout(f2c2Layout);
        f2c2Layout.setHorizontalGroup(
            f2c2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(codigo6, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
        f2c2Layout.setVerticalGroup(
            f2c2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f2c2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(codigo6)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        panelUbicaciones.add(f2c2);

        f2c3.setBackground(new java.awt.Color(232, 232, 232));
        f2c3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f2c3.setMaximumSize(new java.awt.Dimension(50, 50));
        f2c3.setMinimumSize(new java.awt.Dimension(50, 50));
        f2c3.setPreferredSize(new java.awt.Dimension(50, 50));
        f2c3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                f2c3MouseClicked(evt);
            }
        });

        codigo7.setBackground(new java.awt.Color(0, 51, 51));
        codigo7.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        codigo7.setForeground(new java.awt.Color(0, 51, 51));
        codigo7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        codigo7.setText("F2 C3");

        javax.swing.GroupLayout f2c3Layout = new javax.swing.GroupLayout(f2c3);
        f2c3.setLayout(f2c3Layout);
        f2c3Layout.setHorizontalGroup(
            f2c3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f2c3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(codigo7, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );
        f2c3Layout.setVerticalGroup(
            f2c3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f2c3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(codigo7)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        panelUbicaciones.add(f2c3);

        f3c1.setBackground(new java.awt.Color(232, 232, 232));
        f3c1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f3c1.setMaximumSize(new java.awt.Dimension(50, 50));
        f3c1.setMinimumSize(new java.awt.Dimension(50, 50));
        f3c1.setPreferredSize(new java.awt.Dimension(50, 50));
        f3c1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                f3c1MouseClicked(evt);
            }
        });

        codigo9.setBackground(new java.awt.Color(0, 51, 51));
        codigo9.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        codigo9.setForeground(new java.awt.Color(0, 51, 51));
        codigo9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        codigo9.setText("F3 C1");

        javax.swing.GroupLayout f3c1Layout = new javax.swing.GroupLayout(f3c1);
        f3c1.setLayout(f3c1Layout);
        f3c1Layout.setHorizontalGroup(
            f3c1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f3c1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(codigo9, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );
        f3c1Layout.setVerticalGroup(
            f3c1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f3c1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(codigo9)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        panelUbicaciones.add(f3c1);

        f3c2.setBackground(new java.awt.Color(232, 232, 232));
        f3c2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f3c2.setMaximumSize(new java.awt.Dimension(50, 50));
        f3c2.setMinimumSize(new java.awt.Dimension(50, 50));
        f3c2.setPreferredSize(new java.awt.Dimension(50, 50));
        f3c2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                f3c2MouseClicked(evt);
            }
        });

        codigo10.setBackground(new java.awt.Color(0, 51, 51));
        codigo10.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        codigo10.setForeground(new java.awt.Color(0, 51, 51));
        codigo10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        codigo10.setText("F3 C2 ");

        javax.swing.GroupLayout f3c2Layout = new javax.swing.GroupLayout(f3c2);
        f3c2.setLayout(f3c2Layout);
        f3c2Layout.setHorizontalGroup(
            f3c2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f3c2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(codigo10, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );
        f3c2Layout.setVerticalGroup(
            f3c2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f3c2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(codigo10)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        panelUbicaciones.add(f3c2);

        f3c3.setBackground(new java.awt.Color(232, 232, 232));
        f3c3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f3c3.setMaximumSize(new java.awt.Dimension(50, 50));
        f3c3.setMinimumSize(new java.awt.Dimension(50, 50));
        f3c3.setPreferredSize(new java.awt.Dimension(50, 50));
        f3c3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                f3c3MouseClicked(evt);
            }
        });

        codigo12.setBackground(new java.awt.Color(0, 51, 51));
        codigo12.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        codigo12.setForeground(new java.awt.Color(0, 51, 51));
        codigo12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        codigo12.setText("F3 C3");

        javax.swing.GroupLayout f3c3Layout = new javax.swing.GroupLayout(f3c3);
        f3c3.setLayout(f3c3Layout);
        f3c3Layout.setHorizontalGroup(
            f3c3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(codigo12, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
        f3c3Layout.setVerticalGroup(
            f3c3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(f3c3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(codigo12)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        panelUbicaciones.add(f3c3);

        ingrese3.setBackground(new java.awt.Color(255, 255, 255));
        ingrese3.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ingrese3.setForeground(new java.awt.Color(0, 51, 51));
        ingrese3.setText("<html>Seleccione la ubicación en el rack donde ingresar los productos<html>");

        ubicacionSelec.setBackground(new java.awt.Color(255, 255, 255));
        ubicacionSelec.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        ubicacionSelec.setForeground(new java.awt.Color(0, 51, 51));
        ubicacionSelec.setText("Ubicación: ");

        javax.swing.GroupLayout panelUbiLayout = new javax.swing.GroupLayout(panelUbi);
        panelUbi.setLayout(panelUbiLayout);
        panelUbiLayout.setHorizontalGroup(
            panelUbiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUbiLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(panelUbiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelUbicacion)
                    .addGroup(panelUbiLayout.createSequentialGroup()
                        .addComponent(ingrese2)
                        .addGap(12, 12, 12)
                        .addComponent(racksComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ingrese3, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelUbicaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ubicacionSelec, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        panelUbiLayout.setVerticalGroup(
            panelUbiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUbiLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(labelUbicacion)
                .addGap(12, 12, 12)
                .addGroup(panelUbiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelUbiLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(ingrese2))
                    .addComponent(racksComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(ingrese3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(panelUbicaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(ubicacionSelec))
        );

        fondo.add(panelUbi, java.awt.BorderLayout.EAST);

        panelProducto.setBackground(new java.awt.Color(255, 255, 255));
        panelProducto.setMaximumSize(new java.awt.Dimension(213, 326));
        panelProducto.setMinimumSize(new java.awt.Dimension(213, 326));
        panelProducto.setPreferredSize(new java.awt.Dimension(213, 326));

        labelProducto.setBackground(new java.awt.Color(255, 255, 255));
        labelProducto.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        labelProducto.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.borderColor"));
        labelProducto.setText("Datos del producto");

        ingrese.setBackground(new java.awt.Color(255, 255, 255));
        ingrese.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        ingrese.setForeground(new java.awt.Color(0, 51, 51));
        ingrese.setText("ID del producto");

        ingrese1.setBackground(new java.awt.Color(255, 255, 255));
        ingrese1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        ingrese1.setForeground(new java.awt.Color(0, 51, 51));
        ingrese1.setText("Cantidad");

        cantidad.setBackground(new java.awt.Color(255, 255, 255));
        cantidad.setBorder(null);
        cantidad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        cantidad.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        cantidad.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        cantidad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cantidadMousePressed(evt);
            }
        });
        cantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cantidadActionPerformed(evt);
            }
        });

        jSeparator2.setForeground(new java.awt.Color(0, 51, 51));

        ingrese4.setBackground(new java.awt.Color(255, 255, 255));
        ingrese4.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        ingrese4.setForeground(new java.awt.Color(0, 51, 51));
        ingrese4.setText("Fecha (d/m/aa)");

        fechaCampo.setBackground(new java.awt.Color(255, 255, 255));
        fechaCampo.setBorder(null);
        fechaCampo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT))));
        fechaCampo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaCampoActionPerformed(evt);
            }
        });

        jSeparator3.setForeground(new java.awt.Color(0, 51, 51));

        hoy.setBackground(new java.awt.Color(204, 204, 204));
        hoy.setFont(new java.awt.Font("Roboto", 0, 10)); // NOI18N
        hoy.setForeground(new java.awt.Color(0, 0, 0));
        hoy.setText("HOY");
        hoy.setBorder(null);
        hoy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        hoy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hoyActionPerformed(evt);
            }
        });

        botonCrearOrden.setBackground(new java.awt.Color(0, 51, 51));
        botonCrearOrden.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        botonCrearOrden.setForeground(new java.awt.Color(255, 255, 255));
        botonCrearOrden.setText("CREAR ORDEN");
        botonCrearOrden.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 51)));
        botonCrearOrden.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botonCrearOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearOrdenActionPerformed(evt);
            }
        });

        productoComboBox.setBackground(new java.awt.Color(255, 255, 255));
        productoComboBox.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        productoComboBox.setForeground(new java.awt.Color(51, 51, 51));
        productoComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        productoComboBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 227, 227)));
        productoComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productoComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelProductoLayout = new javax.swing.GroupLayout(panelProducto);
        panelProducto.setLayout(panelProductoLayout);
        panelProductoLayout.setHorizontalGroup(
            panelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProductoLayout.createSequentialGroup()
                .addGroup(panelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelProductoLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(panelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(labelProducto, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ingrese1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ingrese, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelProductoLayout.createSequentialGroup()
                                .addComponent(ingrese4, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hoy, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cantidad, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fechaCampo, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(productoComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panelProductoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonCrearOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelProductoLayout.setVerticalGroup(
            panelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelProducto)
                .addGap(12, 12, 12)
                .addComponent(ingrese)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ingrese1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ingrese4)
                    .addComponent(hoy, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(fechaCampo, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(botonCrearOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        fondo.add(panelProducto, java.awt.BorderLayout.WEST);

        containerTitleBar.setBackground(new java.awt.Color(0, 51, 51));

        subTitle.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        subTitle.setForeground(new java.awt.Color(255, 255, 255));
        subTitle.setText("Menú movimientos  >  Generar orden de ingreso");

        javax.swing.GroupLayout containerTitleBarLayout = new javax.swing.GroupLayout(containerTitleBar);
        containerTitleBar.setLayout(containerTitleBarLayout);
        containerTitleBarLayout.setHorizontalGroup(
            containerTitleBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerTitleBarLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(subTitle)
                .addContainerGap(161, Short.MAX_VALUE))
        );
        containerTitleBarLayout.setVerticalGroup(
            containerTitleBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerTitleBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelSinProductos3.setBackground(new java.awt.Color(255, 255, 255));

        ingrese8.setBackground(new java.awt.Color(255, 255, 255));
        ingrese8.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        ingrese8.setForeground(new java.awt.Color(0, 51, 51));
        ingrese8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ingrese8.setText("<html>No hay productos cargados para generar una orden de ingreso<html>");
        ingrese8.setToolTipText("");

        volver3.setBackground(new java.awt.Color(0, 51, 51));
        volver3.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        volver3.setForeground(new java.awt.Color(255, 255, 255));
        volver3.setText("Volver al menú movimientos");
        volver3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 51)));
        volver3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        volver3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                volverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSinProductos3Layout = new javax.swing.GroupLayout(panelSinProductos3);
        panelSinProductos3.setLayout(panelSinProductos3Layout);
        panelSinProductos3Layout.setHorizontalGroup(
            panelSinProductos3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSinProductos3Layout.createSequentialGroup()
                .addGroup(panelSinProductos3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSinProductos3Layout.createSequentialGroup()
                        .addGap(168, 168, 168)
                        .addComponent(volver3, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelSinProductos3Layout.createSequentialGroup()
                        .addGap(153, 153, 153)
                        .addComponent(ingrese8, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(168, Short.MAX_VALUE))
        );
        panelSinProductos3Layout.setVerticalGroup(
            panelSinProductos3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSinProductos3Layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(ingrese8, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(volver3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(149, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout containerLayout = new javax.swing.GroupLayout(container);
        container.setLayout(containerLayout);
        containerLayout.setHorizontalGroup(
            containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(containerTitleBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelSinProductos3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(fondo, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))
        );
        containerLayout.setVerticalGroup(
            containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerLayout.createSequentialGroup()
                .addComponent(containerTitleBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(347, Short.MAX_VALUE))
            .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerLayout.createSequentialGroup()
                    .addGap(0, 53, Short.MAX_VALUE)
                    .addComponent(panelSinProductos3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerLayout.createSequentialGroup()
                    .addGap(0, 53, Short.MAX_VALUE)
                    .addComponent(fondo, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addComponent(container, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(container, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );
    }// </editor-fold>                        

    private void cantidadMousePressed(java.awt.event.MouseEvent evt) {                                      

    }                                     

    private void cantidadActionPerformed(java.awt.event.ActionEvent evt) {                                         

    }                                        

    private void botonCrearOrdenActionPerformed(java.awt.event.ActionEvent evt) {                                                
        String cantidadCargada = cantidad.getText().replaceAll(",", "");
        if (cantidadCargada.equals("")){
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida (entero)", "ERROR", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Date date = (Date) fechaCampo.getValue();
        if (date == null) {
            JOptionPane.showMessageDialog(this, "Ingrese una fecha válidad", "ERROR", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        fecha = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        System.out.println(fecha);
        System.out.println(date);
        
        String res = crearOrdenIngreso();
        if (res.startsWith("OK:")){
            JOptionPane.showMessageDialog(this, res, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.INFORMATION_MESSAGE);
    }                                               

    private void fechaCampoActionPerformed(java.awt.event.ActionEvent evt) {                                           

    }                                          

    private void hoyActionPerformed(java.awt.event.ActionEvent evt) {                                    
       fechaCampo.setValue(new Date());
    }                                   

    private void racksComboBoxActionPerformed(java.awt.event.ActionEvent evt) {                                              
        String seleccion = (String) racksComboBox.getSelectedItem(); 
        if (seleccion == null || seleccion.trim().isEmpty()) return;

        idRack = Integer.parseInt(seleccion.split(",")[1].split("ID")[1].trim());
        idNave = seleccion.split(",")[0].split(" ")[1];

        // si estaba oculto, lo muestro por primera vez
        visibleUbics(true);

        resetearUbicaciones();

        List<String> llenas = buscarUbicacionesLlenas();
        pintarUbicacionesLlenas(llenas);
    }                                             

    private void f1c1MouseClicked(java.awt.event.MouseEvent evt) {                                  
        ubicacionElegida = idNave + "-" + idRack + "-" + 1 + "-" + 1;
        seleccionBoton(f1c1);
        ubicacionSelec.setText("Ubicación "+ubicacionElegida);
    }                                 

    private void f1c2MouseClicked(java.awt.event.MouseEvent evt) {                                  
        ubicacionElegida = idNave + "-" + idRack + "-" + 1 + "-" + 2;
        seleccionBoton(f1c2);
        ubicacionSelec.setText("Ubicación "+ubicacionElegida);
    }                                 

    private void f1c3MouseClicked(java.awt.event.MouseEvent evt) {                                  
        ubicacionElegida = idNave + "-" + idRack + "-" + 1 + "-" + 3;
        seleccionBoton(f1c3);
        ubicacionSelec.setText("Ubicación "+ubicacionElegida);
    }                                 

    private void f2c1MouseClicked(java.awt.event.MouseEvent evt) {                                  
        ubicacionElegida = idNave + "-" + idRack + "-" + 2 + "-" + 1;
        seleccionBoton(f2c1);
        ubicacionSelec.setText("Ubicación "+ubicacionElegida);
    }                                 

    private void f2c2MouseClicked(java.awt.event.MouseEvent evt) {                                  
        ubicacionElegida = idNave + "-" + idRack + "-" + 2 + "-" + 2;
        seleccionBoton(f2c2);
        ubicacionSelec.setText("Ubicación "+ubicacionElegida);
    }                                 

    private void f2c3MouseClicked(java.awt.event.MouseEvent evt) {                                  
        ubicacionElegida = idNave + "-" + idRack + "-" + 2 + "-" + 3;
        seleccionBoton(f2c3);
        ubicacionSelec.setText("Ubicación "+ubicacionElegida);
    }                                 

    private void f3c1MouseClicked(java.awt.event.MouseEvent evt) {                                  
        ubicacionElegida = idNave + "-" + idRack + "-" + 3 + "-" + 1;
        seleccionBoton(f3c1);
        ubicacionSelec.setText("Ubicación "+ubicacionElegida);
    }                                 

    private void f3c2MouseClicked(java.awt.event.MouseEvent evt) {                                  
        ubicacionElegida = idNave + "-" + idRack + "-" + 3 + "-" + 2;
        seleccionBoton(f3c2);
        ubicacionSelec.setText("Ubicación "+ubicacionElegida);
    }                                 

    private void f3c3MouseClicked(java.awt.event.MouseEvent evt) {                                  
        ubicacionElegida = idNave + "-" + idRack + "-" + 3 + "-" + 3;
        seleccionBoton(f3c3);
        ubicacionSelec.setText("Ubicación "+ubicacionElegida);
    }                                 

    private void productoComboBoxActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        String seleccion = (String) productoComboBox.getSelectedItem(); 
        if (seleccion == null || seleccion.trim().isEmpty()) return;
        idProducto = Character.getNumericValue((seleccion.split(",")[0].split("-")[1]).charAt(1));
    }                                                

    private void volverActionPerformed(java.awt.event.ActionEvent evt) {                                       
        frame.mostrarJPanel(frame.getMenuMovimientosPanel());
    }                                      

    // Variables declaration - do not modify                     
    private javax.swing.JButton botonCrearOrden;
    private javax.swing.JFormattedTextField cantidad;
    private javax.swing.JLabel codigo;
    private javax.swing.JLabel codigo1;
    private javax.swing.JLabel codigo10;
    private javax.swing.JLabel codigo12;
    private javax.swing.JLabel codigo4;
    private javax.swing.JLabel codigo5;
    private javax.swing.JLabel codigo6;
    private javax.swing.JLabel codigo7;
    private javax.swing.JLabel codigo9;
    private javax.swing.JPanel container;
    private javax.swing.JPanel containerTitleBar;
    private javax.swing.JPanel f1c1;
    private javax.swing.JPanel f1c2;
    private javax.swing.JPanel f1c3;
    private javax.swing.JPanel f2c1;
    private javax.swing.JPanel f2c2;
    private javax.swing.JPanel f2c3;
    private javax.swing.JPanel f3c1;
    private javax.swing.JPanel f3c2;
    private javax.swing.JPanel f3c3;
    private javax.swing.JFormattedTextField fechaCampo;
    private javax.swing.JPanel fondo;
    private javax.swing.JButton hoy;
    private javax.swing.JLabel ingrese;
    private javax.swing.JLabel ingrese1;
    private javax.swing.JLabel ingrese2;
    private javax.swing.JLabel ingrese3;
    private javax.swing.JLabel ingrese4;
    private javax.swing.JLabel ingrese8;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel labelProducto;
    private javax.swing.JLabel labelUbicacion;
    private javax.swing.JPanel panelProducto;
    private javax.swing.JPanel panelSinProductos3;
    private javax.swing.JPanel panelUbi;
    private javax.swing.JPanel panelUbicaciones;
    private javax.swing.JPopupMenu popup;
    private javax.swing.JComboBox<String> productoComboBox;
    private javax.swing.JComboBox<String> racksComboBox;
    private javax.swing.JLabel subTitle;
    private javax.swing.JLabel ubicacionSelec;
    private javax.swing.JButton volver3;
    // End of variables declaration                   
}