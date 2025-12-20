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
import view.gui.Dashboard;
import view.gui.frames.PrincipalJFrame;


public class Main {

    public static void main(String[] args) {
        iniciarServicios();
        iniciarControladores();
        iniciarShutdownHook();
        iniciarVistas(args);
    }
    
    private static void iniciarServicios(){
        StockService.getInstance();
        RackService.getInstance();
        NaveService.getInstance();
        MovimientoService.getInstance();
        TransformacionService.getInstance();
    }
    
    private static void iniciarControladores(){
        ProductoController.getInstance();
        HistorialController.getInstance();
        MovimientoController.getInstance();
        NaveController.getInstance();
        ConsultasController.getInstance();
        TransformacionController.getInstance();
    }
    
    private static void iniciarVistas(String[] args) {
        if (args.length > 0 && args[0].equals("consola")) {
            iniciarModoConsola();
        } else {
            Dashboard.iniciarModoGUI();
        }
    }
    
    private static void iniciarShutdownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    StockService.getInstance().guardarEnArchivo();
                    MovimientoService.getInstance().guardarHistorial();
                    System.out.println("Datos guardados correctamente al salir.");
                }
        ));
    }

    private static void iniciarModoConsola() {
        Scanner scanner = new Scanner(System.in);

        ProductoView productoView = new ProductoView(scanner);
        OrdenesMovView ordenesMovView = new OrdenesMovView(MovimientoController.getInstance(), ProductoController.getInstance(), scanner);
        NaveView naveView = new NaveView(NaveController.getInstance(), scanner);
        ConsultasView consultasView = new ConsultasView(ConsultasController.getInstance(), ProductoController.getInstance(), scanner);
        TransformacionView transfView = new TransformacionView(TransformacionController.getInstance(), scanner);
        HistorialView historialView = new HistorialView(HistorialController.getInstance(), scanner);

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
