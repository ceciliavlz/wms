package services;

import java.util.*;

import DAO.NaveDAO;
import model.Nave;
import model.Rack;

public class NaveService {
    private Map<Integer, Nave> navesMap = new HashMap<>();
    private RackService rackService;
    private static NaveService instance;

    private NaveService(RackService rackService) {
        this.rackService = rackService;
        cargarDesdeArchivos();
    }
    
    public static NaveService getInstance() {
        if (instance == null) {
            instance = new NaveService(RackService.getInstance());
        }
        
        return instance;
    }

    public Nave crearNave() {
        int nuevoId = getProximoNaveId();
        Nave nave = new Nave(nuevoId, new ArrayList<>());
        navesMap.put(nuevoId, nave);

        NaveDAO.guardarNave(nave);
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
        List<Nave> navesCargadas = NaveDAO.cargarNaves();
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
    
    public List<Integer> getTodosLosRacks(){
        ArrayList<Integer> lista = new ArrayList<>();
        Collection<Rack> racks = rackService.getTodosLosRacks();
        for (Rack r : racks) {
            lista.add(r.getIdRack());
        }
        return lista;
    }
    
    public Integer getIdNavePorRack(int idRack) {
        Rack rack = rackService.getRack(idRack);
            if (rack != null) {
                return rack.getIdNave();
            }
        return null;
    }

}
