package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import model.Producto;
import model.StockUbicacion;
import model.Ubicacion;
import repositories.ProductoDAO;
import repositories.StockRepository;

public class StockService {
    private Map<Integer, Producto> productosMap = new HashMap<>();
    private Map<String, Ubicacion> ubicacionesMap = new HashMap<>();

    private Map<Integer, List<StockUbicacion>> stockPorProducto = new HashMap<>();
    private Map<String, List<StockUbicacion>> stockPorUbicacion = new HashMap<>();
    
    private List<StockUbicacion> stockLista = new ArrayList<>();


    public StockService(){
        cargarStockDesdeArchivo();
        cargarProductosArchivo();
    }

    //registrar en map
    public void registrarProducto(Producto p) {
        p.setIdProducto(getProximoProdId());    //setea id automatico
        productosMap.put(p.getIdProducto(), p);
        ProductoDAO.guardarProducto(p);
    }

    public void registrarUbicacion(Ubicacion u) {
        ubicacionesMap.put(u.getCodigoUbicacion(), u);
    }

    //cargar desde archivo
    public void cargarStockDesdeArchivo(){
        try{
            List<StockUbicacion> stockCargados = StockRepository.cargarStock();

            for (StockUbicacion s : stockCargados){  //armar maps
                stockLista.add(s);

                stockPorProducto
                .computeIfAbsent(s.getIdProducto(), k -> new ArrayList<>())
                .add(s);

                stockPorUbicacion
                .computeIfAbsent(s.getCodigoUbicacion(), k -> new ArrayList<>())
                .add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guardarEnArchivo() {
        try {
            StockRepository.guardarStock(stockLista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarProductosArchivo(){
        List<Producto> cargados = ProductoDAO.cargarProductos();
        for (Producto p : cargados) {
            productosMap.put(p.getIdProducto(), p);
        }
    }

    //prducto nuevo
    public Producto getProductoPorId(int id){
        return(productosMap.get(id));
    }

    //stock
    public boolean agregarStockAUbicacion(int idProducto, String ubicacionCodigo, int cantidad) {
        Producto prod = productosMap.get(idProducto);
        Ubicacion ubi = ubicacionesMap.get(ubicacionCodigo);

        if (prod == null || ubi == null) {
            System.out.println("ERROR: Producto o ubicación no encontrados.");
            return false;
        }

        double pesoActual = pesoEnUbicacion(ubicacionCodigo);
        double pesoNuevo = prod.getPesoUnitario() * cantidad;

        if (pesoActual + pesoNuevo > Ubicacion.getPesoMaximo()) {
            System.out.println("ERROR: No se puede agregar. Supera el peso máximo en " + ubicacionCodigo);
            return false;
        }

        //buscar stock existente
    StockUbicacion stockExistente = buscarStockUbicacion(idProducto, ubicacionCodigo);
        if (stockExistente != null) {
            stockExistente.setCantidad(stockExistente.getCantidad() + cantidad);
        } else {
            StockUbicacion nuevo = new StockUbicacion(idProducto, ubicacionCodigo, cantidad);
            stockLista.add(nuevo);
        
            stockPorProducto
                .computeIfAbsent(idProducto, k -> new ArrayList<>())
                .add(nuevo);

            stockPorUbicacion
                .computeIfAbsent(ubicacionCodigo, k -> new ArrayList<>())
                .add(nuevo);
            }
        if (pesoEnUbicacion(ubicacionCodigo) >= Ubicacion.getPesoMaximo()) {
            ubicacionesMap.get(ubicacionCodigo).setUbicacionLlena(true);
        } else {
            ubicacionesMap.get(ubicacionCodigo).setUbicacionLlena(false);
        }

        return true;
    }

    public boolean retirarStockDeUbicacion(int idProducto, String codigoUbicacion, int cantidad) {

        StockUbicacion stockExistente = buscarStockUbicacion(idProducto, codigoUbicacion);

        if (stockExistente == null) {
            System.out.println("ERROR: No existe stock para ese producto en esa ubicación.");
            return false;
        }

        if (stockExistente.getCantidad() < cantidad) {
            System.out.println("ERROR: Stock insuficiente para retirar.");
            return false;
        }

        if (pesoEnUbicacion(codigoUbicacion) >= Ubicacion.getPesoMaximo()) {
            ubicacionesMap.get(codigoUbicacion).setUbicacionLlena(true);
        } else {
            ubicacionesMap.get(codigoUbicacion).setUbicacionLlena(false);
        }
        //actualiza la lista y todos los maps porq es una referencia
        stockExistente.setCantidad(stockExistente.getCantidad() - cantidad);

        return true;
}

    public boolean moverStockEntreUbicaciones(int idProducto, String codigoUbiOrigen, String codigoUbiDestino, int cantidad) {
        
        if (retirarStockDeUbicacion(idProducto, codigoUbiOrigen, cantidad)) {
            if (agregarStockAUbicacion(idProducto, codigoUbiDestino, cantidad)) {
                return true;
            } else {
                // Revertir retiro si no se puede agregar en destino
                agregarStockAUbicacion(idProducto, codigoUbiOrigen, cantidad);
                System.out.println("ERROR: No se pudo mover el stock. Operación revertida.");
                return false;
            }
        } else {
            System.out.println("ERROR: No se pudo retirar el stock de la ubicación origen.");
            return false;
        }
    }

    //consultas
    public int getStockTotalPorProducto(int idProducto) {
        List<StockUbicacion> lista = stockPorProducto.get(idProducto);
        if (lista == null) return 0;

        return lista.stream().mapToInt(StockUbicacion::getCantidad).sum();
    }

    public List<StockUbicacion> getStockPorUbicacion(String ubicacionCodigo) {
        return stockPorUbicacion.getOrDefault(ubicacionCodigo, List.of());
    }

    public List<StockUbicacion> getStockPorProducto(int idProducto) {
        return stockPorProducto.getOrDefault(idProducto, List.of());
    }

    public int getStockTotal() {
        return stockLista.stream().mapToInt(StockUbicacion::getCantidad).sum();
    }

    public double getPesoTotal() {
        double total = 0;
        for (StockUbicacion s : stockLista) {
            Producto p = productosMap.get(s.getIdProducto());
            total += p.getPesoUnitario() * s.getCantidad();
        }
        return total;
    }
    
    public Map<Integer, Integer> getStockAgrupadoPorProducto() {
        Map<Integer, Integer> result = new HashMap<>();
        for (StockUbicacion s : stockLista) {
            result.merge(s.getIdProducto(), s.getCantidad(), Integer::sum);
        }
        return result;
    }

    public Map<String, Integer> getStockAgrupadoPorUbicacion() {
        Map<String, Integer> result = new HashMap<>();
        for (StockUbicacion s : stockLista) {
            result.merge(s.getCodigoUbicacion(), s.getCantidad(), Integer::sum);
        }
        return result;
    }

    private List<String> getUbicDisponiblesSinOrden(int idRack) {
        List<String> ubiDisponibles = new ArrayList<>();
        
        for (String codigo : ubicacionesMap.keySet()) {
            Ubicacion ubicacion = ubicacionesMap.get(codigo);
            if (!ubicacion.getUbicacionLlena() && ubicacion.getIdRack()==idRack){
                ubiDisponibles.add(codigo);
            }
        }
        return ubiDisponibles;
    }

    public List<String> listarUbicacionesDisponiblesRack(int idRack){
        List<String> ordenadas = getUbicDisponiblesSinOrden(idRack);
        Collections.sort(ordenadas);
        return ordenadas;
    }

    private int getProximoProdId() {
    int idAnterior = 0;
    for (int id : productosMap.keySet()) {
        if (id > idAnterior) {
            idAnterior = id;
        }
    }
    return idAnterior + 1;
    }

    public boolean eliminarProductoPorId(int idProducto) {
    if (!productosMap.containsKey(idProducto)) {
        System.out.println("ERROR: No existe un producto con ese ID.");
        return false;
    }
    // Verifica si tiene stock asociado
    List<StockUbicacion> stockAsociado = stockPorProducto.get(idProducto);
        if (stockAsociado != null && !stockAsociado.isEmpty()) {
            System.out.println("ERROR: No se puede eliminar el producto. Tiene stock asociado.");
            return false;
        }

        productosMap.remove(idProducto);
        ProductoDAO.eliminarProductoDelArchivo(idProducto, productosMap);
        System.out.println("Producto eliminado correctamente.");
        return true;
    }

    //consultas privadas
    private double pesoEnUbicacion(String ubicacionCodigo) {
        double total = 0;
        List<StockUbicacion> lista = stockPorUbicacion.get(ubicacionCodigo);
        if (lista != null) {
            for (StockUbicacion s : lista) {
                Producto p = productosMap.get(s.getIdProducto());
                total += p.getPesoUnitario() * s.getCantidad();
            }
        }
        return total;
    }

    private StockUbicacion buscarStockUbicacion(int idProducto, String ubicacionCodigo) {
        List<StockUbicacion> lista = stockPorProducto.get(idProducto);
        if (lista == null) return null;
        for (StockUbicacion s : lista) {
            if (s.getCodigoUbicacion().equals(ubicacionCodigo)) {
                return s;
            }
        }
        return null;
    }

    //getters
    public Map<Integer, Producto> getProductosMap() {
        return productosMap;
    }

    public Map<String, Ubicacion> getUbicacionesMap() {
        return ubicacionesMap;
    }

    public List<StockUbicacion> getStockLista() {
        return stockLista;
    }
}
