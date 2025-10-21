package model;

public class StockUbicacion {
    private int idProducto;
    private String codigoUbicacion;
    private int cantidad;

    public StockUbicacion(int idProducto, String codigoUbicacion, int cantidad) {
        this.idProducto = idProducto;
        this.codigoUbicacion = codigoUbicacion;
        this.cantidad = cantidad;
    }

    public String toString(){
        return "P" + idProducto + ", Ubicación: " + codigoUbicacion + ", Cant " + cantidad;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getCodigoUbicacion() {
        return codigoUbicacion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setCodigoUbicacion(String codigoUbicacion) {
        this.codigoUbicacion = codigoUbicacion;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
}
