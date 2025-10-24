package repositories;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Producto;
import model.UnidadMedida;

public class ProductoDAO {

    private static final String FILE_PATH = "data/productos.csv";

    public static void guardarProducto(Producto producto){

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            writer.println(producto.getIdProducto() + "," + producto.getDescripcion() + "," + 
            producto.getUnidadMedida().name() + "," + producto.getPesoUnitario() + "," + 
            producto.getCapacidad());
        } catch (IOException e) {
            System.out.println("Error guardando producto: " + e.getMessage());
        }
    }

    public static List<Producto> cargarProductos() {
        List<Producto> productos = new ArrayList<>();
        File archivo = new File(FILE_PATH);
        if (!archivo.exists()) return productos;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                int idProd = Integer.parseInt(partes[0]);
                String desc = (partes[1]);
                UnidadMedida unidad = UnidadMedida.valueOf(partes[2]);
                double peso = Double.parseDouble(partes[3]);
                double capacidad = Double.parseDouble(partes[4]);
                Producto p = new Producto(desc, unidad, peso, capacidad);
                p.setIdProducto(idProd);
                productos.add(p);
            }
        } catch (IOException e) {
            System.out.println("Error cargando productos: " + e.getMessage());
        }
        return productos;
    }

    public static boolean eliminarProductoDelArchivo(int idProducto, Map<Integer, Producto> productosMap) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, false))) {
            for (Producto p : productosMap.values()) {
                // Solo escribe los productos distintos al que queremos eliminar
                if (p.getIdProducto() != idProducto) {
                    writer.println(p.getIdProducto() + "," + p.getDescripcion() + "," + 
                    p.getUnidadMedida().name() + "," + p.getPesoUnitario() + "," + 
                    p.getCapacidad());
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error al eliminar el producto del archivo: " + e.getMessage());
            return false;
        }
    }
}  
