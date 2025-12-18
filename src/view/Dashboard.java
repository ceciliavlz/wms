/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ConsultasController;
import controller.HistorialController;
import controller.MovimientoController;
import controller.NaveController;
import controller.ProductoController;
import controller.TransformacionController;
import services.MovimientoService;
import services.NaveService;
import services.RackService;
import services.StockService;
import services.TransformacionService;
import view.PrincipalJFrame;

/**
 *
 * @author Ce___
 */
public class Dashboard {
    private StockService stockService;
    private RackService rackService;
    private NaveService naveService;
    private MovimientoService movService;
    private TransformacionService transfService;
    
    public Dashboard(StockService stockService, RackService rackService, NaveService naveService,
            MovimientoService movService, TransformacionService transfService){

        //controllers
        HistorialController historialCtrl = new HistorialController(movService,transfService);
        ProductoController productoCtrl = new ProductoController(stockService);
        MovimientoController movimientoCtrl = new MovimientoController(movService);
        NaveController naveCtrl = new NaveController(naveService, stockService);
        ConsultasController consultasCtrl = new ConsultasController(stockService);
        TransformacionController transfController = new TransformacionController(transfService,stockService);
        
        this.stockService = stockService;
        this.rackService = rackService;
        this.naveService = naveService;
        this.movService = movService;
        this.transfService = transfService;
        
        java.awt.EventQueue.invokeLater(() -> new PrincipalJFrame(productoCtrl, naveCtrl, movimientoCtrl).setVisible(true));
    }
}
