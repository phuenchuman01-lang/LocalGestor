package Controlador;

import Modelo.Usuario;
import Modelo.RolUsuario;
import java.util.HashMap;
import java.util.Map;

public class AuthController {
    private final Map<String, Usuario> usuarios;
    private Usuario usuarioActual;

    public AuthController() {
        this.usuarios = new HashMap<>();
        this.usuarios.put("admin", new Usuario("admin", "Administrador Principal", RolUsuario.ADMIN));
    }

    public boolean iniciarSesion(String idUsuario) {
        if (usuarios.containsKey(idUsuario)) {
            this.usuarioActual = usuarios.get(idUsuario);
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
    
}