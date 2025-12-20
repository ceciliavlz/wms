package DAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.OrdenTransformacion;

public class OrdenTransfDAO {
    private static final String FILE_PATH = "data/transformaciones.csv";

    public static void guardarOrdenes(List<OrdenTransformacion> ordenes) throws IOException {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (OrdenTransformacion o : ordenes){
                writer.println(String.join(",",
                    String.valueOf(o.getIdOrdenTransf()),
                    String.valueOf(o.getIdProductoEntrada()),
                    o.getUbicacionProdEntrada(),
                    String.valueOf(o.getCantidadEntrada()),
                    String.valueOf(o.getIdProductoTransformado()),
                    o.getUbicacionSalida(),
                    String.valueOf(o.getCantidadSalida()),
                    o.getFecha().toString()
                ));
            }
        }
    }

    public static List<OrdenTransformacion> cargarOrdenes() throws IOException {
        List<OrdenTransformacion> lista = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return lista;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                int idOrdenTransf = Integer.parseInt(parts[0]);
                int idProdEntrada = Integer.parseInt(parts[1]);
                String ubicacionProdEntrada = parts[2];
                int cantidadEntrada = Integer.parseInt(parts[3]); 
                int idProductoTransformado = Integer.parseInt(parts[4]); 
                String ubicacionSalida = parts[5]; 
                int cantidadSalida = Integer.parseInt(parts[6]);
                LocalDate fecha = LocalDate.parse(parts[7]);

                OrdenTransformacion orden = new OrdenTransformacion(idProdEntrada, ubicacionProdEntrada, cantidadEntrada, 
                    idProductoTransformado,ubicacionSalida,fecha);
                orden.setIdOrdenTransf(idOrdenTransf);
                orden.setCantidadSalida(cantidadSalida);

                lista.add(orden);
            }
        }
        return lista;
    }
}
