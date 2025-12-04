package services;

import model.Role;
import model.User;

public class PermissionService {
    private User currentUser;

    public PermissionService(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Verifica si el usuario actual puede leer
     */
    public boolean canRead() {
        if (currentUser == null) {
            return false;
        }
        // Todos los usuarios autenticados pueden leer
        return true;
    }

    /**
     * Verifica si el usuario actual puede escribir (crear)
     */
    public boolean canWrite() {
        if (currentUser == null) {
            return false;
        }
        return currentUser.getRole() == Role.ADMIN;
    }

    /**
     * Verifica si el usuario actual puede actualizar
     */
    public boolean canUpdate() {
        if (currentUser == null) {
            return false;
        }
        return currentUser.getRole() == Role.ADMIN;
    }

    /**
     * Verifica si el usuario actual puede eliminar
     */
    public boolean canDelete() {
        if (currentUser == null) {
            return false;
        }
        return currentUser.getRole() == Role.ADMIN;
    }

    /**
     * Establece el usuario actual
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Obtiene el usuario actual
     */
    public User getCurrentUser() {
        return currentUser;
    }
}

