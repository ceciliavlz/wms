package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DAO.RackDAO;
import model.Rack;
import model.Ubicacion;

public class RackService {
    private Map<Integer, Rack> racksMap = new HashMap<>();
    private StockService stockService;
    private static RackService instance;

    private RackService(StockService stockService) {
        this.stockService = stockService;
    }
    
    public static RackService getInstance() {
        if (instance == null) {
            instance = new RackService(StockService.getInstance());
        }
        
        return instance;
    }

    public Rack crearRack(int idNave) {
        int nuevoId = getProximoRackId();

        Rack nuevoRack = new Rack(nuevoId, idNave);
        racksMap.put(nuevoId, nuevoRack);

        // registrar ubicaciones en el StockService
        for (Ubicacion u : nuevoRack.getUbicaciones()) {
            stockService.registrarUbicacion(u);
        }
        
        RackDAO.guardarRack(nuevoRack);

        return nuevoRack;
    }

    public void cargarRacksDesdeArchivo() {
        List<Rack> racks = RackDAO.cargarRacks();
        for (Rack r : racks) {
            racksMap.put(r.getIdRack(), r);
            for (Ubicacion u : r.getUbicaciones()){ //carga las ubicaciones del rack al map de ubicaciones
                stockService.registrarUbicacion(u);
            }
        }
    }

    public Rack getRack(int codigo) {
        return racksMap.get(codigo);
    }

    public Collection<Rack> getTodosLosRacks() {
        return racksMap.values();
    }

    private int getProximoRackId(){
        int idAnterior = 0;
        for (int id : racksMap.keySet()) {
            if (id > idAnterior) {
                idAnterior = id;
            }
        }
    return idAnterior + 1;
    }     
}