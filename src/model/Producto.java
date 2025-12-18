package model;

public class Producto {
    private int idProducto;
    private String descripcion;
    private UnidadMedida unidadMedida;
    private double pesoUnitario;
    private double capacidad;
    private int stockMinimo;
    private String grupo;
    private String codigo;

    public Producto(String descripcion, UnidadMedida unidadMedida, double pesoUnitario,
            double capacidad, int stockMinimo, String grupo) {
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.pesoUnitario = pesoUnitario;
        this.capacidad = capacidad;
        this.stockMinimo = stockMinimo;
        this.grupo = grupo;
    }
    
    public Producto(int idTemporal, String descripcionTransf, double pesoTransf, int capTransf, int stockMin, String grupo){
        this.idProducto = idTemporal;
        this.descripcion = descripcionTransf;
        this.pesoUnitario = pesoTransf;
        this.capacidad = capTransf;
        this.stockMinimo = stockMin;
        this.grupo = grupo;
    }

    public int getIdProducto() {return idProducto;}
    public void setIdProducto(int idProducto) {this.idProducto = idProducto;}
    public double getPesoUnitario() {return pesoUnitario;}
    public String getDescripcion() {return descripcion;}
    public UnidadMedida getUnidadMedida() { return unidadMedida;}
    public double getCapacidad() {return capacidad;}
    public String getGrupo() { return grupo;}
    public int getStockMinimo() { return stockMinimo;}
    public String getCodigo() { return codigo; }

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
        return codigo+" | Desc: "+ descripcion+ " | U. medida: "+ unidadMedida+
        " | Peso unit: "+ pesoUnitario+" | Contenedor de "+ capacidad+ " "+ unidadMedida.name().toLowerCase()+
        " | Grupo: "+ grupo +" | Stock minimo: "+ stockMinimo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}

