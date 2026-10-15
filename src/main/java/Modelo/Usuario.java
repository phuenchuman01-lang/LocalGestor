package Modelo;

public class Usuario {
    private final String idUsuario;
    private String nombre;
    private String password;
    private final RolUsuario rol;

    // Constructor original conservado para compatibilidad con Tests unitarios
    public Usuario(String idUsuario, String nombre, RolUsuario rol) {
        this(idUsuario, nombre, "1234", rol);
    }

    // Constructor completo para la interfaz gráfica y autenticación real
    public Usuario(String idUsuario, String nombre, String password, RolUsuario rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
    }

    public boolean validarCredenciales(String id, String pass) {
        return this.idUsuario.equals(id) && this.password != null && this.password.equals(pass);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RolUsuario getRol() {
        return rol;
    }
}