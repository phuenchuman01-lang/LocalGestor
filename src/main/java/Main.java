import Controlador.AdminController;
import Controlador.AuthController;
import Controlador.UserController;
import Modelo.Casilla;
import Modelo.Item;
import Modelo.RegistroHistorial;
import Vista.VistaConsola;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Crear listas en memoria
        List<Casilla> inventario = new ArrayList<>();
        List<RegistroHistorial> auditoria = new ArrayList<>();

        // 2. Inyectar datos de prueba
        // Creamos 5 casillas base
        for (int i = 1; i <= 5; i++) {
            inventario.add(new Casilla("LAB-" + i));
        }

        // Asignamos la primera casilla a un usuario de prueba y le ponemos un objeto
        inventario.get(0).setIdUsuarioAsignado("user01");
        inventario.get(0).agregarItem(new Item("Microscopio", 1));

        // 3. Inicializar Controladores
        AuthController auth = new AuthController();
        // Registramos un usuario de prueba para la simulación
        auth.registrarUsuario("user01", "Estudiante Prueba", Modelo.RolUsuario.USUARIO);

        AdminController adminCtrl = new AdminController(inventario, auditoria);
        UserController userCtrl = new UserController(inventario, auditoria);

        // 4. Inicializar y lanzar la Vista
        VistaConsola vista = new VistaConsola(auth, adminCtrl, userCtrl, inventario, auditoria);
        vista.iniciar();
    }
}