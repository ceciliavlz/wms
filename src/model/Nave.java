package model;

import java.util.ArrayList;
import java.util.List;

public class Nave {
    private int idNave;
    private ArrayList <Rack> racks;
    
    public Nave(int idNave, ArrayList<Rack> racks) {
        this.idNave = idNave;
        this.racks = racks;
    }

    public int getIdNave() {
        return idNave;
    }

    public ArrayList<Rack> getRacks() {
        return racks;
    }

    public void setIdNave(int idNave) {
        this.idNave = idNave;
    }

    public void setRacks(ArrayList<Rack> racks) {
        this.racks = racks;
    }

}


