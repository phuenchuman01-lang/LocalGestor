package Modelo;

public class Usuario {
    private final String idUsuario;
    private String nombre;
    private String password;
    private final RolUsuario rol;

    public Usuario(String idUsuario, String nombre, String password, RolUsuario rol) {
        if (idUsuario == null || idUsuario.isBlank()) throw new IllegalArgumentException("ID no puede estar vacío.");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre no puede estar vacío.");
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
    }

    public boolean validarCredenciales(String id, String pass) {
        return this.idUsuario.equalsIgnoreCase(id) && this.password.equals(pass);
    }

    public String getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPassword() { return password; } // En producción: validar sin exponer
    public void setPassword(String password) { this.password = password; }
    public RolUsuario getRol() { return rol; }
}