package model;

import java.util.ArrayList;
import java.util.List;

public class Rack {
    private int idRack;
    private List<Ubicacion> ubicaciones;
    static final int columnas = 3;
    static final int filas = 3;

    public Rack(int idRack) {
        this.idRack = idRack;
        this.ubicaciones = generarUbicaciones();
    }

    List<Ubicacion> generarUbicaciones(){
        List<Ubicacion> lista = new ArrayList<>();
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                String codigo = this.idRack + "-" + f + "-" + c; //1-1-3
                lista.add(new Ubicacion(codigo, this));
            }
        }
        return lista;
    }   
}
