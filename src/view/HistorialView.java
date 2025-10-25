package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import controller.HistorialController;

public class HistorialView extends View {
    private final HistorialController historialCtrl;
    Scanner sc;

    public HistorialView(HistorialController historialCtrl, Scanner sc){
        this.historialCtrl = historialCtrl;
        this.sc = sc;
    }
    
    public void mostrarMenuHistorial() {
        boolean volver = false;
        
        while (!volver) {
            System.out.println("\n==== Menú Historiales ===========");
            System.out.println("1. Movimientos por producto");
            System.out.println("2. Historial de transformaciones"); //TODO
            System.out.println("0. Volver");
            System.out.println("===========================================");

            int opcion = super.leerEntero(sc);

            switch (opcion) {
                case 1 -> verHistorialDeMovimientos();
                case 2 -> super.mostrarMensaje("TO DO");
                case 0 -> { 
                    volver = true;
                    return;
                 }
                default -> super.mostrarMensaje("\nOpción inválida");
            }
        }
    }

    private void verHistorialDeMovimientos(){
        int id = pedirIdProducto(sc);

        List<String> movimientos = new ArrayList<String>();
        movimientos = historialCtrl.verHistorialMov(id);

        if(movimientos.isEmpty()){

        } else {
            super.mostrarMensaje(" TIPO --- | FECHA --- | CANTIDAD ---");
            for (String mov : movimientos) {
                super.mostrarMensaje(mov);
            }
        }
    }

    public int pedirIdProducto(Scanner sc) {
        System.out.print("Ingrese el ID del producto: ");
        return super.leerEntero(sc);
    }
}
