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
        this.usuarios.put("admin", new Usuario("admin", "Administrador Principal", "admin123", RolUsuario.ADMIN));
    }

    // Conservado para compatibilidad con VistaConsola y AuthControllerTest
    public boolean iniciarSesion(String idUsuario) {
        if (usuarios.containsKey(idUsuario)) {
            this.usuarioActual = usuarios.get(idUsuario);
            return true;
        }
        return false;
    }

    // Nuevo métod sobrecargado para VentanaLogin con validación de contraseña
    public boolean iniciarSesion(String idUsuario, String password) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo de usuario no puede estar vacío.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo de contraseña no puede estar vacío.");
        }

        Usuario usuario = usuarios.get(idUsuario.trim());
        if (usuario != null && usuario.validarCredenciales(idUsuario.trim(), password)) {
            this.usuarioActual = usuario;
            return true;
        }
        throw new IllegalArgumentException("Usuario o contraseña incorrectos.");
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    // Conservado para compatibilidad con Tests unitarios
    public void registrarUsuario(String id, String nombre, RolUsuario rol) {
        if (!usuarios.containsKey(id)) {
            usuarios.put(id, new Usuario(id, nombre, "1234", rol));
        }
    }

    // Métod sobrecargado con validaciones completas para VentanaRegistro
    public void registrarUsuario(String id, String nombre, String password, RolUsuario rol) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de usuario no puede estar vacío.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }
        if (usuarios.containsKey(id.trim())) {
            throw new IllegalArgumentException("El usuario '" + id + "' ya existe en el sistema.");
        }

        usuarios.put(id.trim(), new Usuario(id.trim(), nombre.trim(), password, rol));
    }

    public boolean existeUsuario(String idUsuario) {
        return usuarios.containsKey(idUsuario);
    }
}