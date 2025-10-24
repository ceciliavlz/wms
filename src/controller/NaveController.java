package controller;

import java.util.Scanner;

import model.Nave;
import model.Rack;
import services.NaveService;
import view.NaveView;

public class NaveController {
    NaveService naveService;
    NaveView naveView;
    Scanner sc;

    public NaveController(NaveService naveService, NaveView naveView, Scanner sc){
        this.naveService = naveService;
        this.naveView = naveView;
        this.sc=sc;
    }

    public void mostrarMenuNaves() {

    boolean salir = false;

        while (!salir) {

            naveView.opcionesMenuNaves();

            int opcion = NaveView.leerEntero(sc);

            switch (opcion) {
                case 1 -> listarNaves();
                case 2 -> crearNave();
                case 3 -> crearRack(sc);
                case 4 -> listarRacks(sc);
                case 0 -> { 
                    salir = true;
                    return;
                 }
                default -> System.out.println("\nOpción inválida");
            }
        }
    }

    private void crearNave() {
        Nave nave = naveService.crearNave();
        naveView.mostrarMensaje("Nave creada con ID: " + nave.getIdNave());
    }

    private boolean listarNaves() {
        if (!naveService.hayNaves()) {
            naveView.mostrarMensaje("No hay naves registradas.");
            return false;

        } else{
            System.out.println("\n====== Lista de Naves ======");
            for (Nave nave : naveService.getTodasLasNaves()) {
                naveView.mostrarMensaje(nave.toString());
            }
            System.out.println("============================");
            return true;
        }
    }

    private void crearRack(Scanner sc){
        if (listarNaves()) {
            naveView.mostrarMensaje("Ingrese ID Nave donde crear rack: ");
            int id = naveView.pedirIdNave(sc);
            if (naveService.getNavePorId(id) == null) {
                naveView.mostrarMensaje("No hay nave con ese id"); return;}
            else {
                Rack nuevo = naveService.crearRackEnNave(id);
                naveView.mostrarMensaje("Rack de ID " + nuevo.getIdRack() + " creado con éxito.");
            }
        } else{
            return;
        }
    }

    private void listarRacks(Scanner sc){
        if (naveService.hayNaves()){
            naveView.mostrarMensaje("ID Nave: ");
            int id = naveView.pedirIdNave(sc);
            if (naveService.getRacksDeNave(id).isEmpty()){
                naveView.mostrarMensaje("No hay racks registrados en la nave " + id);
            } else{
            naveView.mostrarMensaje("\n==== Lista de Racks en Nave "+id+" ========");
                naveService.getRacksDeNave(id).forEach(rack -> 
                { naveView.mostrarMensaje(rack.toString());});
            }
        } else {
            naveView.mostrarMensaje("No hay naves registradas.");
        }
    }
}
