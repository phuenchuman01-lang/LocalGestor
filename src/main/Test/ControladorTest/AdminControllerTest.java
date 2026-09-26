package ControladorTest;

import Controlador.AdminController;
import Modelo.Casilla;
import Modelo.RegistroHistorial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AdminControllerTest {
    private AdminController adminController;
    private List<Casilla> casillas;
    private List<RegistroHistorial> auditoria;

    @BeforeEach
    void setUp() {
        casillas = new ArrayList<>();
        auditoria = new ArrayList<>();
        adminController = new AdminController(casillas, auditoria);
    }

    @Test
    void testAgregarModuloCasillas() {
        adminController.agregarModuloCasillas(5, "LAB", "admin");

        assertEquals(5, casillas.size());
        assertEquals("LAB-1", casillas.get(0).getIdCasilla());
        assertEquals(1, auditoria.size());
    }
}