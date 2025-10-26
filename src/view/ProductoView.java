package view;

import java.util.List;
import java.util.Scanner;
import controller.ProductoController;
import model.Producto;

public class ProductoView extends View{
    private final ProductoController productoCtrl;
    Scanner sc;

    public ProductoView(ProductoController productoCtrl, Scanner sc){
        this.productoCtrl = productoCtrl;
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

    public void agregarProductoMenu(){
        String respuesta;

        String descripcion = pedirDescripcion(sc);
        double peso = pedirPeso(sc);
        double capacidad = pedirCapacidad(sc);
        String unidadMedida = pedirUnidadMedida(sc);

        respuesta = productoCtrl.agregarProducto(descripcion, unidadMedida, peso, capacidad);

        if(respuesta.equals("")){
            super.mostrarMensaje("Error al crear producto.");
        } else {
            super.mostrarMensaje(respuesta);
            super.mostrarMensaje("Producto registrado correctamente.");
        }
    }

    public void agregarMuchosProductos(){
        String tecla;
        do {
            agregarProductoMenu();
            super.mostrarMensaje("¿Ingresar otro producto? (x para salir)");
            tecla = sc.nextLine().trim().toLowerCase();
        } while (!tecla.equals("x"));
    }

    public void mostrarProductos() {
        List<String> respuesta = productoCtrl.listarProductos();

        if(respuesta.isEmpty()){
            super.mostrarMensaje("No se encontraron productos.");
        } else {
            for (String producto : respuesta) {
                super.mostrarMensaje(producto);
            }
        }
    }

    public void mostrarProductoID(){
        int id = pedirId(sc);
        String respuesta = productoCtrl.buscarProductoPorId(id);

        if(respuesta.equals("")){
            super.mostrarMensaje("No se encontró ningún producto con esa ID.");
        } else {
            super.mostrarMensaje(respuesta);
        }
    }

     public void eliminarProducto() {
        int id = pedirId(sc);
        String producto = productoCtrl.buscarProductoPorId(id);

        if(producto.equals("")){
            super.mostrarMensaje("No se encontró ningún producto con esa ID.");
        } else {
            super.mostrarMensaje(producto);
            String confirm = leerAprobacion(sc);

            if (confirm.equals("s")) {
                productoCtrl.eliminarProducto(id);
            } else {
                super.mostrarMensaje("Operación cancelada.");
            }
        }
    }

    public String pedirDescripcion(Scanner sc){
        System.out.print("Descripción: ");
        String desc = sc.nextLine();
        return desc;
    }

    public double pedirPeso(Scanner sc){
        System.out.print("Peso unitario: ");
        double peso = sc.nextDouble();
        return peso;
    }

    public double pedirCapacidad(Scanner sc){
       System.out.print("Capacidad del contenedor: ");
        double capacidad = sc.nextDouble();
        return capacidad;
    }

    public String pedirUnidadMedida(Scanner sc){
        System.out.println("Unidad de medida (LITROS | KILOS | UNIDAD | GRAMOS | MILILITROS): ");
        sc.nextLine();
        String uMed = sc.nextLine();
        return uMed;
    }

    public int pedirId(Scanner sc){
        System.out.println("ID del producto: ");
        int id = super.leerEntero(sc);
        return id;
    }

    public String leerAprobacion(Scanner sc){
        System.out.println("¿Está seguro que desea eliminarlo? (s/n): ");
        return sc.nextLine().trim().toLowerCase();
    }

    public void mostrarProducto (Producto p){
        System.out.println(p.toString());
    }
}
