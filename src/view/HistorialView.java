package view;

import java.util.Scanner;

public class HistorialView extends View{
    
    public void opcionesMenuHistorial(){
        System.out.println("\n==== Menú Historiales ===========");
        System.out.println("1. Movimientos por producto");
        System.out.println("2. Historial de transformaciones"); //TODO
        System.out.println("0. Volver");
        System.out.println("===========================================");
    }

    public int pedirIdProducto(Scanner sc) {
        System.out.print("Ingrese el ID del producto: ");
        return super.leerEntero(sc);
    }
}
