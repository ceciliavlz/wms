package view;

import java.util.Collections;
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
        super.mostrarMensaje("\n==== Menú Naves =======================");
        super.mostrarMensaje("1. Ver naves");
        super.mostrarMensaje("2. Crear nave");
        super.mostrarMensaje("3. Crear rack en nave");
        super.mostrarMensaje("4. Listar racks en nave");   
        super.mostrarMensaje("5. Listar ubicaciones disponibles de un rack");
        super.mostrarMensaje("6. Mostrar peso de un rack");
        super.mostrarMensaje("0. Volver");
        super.mostrarMensaje("==============================================");

            int opcion = super.leerEntero(sc);

            switch (opcion) {
                case 1 -> listarNavesMenu();
                case 2 -> crearNuevaNave();
                case 3 -> crearRack();
                case 4 -> listarRacks();
                case 5 -> listarUbicacionesDisponiblesRack();
                case 6 -> pesoUbicacionesRack();
                case 0 -> { 
                    salir = true;
                    return;
                 }
                default -> super.mostrarMensaje("\nOpción inválida");
            }
        }
    }
    private void crearNuevaNave(){
        int id = naveCtrl.crearNave();
        super.mostrarMensaje("Nave creada con ID "+ id);
        }

    private boolean listarNavesMenu() {
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

    private void listarUbicacionesDisponiblesRack(){
        int idRack = pedirIdRack();
        List<String> ubicaciones = naveCtrl.listarUbicacionesRack(idRack);

        if (ubicaciones.isEmpty()){
            super.mostrarMensaje("No hay rack con esa ID");
        } else {
            super.mostrarMensaje("NAVE-RACK-FILA-COL: \nN-R-F-C");
            for (String u : ubicaciones){
                super.mostrarMensaje(u);
            }
        }
    }

    private void pesoUbicacionesRack(){
        int idRack = pedirIdRack();
        List<String> respuesta = naveCtrl.calcularPesosRack(idRack);
        Collections.reverse(respuesta);        
        if (naveCtrl.calcularPesosRack(idRack).isEmpty()){
            super.mostrarMensaje("No se econtro un rack con ese ID");
            return;
        }
        super.mostrarMensaje("==== Peso de ubicaciones rack "+idRack+" ====");
        for (String r : respuesta){
            super.mostrarMensaje(r);
        }
    }

    private int pedirIdNave(){
        super.mostrarMensaje("Ingrese ID nave: ");
        int idNave = super.leerEntero(sc);
        return idNave;
    }

    private int pedirIdRack(){
        super.mostrarMensaje("Ingrese ID rack: ");
        int idNave = super.leerEntero(sc);
        return idNave;
    }
}

