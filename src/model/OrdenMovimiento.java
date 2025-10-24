package model;

import java.time.LocalDate;

public class OrdenMovimiento {

    private TipoMovimiento tipoMovimientoOrden;
    private int idOrdenMov;
    private int cantidad;
    private int idProducto;
    private LocalDate fecha;
    //ingreso y egreso
    private String ubicacion;
    //movimiento interno
    private String ubicacionOrigen;
    private String ubicacionDestino;
    
    //ingreso y egreso
    public OrdenMovimiento(TipoMovimiento tipo, int id, int cantidad, int idProducto, LocalDate fecha, String ubicacion) {
        if (tipo != TipoMovimiento.INGRESO && tipo != TipoMovimiento.EGRESO) {
            throw new IllegalArgumentException("Este constructor solo se usa para INGRESO o EGRESO.");
        }
        this.idOrdenMov = id;
        this.tipoMovimientoOrden = tipo;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.ubicacion = ubicacion;      
    }

    public OrdenMovimiento(TipoMovimiento tipo, int id, int cantidad, int idProducto, LocalDate fecha, String ubiOrigen, String ubiDestino) {
        if (tipo != TipoMovimiento.INTERNO) {
            throw new IllegalArgumentException("Este constructor solo se usa para movimientos INTERNOS.");
        }
        this.idOrdenMov = id;
        this.tipoMovimientoOrden = tipo;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.ubicacionOrigen = ubiOrigen;
        this.ubicacionDestino = ubiDestino; 
    }
    
    public String toStringIngEgr(){
        return("FECHA " + this.fecha +" | ID "+ this.idOrdenMov +" | CANT "+ this.cantidad + " | PROD " +
        this.idProducto + " | UBICACION " + this.ubicacion + " | " + this.tipoMovimientoOrden.name());
    }
    public String toStringInterno(){
        return("FECHA " + this.fecha +" | ID "+ this.idOrdenMov +" | CANT "+ this.cantidad + " | PROD " +
        this.idProducto + " | UB ORIGEN " + this.ubicacionOrigen + " | UB DESTINO " + this.ubicacionDestino 
        +" | "+ this.tipoMovimientoOrden.name());
    }

    public TipoMovimiento getTipoMovimientoOrden() { return tipoMovimientoOrden; }
    public int getIdOrdenMov() { return idOrdenMov; }
    public int getCantidad() { return cantidad; }
    public int getIdProducto() { return idProducto; }
    public LocalDate getFecha() { return fecha; }
    public String getUbicacion() { return ubicacion; }
    public String getUbicacionOrigen() { return ubicacionOrigen; }
    public String getUbicacionDestino() { return ubicacionDestino; }

    public void setTipoMovimientoOrden(TipoMovimiento tipoMovimientoOrden) {
        this.tipoMovimientoOrden = tipoMovimientoOrden;
    }
    public void setIdOrdenMov(int idOrdenMov) {
        this.idOrdenMov = idOrdenMov;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    public void setUbicacionOrigen(String ubicacionOrigen) {
        this.ubicacionOrigen = ubicacionOrigen;
    }
    public void setUbicacionDestino(String ubicacionDestino) {
        this.ubicacionDestino = ubicacionDestino;
    }   
}
