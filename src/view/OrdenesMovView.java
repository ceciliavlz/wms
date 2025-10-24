package view;

import java.util.Scanner;

public class OrdenesMovView extends View{

    public void mostrarMenuOpciones() {
        System.out.println("\n===== Menú órdenes de movimiento =======");
        System.out.println("1. Generar orden de ingreso");
        System.out.println("2. Generar orden de egreso");
        System.out.println("3. Generar orden de traslado interno");
        System.out.println("0. Volver");
        System.out.println("===========================================");
    }

    public int leerIdProducto (Scanner sc){
        System.out.print("ID de producto: ");
        return super.leerEntero(sc);
    }

    public String leerUbicacion(Scanner sc){
        String ubi = sc.nextLine();
        return ubi;
    }

    public int leerCantidad (Scanner sc){
        int cantidad = super.leerEntero(sc);
        return cantidad;
    }
}