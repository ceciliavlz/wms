package model;

public class Ubicacion {
    private String codigoUbicacion;
    private Rack rack;
    static final float pesoMaximo = 1250;

    public Ubicacion(String codigoUbicacion, Rack rack) {
        this.codigoUbicacion = codigoUbicacion;
        this.rack = rack;
    }

    public String getCodigo() {
        return codigoUbicacion;
    }

    public Rack getRack() {
        return rack;
    }

    public String getCodigoUbicacion() {
        return codigoUbicacion;
    }

    public void setCodigoUbicacion(String codigoUbicacion) {
        this.codigoUbicacion = codigoUbicacion;
    }

    public static float getPesoMaximo() {
        return pesoMaximo;
    }
}
