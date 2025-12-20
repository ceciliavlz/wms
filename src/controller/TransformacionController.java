package controller;

import java.time.LocalDate;
import java.util.List;

import model.OrdenTransformacion;
import model.Producto;
import services.StockService;
import services.TransformacionService;

public class TransformacionController {
    private final TransformacionService transfService;
    private final StockService stockService;
    private static TransformacionController instance;
    
    private TransformacionController(TransformacionService transfService, StockService stockService) {
        this.transfService = transfService;
        this.stockService = stockService;
    }
    
    public static TransformacionController getInstance() {
        if (instance == null) {
            instance = new TransformacionController(TransformacionService.getInstance() ,StockService.getInstance());
        }
        
        return instance;
    }

    public String procesarTransformacion(int idProductoEntrada, String ubicacionProdEntrada, int cantidadEntrada,
            Producto productoTransformadoTemporal, String ubicacionSalida, LocalDate fecha) {
        
        Producto prodEntrada = stockService.getProductoPorId(idProductoEntrada);
       
        //estas validaciones se pueden chekar en la vista cuando se pase a javaSwing. MAYBE
        if (prodEntrada == null) {
            return "ERROR: El producto de entrada no existe.";
        }

        if (!stockService.getUbicacionesMap().containsKey(ubicacionProdEntrada)) {
            return "ERROR: La ubicación de entrada no existe.";
        }

        if (!stockService.getUbicacionesMap().containsKey(ubicacionSalida)) {
            return "ERROR: La ubicación de salida no existe.";
        }

        if (cantidadEntrada <= 0) {
            return "ERROR: La cantidad de entrada debe ser mayor a 0.";
        }
        
        int stockDisponible = stockService.getStockTotalPorProducto(idProductoEntrada);
        if (stockDisponible < cantidadEntrada) {
            return "ERROR: No hay suficiente stock del producto de entrada (disponible: " + stockDisponible + ").";
        }

        OrdenTransformacion orden = new OrdenTransformacion(
            idProductoEntrada,
            ubicacionProdEntrada,
            cantidadEntrada,
            0, // el idProductoTransformado se setea después por el Service
            ubicacionSalida,
            fecha
        );

        String resultado = transfService.procesarOrdenTransformacion(orden, productoTransformadoTemporal);

        return resultado;
    }

    public List<OrdenTransformacion> obtenerHistorial() {
        return transfService.getHistorialTransformacion();
    }
}

