package view;

import java.util.Scanner;

public class NaveView extends View{

    public void opcionesMenuNaves(){
        System.out.println("\n==== Menú Naves =======================");
        System.out.println("1. Ver nave");
        System.out.println("2. Crear nave");
        System.out.println("3. Crear rack en nave");
        System.out.println("4. Listar racks en nave");   
        System.out.println("0. Volver");
        System.out.println("==============================================");
    }

    public int pedirIdNave(Scanner sc){
        int idNave = super.leerEntero(sc);
        return idNave;
    }
}

