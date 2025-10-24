package view;

import java.util.Scanner;

public class ConsultasView extends View{

    public void opcionesMenuConsultas(){
        System.out.println("\n==== Menú consultas ================");
        System.out.println("1. Stock de un producto");
        System.out.println("2. Stock en una ubicacion");
        System.out.println("3. Stock agrupado por producto");
        System.out.println("4. Stock agrupado por ubicacion");   
        System.out.println("0. Volver");
        System.out.println("=======================================");
    }

    public int pedirIdProducto(Scanner sc) {
        System.out.print("Ingrese el ID del producto: ");
        return super.leerEntero(sc);
    }

    public String pedirCodigoUbicacion(Scanner sc) {
        System.out.print("Código de ubicación (NAVE-RACK-FILA-COLUMNA): ");
        return sc.nextLine().trim();
    }

    public void mostrarStockDeProducto(int idProd, int stock) {
        if (stock > 0)
            System.out.println("Stock total de P" + idProd + ": " + stock);
        else
            System.out.println("No hay stock de P" + idProd);
    }
}
