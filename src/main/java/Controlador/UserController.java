package Controlador;

import Modelo.Casilla;
import Modelo.RegistroHistorial;
import java.util.List;

public class UserController {
    private final List<Casilla> inventarioCasillas;
    private final List<RegistroHistorial> auditoria;

    public UserController(List<Casilla> inventarioCasillas, List<RegistroHistorial> auditoria) {
        this.inventarioCasillas = inventarioCasillas;
        this.auditoria = auditoria;
    }

    private void registrarAccion(String idUsuario, String accion, String detalle) {
        auditoria.add(new RegistroHistorial(idUsuario, accion, detalle));
    }
}