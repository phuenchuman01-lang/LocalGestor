package ControladorTest;

import Controlador.UserController;
import Modelo.Casilla;
import Modelo.RegistroHistorial;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;

class UserControllerTest {
    private UserController userController;
    private List<Casilla> casillas;
    private List<RegistroHistorial> auditoria;

    @BeforeEach
    void setUp() {
        casillas = new ArrayList<>();
        auditoria = new ArrayList<>();

        Casilla casilla = new Casilla("C-01");
        casilla.setIdUsuarioAsignado("user123");
        casillas.add(casilla);

        userController = new UserController(casillas, auditoria);
    }
}