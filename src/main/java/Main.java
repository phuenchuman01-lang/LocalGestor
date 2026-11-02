import Controlador.AdminController;
import Controlador.AuthController;
import Controlador.UserController;
import Modelo.Casilla;
import Modelo.Item;
import Modelo.RegistroHistorial;
import Modelo.RolUsuario;
import Vista.AppRouter;
import Vista.TemaUI;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Datos (Podría ir en una clase DataSeeder)
        List<Casilla> inventario = new ArrayList<>();
        List<RegistroHistorial> auditoria = new ArrayList<>();

        for (int i = 1; i <= 10; i++) inventario.add(new Casilla("LAB-" + i));
        inventario.get(0).setIdUsuarioAsignado("user01");
        inventario.get(0).agregarItem(new Item("Microscopio Óptico", 1));

        // 2. Controladores
        AuthController auth = new AuthController();
        auth.registrarUsuario("user01", "Estudiante Prueba", "1234", RolUsuario.USUARIO);

        AdminController adminCtrl = new AdminController(inventario, auditoria);
        UserController userCtrl = new UserController(inventario, auditoria);

        // 3. Router
        AppRouter router = new AppRouter(auth, adminCtrl, userCtrl);

        // 4. Ejecución en EDT
        SwingUtilities.invokeLater(() -> {
            TemaUI.aplicarTemaGlobal();
            router.irAlLogin();
        });
    }
}