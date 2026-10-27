import Controlador.AdminController;
import Controlador.AuthController;
import Controlador.UserController;
import Modelo.Casilla;
import Modelo.Item;
import Modelo.RegistroHistorial;
import Modelo.RolUsuario;
import Vista.TemaUI;
import Vista.VentanaLogin;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Casilla> inventario = new ArrayList<>();
        List<RegistroHistorial> auditoria = new ArrayList<>();

        // Inicializamos 10 casillas de laboratorio por defecto
        for (int i = 1; i <= 10; i++) {
            inventario.add(new Casilla("LAB-" + i));
        }

        // Datos iniciales de prueba
        inventario.get(0).setIdUsuarioAsignado("user01");
        inventario.get(0).agregarItem(new Item("Microscopio Óptico", 1));

        AuthController auth = new AuthController();
        // Credenciales por defecto -> Admin: admin / admin123 | Usuario: user01 / 1234
        auth.registrarUsuario("user01", "Estudiante Prueba", "1234", RolUsuario.USUARIO);

        AdminController adminCtrl = new AdminController(inventario, auditoria);
        UserController userCtrl = new UserController(inventario, auditoria);

        // Lanzamos la GUI en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // 1. Aplicamos el tema global primero para pintar toda la interfaz
            TemaUI.aplicarTemaGlobal();

            // 2. Luego instanciamos las ventanas
            VentanaLogin login = new VentanaLogin(auth, adminCtrl, userCtrl);
            login.mostrarVentana();
        });
    }
}