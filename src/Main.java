import model.OrdenMovimiento;
import model.Producto;
import model.Rack;
import model.Ubicacion;
import model.UnidadMedida;
import services.ManejadorStock;
import services.MovimientoService;

public class Main {
    public static void main(String[] args) {
        ManejadorStock service = new ManejadorStock();
        MovimientoService movimientoService = new MovimientoService(service);

        // Registro de productos
        service.registrarProducto(new Producto(1, "Caja", UnidadMedida.KILOS,10,2));
        service.registrarProducto(new Producto(2, "Yerba", UnidadMedida.KILOS,5,5));

        // Registro de ubicaciones
        Rack rack = new Rack(1);
        service.registrarUbicacion(new Ubicacion("R1-1-1",rack ));
        service.registrarUbicacion(new Ubicacion("R1-1-2", rack));

        // Agregar stock
        service.agregarStockAUbicacion(1, "R1-1-1", 5);  // 5 x 10 = 50 kg
        service.agregarStockAUbicacion(2, "R1-1-1", 6);  // 6 x 5 = 30 kg
        service.agregarStockAUbicacion(1, "R1-1-2", 3);  // 3 x 10 = 30 kg

        System.out.println("Stock total de P1: " + service.getStockTotalPorProducto(1));
        System.out.println("Stock total de P2(6): " + service.getStockTotalPorProducto(2));
        System.out.println("Stock en R1-1-1: " + service.getStockPorUbicacion("R1-1-1").toString());
        System.out.println("Stock en R1-1-2: " + service.getStockPorUbicacion("R1-1-2").toString());
        System.out.println("Stock total: " + service.getStockTotal());
        System.out.println("Peso total: " + service.getPesoTotal());
        System.out.println("Agrupado por producto: " + service.getStockAgrupadoPorProducto());
        System.out.println("Agrupado por ubicación: " + service.getStockAgrupadoPorUbicacion());

        //ordenes movimiento
        System.out.println(" ORDEN INGRESO 3 P3 a R1-1-1");
        service.registrarProducto(new Producto(3, "Perfume", UnidadMedida.LITROS,0.10,0.10));
        OrdenMovimiento ordenIngreso = new OrdenMovimiento(
            model.TipoMovimiento.INGRESO, 1,3,3, java.time.LocalDate.now(), "R1-1-1");
        if (movimientoService.procesarOrdenMovimiento(ordenIngreso)) {
            System.out.println("Ingreso procesado.");
        } else {
            System.out.println("Error en ingreso.");
        }

        System.out.println(" ORDEN MOV INTERNO 2 P1 de R1-1-1 a R1-1-2 -> R1-1-1 tiene 3 P1");
        OrdenMovimiento ordenInterno = new OrdenMovimiento(
            model.TipoMovimiento.INTERNO, 2,2,1, java.time.LocalDate.now(), "R1-1-1", "R1-1-2");
        if (movimientoService.procesarOrdenMovimiento(ordenInterno)) {
            System.out.println("movimiento interno procesado.");
        } else {
            System.out.println("Error en ingreso.");
        }

        System.out.println(" ORDEN EGRESO 2 P2 fuera de R1-1-1 ->  R1-1-1 tiene 4 P2");
        OrdenMovimiento ordenEgreso = new OrdenMovimiento(
            model.TipoMovimiento.EGRESO, 3,2,2, java.time.LocalDate.now(), "R1-1-1");
        if (movimientoService.procesarOrdenMovimiento(ordenEgreso)) {
            System.out.println("egreso procesado.");
        } else {
            System.out.println("Error en egreso.");
        }
        
        // Consultas
        System.out.println("Stock total de P1: " + service.getStockTotalPorProducto(1));
        System.out.println("Stock total de P2(4): " + service.getStockTotalPorProducto(2));
        System.out.println("Stock total de P3: " + service.getStockTotalPorProducto(3));
        System.out.println("Stock en R1-1-1: " + service.getStockPorUbicacion("R1-1-1").toString());
        System.out.println("Stock en R1-1-2: " + service.getStockPorUbicacion("R1-1-2").toString());
        System.out.println("Stock total: " + service.getStockTotal());
        System.out.println("Peso total (100): " + service.getPesoTotal());
        System.out.println("Agrupado por producto: " + service.getStockAgrupadoPorProducto());
        System.out.println("Agrupado por ubicación: " + service.getStockAgrupadoPorUbicacion());


        // hook de apagado que dice la ia sirve para no olvidarse de guardar los datos
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            service.guardarEnArchivo();
            movimientoService.guardarHistorial();
            System.out.println("Datos guardados correctamente al salir.");
        }));
    }
}
