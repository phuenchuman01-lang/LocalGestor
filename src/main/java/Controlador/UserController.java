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

    public boolean retirarItem(String idCasilla, String idItem, String idUsuario) {
        for (Casilla casilla : inventarioCasillas) {
            if (casilla.getIdCasilla().equals(idCasilla) && idUsuario.equals(casilla.getIdUsuarioAsignado())) {
                boolean removido = casilla.removerItem(idItem);
                if (removido) {
                    registrarAccion(idUsuario, "RETIRO_ITEM", "Retiró item ID: " + idItem + " de " + idCasilla);
                }
                return removido;
            }
        }
        return false;
    }

    public String buscarUbicacionItem(String nombreItem) {
        for (Casilla casilla : inventarioCasillas) {
            for (Item item : casilla.getItems()) {
                if (item.getNombre().equalsIgnoreCase(nombreItem)) {
                    return "El objeto '" + nombreItem + "' se encuentra en la casilla: " + casilla.getIdCasilla();
                }
            }
        }
        return "Objeto no encontrado en el sistema.";
    }

    private void registrarAccion(String idUsuario, String accion, String detalle) {
        auditoria.add(new RegistroHistorial(idUsuario, accion, detalle));
    }
}