package controller;
import java.time.LocalDate;

import model.OrdenMovimiento;
import model.TipoMovimiento;
import services.MovimientoService;

public class MovimientoController {
    private final MovimientoService movService;

    public MovimientoController(MovimientoService movService) {
        this.movService = movService;
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
}