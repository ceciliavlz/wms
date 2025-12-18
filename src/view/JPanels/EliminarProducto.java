/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.JPanels;

import controller.ProductoController;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import model.Producto;
import view.PrincipalJFrame;

/**
 *
 * @author Ce___
 */
public class EliminarProducto extends javax.swing.JPanel {
    private final DefaultListModel<String> modeloListaCodigos;
    private final DefaultTableModel mt = new DefaultTableModel();
    
    private PrincipalJFrame frame;
    private final ProductoController productoCtrl;
    
    private final ArrayList<String> codigos;
    /**
     * Creates new form EliminarProducto
     */
    public EliminarProducto(PrincipalJFrame frame, ProductoController productoCtrl) {
      initComponents();
        this.productoCtrl = productoCtrl;
        this.frame = frame;
        jScrollPane1.setVisible(false);
        visibilidadTabla(false);
        visibilidadEliminar(false);
        estiloTabla();
        
        String[] ids = {"Código", "Nombre", "U medida", "Peso", "Capacidad", "Grupo"};
        mt.setColumnIdentifiers(ids);
        tablaProductos.setModel(mt);

        //Inicializaciones necesarias
        modeloListaCodigos = new DefaultListModel<>();
        listaSugerenciasCodigos.setModel(modeloListaCodigos);
        
        codigos = new ArrayList<>();

        cargarCodigos();

        popup.add(new JScrollPane(listaSugerenciasCodigos));
        popup.setFocusable(false);
        listaSugerenciasCodigos.setFocusable(false);

        codigoBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                mostrarSugerenciasCodigos(codigoBuscar.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                mostrarSugerenciasCodigos(codigoBuscar.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                mostrarSugerenciasCodigos(codigoBuscar.getText());
            }
        });

        listaSugerenciasCodigos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                codigoBuscar.setText(listaSugerenciasCodigos.getSelectedValue());
                popup.setVisible(false);
                
