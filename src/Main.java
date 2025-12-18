import java.util.Scanner;

import services.StockService;
import services.TransformacionService;
import view.ConsultasView;
import view.HistorialView;
import view.NaveView;
import view.OrdenesMovView;
import view.ProductoView;
import view.TransformacionView;
import view.View;
import services.MovimientoService;
import services.NaveService;
import services.RackService;
import controller.ConsultasController;
import controller.HistorialController;
import controller.MovimientoController;
import controller.NaveController;
import controller.ProductoController;
import controller.TransformacionController;


public class Main {
    public static void main(String[] args) {

Scanner scanner = new Scanner(System.in);

        //SOLO SE INSTANCIAN UNA VEZ
        StockService stockService = new StockService();
        RackService rackService = new RackService(stockService);
        NaveService naveService = new NaveService(rackService);
        MovimientoService movService = new MovimientoService(stockService);
        TransformacionService transfService = new TransformacionService(stockService);

        //controllers
        HistorialController historialCtrl = new HistorialController(movService,transfService);
        ProductoController productoCtrl = new ProductoController(stockService);
        MovimientoController movimientoCtrl = new MovimientoController(movService);
        NaveController naveCtrl = new NaveController(naveService, stockService);
        ConsultasController consultasCtrl = new ConsultasController(stockService);
        TransformacionController transfController = new TransformacionController(transfService,stockService);

        //views
        HistorialView historialView = new HistorialView(historialCtrl, scanner);
        ProductoView productoView = new ProductoView(productoCtrl, scanner);
        OrdenesMovView ordenesMovView = new OrdenesMovView(movimientoCtrl, productoCtrl, scanner);
        NaveView naveView = new NaveView(naveCtrl, scanner);
        ConsultasView consultasView = new ConsultasView(consultasCtrl, productoCtrl, scanner);
        TransformacionView transfView = new TransformacionView(transfController, scanner);
        
        boolean salir = false;
        System.out.println(stockService.getUbicacionesMap().isEmpty());

        while (!salir) {
            System.out.println("\n==== Menú Principal =======================");
            System.out.println("1. NAVES");
            System.out.println("2. PRODUCTOS");
            System.out.println("3. ORDENES DE MOVIMIENTO");
            System.out.println("4. ORDENES DE TRANSFORMACION"); 
            System.out.println("5. HISTORIAL DE MOVIMIENTOS");
            System.out.println("6. CONSULTAS");
            System.out.println("0. Salir");
            System.out.println("==============================================");

            int opcion = View.leerEntero(scanner);

            switch (opcion) {
                case 1:
                    naveView.mostrarMenuNaves();
                    break;
                case 2:
                    productoView.mostrarMenuProductos();
                    break;
                case 3:
                    ordenesMovView.mostrarMenuMovimiento();
                    break;
                case 4:
                    transfView.mostrarMenuTransformacion();
                    break;
                case 5:
                    historialView.mostrarMenuHistorial();
                    break;
                case 6:
                    consultasView.mostrarMenuConsultas();
                    break;
                case 0:
                    salir = true;
                    break;
                default:
                    System.out.println("\nOpción inválida");
            }
        }
        scanner.close();
    
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stockService.guardarEnArchivo();
            movService.guardarHistorial();
            System.out.println("Datos guardados correctamente al salir.");
        }));
    }
}
