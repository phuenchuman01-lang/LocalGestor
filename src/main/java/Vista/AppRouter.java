package Vista;

import Controlador.AdminController;
import Controlador.AuthController;
import Controlador.UserController;

public class AppRouter {
    private final AuthController auth;
    private final AdminController adminCtrl;
    private final UserController userCtrl;

    public AppRouter(AuthController auth, AdminController adminCtrl, UserController userCtrl) {
        this.auth = auth;
        this.adminCtrl = adminCtrl;
        this.userCtrl = userCtrl;
    }

    public void irAlLogin() {
        new VentanaLogin(this).mostrarVentana();
    }

    public void irAlRegistro() {
        new VentanaRegistro(this).mostrarVentana();
    }

    public void irAlMenuPrincipal() {
        if (auth.getUsuarioActual().getRol() == Modelo.RolUsuario.ADMIN) {
            new MenuDocente(this).mostrarVentana();
        } else {
            new MenuEstudiante(this).mostrarVentana();
        }
    }

    public AuthController getAuth() { return auth; }
    public AdminController getAdminCtrl() { return adminCtrl; }
    public UserController getUserCtrl() { return userCtrl; }
}