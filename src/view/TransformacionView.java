package view;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import controller.TransformacionController;
import model.OrdenTransformacion;
import model.Producto;

public class TransformacionView extends View {
    private final TransformacionController transfCtrl;
    private final Scanner sc;

    public TransformacionView(TransformacionController transfCtrl, Scanner sc) {
        this.transfCtrl = transfCtrl;
        this.sc = sc;
    }

    public void mostrarMenuTransformacion() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n====== MENÚ TRANSFORMACIONES ======");
            System.out.println("1. Registrar nueva transformación");
            System.out.println("2. Ver historial de transformaciones");
            System.out.println("0. Volver");
            System.out.println("==================================");

            int opcion = leerEntero(sc);

            switch (opcion) {
                case 1 -> registrarTransformacion();
                case 2 -> verHistorialTransformaciones();
                case 0 -> volver = true;
                default -> mostrarMensaje("\nOpción inválida. Intente nuevamente.");
            }
        }
    }

    private void registrarTransformacion() {
        System.out.println("\n=== REGISTRAR NUEVA TRANSFORMACIÓN ===");
        
        System.out.print("Ingrese ID del producto de entrada: ");
        int idProdEntrada = leerEntero(sc);

        System.out.print("Ingrese ubicación del producto de entrada: ");
        String ubicEntrada = leerStringSinComas(sc);

        System.out.print("Ingrese cantidad de unidades de entrada: ");
        int cantEntrada = leerEntero(sc);

        System.out.print("Ingrese nombre del producto transformado: ");
        String descripcionTransformado = leerStringSinComas(sc);

        System.out.print("Ingrese peso unitario del producto transformado: ");
        double pesoTransformado = leerDouble(sc);

        System.out.print("Ingrese capacidad del nuevo contenedor: ");
        int capacidadTransformado = leerEntero(sc);

        System.out.print("Ingrese ubicación de salida: ");
        String ubicSalida = leerStringSinComas(sc);

        System.out.print("Ingrese stock minimo de producto transformado: ");
        int stockMinimo = leerEntero(sc);

        LocalDate fecha = LocalDate.now();

        // Creamos un producto temporal (el Service le asignará el id real)
        Producto productoTransformadoTemporal = new Producto(0, descripcionTransformado, pesoTransformado, capacidadTransformado,
            stockMinimo,"Producto reenvasado");

        String resultado = transfCtrl.procesarTransformacion(
            idProdEntrada,
            ubicEntrada,
            cantEntrada,
            productoTransformadoTemporal,
            ubicSalida,
            fecha
        );

        mostrarMensaje("\nResultado: " + resultado);
    }

    private void verHistorialTransformaciones() {
        System.out.println("\n=== HISTORIAL DE TRANSFORMACIONES ===");
        List<OrdenTransformacion> historial = transfCtrl.obtenerHistorial();

        if (historial.isEmpty()) {
            mostrarMensaje(" No hay transformaciones registradas.");
            return;
        }

        mostrarMensaje("ID | FECHA | PROD ENTRADA → PROD SALIDA | CANTIDADES | UBICACIONES");
        mostrarMensaje("-------------------------------------------------------------");

        for (OrdenTransformacion o : historial) {
            String usuario = o.getUsuarioResponsable() != null ? " | Usuario: " + o.getUsuarioResponsable() : "";
            mostrarMensaje(
                "Orden " + o.getIdOrdenTransf() +
                " | " + o.getFecha() +
                " | P" + o.getIdProductoEntrada() + " → P" + o.getIdProductoTransformado() +
                " | -" + o.getCantidadEntrada() + " → +" + o.getCantidadSalida() +
                " | " + o.getUbicacionProdEntrada() + " → " + o.getUbicacionSalida() + usuario
            );
        }
    }
}
