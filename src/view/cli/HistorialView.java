package view.cli;

import java.util.List;
import java.util.Scanner;
import controller.HistorialController;

public class HistorialView extends View {
    private final HistorialController historialCtrl;
    private final Scanner sc;

    public HistorialView(HistorialController historialCtrl, Scanner sc) {
        this.historialCtrl = historialCtrl;
        this.sc = sc;
    }

    public void mostrarMenuHistorial() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n====== MENÚ HISTORIALES ======");
            System.out.println("1. Ver movimientos y transformaciones por producto");
            System.out.println("2. Ver historial general de transformaciones");
            System.out.println("0. Volver");
            System.out.println("==============================");

            int opcion = leerEntero(sc);

            switch (opcion) {
                case 1 -> verHistorialDeProducto();
                case 2 -> verHistorialDeTransformaciones();
                case 0 -> volver = true;
                default -> mostrarMensaje("\nOpción inválida. Intente nuevamente.");
            }
        }
    }

    private void verHistorialDeProducto() {
        int id = pedirIdProducto();

        List<String> movimientos = historialCtrl.verHistorialProducto(id);

        if (movimientos.isEmpty()) {
            mostrarMensaje("\n No se encontraron movimientos ni transformaciones para ese producto.");
        } else {
            mostrarMensaje("\n=== HISTORIAL DEL PRODUCTO " + id + " ===");
            mostrarMensaje("TIPO | FECHA | DETALLE");
            mostrarMensaje("------------------------------------------");
            for (String mov : movimientos) {
                mostrarMensaje(mov);
            }
            mostrarMensaje("------------------------------------------");
        }
    }

    private void verHistorialDeTransformaciones() {
        List<String> transformaciones = historialCtrl.verHistorialTransformaciones();

        if (transformaciones.isEmpty()) {
            mostrarMensaje("\nNo hay transformaciones registradas.");
        } else {
            mostrarMensaje("\n=== HISTORIAL GENERAL DE TRANSFORMACIONES ===");
            mostrarMensaje("TIPO | FECHA | DETALLE");
            mostrarMensaje("---------------------------------------------");
            for (String t : transformaciones) {
                mostrarMensaje(t);
            }
            mostrarMensaje("---------------------------------------------");
        }
    }

    private int pedirIdProducto() {
        System.out.print("Ingrese el ID del producto: ");
        return leerEntero(sc);
    }
}

