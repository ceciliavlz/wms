package controller;
import java.time.LocalDate;

import model.OrdenMovimiento;
import model.TipoMovimiento;
import services.MovimientoService;
import services.PermissionService;

public class MovimientoController {
    private final MovimientoService movService;
    private PermissionService permissionService;

    public MovimientoController(MovimientoService movService) {
        this.movService = movService;
    }

    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public String generarOrdenIngreso(int productoID, String ubicacion, int cantidad) {
        if (permissionService != null && !permissionService.canWrite()) {
            return "ERROR: No tiene permisos para crear movimientos.";
        }
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
        if (permissionService != null && !permissionService.canWrite()) {
            return "ERROR: No tiene permisos para crear movimientos.";
        }
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
        if (permissionService != null && !permissionService.canWrite()) {
            return "ERROR: No tiene permisos para crear movimientos.";
        }
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