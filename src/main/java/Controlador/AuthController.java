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
}