                datosProducto(codigoBuscar.getText());
                visibilidadTabla(true);
                visibilidadEliminar(true);
            }
        });
    }

    private void mostrarSugerenciasCodigos(String codigo) {
        modeloListaCodigos.clear();

        if (codigo.isEmpty()) {
            popup.setVisible(false);
            return;
        }
        List<String> coincidencias = new ArrayList<>();
        for (String item : codigos) {
            if (item.toLowerCase().contains(codigo.toLowerCase())) {
                coincidencias.add(item);
            }
        }
        if (coincidencias.isEmpty()) {
            popup.setVisible(false);
            return;
        }
        for (String c : coincidencias) {
            modeloListaCodigos.addElement(c);
        }

        popup.setPopupSize(codigoBuscar.getWidth(), 120);
        popup.show(codigoBuscar, 0, codigoBuscar.getHeight());
    }
    
    private void cargarCodigos(){
       for (Producto p : productoCtrl.listarProductosObjeto()){
           codigos.add(p.getCodigo());
       }
    }
    
    private void visibilidadTabla(boolean visible){
        jScrollPane2.setVisible(visible);
        tablaProductos.setVisible(visible);
        productoEncontrado.setVisible(visible);
    }
    
    private void visibilidadEliminar(boolean visibilidad){
        panelEliminar.setVisible(visibilidad);
        confirmacion.setVisible(visibilidad);
        eliminar.setVisible(visibilidad);
    }
    
    private void datosProducto(String buscado){
        mt.setRowCount(0);
        productoEncontrado.setText("Producto " + buscado);
        
        Producto p = productoCtrl.buscarProductoPorCodigo(buscado);
        mt.addRow(new Object[] {p.getCodigo() , p.getDescripcion(), p.getUnidadMedida(), p.getPesoUnitario(),
                p.getCapacidad(), "<html>"+p.getGrupo()+"<html>"});
    }
    
   private void estiloTabla(){
        tablaProductos.setFont(new java.awt.Font("Roboto", 0, 14));
        tablaProductos.setRowHeight(55);
        tablaProductos.setBackground(Color.white); // blanco suave
        tablaProductos.setForeground(new java.awt.Color(102,102,102));
        tablaProductos.setSelectionBackground(new java.awt.Color(220, 240, 240));
        tablaProductos.setSelectionForeground(new java.awt.Color(0, 50, 50));
        tablaProductos.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tablaProductos.setGridColor(new java.awt.Color(240, 240, 240));
        JTableHeader header = tablaProductos.getTableHeader();
        header.setFont(new java.awt.Font("Roboto Light",Font.BOLD, 14));
        header.setOpaque(false);
        header.setBackground(Color.white);
        header.setForeground(new java.awt.Color(50, 50, 50));
        header.setBorder(javax.swing.BorderFactory.createEmptyBorder());
   }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popup = new javax.swing.JPopupMenu();
        container2 = new javax.swing.JPanel();
        containerTitleBar2 = new javax.swing.JPanel();
        subTitle2 = new javax.swing.JLabel();
        ingrese = new javax.swing.JLabel();
        codigoBuscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaSugerenciasCodigos = new javax.swing.JList<>();
        panelCodigos = new javax.swing.JPanel();
        ingrese3 = new javax.swing.JLabel();
        ingrese4 = new javax.swing.JLabel();
        ingrese5 = new javax.swing.JLabel();
        ingrese2 = new javax.swing.JLabel();
        productoEncontrado = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        panelEliminar = new javax.swing.JPanel();
        confirmacion = new javax.swing.JLabel();
        eliminar = new javax.swing.JButton();

        setBackground(new java.awt.Color(232, 232, 232));

        container2.setBackground(new java.awt.Color(255, 255, 255));
        container2.setPreferredSize(new java.awt.Dimension(580, 400));

        containerTitleBar2.setBackground(new java.awt.Color(0, 51, 51));

        subTitle2.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        subTitle2.setForeground(new java.awt.Color(255, 255, 255));
        subTitle2.setText("Menú productos  >  Eliminar producto");

        javax.swing.GroupLayout containerTitleBar2Layout = new javax.swing.GroupLayout(containerTitleBar2);
        containerTitleBar2.setLayout(containerTitleBar2Layout);
        containerTitleBar2Layout.setHorizontalGroup(
            containerTitleBar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerTitleBar2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(subTitle2, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        containerTitleBar2Layout.setVerticalGroup(
            containerTitleBar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerTitleBar2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subTitle2, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        ingrese.setBackground(new java.awt.Color(255, 255, 255));
        ingrese.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        ingrese.setForeground(new java.awt.Color(0, 51, 51));
        ingrese.setText("Ingrese ID o código del producto a eliminar:");

        codigoBuscar.setBackground(new java.awt.Color(217, 217, 217));
        codigoBuscar.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        codigoBuscar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        codigoBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        codigoBuscar.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        codigoBuscar.setMargin(new java.awt.Insets(2, 10, 2, 6));
        codigoBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                codigoBuscarMousePressed(evt);
            }
        });
        codigoBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codigoBuscarActionPerformed(evt);
            }
        });

        listaSugerenciasCodigos.setBackground(new java.awt.Color(255, 255, 255));
        listaSugerenciasCodigos.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        listaSugerenciasCodigos.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listaSugerenciasCodigos.setFocusable(false);
        jScrollPane1.setViewportView(listaSugerenciasCodigos);

        panelCodigos.setBackground(new java.awt.Color(228, 228, 228));

        ingrese3.setBackground(new java.awt.Color(255, 255, 255));
        ingrese3.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        ingrese3.setForeground(new java.awt.Color(0, 51, 51));
        ingrese3.setText("MP: Materia Prima");

        ingrese4.setBackground(new java.awt.Color(255, 255, 255));
        ingrese4.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        ingrese4.setForeground(new java.awt.Color(0, 51, 51));
        ingrese4.setText("PF: Producto Final");

        ingrese5.setBackground(new java.awt.Color(255, 255, 255));
        ingrese5.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        ingrese5.setForeground(new java.awt.Color(0, 51, 51));
        ingrese5.setText("PR: Producto Reenvasado");

        ingrese2.setBackground(new java.awt.Color(255, 255, 255));
        ingrese2.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        ingrese2.setForeground(new java.awt.Color(0, 51, 51));
        ingrese2.setText("Códigos:");

        javax.swing.GroupLayout panelCodigosLayout = new javax.swing.GroupLayout(panelCodigos);
        panelCodigos.setLayout(panelCodigosLayout);
        panelCodigosLayout.setHorizontalGroup(
            panelCodigosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCodigosLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelCodigosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ingrese3)
                    .addComponent(ingrese4)
                    .addComponent(ingrese5)
                    .addComponent(ingrese2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelCodigosLayout.setVerticalGroup(
            panelCodigosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCodigosLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(ingrese2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ingrese3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ingrese4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ingrese5)
                .addGap(14, 14, 14))
        );

        productoEncontrado.setBackground(new java.awt.Color(255, 255, 255));
        productoEncontrado.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        productoEncontrado.setForeground(new java.awt.Color(0, 51, 51));
        productoEncontrado.setText("PRODUCTO ENCONTRADO");

        tablaProductos.setBackground(new java.awt.Color(255, 255, 255));
        tablaProductos.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        tablaProductos.setForeground(new java.awt.Color(102, 102, 102));
        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Descripción", "Unidad de medida", "Peso unitario", "Capacidad contenedor", "Grupo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablaProductos.setToolTipText("");
        tablaProductos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        tablaProductos.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportView(tablaProductos);

        panelEliminar.setBackground(new java.awt.Color(232, 232, 232));

        confirmacion.setBackground(new java.awt.Color(255, 255, 255));
        confirmacion.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        confirmacion.setForeground(new java.awt.Color(0, 51, 51));
        confirmacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/advertencia.png"))); // NOI18N
        confirmacion.setText("<html>¿Está seguro que desea eliminar el producto?<html>");

        eliminar.setBackground(new java.awt.Color(0, 51, 51));
        eliminar.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        eliminar.setForeground(new java.awt.Color(255, 255, 255));
        eliminar.setText("ELIMINAR");
        eliminar.setBorder(null);
        eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelEliminarLayout = new javax.swing.GroupLayout(panelEliminar);
        panelEliminar.setLayout(panelEliminarLayout);
        panelEliminarLayout.setHorizontalGroup(
            panelEliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEliminarLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(confirmacion, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        panelEliminarLayout.setVerticalGroup(
            panelEliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEliminarLayout.createSequentialGroup()
                .addGroup(panelEliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEliminarLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(confirmacion, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelEliminarLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout container2Layout = new javax.swing.GroupLayout(container2);
        container2.setLayout(container2Layout);
        container2Layout.setHorizontalGroup(
            container2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(containerTitleBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(container2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(container2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(container2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 30, Short.MAX_VALUE))
                    .addGroup(container2Layout.createSequentialGroup()
                        .addGroup(container2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(container2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(ingrese, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(codigoBuscar, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(productoEncontrado, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(53, 53, 53)
                        .addComponent(panelCodigos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(container2Layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(panelEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        container2Layout.setVerticalGroup(
            container2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(container2Layout.createSequentialGroup()
                .addComponent(containerTitleBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(container2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelCodigos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(container2Layout.createSequentialGroup()
                        .addComponent(ingrese)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(codigoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addComponent(productoEncontrado, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(container2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(container2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void codigoBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigoBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_codigoBuscarActionPerformed

    private void codigoBuscarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_codigoBuscarMousePressed

    }//GEN-LAST:event_codigoBuscarMousePressed

    private void eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarActionPerformed
        int id = Integer.parseInt(codigoBuscar.getText().split("-")[1]);
        productoCtrl.eliminarProducto(id);
        JOptionPane.showMessageDialog(
            null,
            "Producto "+codigoBuscar.getText()+" eliminado con éxito",
            "Éxito", // Título de la ventana de diálogo
            JOptionPane.INFORMATION_MESSAGE // Tipo de mensaje (icono de información)
        );
        visibilidadEliminar(false);
    }//GEN-LAST:event_eliminarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField codigoBuscar;
    private javax.swing.JLabel confirmacion;
    private javax.swing.JPanel container;
    private javax.swing.JPanel container1;
    private javax.swing.JPanel container2;
    private javax.swing.JPanel containerTitleBar;
    private javax.swing.JPanel containerTitleBar1;
    private javax.swing.JPanel containerTitleBar2;
    private javax.swing.JButton eliminar;
    private javax.swing.JLabel ingrese;
    private javax.swing.JLabel ingrese2;
    private javax.swing.JLabel ingrese3;
    private javax.swing.JLabel ingrese4;
    private javax.swing.JLabel ingrese5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> listaSugerenciasCodigos;
    private javax.swing.JPanel panelCodigos;
    private javax.swing.JPanel panelEliminar;
    private javax.swing.JPopupMenu popup;
    private javax.swing.JLabel productoEncontrado;
    private javax.swing.JLabel subTitle;
    private javax.swing.JLabel subTitle1;
    private javax.swing.JLabel subTitle2;
    private javax.swing.JTable tablaProductos;
    // End of variables declaration//GEN-END:variables
}
