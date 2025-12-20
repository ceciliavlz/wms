package view.cli;

import java.util.List;
import java.util.Scanner;
import controller.ConsultasController;
import controller.ProductoController;

public class ConsultasView extends View{
    private final ConsultasController consultasCtrl;
    private final ProductoController productoCtrl;
    Scanner sc;

    public ConsultasView(ConsultasController consultasCtrl, ProductoController productoCtrl, Scanner sc){
        this.consultasCtrl = consultasCtrl;
        this.productoCtrl = productoCtrl;
        this.sc = sc;
    }

    public void mostrarMenuConsultas() {
        boolean salir = false;
        while (!salir) {   
            System.out.println("\n==== Menú consultas ================");
            System.out.println("1. Stock de un producto");
            System.out.println("2. Stock en una ubicacion");
            System.out.println("3. Stock agrupado por producto");
            System.out.println("4. Stock agrupado por ubicacion");   
            System.out.println("0. Volver");
            System.out.println("=======================================");

            int opcion = super.leerEntero(sc);

            switch (opcion) {
                case 1 -> stockDeProducto();
                case 2 -> stockDeUbicacion();
                case 3 -> stockAgrupadoPorProducto();
                case 4 -> stockAgrupadoPorUbicacion();
                case 0 -> { salir = true; }
                default -> super.mostrarMensaje("\nOpción inválida");
            }
        }
    }

    private void stockDeProducto(){
        int idProd = pedirIdProducto();
        if (productoCtrl.existeProducto(idProd)){
            int stock = consultasCtrl.stockDeUnProducto(idProd);
            mostrarStockDeProducto(idProd, stock);
        }
    }

    private void stockDeUbicacion(){
        String ubicacion = pedirCodigoUbicacion(); //CHECKAR UBICACION EXISTE
        List<String> respuesta = consultasCtrl.stockDeUnaUbicacion(ubicacion);

        if (respuesta.isEmpty()){
            super.mostrarMensaje("No hay stock en esa ubicación.");
        } else {
            super.mostrarMensaje("Stock en la ubicación " + ubicacion + ":");
            for (String stock : respuesta) {
                super.mostrarMensaje(stock);
            }           
        }
    }

    private void stockAgrupadoPorProducto(){
        List<String> respuesta = consultasCtrl.agrupadoPorProducto();
        if (respuesta.isEmpty()){
            super.mostrarMensaje("No hay stock de ningun producto");
        } else {
            super.mostrarMensaje("\n=== Stock agrupado por producto ===");
            for (String stockProducto : respuesta) {
                super.mostrarMensaje(stockProducto);
            }
        }
    }

    private void stockAgrupadoPorUbicacion(){
        List<String> respuesta = consultasCtrl.agrupadoPorUbicacion();
        if (respuesta.isEmpty()){
            super.mostrarMensaje("No hay stock de ningun producto");
        } else {
            super.mostrarMensaje("\n=== Stock agrupado por ubicación ===");
            for (String stockProducto : respuesta) {
                super.mostrarMensaje(stockProducto);
            }
        }
    }

    private int pedirIdProducto() {
        System.out.print("Ingrese el ID del producto: ");
        return super.leerEntero(sc);
    }

    private String pedirCodigoUbicacion() {
        System.out.print("Código de ubicación (NAVE-RACK-FILA-COLUMNA): ");
        return sc.nextLine().trim();
    }

    private void mostrarStockDeProducto(int idProd, int stock) {
        if (stock > 0)
            super.mostrarMensaje("Stock total de P" + idProd + ": " + stock);
        else
            super.mostrarMensaje("No hay stock de P" + idProd);
    }
}
