package controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Producto;
import model.UnidadMedida;
import services.StockService;

public class ProductoController {
    private final StockService stockService;

    public ProductoController(StockService stockService) {
        this.stockService = stockService;
    }

    public String agregarProducto(String descripcion, String unidadMedida, double peso, double capacidad) {
        UnidadMedida unidadMed;

        try { 
            unidadMed = UnidadMedida.valueOf(unidadMedida.trim().toUpperCase());
        } catch (Exception e) {
            return "";
        }

        Producto p = new Producto(descripcion, unidadMed, peso, capacidad);
        stockService.registrarProducto(p);

        return p.toString();
    }

    public List<String> listarProductos() {
        List<String> respuesta = new ArrayList<String>();
        Map<Integer, Producto> productos = stockService.getProductosMap();
        
        for (Producto p : productos.values()) {
             respuesta.add(p.toString());
        }

        return respuesta;
    }

    public String buscarProductoPorId(int id) {
        Producto p = stockService.getProductoPorId(id);
        if (p != null) {
            return p.toString();
        } else {
            return "";
        }
    }

    public void eliminarProducto(int id) {
        stockService.eliminarProductoPorId(id);
    }

    public boolean existeProducto(int id){
        if (stockService.getProductoPorId(id) == null){
            return false;
        }
        return true;
    }
}
