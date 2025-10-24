package view;


import java.util.Map;
import java.util.Scanner;
import model.Producto;
import model.UnidadMedida;


public class ProductoView extends View{

    public void opcionesMenuProductos(){
        System.out.println("\n==== Menú Productos ================");
        System.out.println("1. Agregar productos");
        System.out.println("2. Listar productos");
        System.out.println("3. Buscar producto por descripcion");
        System.out.println("4. Eliminar producto");
        System.out.println("0. Volver");
        System.out.println("=======================================");
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
        System.out.print("Unidad de medida (LITROS | KILO | UNIDAD | GRAMOS | MILILITROS): ");
        String uMed = sc.nextLine();
        return uMed;
    }

    public int pedirId(Scanner sc){
        System.out.println("ID del producto: ");
        int id = super.leerEntero(sc);
        return id;
    }

    public void mostrarProductos(Map<Integer, Producto> productos) {
        for (Producto p : productos.values()) {
            System.out.println(p.toString());
        }
    }

    public String leerAprobacion(Scanner sc){
        System.out.println("¿Está seguro que desea eliminarlo? (s/n): ");
        return sc.nextLine().trim().toLowerCase();
    }

    public void mostrarProducto (Producto p){
        System.out.println(p.toString());
    }
}
