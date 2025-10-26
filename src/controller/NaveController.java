package controller;

import java.util.ArrayList;
import java.util.List;
import model.Nave;
import model.Rack;
import services.NaveService;
import services.StockService;


public class NaveController {
    NaveService naveService;
    private StockService stockService;

    public NaveController(NaveService naveService, StockService stockService){
        this.naveService = naveService;
        this.stockService = stockService;
    }

    public int crearNave() {  
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
    
}
