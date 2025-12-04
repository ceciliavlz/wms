package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import model.Role;
import model.User;

public class AuthService {
    private static final String USERS_FILE = "data/users.txt";
    private List<User> users = new ArrayList<>();
    private User currentUser = null;

    public AuthService() {
        cargarUsuarios();
    }

    /**
     * Genera el hash SHA-256 de una contraseña
     */
    public String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            
            // Convertir bytes a hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar hash SHA-256", e);
        }
    }

    /**
     * Autentica un usuario con username y password
     * Devuelve el User si las credenciales son correctas, null en caso contrario
     */
    public User login(String username, String password) {
        String passwordHash = sha256(password);
        
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPasswordHash().equals(passwordHash)) {
                this.currentUser = user;
                return user;
            }
        }
        
        return null;
    }

    /**
     * Cierra la sesión del usuario actual
     */
    public void logout() {
        this.currentUser = null;
    }

    /**
     * Obtiene el usuario actualmente autenticado
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Verifica si hay un usuario autenticado
     */
    public boolean isAuthenticated() {
        return currentUser != null;
    }

    /**
     * Carga los usuarios desde el archivo users.txt
     * Formato: username,passwordHash,role
     */
    private void cargarUsuarios() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            // Si el archivo no existe, crear usuarios por defecto
            crearUsuariosPorDefecto();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Ignorar líneas vacías y comentarios
                }

                String[] partes = line.split(",");
                if (partes.length == 3) {
                    String username = partes[0].trim();
                    String passwordHash = partes[1].trim();
                    Role role = Role.valueOf(partes[2].trim().toUpperCase());
                    
                    users.add(new User(username, passwordHash, role));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
            crearUsuariosPorDefecto();
        }
    }

    /**
     * Crea usuarios por defecto si no existe el archivo
     */
    private void crearUsuariosPorDefecto() {
        // Usuario admin: admin / admin123
        String adminHash = sha256("admin123");
        users.add(new User("admin", adminHash, Role.ADMIN));

        // Usuario user: user / user123
        String userHash = sha256("user123");
        users.add(new User("user", userHash, Role.USER));
    }
}

