package controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.StockUbicacion;
import services.StockService;

public class ConsultasController {
    private final StockService stockService;

    public ConsultasController(StockService stockService) {
        this.stockService = stockService;
    }

    public int stockDeUnProducto(int idProd) {
        return stockService.getStockTotalPorProducto(idProd);
    }

    public List<String> stockDeUnaUbicacion(String codigo) {
        List<String> stock = new ArrayList<>();

        List<StockUbicacion> stockUbicacion = stockService.getStockPorUbicacion(codigo);
        
        for (StockUbicacion stockPorUbicacion : stockUbicacion) {
            if (stockPorUbicacion.getCantidad() > 0){
              stock.add(stockPorUbicacion.toString());
                }
            }
        return stock;
    }

    public List<String> agrupadoPorProducto() {
        List<String> stockProducto = new ArrayList<>();

        stockService.getStockAgrupadoPorProducto().forEach((id, cantidad) -> {
            if (cantidad > 0){
                stockProducto.add("Producto " + id + ": " + cantidad);
            }
        });
        return stockProducto;
    }

    public List<String> agrupadoPorUbicacion() {
        List<String> stockProducto = new ArrayList<>();

        stockService.getStockAgrupadoPorUbicacion().forEach((codigo, cantidad) ->{
            if (cantidad > 0){
                stockProducto.add("Ubicación " + codigo + ": " + cantidad);
            }
        });  
        return stockProducto;
    }

    public List<String> mostrarUbicacionesDeProducto(int idProd) {
        List<String> ubicaciones = new ArrayList<>();
        stockService.getStockPorProducto(idProd).stream()  
            .sorted(Comparator.comparing(StockUbicacion::getCodigoUbicacion))
            .forEach(su -> ubicaciones.add(su.getCodigoUbicacion() + " : " + su.getCantidad() + " unidades"));
        return ubicaciones;
    }
}
