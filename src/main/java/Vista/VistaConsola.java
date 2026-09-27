package Vista;

import Controlador.AdminController;
import Controlador.AuthController;
import Controlador.UserController;
import Modelo.Casilla;
import Modelo.Item;
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
        boolean enMenu = true;
        String idActual = auth.getUsuarioActual().getIdUsuario();

        while (enMenu) {
            System.out.println("\n Panel de Administración ");
            System.out.println("1. Agregar nuevas casillas al sistema");
            System.out.println("2. Asignar casilla a usuario");
            System.out.println("3. Ver historial de auditoría");
            System.out.println("4. Ver matriz de casillas");
            System.out.println("5. Cerrar sesión");
            System.out.print("Opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    System.out.print("Cantidad de casillas a crear: ");
                    int cantidad = Integer.parseInt(scanner.nextLine());
                    System.out.print("Prefijo (Ej: LAB): ");
                    String prefijo = scanner.nextLine();
                    adminCtrl.agregarModuloCasillas(cantidad, prefijo, idActual);
                    System.out.println("Módulo de casillas agregado exitosamente.");
                    break;
                case "2":
                    System.out.print("ID de la casilla: ");
                    String idCasilla = scanner.nextLine();
                    System.out.print("ID del usuario a asignar: ");
                    String idUsuario = scanner.nextLine();
                    adminCtrl.asignarCasilla(idCasilla, idUsuario, idActual);
                    System.out.println("Asignación registrada.");
                    break;
                case "3":
                    System.out.println("\n REGISTRO DE AUDITORÍA ");
                    for (RegistroHistorial reg : adminCtrl.verHistorial()) {
                        System.out.printf("[%s] Usuario: %s | Acción: %s | Detalle: %s\n",
                                reg.getFechaHora(), reg.getIdUsuario(), reg.getAccion(), reg.getDetalle());
                    }
                    break;
                case "4":
                    mostrarMatrizCasillas();
                    break;
                case "5":
                    enMenu = false;
                    auth.cerrarSesion();
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void menuUsuario() {
        boolean enMenu = true;
        String idActual = auth.getUsuarioActual().getIdUsuario();

        while (enMenu) {
            System.out.println("\n Panel de Usuario ");
            System.out.println("1. Guardar objeto en mi casilla");
            System.out.println("2. Retirar objeto");
            System.out.println("3. Buscar ubicación de un objeto");
            System.out.println("4. Ver estado de contenedores (Matriz)");
            System.out.println("5. Cerrar sesión");
            System.out.print("Opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    System.out.print("ID de su casilla: ");
                    String idCasilla = scanner.nextLine();
                    System.out.print("Nombre del objeto: ");
                    String nombreItem = scanner.nextLine();
                    System.out.print("Cantidad: ");
                    int cantidad = Integer.parseInt(scanner.nextLine());

                    boolean guardado = userCtrl.guardarItem(idCasilla, new Item(nombreItem, cantidad), idActual);
                    if (guardado) {
                        System.out.println("Objeto guardado exitosamente.");
                    } else {
                        System.out.println("Error: Verifique que la casilla exista y le pertenezca.");
                    }
                    break;
                case "2":
                    System.out.print("ID de su casilla: ");
                    String idCasillaRetiro = scanner.nextLine();
                    System.out.print("ID del objeto a retirar: ");
                    String idItem = scanner.nextLine();

                    boolean retirado = userCtrl.retirarItem(idCasillaRetiro, idItem, idActual);
                    if (retirado) {
                        System.out.println("Objeto retirado.");
                    } else {
                        System.out.println("No se pudo retirar el objeto. Verifique los IDs.");
                    }
                    break;
                case "3":
                    System.out.print("Ingrese el nombre del objeto a buscar: ");
                    String busqueda = scanner.nextLine();
                    String resultado = userCtrl.buscarUbicacionItem(busqueda);
                    System.out.println(resultado);
                    break;
                case "4":
                    mostrarMatrizCasillas();
                    break;
                case "5":
                    enMenu = false;
                    auth.cerrarSesion();
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }


    private void mostrarMatrizCasillas() {
        System.out.println("\n MAPA DE CONTENEDORES ");
        int columnas = 4;
        int contador = 0;

        for (Casilla casilla : inventario) {
            String estado = (casilla.getIdUsuarioAsignado() == null) ? "[LIBRE]" : "[USADA]";

            System.out.printf("%-15s", casilla.getIdCasilla() + " " + estado);

            contador++;
            if (contador % columnas == 0) {
                System.out.println();
            }
        }
        System.out.println("\n-----------------------------");
    }
}