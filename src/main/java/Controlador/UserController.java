package Controlador;

import Modelo.Casilla;
import Modelo.Item;
import Modelo.RegistroHistorial;
import java.util.Collections;
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
            if (casilla.getIdCasilla().equalsIgnoreCase(idCasilla) && idUsuario.equals(casilla.getIdUsuarioAsignado())) {
                casilla.agregarItem(item);
                registrarAccion(idUsuario, "INGRESO_ITEM", "Agregó " + item.getNombre() + " (x" + item.getCantidad() + ") en " + idCasilla);
                return true;
            }
        }
        return false;
    }

    public boolean retirarItem(String idCasilla, String idItem, String idUsuario) {
        for (Casilla casilla : inventarioCasillas) {
            if (casilla.getIdCasilla().equalsIgnoreCase(idCasilla) && idUsuario.equals(casilla.getIdUsuarioAsignado())) {
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

    public Casilla buscarCasillaPorUsuario(String idUsuario) {
        for (Casilla c : inventarioCasillas) {
            if (idUsuario.equals(c.getIdUsuarioAsignado())) {
                return c;
            }
        }
        return null;
    }

    public void reclamarCasilla(String idCasilla, String idUsuario) {
        if (buscarCasillaPorUsuario(idUsuario) != null) {
            throw new IllegalStateException("Ya posees una casilla asignada.");
        }
        for (Casilla c : inventarioCasillas) {
            if (c.getIdCasilla().equalsIgnoreCase(idCasilla)) {
                if (!c.estaDisponible()) {
                    throw new IllegalStateException("La casilla " + idCasilla + " ya está ocupada.");
                }
                c.ocupar(idUsuario);
                registrarAccion(idUsuario, "AUTO_ASIGNACION", "Registró y ocupó la casilla " + idCasilla);
                return;
            }
        }
        throw new IllegalArgumentException("La casilla " + idCasilla + " no fue encontrada.");
    }

    public void liberarMiCasilla(String idCasilla, String idUsuario) {
        for (Casilla c : inventarioCasillas) {
            if (c.getIdCasilla().equalsIgnoreCase(idCasilla) && idUsuario.equals(c.getIdUsuarioAsignado())) {
                c.liberar();
                registrarAccion(idUsuario, "LIBERACION_USUARIO", "Liberó su casilla " + idCasilla);
                return;
            }
        }
        throw new IllegalArgumentException("No tienes permisos sobre esta casilla o no existe.");
    }

    public List<Casilla> obtenerTodasLasCasillas() {
        return Collections.unmodifiableList(inventarioCasillas);
    }

    private void registrarAccion(String idUsuario, String accion, String detalle) {
        auditoria.add(new RegistroHistorial(idUsuario, accion, detalle));
    }
}