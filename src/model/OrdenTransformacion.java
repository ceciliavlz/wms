package model;

import java.time.LocalDate;

public class OrdenTransformacion {

    private int idOrdenTransf;
    private int idProductoEntrada;
    private String ubicacionProdEntrada;
    private int cantidadEntrada;
    private int idProductoTransformado;
    private String ubicacionSalida;
    private int cantidadSalida;
    private LocalDate fecha;

    public OrdenTransformacion(int idProductoEntrada, String ubicacionProdEntrada, int cantidadEntrada, int idProductoTransformado,
            String ubicacionSalida, LocalDate fecha) {
        if (cantidadEntrada <= 0) {
            throw new IllegalArgumentException("La cantidad de entrada debe ser mayor a 0");
        }
        this.idProductoEntrada = idProductoEntrada;
        this.ubicacionProdEntrada = ubicacionProdEntrada;
        this.cantidadEntrada = cantidadEntrada;
        this.idProductoTransformado = idProductoTransformado;
        this.ubicacionSalida = ubicacionSalida;
        this.fecha = fecha;
    }

    public int getIdOrdenTransf() { return idOrdenTransf;}
    public int getIdProductoEntrada() {return idProductoEntrada;}
    public String getUbicacionProdEntrada() { return ubicacionProdEntrada;}
    public int getCantidadEntrada() { return cantidadEntrada;}
    public String getUbicacionSalida() {return ubicacionSalida;}
    public LocalDate getFecha() { return fecha;}
    public int getIdProductoTransformado() {return idProductoTransformado;}
    public int getCantidadSalida() {return cantidadSalida; }

    public void setIdProductoEntrada(int idProductoEntrada) {this.idProductoEntrada = idProductoEntrada;}
    public void setUbicacionProdEntrada(String ubicacionProdEntrada) {this.ubicacionProdEntrada = ubicacionProdEntrada;}
    public void setCantidadEntrada(int cantidadEntrada) {this.cantidadEntrada = cantidadEntrada; }
    public void setUbicacionSalida(String ubicacionSalida) {this.ubicacionSalida = ubicacionSalida;}
    public void setFecha(LocalDate fecha) {this.fecha = fecha;}
    public void setIdOrdenTransf(int idOrdenTransf) {this.idOrdenTransf = idOrdenTransf; }
    public void setIdProductoTransformado(int idProductoTransformado) {this.idProductoTransformado = idProductoTransformado;}
    public void setCantidadSalida(int cantidadSalida) {this.cantidadSalida = cantidadSalida;}

    @Override
    public String toString() {
        return idOrdenTransf + "," +
               idProductoEntrada + "," +
               ubicacionProdEntrada + "," +
               cantidadEntrada + "," +
               idProductoTransformado + "," + 
               cantidadSalida+ ","+
               ubicacionSalida + "," +
               fecha;
    }
}
