package DAO;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

import model.OrdenMovimiento;
import model.TipoMovimiento;

public class OrdenMovDAO {
    private static final String FILE_PATH = "data/movimientos.csv";

    public static void guardarOrdenes(List<OrdenMovimiento> ordenes) throws IOException {
    File dir = new File("data");
    if (!dir.exists()) dir.mkdirs();
    
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (OrdenMovimiento o : ordenes){
                writer.println(String.join(",",
                    o.getTipoMovimientoOrden().name(),
                    String.valueOf(o.getIdOrdenMov()), // valueOf ppasa el int a string
                    String.valueOf(o.getCantidad()), 
                    String.valueOf(o.getIdProducto()),
                    o.getFecha().toString(),
                    o.getUbicacion() == null ? "" : o.getUbicacion(), //si es null escribe "", sino escribe la ubicacion
                    o.getUbicacionOrigen() == null ? "" : o.getUbicacionOrigen(),
                    o.getUbicacionDestino() == null ? "" : o.getUbicacionDestino()
                ));
            }
        }
    }

    public static List<OrdenMovimiento> cargarOrdenes() throws IOException {
        List<OrdenMovimiento> lista = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return lista;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 8) {
                    TipoMovimiento tipo = TipoMovimiento.valueOf(parts[0]);
                    int idOrden = Integer.parseInt(parts[1]);
                    int cant = Integer.parseInt(parts[2]);
                    int idProducto = Integer.parseInt(parts[3]); 
                    LocalDate fecha = LocalDate.parse(parts[4]);
                    if (tipo == TipoMovimiento.INTERNO){ 
                        String ubiOrigen = parts[6];
                        String ubiDestino = parts[7];            
                        lista.add(new OrdenMovimiento( tipo, idOrden, cant, idProducto, fecha, ubiOrigen, ubiDestino));
                    } else{
                        String ubi = parts[5];
                        lista.add(new OrdenMovimiento( tipo, idOrden, cant, idProducto, fecha, ubi));
                    }
                }
            }
        }
        return lista;
    }
}

