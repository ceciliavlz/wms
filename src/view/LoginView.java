package view;

import java.util.Scanner;

import model.User;
import services.AuthService;

public class LoginView {
    private AuthService authService;
    private Scanner scanner;

    public LoginView(AuthService authService, Scanner scanner) {
        this.authService = authService;
        this.scanner = scanner;
    }

    /**
     * Muestra el formulario de login y retorna el usuario autenticado
     */
    public User mostrarLogin() {
        System.out.println("\n==============================================");
        System.out.println("     SISTEMA DE GESTIÓN DE ALMACENES (WMS)");
        System.out.println("==============================================");
        System.out.println("Por favor, ingrese sus credenciales:\n");

        int intentos = 0;
        final int MAX_INTENTOS = 3;

        while (intentos < MAX_INTENTOS) {
            System.out.print("Usuario: ");
            String username = scanner.nextLine().trim();

            System.out.print("Contraseña: ");
            String password = scanner.nextLine().trim();

            User usuario = authService.login(username, password);

            if (usuario != null) {
                System.out.println("\n✓ Login exitoso. Bienvenido, " + usuario.getUsername() + " (" + usuario.getRole() + ")");
                return usuario;
            } else {
                intentos++;
                int intentosRestantes = MAX_INTENTOS - intentos;
                if (intentosRestantes > 0) {
                    System.out.println("\n✗ Credenciales incorrectas. Intentos restantes: " + intentosRestantes + "\n");
                } else {
                    System.out.println("\n✗ Máximo de intentos alcanzado. Saliendo del sistema...");
                    return null;
                }
            }
        }

        return null;
    }
}

