package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.OrdenMovimiento;
import repositories.OrdenMovRepository;

public class MovimientoService {
    private StockService StockService;
    private List<OrdenMovimiento> historialMovimientos = new ArrayList<>();         //thinking the thinks hmhmmh

    public MovimientoService(StockService StockService) {
        this.StockService = StockService;
        cargarHistorial();
    }

    public boolean procesarOrdenMovimiento(OrdenMovimiento orden) {
        boolean ok = false;
        orden.setIdOrdenMov(getProximoOrdenId());   //asigna id automatico
        switch (orden.getTipoMovimientoOrden()) {
            case INGRESO:
                ok = StockService.agregarStockAUbicacion(
                    orden.getIdProducto(), 
                    orden.getUbicacion(), 
                    orden.getCantidad());
                    break;
            case EGRESO:
                ok = StockService.retirarStockDeUbicacion(
                    orden.getIdProducto(), 
                    orden.getUbicacion(), 
                    orden.getCantidad());
                    break;
            case INTERNO:
                ok = StockService.moverStockEntreUbicaciones(
                    orden.getIdProducto(), 
                    orden.getUbicacionOrigen(), 
                    orden.getUbicacionDestino(), 
                    orden.getCantidad());
                    break;
        }
        if (ok) {
            historialMovimientos.add(orden);
        }
        return ok;
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
        return (historialMovimientos.size() + 1);        
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
