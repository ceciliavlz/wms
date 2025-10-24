package view;

import java.util.Scanner;

public abstract class View {

    public void mostrarMensaje(String msj){
        System.out.println(msj);
    }

    public static int leerEntero(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Ingrese un número válido: ");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine(); // limpiar buffer
        return val;
    }

}
