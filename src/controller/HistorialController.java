package controller;

import java.util.List;
import java.util.Scanner;

import model.OrdenMovimiento;
import services.MovimientoService;
import view.HistorialView;

public class HistorialController {
    HistorialView historialView;
    MovimientoService movService;
    Scanner sc;

    public HistorialController (MovimientoService movService, HistorialView historialView, Scanner sc){
        this.historialView = historialView;
        this.movService = movService;
        this.sc = sc;
    }

    public void mostrarMenuHistorial() {

    boolean volver = false;

        while (!volver) {

            historialView.opcionesMenuHistorial();

            int opcion = HistorialView.leerEntero(sc);

            switch (opcion) {
                case 1 -> verHistorialMov(sc);
                case 2 -> System.out.println("TO DO");
                case 0 -> { 
                    volver = true;
                    return;
                 }
                default -> historialView.mostrarMensaje("\nOpción inválida");
            }
        }
    }

    private void verHistorialMov(Scanner sc) {
        int id = historialView.pedirIdProducto(sc);
        List<OrdenMovimiento> historial = movService.historialMovimientoProducto(id);
        if (historial.isEmpty()) {
            historialView.mostrarMensaje("No hay movimientos registrados para ese producto.");
        } else {
            for (OrdenMovimiento o : historial) {
                historialView.mostrarMensaje(" TIPO --- | FECHA --- | CANTIDAD ---");
                historialView.mostrarMensaje(o.getTipoMovimientoOrden() + " | " + o.getFecha() + " | " + o.getCantidad());
            }
        }
    }
}
