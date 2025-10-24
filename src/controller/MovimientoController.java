package controller;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Stream;

import model.OrdenMovimiento;
import model.StockUbicacion;
import model.TipoMovimiento;
import model.Ubicacion;
import services.MovimientoService;
import services.StockService;
import view.OrdenesMovView;

public class MovimientoController {
    private final MovimientoService movService;
    private final StockService stockService;
    private final OrdenesMovView movView;
    Scanner sc;

    public MovimientoController(StockService stockService, MovimientoService movService, OrdenesMovView movView, Scanner sc) {
        this.stockService = stockService;
        this.movService = movService;
        this.movView = movView;
        this.sc = sc;
    }

    public void mostrarMenuMovimiento() {

    boolean volver = false;

        while (!volver) {

            movView.mostrarMenuOpciones();

            int opcion = OrdenesMovView.leerEntero(sc);

            switch (opcion) {
                case 1 -> generarOrdenIngreso(sc);
                case 2 -> generarOrdenEgreso(sc);
                case 3 -> generarOrdenMovInterno(sc);
                case 0 -> { 
                    volver = true;
                    return;
                 }
                default -> movView.mostrarMensaje("\nOpción inválida");
            }
        }
    }

    private void generarOrdenIngreso(Scanner sc) {
        int id = movView.leerIdProducto(sc);
        if (!stockService.getProductosMap().containsKey(id)) {
            movView.mostrarMensaje("ERROR: Producto no registrado.");
            return; }

        listarUbicacionesOrdenadas();
        movView.mostrarMensaje("Ingrese codigo ubicacion destino:     (NAVE-RACK-FILA-COLUMNA Ej 1-1-2-3)");
        String ubi = movView.leerUbicacion(sc);
        movView.mostrarMensaje("Cantidad a ingresar: ");
        int cantidad = movView.leerCantidad(sc);

        OrdenMovimiento ingreso = new OrdenMovimiento(
            TipoMovimiento.INGRESO,
            0, //temporario, se asigna en procesarOrdenMovimiento
            cantidad,
            id,
            LocalDate.now(),
            ubi
        );

        boolean procesada = movService.procesarOrdenMovimiento(ingreso);
        if (procesada) { movView.mostrarMensaje("OK: Producto ID "+id+" guardado en "+ingreso.getUbicacion());}
    }

    private void listarUbicacionesOrdenadas() {
        Stream<Ubicacion> ubisOrdenadas= stockService.listarUbiDisponiblesOrdenadas();
        ubisOrdenadas.forEach(u -> movView.mostrarMensaje(" - " + u.getCodigoUbicacion()));
    }

    public void generarOrdenEgreso(Scanner sc) {
        int id = movView.leerIdProducto(sc);
        if (!stockService.getProductosMap().containsKey(id)) {
            movView.mostrarMensaje("ERROR: Producto no registrado.");
            return;
        }
        mostrarUbicacionesDeProducto(id);

        movView.mostrarMensaje("Ubicación origen:  NAVE-RACK-FILA-COLUMNA");
        String ubi = movView.leerUbicacion(sc);
        movView.mostrarMensaje("Cantidad a retirar: ");
        int cantidad = movView.leerCantidad(sc);

        OrdenMovimiento egreso = new OrdenMovimiento(
            TipoMovimiento.EGRESO,
            0, //temporario, se asigna en procesarOrdenMovimiento
            cantidad,
            id,
            LocalDate.now(),
            ubi
        );
        if (movService.procesarOrdenMovimiento(egreso)) {
            movView.mostrarMensaje("Egreso realizado correctamente.");
        }
    }

    private void mostrarUbicacionesDeProducto(int idProd) {
        movView.mostrarMensaje("=== UBICACIONES DEL PRODUCTO ID " + idProd + " ===");
        stockService.getStockPorProducto(idProd).stream()  
            .sorted(Comparator.comparing(StockUbicacion::getCodigoUbicacion))
            .forEach(su -> movView.mostrarMensaje(" - " + su.getCodigoUbicacion() + " : " + su.getCantidad() + " unidades"));
        movView.mostrarMensaje("================================");
    }

    public void generarOrdenMovInterno(Scanner sc){
        int id = movView.leerIdProducto(sc);
        if (!stockService.getProductosMap().containsKey(id)) {
            movView.mostrarMensaje("ERROR: Producto no registrado.");
            return;
        }
        mostrarUbicacionesDeProducto(id);

        movView.mostrarMensaje("Ubicación origen: ");
        String ubiOrigen = movView.leerUbicacion(sc);
        movView.mostrarMensaje("Ubicación destino: ");
        String ubiDestino = movView.leerUbicacion(sc);
        movView.mostrarMensaje("Cantidad a mover: ");
        int cant = movView.leerCantidad(sc);

        OrdenMovimiento interno = new OrdenMovimiento(
            TipoMovimiento.INTERNO,
            0,  //temporario, se asigna en procesarOrdenMovimiento
            cant,
            id,
            LocalDate.now(),
            ubiOrigen,
            ubiDestino
        );
        if (movService.procesarOrdenMovimiento(interno)) {
            movView.mostrarMensaje("Movimiento interno realizado correctamente.");
        }

    }
}