import java.time.LocalDate;

import model.OrdenMovimiento;
import model.Producto;
import model.StockUbicacion;
import model.TipoMovimiento;
import model.Ubicacion;
import model.UnidadMedida;
import services.StockService;
import services.MovimientoService;
import services.RackService;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StockService stockService = new StockService();
        RackService rackService = new RackService(stockService);
        MovimientoService movService = new MovimientoService(stockService);
        //SOLO SE INSTANCIAN UNA VEZ

        boolean salir = false;

        while (!salir) {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Registrar nuevo producto");
            System.out.println("2. Crear nuevo rack");
            System.out.println("3. Ver ubicaciones disponibles");
            System.out.println("4. Ingreso de stock de un producto");
            System.out.println("5. Egreso de stock");
            System.out.println("6. Mover stock entre ubicaciones");
            System.out.println("7. Ver stock total por producto");
            System.out.println("8. Ver movimientos de un producto");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = leerEntero(sc);

            switch (opcion) {
                case 1 -> registrarProducto(sc, stockService,movService);
                case 2 -> crearRack(rackService);
                case 3 -> mostrarUbicaciones(stockService);
                case 4 -> ingresoStock(sc, stockService, movService);
                case 5 -> egresoStock(sc, stockService, movService);
                case 6 -> moverStock(sc, stockService, movService);
                case 7 -> verStockPorProducto(sc, stockService);
                case 8 -> verHistorial(sc, movService);
                case 9 -> {
                    salir = true;
                    movService.guardarHistorial();
                    stockService.guardarEnArchivo();
                    System.out.println("Datos guardados. ¡Hasta luego!");
                }
                default -> System.out.println("Opción inválida.");
            }
        }
        sc.close();
    }

    private static int leerEntero(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Ingrese un número válido: ");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine(); // limpiar buffer
        return val;
    }

    private static void registrarProducto(Scanner sc, StockService stockService, MovimientoService movService) {

        int id = stockService.getProximoProdId();
        System.out.print("PRODUCTO NUEVO (ID " + id + ")");
        System.out.print("Descripción: ");
        String desc = sc.nextLine();
        System.out.print("Peso unitario: ");
        double peso = sc.nextDouble();
        System.out.print("Capacidad del contenedor: ");
        double capacidad = sc.nextDouble();
        sc.nextLine();
        //unidad medida
        System.out.print("Unidad de medida. Elija la opción: ");
        System.out.println("1. kg | 2. lt | 3. unidad | 4. gr ");
        int medida = leerEntero(sc); sc.nextLine();
        UnidadMedida uMed = UnidadMedida.UNIDAD;
        switch (medida) {
            case 1 ->  uMed = UnidadMedida.KILOS;       
            case 2 ->  uMed = UnidadMedida.LITROS;       
            case 3 ->  uMed = UnidadMedida.UNIDAD;       
            case 4 ->  uMed = UnidadMedida.GRAMOS;       
            default -> System.out.println("Opción inválida.");
            }

        Producto p = new Producto(id, desc, uMed, peso, capacidad);
        stockService.registrarProducto(p);
        System.out.println("Producto registrado correctamente. Asígnelo a una ubicacion");

        mostrarUbicaciones(stockService);
        System.out.print("Ubicación destino: ");
        String ubi = sc.nextLine();
        System.out.print("Cantidad a ingresar: ");
        int cantidad = leerEntero(sc);

        OrdenMovimiento ingreso = new OrdenMovimiento(
            TipoMovimiento.INGRESO,
            movService.getProximoOrdenId(),
            cantidad,
            p.getIdProducto(),
            LocalDate.now(),
            ubi
        );

        boolean procesada = movService.procesarOrdenMovimiento(ingreso);
        if (procesada) { System.out.println("Ingreso realizado correctamente.");}
    }

    private static void crearRack(RackService rackService) {
        int id = rackService.getProximoRackId();
        rackService.crearRack(id);

        System.out.print("Se creó un nuevo Rack con éxito (CODIGO " + id + ")");
    }

    private static void mostrarUbicaciones(StockService stockService) {
        System.out.println("\n=== UBICACIONES DISPONIBLES ===");
        stockService.getUbicacionesDisponibles().values().stream()  //este berenjenal lista en orden ascendente
            .sorted(Comparator.comparingInt((Ubicacion u) -> u.getIdRack())
                      .thenComparingInt(Ubicacion::getFila)
                      .thenComparingInt(Ubicacion::getColumna))
            .forEach(u -> System.out.println(" - " + u.getCodigoUbicacion()));
    }

    private static void ingresoStock(Scanner sc, StockService stockService, MovimientoService movService) {
        System.out.print("ID de producto: ");
        int idProd = leerEntero(sc);
        if (!stockService.getProductosMap().containsKey(idProd)) {
            System.out.println("ERROR: Producto no registrado.");
            return;
        }

        mostrarUbicaciones(stockService);
        System.out.print("Ubicación destino: ");
        String ubi = sc.nextLine();
        System.out.print("Cantidad a ingresar: ");
        int cantidad = leerEntero(sc);

        OrdenMovimiento ingreso = new OrdenMovimiento(
            TipoMovimiento.INGRESO,
            movService.getProximoOrdenId(),
            cantidad,
            idProd,
            LocalDate.now(),
            ubi
        );

        boolean procesada = movService.procesarOrdenMovimiento(ingreso);
        if (procesada) { System.out.println("Ingreso realizado correctamente.");}
    }

    private static void egresoStock(Scanner sc, StockService stockService, MovimientoService movService) {
        System.out.print("ID de producto: ");
        int idProd = leerEntero(sc);
        mostrarUbicaciones(stockService);
        System.out.print("Ubicación origen: ");
        String ubi = sc.nextLine();
        System.out.print("Cantidad a retirar: ");
        int cantidad = leerEntero(sc);

        OrdenMovimiento egreso = new OrdenMovimiento(
            TipoMovimiento.EGRESO,
            movService.getProximoOrdenId(),
            cantidad,
            idProd,
            LocalDate.now(),
            ubi
        );
        if (movService.procesarOrdenMovimiento(egreso)) {
            System.out.println("Egreso realizado correctamente.");
        }
    }

    private static void moverStock(Scanner sc, StockService stockService, MovimientoService movService) {
        System.out.print("ID de producto: ");
        int idProd = leerEntero(sc);
        mostrarUbicaciones(stockService);
        System.out.print("Ubicación origen: ");
        String ubiOrigen = sc.nextLine();
        System.out.print("Ubicación destino: ");
        String ubiDestino = sc.nextLine();
        System.out.print("Cantidad a mover: ");
        int cantidad = leerEntero(sc);

        OrdenMovimiento interno = new OrdenMovimiento(
            TipoMovimiento.INTERNO,
            movService.getProximoOrdenId(),
            cantidad,
            idProd,
            LocalDate.now(),
            ubiOrigen,
            ubiDestino
        );
        if (movService.procesarOrdenMovimiento(interno)) {
            System.out.println("Movimiento interno realizado correctamente.");
        }
    }

    private static void verStockPorProducto(Scanner sc, StockService stockService) {
        System.out.print("ID de producto: ");
        int idProd = leerEntero(sc);

        int total = stockService.getStockTotalPorProducto(idProd);
        System.out.println("Stock total del producto " + idProd + ": " + total);
        List<StockUbicacion> lista = stockService.getStockPorProducto(idProd);
        if (lista != null) {
            for (StockUbicacion s : lista) {
                System.out.println(" - " + s.getCodigoUbicacion() + ": " + s.getCantidad());
            }
        }
    }

    private static void verHistorial(Scanner sc, MovimientoService movService) {
        System.out.print("ID de producto: ");
        int idProd = leerEntero(sc);
        List<OrdenMovimiento> historial = movService.historialMovimientoProducto(idProd);
        if (historial.isEmpty()) {
            System.out.println("No hay movimientos registrados para ese producto.");
        } else {
            for (OrdenMovimiento o : historial) {
                System.out.println(o.getTipoMovimientoOrden() + " - " + o.getFecha() + " - Cantidad: " + o.getCantidad());
            }
        }
    }
}
