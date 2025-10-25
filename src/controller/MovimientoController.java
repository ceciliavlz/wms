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

        if (movService.procesarOrdenMovimiento(ordenIngreso)) { 
            return "OK: Producto ID " + productoID + " guardado en " + ordenIngreso.getUbicacion();
        } else {
            return "Fallo al procesar el moviento";
        }
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

        if (movService.procesarOrdenMovimiento(ordenEgreso)) {
            return "OK: Producto ID " + productoID + " retirado de " + ordenEgreso.getUbicacion();
        } else {
            return "Fallo al procesar el moviento";
        }
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

        if (movService.procesarOrdenMovimiento(ordenInterno)) {
            return "OK: Producto ID " + productoID + "trasladado de " + ordenInterno.getUbicacionOrigen() + " a " + ordenInterno.getUbicacionDestino();
        } else {
            return "Fallo al procesar el moviento";
        }

    }

    /*
    private void listarUbicacionesOrdenadas() {
        Stream<Ubicacion> ubisOrdenadas= stockService.listarUbiDisponiblesOrdenadas();
        ubisOrdenadas.forEach(u -> movView.mostrarMensaje(" - " + u.getCodigoUbicacion()));
    }

    private void mostrarUbicacionesDeProducto(int idProd) {
        movView.mostrarMensaje("=== UBICACIONES DEL PRODUCTO ID " + idProd + " ===");
        stockService.getStockPorProducto(idProd).stream()  
            .sorted(Comparator.comparing(StockUbicacion::getCodigoUbicacion))
            .forEach(su -> movView.mostrarMensaje(" - " + su.getCodigoUbicacion() + " : " + su.getCantidad() + " unidades"));
        movView.mostrarMensaje("================================");
    }
    */
}