package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.OrdenMovimiento;
import repositories.OrdenMovRepository;

public class MovimientoService {
    private StockService stockService;
    private List<OrdenMovimiento> historialMovimientos = new ArrayList<>();

    public MovimientoService(StockService stockService) {
        this.stockService = stockService;
        cargarHistorial();
    }

    public String procesarOrdenMovimiento(OrdenMovimiento orden) {
        String resultado = "";
        orden.setIdOrdenMov(getProximoOrdenId());   //asigna id autoincremental
        switch (orden.getTipoMovimientoOrden()) {
            case INGRESO:
                resultado = stockService.agregarStockAUbicacion(
                    orden.getIdProducto(), 
                    orden.getUbicacion(), 
                    orden.getCantidad());
                    break;
            case EGRESO:
                resultado = stockService.retirarStockDeUbicacion(
                    orden.getIdProducto(), 
                    orden.getUbicacion(), 
                    orden.getCantidad());
                    break;
            case INTERNO:
                resultado = stockService.moverStockEntreUbicaciones(
                    orden.getIdProducto(), 
                    orden.getUbicacionOrigen(), 
                    orden.getUbicacionDestino(), 
                    orden.getCantidad());
                    break;
        }
        System.out.println(resultado.startsWith("OK"));
        if (resultado.startsWith("OK:")) {
            historialMovimientos.add(orden);
        }
        return resultado;
    }   

    public void guardarHistorial() {
        try {
            OrdenMovRepository.guardarOrdenes(historialMovimientos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarHistorial() {
        try { historialMovimientos = OrdenMovRepository.cargarOrdenes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getProximoOrdenId(){
        if (historialMovimientos.isEmpty()){
            return 1;
        } else {
            return historialMovimientos.getLast().getIdOrdenMov() + 1;
        }     
    }

    public List<OrdenMovimiento> historialMovimientoProducto(int idProducto){
        List<OrdenMovimiento> listaProductos = new ArrayList<>();
        for (OrdenMovimiento o : historialMovimientos){
            if (idProducto == o.getIdProducto()){
                listaProductos.add(o);
            }
        }
        return listaProductos;
    }   
}
