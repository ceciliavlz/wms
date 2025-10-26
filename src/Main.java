import java.util.Scanner;

import services.StockService;
import view.ConsultasView;
import view.HistorialView;
import view.NaveView;
import view.OrdenesMovView;
import view.ProductoView;
import view.View;
import services.MovimientoService;
import services.NaveService;
import services.RackService;
import controller.ConsultasController;
import controller.HistorialController;
import controller.MovimientoController;
import controller.NaveController;
import controller.ProductoController;


public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        //SOLO SE INSTANCIAN UNA VEZ
        StockService stockService = new StockService();
        RackService rackService = new RackService(stockService);
        NaveService naveService = new NaveService(rackService);
        MovimientoService movService = new MovimientoService(stockService);

        //controllers
        HistorialController historialCtrl = new HistorialController(movService);
        ProductoController productoCtrl = new ProductoController(stockService);
        MovimientoController movimientoCtrl = new MovimientoController(movService, stockService);
        NaveController naveCtrl = new NaveController(naveService, stockService);
        ConsultasController consultasCtrl = new ConsultasController(stockService);

        //views
        HistorialView historialView = new HistorialView(historialCtrl, scanner);
        ProductoView productoView = new ProductoView(productoCtrl, scanner);
        OrdenesMovView ordenesMovView = new OrdenesMovView(movimientoCtrl, productoCtrl, scanner);
        NaveView naveView = new NaveView(naveCtrl, scanner);
        ConsultasView consultasView = new ConsultasView(consultasCtrl, productoCtrl, scanner);
        
        boolean salir = false;
        System.out.println(stockService.getUbicacionesMap().isEmpty());

        while (!salir) {
            System.out.println("\n==== Menú Principal =======================");
            System.out.println("1. NAVES");
            System.out.println("2. PRODUCTOS");
            System.out.println("3. ORDENES DE MOVIMIENTO");
            System.out.println("4. ORDENES DE TRANSFORMACION"); //TODO
            System.out.println("5. HISTORIAL DE MOVIMIENTOS");
            System.out.println("6. CONSULTAS");
            System.out.println("0. Salir");
            System.out.println("==============================================");

            int opcion = View.leerEntero(scanner);

            switch (opcion) {
                case 1 -> naveView.mostrarMenuNaves();
                case 2 -> productoView.mostrarMenuProductos();
                case 3 -> ordenesMovView.mostrarMenuMovimiento();
                case 4 -> System.out.println(" -- Pendiente --");
                case 5 -> historialView.mostrarMenuHistorial();
                case 6 -> consultasView.mostrarMenuConsultas();
                case 0 -> { 
                    salir = true;
                    return;
                 }
                default -> System.out.println("\nOpción inválida");
            }
        }
        scanner.close();
    
        movService.guardarHistorial();
        stockService.guardarEnArchivo();
        System.out.println("Datos guardados. ¡Hasta luego!");
    }
}

   /*    boolean salir = false;

        while (!salir) {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Registrar nuevo producto");
            System.out.println("2. Crear nueva nave");
            System.out.println("3. Crear nuevo rack");
            System.out.println("4. Ver ubicaciones disponibles");
            System.out.println("5. Ingreso de stock de un producto");
            System.out.println("6. Egreso de stock");
            System.out.println("7. Mover stock entre ubicaciones");
            System.out.println("8. Ver stock total por producto");
            System.out.println("9. Ver movimientos de un producto");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = leerEntero(sc);

            switch (opcion) {
                case 1 -> registrarProducto(sc, stockService,movService);
                case 2 -> crearNave(naveService);
                case 3 -> crearRackEnNave(sc, naveService);
                case 4 -> mostrarUbicaciones(stockService);
                case 5 -> ingresoStock(sc, stockService, movService);
                case 6 -> egresoStock(sc, stockService, movService);
                case 7 -> moverStock(sc, stockService, movService);
                case 8 -> verStockPorProducto(sc, stockService);
                case 9 -> verHistorial(sc, movService);
                case 0 -> {
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
        System.out.print("Descripción: ");
        String desc = sc.nextLine();
        System.out.print("Peso unitario: ");
        double peso = sc.nextDouble();
        System.out.print("Capacidad del contenedor: ");
        double capacidad = sc.nextDouble();
        sc.nextLine();
        //unidad medida
        System.out.print("Unidad de medida. Elija la opción: ");
        System.out.println("LITRO | KILO | UNIDAD");
        UnidadMedida uMed;
        try { uMed = UnidadMedida.valueOf(sc.nextLine().trim().toUpperCase());  } catch (Exception e) { System.out.println("Unidad inválida."); return; }
        Producto p = new Producto(desc, uMed, peso, capacidad); //id asignado en registrarProducto
        stockService.registrarProducto(p);
        System.out.println("Producto registrado correctamente (ID " + p.getIdProducto() + "). Asígnelo a una ubicacion");

        mostrarUbicaciones(stockService);
        System.out.print("Ubicación destino: ");
        String ubi = sc.nextLine();
        System.out.print("Cantidad a ingresar: ");
        int cantidad = leerEntero(sc);

        OrdenMovimiento ingreso = new OrdenMovimiento(
            TipoMovimiento.INGRESO,
            0, //temporario, se asigna en procesarOrdenMovimiento
            cantidad,
            p.getIdProducto(),
            LocalDate.now(),
            ubi
        );

        boolean procesada = movService.procesarOrdenMovimiento(ingreso);
        if (procesada) { System.out.println("Ingreso realizado correctamente.");}
    }

    private static void crearNave(NaveService naveService) {
        Nave nave = naveService.crearNave();
        System.out.println("Nave creada con ID: " + nave.getIdNave());
    }

    private static void crearRackEnNave(Scanner sc, NaveService naveService) {
        System.out.print("Ingrese el ID de la nave donde quiere crear el rack: ");
        int idNave = leerEntero(sc);
        Rack nuevo = naveService.crearRackEnNave(idNave);
        if (nuevo != null) {
            System.out.println("Rack creado con ID " + nuevo.getIdRack() + " en la nave " + idNave);
        } else {
            System.out.println("No se encontró la nave con ID " + idNave);
        }
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
            0, //temporario, se asigna en procesarOrdenMovimiento
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
            0, //temporario, se asigna en procesarOrdenMovimiento
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
            0,  //temporario, se asigna en procesarOrdenMovimiento
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
*/ 