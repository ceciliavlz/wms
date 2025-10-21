package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.OrdenMovimiento;
import repositories.OrdenMovRepository;

public class MovimientoService {
    private ManejadorStock manejadorStock;
    private List<OrdenMovimiento> historialMovimientos = new ArrayList<>();
    private OrdenMovRepository repo = new OrdenMovRepository(); 

    public MovimientoService(ManejadorStock manejadorStock) {
        this.manejadorStock = manejadorStock;
        cargarHistorial();
    }

    public boolean procesarOrdenMovimiento(OrdenMovimiento orden) {
        boolean ok = false;
        switch (orden.getTipoMovimientoOrden()) {
            case INGRESO:
                ok = manejadorStock.agregarStockAUbicacion(
                    orden.getIdProducto(), 
                    orden.getUbicacion(), 
                    orden.getCantidad());
                    break;
            case EGRESO:
                ok = manejadorStock.retirarStockDeUbicacion(
                    orden.getIdProducto(), 
                    orden.getUbicacion(), 
                    orden.getCantidad());
                    break;
            case INTERNO:
                ok = manejadorStock.moverStockEntreUbicaciones(
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
            repo.guardarOrdenes(historialMovimientos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarHistorial() {
        try {
            historialMovimientos = repo.cargarOrdenes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
