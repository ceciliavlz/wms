package controller;

import java.util.ArrayList;
import java.util.List;
import model.Nave;
import model.Rack;
import services.NaveService;


public class NaveController {
    NaveService naveService;

    public NaveController(NaveService naveService){
        this.naveService = naveService;
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
}
