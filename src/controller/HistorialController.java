package controller;

import java.util.ArrayList;
import java.util.List;
import model.OrdenMovimiento;
import services.MovimientoService;

public class HistorialController {
    MovimientoService movService;

    public HistorialController (MovimientoService movService){
        this.movService = movService;
    }

    public List<String> verHistorialMov(int id) {
        List<String> response = new ArrayList<String>();

        List<OrdenMovimiento> historial = movService.historialMovimientoProducto(id);

        for (OrdenMovimiento o : historial) {
            response.add(o.getTipoMovimientoOrden() + " | " + o.getFecha() + " | " + o.getCantidad());
        }
        
        return response;
    }
}