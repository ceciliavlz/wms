package model;

import java.util.ArrayList;
import java.util.List;


public class Rack {
    private int idRack;
    private int idNave;
    static final int filas = 3;
    static final int columnas = 3;
    private List<Ubicacion> ubicaciones = new ArrayList<>();

   public Rack(int codigoRack, int idNave) {
        this.idRack = codigoRack;
        this.idNave = idNave;
        generarUbicaciones();
    }

    private void generarUbicaciones(){
        for (int f = 1; f <= filas; f++) {
            for (int c = 1; c <= columnas; c++) {
                String codigo = this.idNave + this.idRack + "-" + f + "-" + c; //1-1-3
                ubicaciones.add(new Ubicacion(codigo, this.idNave, this.idRack, f, c));
            }
        }
    }

    public String toString() {
        return "ID: " + idRack + ", Ubicaciones: " + ubicaciones.size();
    }

    public int getIdRack() { return idRack; }
    public int getIdNave() { return idNave; }
    public List<Ubicacion> getUbicaciones() { return ubicaciones; }
    public static int getColumnas() { return columnas; }
    public static int getFilas() {return filas; }
}
