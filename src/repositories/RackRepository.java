package repositories;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import model.Rack;

public class RackRepository {

    private static final String FILE_PATH = "data/racks.csv";

    public static void guardarRack(Rack rack){

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            writer.println(rack.getIdRack() + "," + rack.getIdNave());
        } catch (IOException e) {
            System.out.println("Error guardando rack: " + e.getMessage());
        }
    }

    public static List<Rack> cargarRacks() {
        List<Rack> racks = new ArrayList<>();
        File archivo = new File(FILE_PATH);
        if (!archivo.exists()) return racks;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                int idRack = Integer.parseInt(partes[0]);
                int idNave = Integer.parseInt(partes[1]);
                racks.add(new Rack(idRack, idNave));
            }
        } catch (IOException e) {
            System.out.println("Error cargando racks: " + e.getMessage());
        }
        return racks;
    }
}
