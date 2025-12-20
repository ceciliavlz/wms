package view;

import java.util.List;
import java.util.Scanner;
import controller.ProductoController;

public class ProductoView extends View{
    private final ProductoController productoCtrl = ProductoController.getInstance();
    Scanner sc;

    public ProductoView(Scanner sc){
        this.sc = sc;
    }

    public void mostrarMenuProductos() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n==== Menú Productos ================");
            System.out.println("1. Agregar producto");
            System.out.println("2. Agregar varios productos");
            System.out.println("3. Listar productos");
            System.out.println("4. Buscar producto por ID");
            System.out.println("5. Eliminar producto");
            System.out.println("0. Volver");
            System.out.println("======================================");

            int opcion = super.leerEntero(sc);

            switch (opcion) {
                case 1 -> agregarProductoMenu();
                case 2 -> agregarMuchosProductos();
                case 3 -> mostrarProductos();
                case 4 -> mostrarProductoID();
                case 5 -> eliminarProducto();
                case 0 -> { 
                    salir = true;
                    return;
                 }
                default -> super.mostrarMensaje("\nOpción inválida");
            }
        }
    }

    private void agregarProductoMenu(){
        String respuesta;

        String descripcion = pedirDescripcion();
        double peso = pedirPeso();
        double capacidad = pedirCapacidad();
        String unidadMedida = pedirUnidadMedida();
        int stockMin = pedirStockMin();
        String grupo = pedirGrupo();

        respuesta = productoCtrl.agregarProducto(descripcion, unidadMedida, peso, capacidad, stockMin, grupo);

        if(respuesta.equals("")){
            super.mostrarMensaje("Error al crear producto.");
        } else {
            super.mostrarMensaje(respuesta);
            super.mostrarMensaje("Producto registrado correctamente.");
        }
    }

    private void agregarMuchosProductos(){
        String tecla;
        do {
            agregarProductoMenu();
            super.mostrarMensaje("¿Ingresar otro producto? (x para salir)");
            tecla = sc.nextLine().trim().toLowerCase();
        } while (!tecla.equals("x"));
    }

    private void mostrarProductos() {
        List<String> respuesta = productoCtrl.listarProductos();

        if(respuesta.isEmpty()){
            super.mostrarMensaje("No se encontraron productos.");
        } else {
            for (String producto : respuesta) {
                super.mostrarMensaje(producto);
            }
        }
    }

    private void mostrarProductoID(){
        int id = pedirId();
        String respuesta = productoCtrl.buscarProductoPorId(id);

        if(respuesta.equals("")){
            super.mostrarMensaje("No se encontró ningún producto con esa ID.");
        } else {
            super.mostrarMensaje(respuesta);
        }
    }

     private void eliminarProducto() {
        int id = pedirId();
        String producto = productoCtrl.buscarProductoPorId(id);

        if(producto.equals("")){
            super.mostrarMensaje("No se encontró ningún producto con esa ID.");
        } else {
            super.mostrarMensaje(producto);
            String confirm = leerAprobacion();

            if (confirm.equals("s")) {
                productoCtrl.eliminarProducto(id);
            } else {
                super.mostrarMensaje("Operación cancelada.");
            }
        }
    }

    private String pedirDescripcion(){
        super.mostrarMensaje("Descripción: ");
        String desc = super.leerStringSinComas(sc);
        return desc;
    }

    private double pedirPeso(){
        super.mostrarMensaje("Peso unitario (en Kg): ");
        double peso = super.leerDouble(sc);
        return peso;
    }

    private double pedirCapacidad(){
       super.mostrarMensaje("Capacidad del contenedor: ");
        double capacidad = super.leerDouble(sc);
        return capacidad;
    }

    private String pedirUnidadMedida(){
        super.mostrarMensaje("Unidad de medida (LITROS | KILOS | UNIDAD | GRAMOS | MILILITROS): ");
        String uMed = sc.nextLine();
        return uMed;
    }

    private int pedirId(){
        System.out.println("ID del producto: ");
        int id = super.leerEntero(sc);
        return id;
    }

    private int pedirStockMin(){
        System.out.println("Stock minimo del producto: ");
        int stockMin= super.leerEntero(sc);
        return stockMin;
    }

    private String pedirGrupo(){
        System.out.println("GRUPO: Ingrese opción 1. Materia prima 2. Producto final");
        int opcion = super.leerEntero(sc);
        String grupo = "";
        switch (opcion) {
            case 1: grupo = "Materia prima"; break;
            case 2: grupo = "Producto final"; break;
            default: break;
        }
        return grupo;
    }

    private String leerAprobacion(){
        System.out.println("¿Está seguro que desea eliminarlo? (s/n): ");
        return sc.nextLine().trim().toLowerCase();
    }
}
