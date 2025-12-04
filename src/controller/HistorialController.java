package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.OrdenMovimiento;
import model.OrdenTransformacion;
import services.MovimientoService;
import services.TransformacionService;

public class HistorialController {
    private final MovimientoService movService;
    private final TransformacionService transfService;

    public HistorialController (MovimientoService movService, TransformacionService transfService){
        this.movService = movService;
        this.transfService = transfService;
    }

    public List<String> verHistorialProducto(int id) {
        List<RegistroHistorial> registros = new ArrayList<>();

        List<OrdenMovimiento> historialMov = movService.historialMovimientoProducto(id);
        List<OrdenTransformacion> historialTransfSalida = transfService.historialTransfSalida(id);
        List<OrdenTransformacion> historialTransfEntrada = transfService.historialTransfEntrada(id);

        for (OrdenMovimiento o : historialMov) {
            String usuario = o.getUsuarioResponsable() != null ? " | Usuario: " + o.getUsuarioResponsable() : "";
            String descripcion = switch (o.getTipoMovimientoOrden()) {
                case INGRESO -> "INGRESO | " + o.getFecha() + " | +" + o.getCantidad() + " | U" + o.getUbicacion() + usuario;
                case EGRESO -> "EGRESO | " + o.getFecha() + " | -" + o.getCantidad() + " | U" + o.getUbicacion() + usuario;
                case INTERNO -> "INTERNO | " + o.getFecha() + " | " + o.getCantidad()
                        + " - U" + o.getUbicacionOrigen() + " | + U" + o.getUbicacionDestino() + usuario;
            };
            registros.add(new RegistroHistorial(o.getFecha(), descripcion));
        }

        for (OrdenTransformacion o : historialTransfSalida) {
            String usuario = o.getUsuarioResponsable() != null ? " | Usuario: " + o.getUsuarioResponsable() : "";
            registros.add(new RegistroHistorial(
                o.getFecha(),
                "TRANSFORMACION | " + o.getFecha() + " | +" + o.getCantidadSalida()
                + " | U" + o.getUbicacionSalida()
                + " | TRANSFORMADO DE P" + o.getIdProductoEntrada() + usuario
            ));
        }

        for (OrdenTransformacion o : historialTransfEntrada) {
            String usuario = o.getUsuarioResponsable() != null ? " | Usuario: " + o.getUsuarioResponsable() : "";
            registros.add(new RegistroHistorial(
                o.getFecha(),
                "TRANSFORMACION | " + o.getFecha() + " | -" + o.getCantidadEntrada()
                + " | U" + o.getUbicacionProdEntrada()
                + " | TRANSFORMADO EN P" + o.getIdProductoTransformado() + usuario
            ));
        }

        //Ordenar por fecha
        registros.sort(Comparator.comparing(RegistroHistorial::fecha));

        //Devolver solo las descripciones
        return registros.stream()
                        .map(RegistroHistorial::descripcion)
                        .toList();
    }

     // Clase auxiliar interna
    private record RegistroHistorial(LocalDate fecha, String descripcion) {};


    public List<String> verHistorialTransformaciones() {
        List<RegistroHistorial> registros = new ArrayList<>();

        List<OrdenTransformacion> historial = transfService.getHistorialCompleto();

        for (OrdenTransformacion o : historial) {
            String usuario = o.getUsuarioResponsable() != null ? " | Usuario: " + o.getUsuarioResponsable() : "";
            String descripcion = "TRANSFORMACION | " + o.getFecha()
                + " | P" + o.getIdProductoEntrada()
                + " → P" + o.getIdProductoTransformado()
                + " | -" + o.getCantidadEntrada()
                + " +"
                + o.getCantidadSalida()
                + " | U entrada: " + o.getUbicacionProdEntrada()
                + " → U salida: " + o.getUbicacionSalida()
                + usuario;

            registros.add(new RegistroHistorial(o.getFecha(), descripcion));
        }

        //orden cronologico
        registros.sort(Comparator.comparing(RegistroHistorial::fecha));

        return registros.stream()
            .map(RegistroHistorial::descripcion)
            .toList();
    }

}


