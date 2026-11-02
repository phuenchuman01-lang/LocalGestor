package Controlador;

import Modelo.RolUsuario;
import Modelo.Usuario;
import java.util.HashMap;
import java.util.Map;

public class AuthController {
    private final Map<String, Usuario> usuarios;
    private Usuario usuarioActual;

    public AuthController() {
        this.usuarios = new HashMap<>();
        // Admin por defecto
        this.usuarios.put("admin", new Usuario("admin", "Administrador Principal", "admin123", RolUsuario.ADMIN));
    }

    public boolean iniciarSesion(String idUsuario, String password) {
        if (idUsuario == null || idUsuario.isBlank() || password == null || password.isBlank()) {
            return false; // Flujo normal, no excepción
        }
        Usuario usuario = usuarios.get(idUsuario.trim());
        if (usuario != null && usuario.validarCredenciales(idUsuario.trim(), password)) {
            this.usuarioActual = usuario;
            return true;
        }
        return false;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void registrarUsuario(String id, String nombre, String password, RolUsuario rol) {
        if (id == null || id.trim().isEmpty() || nombre == null || nombre.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }
        if (usuarios.containsKey(id.trim())) {
            throw new IllegalArgumentException("El usuario '" + id + "' ya existe.");
        }
        usuarios.put(id.trim(), new Usuario(id.trim(), nombre.trim(), password, rol));
    }
}