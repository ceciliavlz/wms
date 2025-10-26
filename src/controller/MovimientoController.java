package controller;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import model.OrdenMovimiento;
import model.StockUbicacion;
import model.TipoMovimiento;
import services.MovimientoService;
import services.StockService;

public class MovimientoController {
    private final MovimientoService movService;
    StockService stockService;

    public MovimientoController(MovimientoService movService,StockService stockService) {
        this.movService = movService;
        this.stockService = stockService;
    }

    public String generarOrdenIngreso(int productoID, String ubicacion, int cantidad) {
        OrdenMovimiento ordenIngreso = new OrdenMovimiento(
            TipoMovimiento.INGRESO,
            0, //temporario, se asigna en procesarOrdenMovimiento
            cantidad,
            productoID,
            LocalDate.now(),
            ubicacion
        );
            return movService.procesarOrdenMovimiento(ordenIngreso);
    }

    public String generarOrdenEgreso(int productoID, String ubicacion, int cantidad) {
        OrdenMovimiento ordenEgreso = new OrdenMovimiento(
            TipoMovimiento.EGRESO,
            0, //temporario, se asigna en procesarOrdenMovimiento
            cantidad,
            productoID,
            LocalDate.now(),
            ubicacion
        );
        return movService.procesarOrdenMovimiento(ordenEgreso);
    }

    public String generarOrdenMovInterno(int productoID, String ubicacionOrigen, String ubicacionDestino, int cantidad){
        OrdenMovimiento ordenInterno = new OrdenMovimiento(
            TipoMovimiento.INTERNO,
            0,  //temporario, se asigna en procesarOrdenMovimiento
            cantidad,
            productoID,
            LocalDate.now(),
            ubicacionOrigen,
            ubicacionDestino
        );
        return movService.procesarOrdenMovimiento(ordenInterno);
    }

    /*
    private void listarUbicacionesOrdenadas() {
        Stream<Ubicacion> ubisOrdenadas= stockService.listarUbiDisponiblesOrdenadas();
        ubisOrdenadas.forEach(u -> movView.mostrarMensaje(" - " + u.getCodigoUbicacion()));
    }    */

    public List<String> mostrarUbicacionesDeProducto(int idProd) {
        List<String> ubicaciones = new ArrayList<>();
        stockService.getStockPorProducto(idProd).stream()  
            .sorted(Comparator.comparing(StockUbicacion::getCodigoUbicacion))
            .forEach(su -> ubicaciones.add(su.getCodigoUbicacion() + " : " + su.getCantidad() + " unidades"));
        return ubicaciones;
    }

}