package model;

public class Producto {
    private int idProducto;
    private String descripcion;
    private UnidadMedida unidadMedida;
    private double pesoUnitario;
    private double capacidad;

    public Producto(int idProducto, String descripcion, UnidadMedida unidadMedida, double pesoUnitario,
            double capacidad) {
        this.idProducto = idProducto;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.pesoUnitario = pesoUnitario;
        this.capacidad = capacidad;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public double getPesoUnitario() {
        return pesoUnitario;
    }

    public String getDescripcion() {
        return descripcion;
    }

}
