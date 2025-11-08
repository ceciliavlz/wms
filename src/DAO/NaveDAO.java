package DAO;
import java.io.*;
import java.util.*;

import model.Nave;

public class NaveDAO {
    private static final String FILE_PATH = "data/naves.csv";

    public static void guardarNave(Nave nave) {
    File dir = new File("data");
    if (!dir.exists()) dir.mkdirs();
    
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            pw.println(nave.getIdNave());
        } catch (IOException e) {
            System.out.println("Error guardando nave: " + e.getMessage());
        }
    }

    public static List<Nave> cargarNaves() {
        List<Nave> naves = new ArrayList<>();
        File archivo = new File(FILE_PATH);
        if (!archivo.exists()) return naves;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                int idNave = Integer.parseInt(linea.trim());
                naves.add(new Nave(idNave, new ArrayList<>()));
            }
        } catch (IOException e) {
            System.out.println("Error cargando naves: " + e.getMessage());
        }
        return naves;
    }
}