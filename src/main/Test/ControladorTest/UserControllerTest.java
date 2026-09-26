package ControladorTest;

import Controlador.UserController;
import Modelo.Casilla;
import Modelo.Item;
import Modelo.RegistroHistorial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testGuardarItemExitoso() {
        Item item = new Item("Tubo de ensayo", 5);
        boolean resultado = userController.guardarItem("C-01", item, "user123");

        assertTrue(resultado);
        assertEquals(1, casillas.get(0).getItems().size());
        assertEquals(1, auditoria.size());
        assertEquals("INGRESO_ITEM", auditoria.get(0).getAccion());
    }

    @Test
    void testGuardarItemFallaPorCasillaAjena() {
        Item item = new Item("Tubo de ensayo", 5);
        boolean resultado = userController.guardarItem("C-01", item, "otroUser");

        assertFalse(resultado);
        assertTrue(casillas.get(0).getItems().isEmpty());
    }
}