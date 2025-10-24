package controller;

import java.util.Map;
import java.util.Scanner;

import model.Producto;
import model.UnidadMedida;
import services.StockService;
import view.ProductoView;

public class ProductoController {
    private final StockService stockService;
    private ProductoView productoView;
    Scanner sc;

    public ProductoController(StockService stockService, ProductoView productoView, Scanner sc) {
        this.stockService = stockService;
        this.productoView = productoView; 
        this.sc = sc;
    }

    public void mostrarMenuProductos() {
    boolean salir = false;

        while (!salir) {
            
            productoView.opcionesMenuProductos();

            int opcion = ProductoView.leerEntero(sc);

            switch (opcion) {
                case 1 -> agregarProducto(sc);
                case 2 -> listarProductos();
                case 3 -> buscarProductoPorDescripcion(sc);
                case 4 -> eliminarProducto(sc);
                case 0 -> { 
                    salir = true;
                    return;
                 }
                default -> productoView.mostrarMensaje("\nOpción inválida");
            }
        }
    }

    private void agregarProducto(Scanner sc) {

        String desc = productoView.pedirDescripcion(sc);
        double peso = productoView.pedirPeso(sc);
        double capacidad = productoView.pedirCapacidad(sc);
        sc.nextLine();
        String uMed = productoView.pedirUnidadMedida(sc);
        UnidadMedida unidadMed;
        try { unidadMed = UnidadMedida.valueOf(uMed.trim().toUpperCase());
        } catch (Exception e) {
            productoView.mostrarMensaje("Unidad inválida.");
            return;}

        Producto p = new Producto(desc, unidadMed, peso, capacidad);

        stockService.registrarProducto(p);
        productoView.mostrarMensaje("Producto registrado correctamente.");
        productoView.mostrarProducto(p);
    }

    public void agregarMuchosProductos(Scanner sc){
        String tecla;
        do {
            agregarProducto(sc);
            productoView.mostrarMensaje("¿Ingresar otro producto? (x para salir)");
            tecla = sc.nextLine().trim().toLowerCase();
        } while (!tecla.equals("x"));
    }

    public void listarProductos() {
        Map<Integer, Producto> productos = stockService.getProductosMap();
        productoView.mostrarProductos(productos);
    }

    public void buscarProductoPorDescripcion(Scanner sc) {
        String desc = productoView.pedirDescripcion(sc);
        Producto p = stockService.buscarProductoPorDescripcion(desc);

        if (p != null) {
            productoView.mostrarProducto(p);
        } else {
            productoView.mostrarMensaje("No se encontró ningún producto con esa descripción.");
        }
    }

    public void eliminarProducto(Scanner sc) {
        int id = productoView.pedirId(sc);

        Producto p = stockService.getProductoPorId(id);
            if (p == null) {
                productoView.mostrarMensaje("No existe un producto con ese ID.");
                return;
            }

        productoView.mostrarProducto(p);

        String confirm = productoView.leerAprobacion(sc);

        if (confirm.equals("s")) {
            stockService.eliminarProductoPorId(id);
        } else {
            productoView.mostrarMensaje("Operación cancelada.");
        }
    }
}
