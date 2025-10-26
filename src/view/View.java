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
    
    public static double leerDouble(Scanner sc) {
        while (!sc.hasNextDouble()) {
            System.out.print("Ingrese un número decimal válido: ");
            sc.next();
        }
        double val = sc.nextDouble();
        sc.nextLine();
        return val;
    }

    public static String leerStringSinComas(Scanner sc) {
        String input;
        while (true) {
            input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.print("El texto no puede estar vacío. Ingrese nuevamente: ");
            } else if (input.contains(",")) {
                System.out.print("No se permiten comas. Ingrese nuevamente: ");
            } else {
                break;
            }
        }
        return input;
    }

}
