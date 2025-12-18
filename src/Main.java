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
import view.Dashboard;


public class Main {

    // Servicios compartidos entre consola y GUI
    private static StockService stockService;
    private static RackService rackService;
    private static NaveService naveService;
    private static MovimientoService movService;
    private static TransformacionService transfService;

    public static void main(String[] args) {

        // Instancias únicas
        stockService = new StockService();
        rackService = new RackService(stockService);
        naveService = new NaveService(rackService);
        movService = new MovimientoService(stockService);
        transfService = new TransformacionService(stockService);

        // 2) Registrar shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stockService.guardarEnArchivo();
            movService.guardarHistorial();
            System.out.println("Datos guardados correctamente al salir.");
        }));

        // 3) Elegir modo
        if (args.length > 0 && args[0].equals("consola")) {
            iniciarModoConsola();
        } else {
            iniciarModoGUI();
        }
    }

    private static void iniciarModoGUI() {
        new Dashboard(
            stockService,
            rackService,
            naveService,
            movService,
            transfService);
    }

    private static void iniciarModoConsola() {
        Scanner scanner = new Scanner(System.in);

        ProductoController productoCtrl = new ProductoController(stockService);
        MovimientoController movimientoCtrl = new MovimientoController(movService);
        NaveController naveCtrl = new NaveController(naveService, stockService);
        ConsultasController consultasCtrl = new ConsultasController(stockService);
        TransformacionController transfController = new TransformacionController(transfService, stockService);
        HistorialController historialCtrl = new HistorialController(movService, transfService);

        ProductoView productoView = new ProductoView(productoCtrl, scanner);
        OrdenesMovView ordenesMovView = new OrdenesMovView(movimientoCtrl, productoCtrl, scanner);
        NaveView naveView = new NaveView(naveCtrl, scanner);
        ConsultasView consultasView = new ConsultasView(consultasCtrl, productoCtrl, scanner);
        TransformacionView transfView = new TransformacionView(transfController, scanner);
        HistorialView historialView = new HistorialView(historialCtrl, scanner);

        boolean salir = false;

        while (!salir) {
            // menú...
            int opcion = View.leerEntero(scanner);

            switch (opcion) {
                case 1 -> naveView.mostrarMenuNaves();
                case 2 -> productoView.mostrarMenuProductos();
                case 3 -> ordenesMovView.mostrarMenuMovimiento();
                case 4 -> transfView.mostrarMenuTransformacion();
                case 5 -> historialView.mostrarMenuHistorial();
                case 6 -> consultasView.mostrarMenuConsultas();
                case 0 -> salir = true;
            }
        }

        scanner.close();
    }
}
