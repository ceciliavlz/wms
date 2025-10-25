package controller;

import java.util.ArrayList;
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
            stock.add(stockPorUbicacion.toString());
            }
        return stock;
    }

    public List<String> agrupadoPorProducto() {
        List<String> stockProducto = new ArrayList<>();

        stockService.getStockAgrupadoPorProducto().forEach((id, cantidad) ->
            stockProducto.add("Producto " + id + ": " + cantidad)
        );

        return stockProducto;
    }

    public List<String> agrupadoPorUbicacion() {
        List<String> stockProducto = new ArrayList<>();

        stockService.getStockAgrupadoPorUbicacion().forEach((codigo, cantidad) ->
            stockProducto.add("Ubicación " + codigo + ": " + cantidad)
        );
        
        return stockProducto;
    }

}
