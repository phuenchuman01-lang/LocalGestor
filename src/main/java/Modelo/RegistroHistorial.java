package Modelo;

import java.time.LocalDateTime;

public class RegistroHistorial {
    private final String fechaHora;
    private final String idUsuario;
    private final String accion;
    private final String detalle;

    public RegistroHistorial(String idUsuario, String accion, String detalle) {
        this.fechaHora = LocalDateTime.now().toString();
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
}