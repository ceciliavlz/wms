package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Nave;
import model.Rack;
import services.NaveService;
import services.PermissionService;
import services.StockService;


public class NaveController {
    private final NaveService naveService;
    private final StockService stockService;
    private PermissionService permissionService;

    public NaveController(NaveService naveService, StockService stockService){
        this.naveService = naveService;
        this.stockService = stockService;
    }

    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public int crearNave() {
        if (permissionService != null && !permissionService.canWrite()) {
            System.out.println("ERROR: No tiene permisos para crear naves.");
            return 0;
        }  
        Nave nave = naveService.crearNave();    //VALIDAR
        return nave.getIdNave();    
    }

    public List<String> listarNaves() {
        List<String> naves = new ArrayList<>();

        for (Nave nave : naveService.getTodasLasNaves()) {
            naves.add(nave.toString());
        }
        return naves;
    }

    public int crearRack(int idNave){
        if (permissionService != null && !permissionService.canWrite()) {
            System.out.println("ERROR: No tiene permisos para crear racks.");
            return 0;
        }
        if (naveService.getNavePorId(idNave) == null) {
            return 0; }
        else {
            Rack nuevo = naveService.crearRackEnNave(idNave);
            return nuevo.getIdRack();
        }
    }

    public List<String> listarRacks(int idNave){
        List<String> racks = new ArrayList<>();
        naveService.getRacksDeNave(idNave).forEach(rack ->
        { racks.add(rack.toString());});

        return racks;
    }

    public boolean hayNaves(){
        return naveService.hayNaves();
    }

    public List<String> listarUbicacionesRack(int idRack){
        return stockService.listarUbicacionesDisponiblesRack(idRack);
    }

    public List<String> calcularPesosRack(int idRack){
        List<String> resultado = new ArrayList<String>();

        for (Map.Entry<String,Double> peso : stockService.pesosDeRack(idRack).entrySet()){
            resultado.add(peso.getKey() +": "+peso.getValue() +"kg.");
        }
        return resultado;
    }
}
