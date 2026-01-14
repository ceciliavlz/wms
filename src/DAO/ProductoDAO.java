package DAO;

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
            producto.getCapacidad()+ "," +producto.getStockMinimo()+ "," +producto.getGrupo()+ "," +producto.getCodigo());
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
                linea = linea.trim();
                if (linea.isEmpty()) continue; // Ignorar líneas vacías
                
                String[] partes = linea.split(",");
                
                // Formato antiguo: id,descripcion,unidadMedida,peso,capacidad (5 campos)
                // Formato nuevo: id,descripcion,unidadMedida,peso,capacidad,stockMinimo,grupo,codigo (8 campos)
                if (partes.length >= 5) {
                    int idProd = Integer.parseInt(partes[0].trim());
                    String desc = partes[1].trim();
                    UnidadMedida unidad = UnidadMedida.valueOf(partes[2].trim());
                    double peso = Double.parseDouble(partes[3].trim());
                    double capacidad = Double.parseDouble(partes[4].trim());
                    
                    // Valores por defecto para formato antiguo
                    int stockMin = 0;
                    String grupo = "Producto final";
                    String codigo = "";
                    
                    // Si tiene más campos, usar los valores del archivo
                    if (partes.length >= 6) {
                        stockMin = Integer.parseInt(partes[5].trim());
                    }
                    if (partes.length >= 7) {
                        grupo = partes[6].trim();
                    }
                    if (partes.length >= 8) {
                        codigo = partes[7].trim();
                    } else {
                        // Generar código si no existe (compatibilidad con formato antiguo)
                        switch (grupo) {
                            case "Materia prima": codigo = "MP-0" + idProd; break;
                            case "Producto final": codigo = "PF-0" + idProd; break;
                            case "Producto reenvasado": codigo = "PR-0" + idProd; break;
                            default: codigo = "PF-0" + idProd; break;
                        }
                    }
                    
                    Producto p = new Producto(desc, unidad, peso, capacidad, stockMin, grupo);
                    p.setIdProducto(idProd);
                    p.setCodigo(codigo);
                    productos.add(p);
                }
            }
        } catch (IOException e) {
            System.out.println("Error cargando productos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error procesando línea de producto: " + e.getMessage());
            e.printStackTrace();
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
                    p.getCapacidad() + "," + p.getStockMinimo() + "," + p.getGrupo() + "," + p.getCodigo());
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error al eliminar el producto del archivo: " + e.getMessage());
            return false;
        }
    }
}  
