package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Rack;
import model.Ubicacion;
import repositories.RackRepository;

public class RackService {
    private Map<Integer, Rack> racksMap = new HashMap<>();
    private StockService stockService;

    public RackService(StockService stockService) {
        this.stockService = stockService;
    }

    public Rack crearRack(int idNave) {
        int nuevoId = getProximoRackId();
        if (racksMap.containsKey(nuevoId)) {
            System.out.println("Ya existe un rack con ese código.");
            return racksMap.get(nuevoId);
        }

        Rack nuevoRack = new Rack(nuevoId, idNave);
        racksMap.put(nuevoId, nuevoRack);

        // registrar ubicaciones en el StockService
        for (Ubicacion u : nuevoRack.getUbicaciones()) {
            stockService.registrarUbicacion(u);
        }
        
        RackRepository.guardarRack(nuevoRack);

        return nuevoRack;
    }

    public void cargarRacksDesdeArchivo() {
        List<Rack> racks = RackRepository.cargarRacks();
        for (Rack r : racks) {
            racksMap.put(r.getIdRack(), r);
        }
    }

    public Rack getRack(int codigo) {
        return racksMap.get(codigo);
    }

    public Collection<Rack> getTodosLosRacks() {
        return racksMap.values();
    }

    private int getProximoRackId(){
        return (racksMap.size() + 1);        
    }
}