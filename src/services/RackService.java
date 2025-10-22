package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import model.Rack;
import model.Ubicacion;

public class RackService {
    private Map<Integer, Rack> racksMap = new HashMap<>();
    private StockService stockService;

    public RackService(StockService stockService) {
        this.stockService = stockService;
    }

    public Rack crearRack(int idRack) {
        if (racksMap.containsKey(idRack)) {
            System.out.println("Ya existe un rack con ese código.");
            return racksMap.get(idRack);
        }

        Rack nuevoRack = new Rack(idRack);
        racksMap.put(idRack, nuevoRack);

        // registrar ubicaciones en el StockService
        for (Ubicacion u : nuevoRack.getUbicaciones()) {
            stockService.registrarUbicacion(u);
        }

        System.out.println("Rack creado: " + idRack + " con " 
            + (nuevoRack.getFilas() * nuevoRack.getColumnas()) + " ubicaciones.");
        return nuevoRack;
    }

    public Rack getRack(int codigo) {
        return racksMap.get(codigo);
    }

    public Collection<Rack> getTodosLosRacks() {
        return racksMap.values();
    }

    public int getProximoRackId(){
        return (racksMap.size() + 1);        
    }
}