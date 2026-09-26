package ControladorTest;

import Controlador.AuthController;
import Modelo.RolUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController();
    }

    @Test
    void testInicioSesionAdminPorDefecto() {
        boolean login = authController.iniciarSesion("admin");

        assertTrue(login);
        assertNotNull(authController.getUsuarioActual());
        assertEquals(RolUsuario.ADMIN, authController.getUsuarioActual().getRol());
    }

    @Test
    void testRegistroEInicioSesionNuevoUsuario() {
        authController.registrarUsuario("user01", "Estudiante", RolUsuario.USUARIO);
        boolean login = authController.iniciarSesion("user01");

        assertTrue(login);
        assertEquals("user01", authController.getUsuarioActual().getIdUsuario());
    }
}