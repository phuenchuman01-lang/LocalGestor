package Vista;

import Controlador.AdminController;
import Controlador.AuthController;
import Controlador.UserController;
import Modelo.Casilla;
import Modelo.Item;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuEstudiante {
    private final JFrame frame = new JFrame("Panel de Usuario - LocalGestor");
    private final AuthController auth;
    private final AdminController adminCtrl;
    private final UserController userCtrl;
    private final JPanel panelCentro;

    public MenuEstudiante(AuthController auth, AdminController adminCtrl, UserController userCtrl) {
        this.auth = auth;
        this.adminCtrl = adminCtrl;
        this.userCtrl = userCtrl;

        frame.setSize(600, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Bienvenido: " + auth.getUsuarioActual().getNombre()
                + " (" + auth.getUsuarioActual().getIdUsuario() + ")", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        frame.add(titulo, BorderLayout.NORTH);

        panelCentro = new JPanel(new GridBagLayout());
        frame.add(panelCentro, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnBuscarItem = new JButton("Buscar Ubicación de un Objeto");
        estilizarBoton(btnBuscarItem);
        btnBuscarItem.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(frame, "Ingrese el nombre del objeto a localizar:");
            if (nombre != null && !nombre.trim().isEmpty()) {
                String resultado = userCtrl.buscarUbicacionItem(nombre.trim());
                JOptionPane.showMessageDialog(frame, resultado, "Búsqueda en Laboratorio", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        estilizarBoton(btnCerrarSesion);
        btnCerrarSesion.addActionListener(e -> {
            auth.cerrarSesion();
            frame.dispose();
            new VentanaLogin(auth, adminCtrl, userCtrl).mostrarVentana();
        });

        panelSur.add(btnBuscarItem);
        panelSur.add(btnCerrarSesion);
        frame.add(panelSur, BorderLayout.SOUTH);

        cargarOpcionesCasilla();
    }

    // --- MÉTODO AUXILIAR PARA INYECTAR LA ESTÉTICA DEL LOGIN ---
    private void estilizarBoton(JButton btn) {
        btn.setOpaque(true);
        btn.setBackground(TemaUI.AZUL_OSCURO);
        btn.setForeground(TemaUI.TEXTO_CLARO);
    }

    private void cargarOpcionesCasilla() {
        panelCentro.removeAll();
        String miId = auth.getUsuarioActual().getIdUsuario();
        Casilla miCasilla = userCtrl.buscarCasillaPorUsuario(miId);

        if (miCasilla == null) {
            JButton btnRegistrar = new JButton("Reclamar Casilla Disponible");
            btnRegistrar.setFont(new Font("Arial", Font.BOLD, 16));
            estilizarBoton(btnRegistrar);
            btnRegistrar.addActionListener(e -> mostrarVentanaReclamo());
            panelCentro.add(btnRegistrar);
        } else {
            JButton btnAdministrar = new JButton("Administrar Mi Casilla (" + miCasilla.getIdCasilla() + ")");
            btnAdministrar.setFont(new Font("Arial", Font.BOLD, 16));
            estilizarBoton(btnAdministrar);
            btnAdministrar.addActionListener(e -> mostrarVentanaAdministracion(miCasilla));
            panelCentro.add(btnAdministrar);
        }

        panelCentro.revalidate();
        panelCentro.repaint();
    }

    private void mostrarVentanaReclamo() {
        List<String> opciones = new ArrayList<>();
        for (Casilla c : userCtrl.obtenerTodasLasCasillas()) {
            opciones.add(c.getIdCasilla() + (c.estaDisponible() ? " - DISPONIBLE" : " - OCUPADA"));
        }

        if (opciones.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay casillas creadas en el sistema.");
            return;
        }

        String seleccion = (String) JOptionPane.showInputDialog(
                frame, "Selecciona una casilla disponible:\n\n",
                "Inventario de Casillas", JOptionPane.PLAIN_MESSAGE, null,
                opciones.toArray(), opciones.get(0)
        );

        if (seleccion != null) {
            if (seleccion.contains("OCUPADA")) {
                JOptionPane.showMessageDialog(frame, "Esa casilla ya se encuentra ocupada.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String idCasilla = seleccion.split(" - ")[0].trim();
                try {
                    userCtrl.reclamarCasilla(idCasilla, auth.getUsuarioActual().getIdUsuario());
                    JOptionPane.showMessageDialog(frame, "¡Casilla " + idCasilla + " asignada a tu cuenta!");
                    cargarOpcionesCasilla();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void mostrarVentanaAdministracion(Casilla casilla) {
        String miId = auth.getUsuarioActual().getIdUsuario();
        JDialog dialog = new JDialog(frame, "Administrando Casilla " + casilla.getIdCasilla(), true);
        dialog.setSize(420, 320);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(frame);

        DefaultListModel<Item> modeloLista = new DefaultListModel<>();
        for (Item item : casilla.getItems()) {
            modeloLista.addElement(item);
        }
        JList<Item> listaItems = new JList<>(modeloLista);
        dialog.add(new JScrollPane(listaItems), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 5, 5));
        JButton btnAgregar = new JButton("Guardar Objeto");
        estilizarBoton(btnAgregar);
        JButton btnQuitar = new JButton("Retirar Objeto");
        estilizarBoton(btnQuitar);
        JButton btnLiberar = new JButton("Liberar Casilla");
        estilizarBoton(btnLiberar);
        JButton btnCerrar = new JButton("Cerrar");
        estilizarBoton(btnCerrar);

        btnAgregar.addActionListener(e -> {
            JTextField txtNombre = new JTextField();
            JTextField txtCantidad = new JTextField("1");
            Object[] campos = {"Nombre del objeto:", txtNombre, "Cantidad:", txtCantidad};

            if (JOptionPane.showConfirmDialog(dialog, campos, "Guardar Objeto", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    String nombre = txtNombre.getText().trim();
                    int cant = Integer.parseInt(txtCantidad.getText().trim());
                    if (!nombre.isEmpty()) {
                        Item nuevoItem = new Item(nombre, cant);
                        if (userCtrl.guardarItem(casilla.getIdCasilla(), nuevoItem, miId)) {
                            modeloLista.addElement(nuevoItem);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Ingrese una cantidad numérica válida.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnQuitar.addActionListener(e -> {
            Item seleccionado = listaItems.getSelectedValue();
            if (seleccionado != null) {
                if (userCtrl.retirarItem(casilla.getIdCasilla(), seleccionado.getIdItem(), miId)) {
                    modeloLista.removeElement(seleccionado);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Selecciona un objeto de la lista primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnLiberar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog,
                    "¿Seguro que deseas liberar esta casilla?\nTodos los objetos serán retirados.",
                    "Confirmar Liberación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                userCtrl.liberarMiCasilla(casilla.getIdCasilla(), miId);
                dialog.dispose();
                cargarOpcionesCasilla();
            }
        });

        btnCerrar.addActionListener(e -> dialog.dispose());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnQuitar);
        panelBotones.add(btnLiberar);
        panelBotones.add(btnCerrar);
        dialog.add(panelBotones, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public void mostrarVentana() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}