package repositories;

import java.io.*;
import java.util.*;

import model.StockUbicacion;

public class StockRepository {

    private static final String FILE_PATH = "data/stock.csv";
    
    public static void guardarStock(List<StockUbicacion> stockLista) throws IOException {
        // Lógica para guardar el stock en el archivo CSV
    File dir = new File("data");
    if (!dir.exists()) dir.mkdirs();
    
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (StockUbicacion s : stockLista){
                writer.println(s.getIdProducto() + "," + s.getCodigoUbicacion() + "," + s.getCantidad());
            }
        }
    }

    public static List<StockUbicacion> cargarStock() throws IOException {
        // Lógica para cargar el stock desde el archivo CSV
        List<StockUbicacion> lista = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return lista; // Retorna lista vacía si el archivo no existe
        
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] partes = line.split(",");
                if (partes.length == 3) {
                    int idProducto = Integer.parseInt(partes[0]);
                    String codigoUbicacion = partes[1];
                    int cantidad = Integer.parseInt(partes[2]);
                    lista.add(new StockUbicacion(idProducto, codigoUbicacion, cantidad));
                }
            }
        }   
        return lista;
    }
}
