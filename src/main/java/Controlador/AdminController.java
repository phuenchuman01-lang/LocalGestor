package Controlador;

import Modelo.Casilla;
import Modelo.Item;
import Modelo.RegistroHistorial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminController {
    private final List<Casilla> inventarioCasillas;
    private final List<RegistroHistorial> auditoria;

    public AdminController(List<Casilla> inventarioCasillas, List<RegistroHistorial> auditoria) {
        this.inventarioCasillas = inventarioCasillas;
        this.auditoria = auditoria;
    }

    public void agregarModuloCasillas(int cantidad, String prefijo, String idAdmin) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de casillas debe ser mayor a cero.");
        }
        if (prefijo == null || prefijo.trim().isEmpty()) {
            throw new IllegalArgumentException("El prefijo no puede estar vacío.");
        }

        String prefijoLimpio = prefijo.trim().toUpperCase();
        int creadas = 0;
        int indice = 1;

        while (creadas < cantidad) {
            String idCasilla = prefijoLimpio + "-" + indice;
            if (buscarCasillaPorId(idCasilla) == null) {
                inventarioCasillas.add(new Casilla(idCasilla));
                creadas++;
            }
            indice++;
        }
        registrarAccion(idAdmin, "CREACION_MODULO", "Se agregaron " + cantidad + " casillas (" + prefijoLimpio + ")");
    }

    public void asignarCasilla(String idCasilla, String idUsuario, String idAdmin) {
        Casilla casilla = buscarCasillaPorId(idCasilla);
        if (casilla == null) {
            throw new IllegalArgumentException("La casilla " + idCasilla + " no existe.");
        }
        if (!casilla.estaDisponible()) {
            throw new IllegalStateException("La casilla " + idCasilla + " ya está ocupada por " + casilla.getIdUsuarioAsignado());
        }

        casilla.setIdUsuarioAsignado(idUsuario);
        registrarAccion(idAdmin, "ASIGNACION", "Casilla " + idCasilla + " asignada a " + idUsuario);
    }

    public void liberarCasilla(String idCasilla, String idAdmin) {
        Casilla casilla = buscarCasillaPorId(idCasilla);
        if (casilla == null) {
            throw new IllegalArgumentException("Casilla no encontrada.");
        }
        String exDueno = casilla.getIdUsuarioAsignado();
        casilla.liberar();
        registrarAccion(idAdmin, "LIBERACION", "Liberó casilla " + idCasilla + " (Ex-dueño: " + exDueno + ")");
    }

    public void moverContenidoCasilla(String idOrigen, String idDestino, String idAdmin) {
        Casilla origen = buscarCasillaPorId(idOrigen);
        Casilla destino = buscarCasillaPorId(idDestino);

        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Una o ambas casillas no existen en el sistema.");
        }
        if (origen.estaDisponible()) {
            throw new IllegalStateException("La casilla de origen está libre; no hay nada que mover.");
        }
        if (!destino.estaDisponible()) {
            throw new IllegalStateException("La casilla de destino (" + idDestino + ") ya está ocupada.");
        }

        destino.ocupar(origen.getIdUsuarioAsignado());
        for (Item item : origen.getItems()) {
            destino.agregarItem(item);
        }
        origen.liberar();

        registrarAccion(idAdmin, "TRASLADO", "Movió contenido de " + idOrigen + " hacia " + idDestino);
    }

    public void agregarItemComoAdmin(String idCasilla, Item item, String idAdmin) {
        Casilla casilla = buscarCasillaPorId(idCasilla);
        if (casilla == null) {
            throw new IllegalArgumentException("Casilla no encontrada.");
        }
        casilla.agregarItem(item);
        registrarAccion(idAdmin, "INGRESO_ADMIN", "Agregó " + item.getNombre() + " (x" + item.getCantidad() + ") en " + idCasilla);
    }

    public boolean retirarItemComoAdmin(String idCasilla, String idItem, String idAdmin) {
        Casilla casilla = buscarCasillaPorId(idCasilla);
        if (casilla == null) {
            throw new IllegalArgumentException("Casilla no encontrada.");
        }
        boolean removido = casilla.removerItem(idItem);
        if (removido) {
            registrarAccion(idAdmin, "RETIRO_ADMIN", "Retiró item ID " + idItem + " de " + idCasilla);
        }
        return removido;
    }

    public Casilla buscarCasillaPorId(String idCasilla) {
        for (Casilla c : inventarioCasillas) {
            if (c.getIdCasilla().equalsIgnoreCase(idCasilla)) {
                return c;
            }
        }
        return null;
    }

    public List<Casilla> obtenerTodasLasCasillas() {
        return Collections.unmodifiableList(inventarioCasillas);
    }

    public List<RegistroHistorial> verHistorial() {
        return new ArrayList<>(auditoria);
    }

    private void registrarAccion(String idUsuario, String accion, String detalle) {
        auditoria.add(new RegistroHistorial(idUsuario, accion, detalle));
    }
}