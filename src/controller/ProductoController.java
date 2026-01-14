package controller;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import model.Producto;
import model.StockUbicacion;
import model.UnidadMedida;
import services.PermissionService;
import services.StockService;

public class ProductoController {
    private final StockService stockService;
    private PermissionService permissionService;

    public ProductoController(StockService stockService) {
        this.stockService = stockService;
    }

    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public String agregarProducto(String descripcion, String unidadMedida, double peso, double capacidad, int stockMin, String grupo) {
        if (permissionService != null && !permissionService.canWrite()) {
            return "ERROR: No tiene permisos para crear productos.";
        }
        UnidadMedida unidadMed;

        try { 
            unidadMed = UnidadMedida.valueOf(unidadMedida.trim().toUpperCase());
        } catch (Exception e) {
            return "";
        }

        Producto p = new Producto(descripcion, unidadMed, peso, capacidad, stockMin, grupo);
        stockService.registrarProducto(p); //asigna id y codigo con grupo

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
        if (permissionService != null && !permissionService.canDelete()) {
            System.out.println("ERROR: No tiene permisos para eliminar productos.");
            return;
        }
        stockService.eliminarProductoPorId(id);
    }

    public boolean existeProducto(int id){
        if (stockService.getProductoPorId(id) == null){
            return false;
        }
        return true;
    }

    public List<String> mostrarUbicacionesDeProducto(int idProd) {
        List<String> ubicaciones = new ArrayList<>();
        stockService.getStockPorProducto(idProd).stream()  
            .sorted(Comparator.comparing(StockUbicacion::getCodigoUbicacion))
            .forEach(su -> ubicaciones.add(su.getCodigoUbicacion() + " : " + su.getCantidad() + " unidades"));
        return ubicaciones;
    }
}
