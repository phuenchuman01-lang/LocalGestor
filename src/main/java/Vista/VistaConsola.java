package Vista;

import Controlador.AdminController;
import Controlador.AuthController;
import Controlador.UserController;
import Modelo.Casilla;
import Modelo.RegistroHistorial;
import Modelo.RolUsuario;

import java.util.List;
import java.util.Scanner;

public class VistaConsola {
    private final Scanner scanner;
    private final AuthController auth;
    private final AdminController adminCtrl;
    private final UserController userCtrl;
    private final List<Casilla> inventario;

    public VistaConsola(AuthController auth, AdminController adminCtrl, UserController userCtrl, List<Casilla> inventario, List<RegistroHistorial> auditoria) {
        this.scanner = new Scanner(System.in);
        this.auth = auth;
        this.adminCtrl = adminCtrl;
        this.userCtrl = userCtrl;
        this.inventario = inventario;
    }

    public void iniciar() {
        boolean ejecutando = true;
        System.out.println("=== Bienvenido al Gestor Local ===");

        while (ejecutando) {
            System.out.print("\nIngrese su ID de usuario (o 'salir' para apagar): ");
            String id = scanner.nextLine();

            if (id.equalsIgnoreCase("salir")) {
                ejecutando = false;
                System.out.println("Cerrando el sistema...");
                break;
            }

            if (auth.iniciarSesion(id)) {
                if (auth.getUsuarioActual().getRol() == RolUsuario.ADMIN) {
                    menuAdmin();
                } else {
                    menuUsuario();
                }
            } else {
                System.out.println("Usuario no encontrado o credenciales inválidas.");
            }
        }
        scanner.close();
    }

    private void menuAdmin() {
    }

    private void menuUsuario() {
    }
}