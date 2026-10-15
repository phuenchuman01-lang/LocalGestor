package Modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistroHistorial {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String fechaHora;
    private final String idUsuario;
    private final String accion;
    private final String detalle;

    public RegistroHistorial(String idUsuario, String accion, String detalle) {
        this.fechaHora = LocalDateTime.now().format(FORMATO);
        this.idUsuario = idUsuario;
        this.accion = accion;
        this.detalle = detalle;
    }

    public RegistroHistorial(String fechaHora, String idUsuario, String accion, String detalle) {
        this.fechaHora = fechaHora;
        this.idUsuario = idUsuario;
        this.accion = accion;
        this.detalle = detalle;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getAccion() {
        return accion;
    }

    public String getDetalle() {
        return detalle;
    }

    @Override
    public String toString() {
        return String.format("[%s] Usuario: %s | Acción: %s | %s", fechaHora, idUsuario, accion, detalle);
    }
}