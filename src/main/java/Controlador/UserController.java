package Controlador;

import Modelo.Casilla;
import Modelo.Item;
import Modelo.RegistroHistorial;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserController {
    private final List<Casilla> inventarioCasillas;
    private final List<RegistroHistorial> auditoria;

    public UserController(List<Casilla> inventarioCasillas, List<RegistroHistorial> auditoria) {
        this.inventarioCasillas = inventarioCasillas;
        this.auditoria = auditoria;
    }

    public boolean guardarItem(String idCasilla, Item item, String idUsuario) {
        Optional<Casilla> casillaOpt = inventarioCasillas.stream()
                .filter(c -> c.getIdCasilla().equalsIgnoreCase(idCasilla) && idUsuario.equals(c.getIdUsuarioAsignado()))
                .findFirst();

        if (casillaOpt.isPresent()) {
            casillaOpt.get().agregarItem(item);
            registrarAccion(idUsuario, "INGRESO_ITEM", "Agregó " + item.getNombre() + " en " + idCasilla);
            return true;
        }
        return false;
    }

    public boolean retirarItem(String idCasilla, String idItem, String idUsuario) {
        for (Casilla c : inventarioCasillas) {
            if (c.getIdCasilla().equalsIgnoreCase(idCasilla) && idUsuario.equals(c.getIdUsuarioAsignado())) {
                boolean removido = c.removerItem(idItem);
                if (removido) registrarAccion(idUsuario, "RETIRO_ITEM", "Retiró item de " + idCasilla);
                return removido;
            }
        }
        return false;
    }

    public String buscarUbicacionItem(String nombreItem) {
        for (Casilla c : inventarioCasillas) {
            for (Item item : c.getItems()) {
                if (item.getNombre().equalsIgnoreCase(nombreItem)) {
                    return "El objeto '" + nombreItem + "' está en la casilla: " + c.getIdCasilla();
                }
            }
        }
        return "Objeto no encontrado.";
    }

    public Casilla buscarCasillaPorUsuario(String idUsuario) {
        return inventarioCasillas.stream()
                .filter(c -> idUsuario.equals(c.getIdUsuarioAsignado()))
                .findFirst()
                .orElse(null);
    }

    public void reclamarCasilla(String idCasilla, String idUsuario) {
        if (buscarCasillaPorUsuario(idUsuario) != null) {
            throw new IllegalStateException("Ya posees una casilla asignada.");
        }
        for (Casilla c : inventarioCasillas) {
            if (c.getIdCasilla().equalsIgnoreCase(idCasilla)) {
                if (!c.estaDisponible()) throw new IllegalStateException("Casilla ocupada.");
                c.ocupar(idUsuario);
                registrarAccion(idUsuario, "AUTO_ASIGNACION", "Ocupó la casilla " + idCasilla);
                return;
            }
        }
        throw new IllegalArgumentException("Casilla no encontrada.");
    }

    public void liberarMiCasilla(String idCasilla, String idUsuario) {
        for (Casilla c : inventarioCasillas) {
            if (c.getIdCasilla().equalsIgnoreCase(idCasilla) && idUsuario.equals(c.getIdUsuarioAsignado())) {
                c.liberar();
                c.vaciarContenido(); // Vaciamos al liberar por parte del usuario
                registrarAccion(idUsuario, "LIBERACION_USUARIO", "Liberó casilla " + idCasilla);
                return;
            }
        }
        throw new IllegalArgumentException("No tienes permisos sobre esta casilla.");
    }

    public List<Casilla> obtenerTodasLasCasillas() {
        return Collections.unmodifiableList(inventarioCasillas);
    }

    private void registrarAccion(String idUsuario, String accion, String detalle) {
        auditoria.add(new RegistroHistorial(idUsuario, accion, detalle));
    }
}