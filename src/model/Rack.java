package model;

import java.util.ArrayList;
import java.util.List;


public class Rack {
    private int idRack;
    static final int filas = 3;
    static final int columnas = 3;
    private List<Ubicacion> ubicaciones = new ArrayList<>();

   public Rack(int codigoRack) {
        this.idRack = codigoRack;
        generarUbicaciones();
    }

    private void generarUbicaciones(){
        for (int f = 1; f <= filas; f++) {
            for (int c = 1; c <= columnas; c++) {
                String codigo = this.idRack + "-" + f + "-" + c; //1-1-3
                ubicaciones.add(new Ubicacion(codigo, this.idRack, f, c));
            }
        }
    }

    public int getIdRack() { return idRack; }
    public List<Ubicacion> getUbicaciones() { return ubicaciones; }
    public static int getColumnas() { return columnas; }
    public static int getFilas() {return filas; }
}
