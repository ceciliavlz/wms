package controller;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import model.StockUbicacion;
import services.StockService;
import view.ConsultasView;


public class ConsultasController {
    private final StockService stockService;
    private final ConsultasView consultasView;
    private Scanner sc;

    public ConsultasController(StockService stockService, ConsultasView consultasView, Scanner sc) {
        this.stockService = stockService;
        this.consultasView = consultasView;
        this.sc = sc;
    }

    public void mostrarMenuConsultas() {

    boolean salir = false;

        while (!salir) {
            
            consultasView.opcionesMenuConsultas();

            int opcion = ConsultasView.leerEntero(sc);

            switch (opcion) {
                case 1 -> stockDeUnProducto(sc);
                case 2 -> stockDeUnaUbicacion(sc);
                case 3 -> stockAgrupadoPorProducto();
                case 4 -> stockAgrupadoPorUbicacion();
                case 0 -> { salir = true; }
                default -> consultasView.mostrarMensaje("\nOpción inválida");
            }
        }
    }

    private void stockDeUnProducto(Scanner sc) {
        int idProd = consultasView.pedirIdProducto(sc);
        int stock = stockService.getStockTotalPorProducto(idProd);
        consultasView.mostrarStockDeProducto(idProd, stock);
    }

    private void stockDeUnaUbicacion(Scanner sc) {
        stockService.listarUbiDisponiblesOrdenadas();
        String codigo = consultasView.pedirCodigoUbicacion(sc);
        List<StockUbicacion> lista = stockService.getStockPorUbicacion(codigo);

        consultasView.mostrarMensaje("\nStock en la ubicación " + codigo + ":");
        if (lista == null || lista.isEmpty()) {
            consultasView.mostrarMensaje("No hay stock en esa ubicación.");
        } else {
            for (StockUbicacion s : lista) {
                consultasView.mostrarMensaje(s.toString());
            }
        }
    }

    private void stockAgrupadoPorProducto() {
        Map<Integer, Integer> agrupado = stockService.getStockAgrupadoPorProducto();
        consultasView.mostrarMensaje("\n=== Stock agrupado por producto ===");
        agrupado.forEach((id, cantidad) ->
            consultasView.mostrarMensaje("Producto " + id + ": " + cantidad)
        );
    }

    private void stockAgrupadoPorUbicacion() {
        Map<String, Integer> agrupado = stockService.getStockAgrupadoPorUbicacion();
        consultasView.mostrarMensaje("\n=== Stock agrupado por ubicación ===");
        agrupado.forEach((codigo, cantidad) ->
            consultasView.mostrarMensaje("Ubicación " + codigo + ": " + cantidad)
        );
    }
}
