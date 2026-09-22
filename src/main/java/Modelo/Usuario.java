package Modelo;

public class Usuario {
    private final String idUsuario;
    private String nombre;
    private final RolUsuario rol;

    public Usuario(String idUsuario, String nombre, RolUsuario rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.rol = rol;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public RolUsuario getRol() {
        return rol;
    }
}