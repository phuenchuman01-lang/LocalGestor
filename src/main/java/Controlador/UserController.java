package Controlador;

import Modelo.Casilla;
import Modelo.Item;
import Modelo.RegistroHistorial;
import java.util.List;

public class UserController {
    private final List<Casilla> inventarioCasillas;
    private final List<RegistroHistorial> auditoria;

    public UserController(List<Casilla> inventarioCasillas, List<RegistroHistorial> auditoria) {
        this.inventarioCasillas = inventarioCasillas;
        this.auditoria = auditoria;
    }

    public boolean guardarItem(String idCasilla, Item item, String idUsuario) {
        for (Casilla casilla : inventarioCasillas) {
            if (casilla.getIdCasilla().equals(idCasilla) && idUsuario.equals(casilla.getIdUsuarioAsignado())) {
                casilla.agregarItem(item);
                registrarAccion(idUsuario, "INGRESO_ITEM", "Agregó " + item.getNombre() + " en " + idCasilla);
                return true;
            }
        }
        return false;
    }

    private void registrarAccion(String idUsuario, String accion, String detalle) {
        auditoria.add(new RegistroHistorial(idUsuario, accion, detalle));
    }
}