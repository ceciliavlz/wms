package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import DAO.OrdenTransfDAO;
import model.OrdenTransformacion;
import model.Producto;

public class TransformacionService {
    private StockService stockService;
    private List<OrdenTransformacion> historialTransformacion = new ArrayList<>();
    
    public TransformacionService(StockService stockService) {
        this.stockService = stockService;
        cargarHistorial();
    }

    public String procesarOrdenTransformacion(OrdenTransformacion orden, Producto datosProdTransformado) {
        orden.setIdOrdenTransf(getProximoOrdenId());   //asigna id autoincremental

        Producto productoEntrada = stockService.getProductoPorId(orden.getIdProductoEntrada());
        datosProdTransformado.setUnidadMedida(productoEntrada.getUnidadMedida());

        int idProd = stockService.registrarProducto(datosProdTransformado); //registra el nuevo producto
        orden.setIdProductoTransformado(idProd);
        
        Producto productoTransformado = stockService.getProductoPorId(idProd);

        double cantidadTransformadas = (orden.getCantidadEntrada() * productoEntrada.getCapacidad())
                                        / productoTransformado.getCapacidad();
        int unidadesTransformadas = (int) Math.floor(cantidadTransformadas);
        System.out.println(unidadesTransformadas);
        orden.setCantidadSalida(unidadesTransformadas); //set cantidad del nuevo producto

        String quitarStockViejo = stockService.retirarStockDeUbicacion(productoEntrada.getIdProducto(), 
            orden.getUbicacionProdEntrada(), orden.getCantidadEntrada()); //quito stock del producto de entrada
        String agregarStockNuevo = stockService.agregarStockAUbicacion(productoTransformado.getIdProducto(), 
            orden.getUbicacionSalida(), unidadesTransformadas); //agrego stock transformado a ubicacion

        if (quitarStockViejo.startsWith("OK:") && agregarStockNuevo.startsWith("OK:")) {
            historialTransformacion.add(orden);
            guardarHistorial();
            return "OK. Transformación realizada con éxito.";
        }

        return "ERROR: No se pudo realizar la transformación.";
    }   

    private int getProximoOrdenId(){
        if (historialTransformacion.isEmpty()){
            return 1;
        } else {
            return historialTransformacion.getLast().getIdOrdenTransf() + 1;
        }     
    }

    public void guardarHistorial() {
        try {
            OrdenTransfDAO.guardarOrdenes(historialTransformacion);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarHistorial() {
        try { historialTransformacion = OrdenTransfDAO.cargarOrdenes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<OrdenTransformacion> getHistorialTransformacion() {
        return historialTransformacion;
    }

    public List<OrdenTransformacion> historialTransfSalida (int idProd){
        List<OrdenTransformacion> lista = new ArrayList<>();
        for (OrdenTransformacion o : historialTransformacion) {
            if (o.getIdProductoTransformado() == idProd){
                lista.add(o);
            }
        }
        return lista;
    }

    public List<OrdenTransformacion> historialTransfEntrada (int idProd){
        List<OrdenTransformacion> lista = new ArrayList<>();
        for (OrdenTransformacion o : historialTransformacion) {
            if (o.getIdProductoEntrada() == idProd){
                lista.add(o);
            }
        }
        return lista;
    }

    public List<OrdenTransformacion> getHistorialCompleto() {
        return new ArrayList<>(historialTransformacion);
    }

}
