package model;

public class Producto {
    private int idProducto;
    private String descripcion;
    private UnidadMedida unidadMedida;
    private double pesoUnitario;
    private double capacidad;

    public Producto(String descripcion, UnidadMedida unidadMedida, double pesoUnitario,
            double capacidad) {
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.pesoUnitario = pesoUnitario;
        this.capacidad = capacidad;
    }

    public int getIdProducto() {return idProducto;}
    public void setIdProducto(int idProducto) {this.idProducto = idProducto;}
    public double getPesoUnitario() {return pesoUnitario;}
    public String getDescripcion() {return descripcion;}
    public UnidadMedida getUnidadMedida() { return unidadMedida;}
    public double getCapacidad() {return capacidad;}

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public void setPesoUnitario(double pesoUnitario) {
        this.pesoUnitario = pesoUnitario;
    }

    public void setCapacidad(double capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public String toString() {
        return "P"+idProducto+ " | Desc: "+ descripcion+ " | U. medida: "+ unidadMedida+
        " | Peso unit: "+ pesoUnitario+" | Contenedor de "+ capacidad+ " "+ unidadMedida.name().toLowerCase();
    }
}
