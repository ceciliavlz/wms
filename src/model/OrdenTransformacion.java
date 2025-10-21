package model;

public class OrdenTransformacion {
    private Producto productoEntrada;
    private int stockEntrada;
    private int stockSalida;
    private double nuevaCapacidad;
    private Ubicacion ubicacionTransformacion;

    public OrdenTransformacion(Producto productoEntrada, int stockEntrada, int stockSalida, double nuevaCapacidad,
            Ubicacion ubicacionTransformacion) {
        this.productoEntrada = productoEntrada;
        this.stockEntrada = stockEntrada;
        this.stockSalida = stockSalida;
        this.nuevaCapacidad = nuevaCapacidad;
        this.ubicacionTransformacion = ubicacionTransformacion;
    }  
}
