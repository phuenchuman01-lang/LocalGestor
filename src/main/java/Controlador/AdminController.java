package Controlador;

import Modelo.Casilla;
import Modelo.RegistroHistorial;
import java.util.List;


public class AdminController {
    private final List<Casilla> inventarioCasillas;
    private final List<RegistroHistorial> auditoria;

    public AdminController(List<Casilla> inventarioCasillas, List<RegistroHistorial> auditoria) {
        this.inventarioCasillas = inventarioCasillas;
        this.auditoria = auditoria;
    }

    public void agregarModuloCasillas(int cantidad, String prefijo, String idAdmin) {
        for (int i = 1; i <= cantidad; i++) {
            String idCasilla = prefijo + "-" + i;
            inventarioCasillas.add(new Casilla(idCasilla));
        }
        registrarAccion(idAdmin, "CREACION_MODULO", "Se agregaron " + cantidad + " casillas (" + prefijo + ")");
    }

    private void registrarAccion(String idUsuario, String accion, String detalle) {
        auditoria.add(new RegistroHistorial(idUsuario, accion, detalle));
    }
}
