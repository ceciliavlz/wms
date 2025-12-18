package view;

import java.util.Scanner;

import controller.MovimientoController;
import controller.ProductoController;

public class OrdenesMovView extends View{
    private final MovimientoController movimientoCtrl;
    private final ProductoController productoCtrl;

    Scanner sc;

    public OrdenesMovView(MovimientoController movimientoCtrl, ProductoController productoCtrl, Scanner sc){
        this.movimientoCtrl = movimientoCtrl;
        this.productoCtrl = productoCtrl;
        this.sc = sc;
    }

    public void mostrarMenuMovimiento() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n===== Menú órdenes de movimiento =======");
            System.out.println("1. Generar orden de ingreso");
            System.out.println("2. Generar orden de egreso");
            System.out.println("3. Generar orden de traslado interno");
            System.out.println("0. Volver");
            System.out.println("===========================================");

            int opcion = super.leerEntero(sc);

            switch (opcion) {
                case 1 -> generarOrdenIngreso();
                case 2 -> generarOrdenEgreso();
                case 3 -> generarOrdenMovInterno();
                case 0 -> { 
                    volver = true;
                    return;
                 }
                default -> super.mostrarMensaje("\nOpción inválida");
            }
        }
    }

    private void generarOrdenIngreso() {
        int id = leerIdProducto();
        String producto = productoCtrl.buscarProductoPorId(id);

        if(producto.equals("")){
            super.mostrarMensaje("No se encontró ningún producto con esa ID.");
        } else {
            super.mostrarMensaje(producto);
            super.mostrarMensaje("Ingrese codigo ubicacion destino:     (NAVE-RACK-FILA-COLUMNA Ej 1-1-2-3)");
            String ubicacion = leerUbicacion();   //VALIDAR UBICACION
            super.mostrarMensaje("Cantidad a ingresar: ");
            int cantidad = leerCantidad();    

            String respuesta = movimientoCtrl.generarOrdenIngreso(id, ubicacion, cantidad);
            super.mostrarMensaje(respuesta);
        }
    }

    private void generarOrdenEgreso() {
        int id = leerIdProducto();
        String producto = productoCtrl.buscarProductoPorId(id);

        if(producto.equals("")){
            super.mostrarMensaje("No se encontró ningún producto con esa ID.");
        } else {
            super.mostrarMensaje(producto);
            listarUbicacionesProducto(id);
            super.mostrarMensaje("Ingrese codigo ubicación:     (NAVE-RACK-FILA-COLUMNA Ej 1-1-2-3)");
            String ubicacion = leerUbicacion();
            super.mostrarMensaje("Cantidad a quitar: ");
            int cantidad = leerCantidad();

            String respuesta = movimientoCtrl.generarOrdenEgreso(id, ubicacion, cantidad);
            super.mostrarMensaje(respuesta);
        }
    }

    private void generarOrdenMovInterno() {
        int id = leerIdProducto();
        String producto = productoCtrl.buscarProductoPorId(id);

        if(producto.equals("")){
            super.mostrarMensaje("No se encontró ningún producto con esa ID.");
        } else {
            super.mostrarMensaje(producto);
            listarUbicacionesProducto(id);
            super.mostrarMensaje("Ubicación origen: ");
            String ubicacionOrigen = leerUbicacion();

            super.mostrarMensaje("Ubicación destino: ");
            String ubicacionDestino = leerUbicacion();

            super.mostrarMensaje("Cantidad a mover: ");
            int cantidad = leerCantidad();

            String respuesta = movimientoCtrl.generarOrdenMovInterno(id, ubicacionOrigen, ubicacionDestino, cantidad);
            super.mostrarMensaje(respuesta);
        }
    }

    private void listarUbicacionesProducto(int idProd){
        super.mostrarMensaje("Ubicado en: ");
        for(String ubicaciones : productoCtrl.mostrarUbicacionesDeProducto(idProd)){
            super.mostrarMensaje(ubicaciones);
        }
    }
    
    private int leerIdProducto (){
        System.out.print("ID de producto: ");
        return super.leerEntero(sc);
    }

    private String leerUbicacion(){
        String ubi = super.leerStringSinComas(sc);
        return ubi;
    }

    private int leerCantidad (){
        int cantidad = super.leerEntero(sc);
        return cantidad;
    }
}