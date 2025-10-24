package model;

public class Ubicacion {
    private String codigoUbicacion;
    private int idNave;
    private int idRack;
    private int fila;
    private int columna;
    private boolean ubicacionLlena;
    static final float PESO_MAXIMO = 1250;  

    public Ubicacion(String codigoUbicacion, int idNave, int idRack, int fila, int columna) {
        this.codigoUbicacion = codigoUbicacion;
        this.idNave = idNave;
        this.idRack = idRack;
        this.fila = fila;
        this.columna = columna;
        this.ubicacionLlena = false;
    }

    public String getCodigoUbicacion() { return codigoUbicacion; }
    public int getIdRack() { return idRack; }
    public int getFila() { return fila; }
    public int getColumna() { return columna; }
    public static float getPesoMaximo() { return PESO_MAXIMO; }
    public boolean getUbicacionLlena() {return ubicacionLlena; }

    public String toString() {
        return codigoUbicacion;
    }
    
    public void setUbicacionLlena(boolean ubicacionLlena){
        this.ubicacionLlena = ubicacionLlena;
    }
}
