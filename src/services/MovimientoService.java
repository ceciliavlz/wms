package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import DAO.OrdenMovDAO;
import model.OrdenMovimiento;

public class MovimientoService {
    private StockService stockService;
    private List<OrdenMovimiento> historialMovimientos = new ArrayList<>();
    private String usuarioActual = null;

    public MovimientoService(StockService stockService) {
        this.stockService = stockService;
        cargarHistorial();
    }

    public void setUsuarioActual(String username) {
        this.usuarioActual = username;
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
            // Asignar usuario responsable para auditoría
            if (usuarioActual != null) {
                orden.setUsuarioResponsable(usuarioActual);
            }
            historialMovimientos.add(orden);
        }
        return resultado;
    }   

    public void guardarHistorial() {
        try {
            OrdenMovDAO.guardarOrdenes(historialMovimientos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarHistorial() {
        try { historialMovimientos = OrdenMovDAO.cargarOrdenes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getProximoOrdenId(){
        if (historialMovimientos.isEmpty()){
            return 1;
        } else {
            return historialMovimientos.get(historialMovimientos.size() - 1).getIdOrdenMov() + 1;
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
