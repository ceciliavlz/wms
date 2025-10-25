package view;

import java.util.List;
import java.util.Scanner;

import controller.NaveController;

public class NaveView extends View{
    private final NaveController naveCtrl;
    Scanner sc;

    public NaveView(NaveController naveCtrl, Scanner sc){
        this.naveCtrl = naveCtrl;
        this.sc = sc;
    }    

    public void mostrarMenuNaves() {
    boolean salir = false;

        while (!salir) {
        System.out.println("\n==== Menú Naves =======================");
        System.out.println("1. Ver naves");
        System.out.println("2. Crear nave");
        System.out.println("3. Crear rack en nave");
        System.out.println("4. Listar racks en nave");   
        System.out.println("0. Volver");
        System.out.println("==============================================");

            int opcion = super.leerEntero(sc);

            switch (opcion) {
                case 1 -> listarNavesMenu();
                case 2 -> crearNuevaNave();
                case 3 -> crearRack();
                case 4 -> listarRacks();
                case 0 -> { 
                    salir = true;
                    return;
                 }
                default -> super.mostrarMensaje("\nOpción inválida");
            }
        }
    }
    public void crearNuevaNave(){
        int id = naveCtrl.crearNave();
        super.mostrarMensaje("Nave creada con ID "+ id);
        }

    public boolean listarNavesMenu() {
        List<String> respuesta = naveCtrl.listarNaves();
        if (respuesta.isEmpty()) {
            super.mostrarMensaje("No hay naves registradas.");
            return false;

        } else{
            super.mostrarMensaje("\n====== Lista de Naves ======");
            for (String nave : respuesta) {
                super.mostrarMensaje(nave);
            }
            super.mostrarMensaje("============================");
            return true;
        }
    }

    private void crearRack(){
        if (listarNavesMenu()) {
            int idNave = pedirIdNave();
            int idRack = naveCtrl.crearRack(idNave);

            if (idRack == 0){
                super.mostrarMensaje("No se encontró nave con ID "+idNave);
            } else {
                super.mostrarMensaje("Rack de ID "+ idRack +" creado con éxito.");
            }
        } else {
            return; }
    }

    private void listarRacks(){
        if (naveCtrl.hayNaves()){
            int idNave = pedirIdNave();
            List<String> respuesta = naveCtrl.listarRacks(idNave);
            if (respuesta.isEmpty()){
                super.mostrarMensaje("No hay racks registrados en la nave " + idNave);
            } else{
                super.mostrarMensaje("==== Lista de Racks en Nave "+idNave+" ========");
                for (String rack : respuesta) {
                super.mostrarMensaje(rack);
                }
                super.mostrarMensaje("====================================");
            }
        } else{
            super.mostrarMensaje("No hay naves registradas."); }
    }

    public int pedirIdNave(){
        super.mostrarMensaje("Ingrese ID nave: ");
        int idNave = super.leerEntero(sc);
        return idNave;
    }
}

