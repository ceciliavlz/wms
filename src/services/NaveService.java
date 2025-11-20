package services;

import java.util.*;

import model.Nave;
import model.Rack;
import repositories.NaveRepository;

public class NaveService {
    private Map<Integer, Nave> navesMap = new HashMap<>();
    private RackService rackService;

    public NaveService(RackService rackService) {
        this.rackService = rackService;
        cargarDesdeArchivos();
    }

    public Nave crearNave() {
        int nuevoId = getProximoNaveId();
        Nave nave = new Nave(nuevoId, new ArrayList<>());
        navesMap.put(nuevoId, nave);

        NaveRepository.guardarNave(nave);
        return nave;
    }

    public Rack crearRackEnNave(int idNave) {
        Nave nave = navesMap.get(idNave);
        if (nave == null) {
            return null;
        }

        Rack nuevoRack = rackService.crearRack(idNave); // le pasamos el id de la nave
        nave.getRacks().add(nuevoRack);
        return nuevoRack;
    }

        // Cargar naves y racks desde archivo
    public void cargarDesdeArchivos() {
        // Cargar naves
        List<Nave> navesCargadas = NaveRepository.cargarNaves();
        for (Nave n : navesCargadas) {
            navesMap.put(n.getIdNave(), n);
        }

        // Cargar racks
        rackService.cargarRacksDesdeArchivo();

        // Reconstruir relación nave → racks
        for (Rack r : rackService.getTodosLosRacks()) {
            Nave nave = navesMap.get(r.getIdNave());
            if (nave != null) {
                nave.getRacks().add(r);
            }
        }

        System.out.println("Naves y racks cargados desde archivos.");
    }

    public Collection<Nave> getTodasLasNaves() {
        return navesMap.values();
    }

    private int getProximoNaveId() {
        int idAnterior = 0;
        for (int id : navesMap.keySet()) {
            if (id > idAnterior) {
                idAnterior = id;
            }
        }
        return idAnterior + 1;
    }

    public Nave getNavePorId(int idNave) {
        return navesMap.get(idNave);
    }

    public List<Rack> getRacksDeNave(int idNave) {
        Nave nave = navesMap.get(idNave);
        if (nave != null) {
            return nave.getRacks();
        }
        return Collections.emptyList();
    }

    public Rack getRackEnNave(int idNave, int idRack) {
        Nave nave = navesMap.get(idNave);
        if (nave != null) {
            for (Rack r : nave.getRacks()) {
                if (r.getIdRack() == idRack) {
                    return r;
                }
            }
        }
        return null;
    }

    public boolean hayNaves(){
        return !getTodasLasNaves().isEmpty();
    }

    public boolean hayRacks(){
        for(Nave nave : navesMap.values()){
            if (!getRacksDeNave(nave.getIdNave()).isEmpty()){
                return true;
            }
        } return false;
    } 
}
