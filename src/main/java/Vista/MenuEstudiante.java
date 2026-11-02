package Vista;

import Modelo.Casilla;
import Modelo.Item;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuEstudiante {
    private final JFrame frame = new JFrame("Panel de Usuario - LocalGestor");
    private final AppRouter router;
    private final JPanel panelCentro;

    public MenuEstudiante(AppRouter router) {
        this.router = router;

        frame.setSize(600, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Bienvenido: " + router.getAuth().getUsuarioActual().getNombre()
                + " (" + router.getAuth().getUsuarioActual().getIdUsuario() + ")", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        frame.add(titulo, BorderLayout.NORTH);

        panelCentro = new JPanel(new GridBagLayout());
        frame.add(panelCentro, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnBuscarItem = new JButton("Buscar Ubicación de un Objeto");
        TemaUI.estilizarBoton(btnBuscarItem);
        btnBuscarItem.addActionListener(e -> {
            // Usamos DialogoUI
            String nombre = DialogoUI.mostrarInput(frame, "Ingrese el nombre del objeto a localizar:", "Búsqueda en Laboratorio");
            if (nombre != null && !nombre.trim().isEmpty()) {
                String resultado = router.getUserCtrl().buscarUbicacionItem(nombre.trim());
                DialogoUI.mostrarMensaje(frame, resultado, "Búsqueda en Laboratorio", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        TemaUI.estilizarBoton(btnCerrarSesion);
        btnCerrarSesion.addActionListener(e -> {
            router.getAuth().cerrarSesion();
            frame.dispose();
            router.irAlLogin();
        });

        panelSur.add(btnBuscarItem);
        panelSur.add(btnCerrarSesion);
        frame.add(panelSur, BorderLayout.SOUTH);

        cargarOpcionesCasilla();
    }

    private void cargarOpcionesCasilla() {
        panelCentro.removeAll();
        String miId = router.getAuth().getUsuarioActual().getIdUsuario();
        Casilla miCasilla = router.getUserCtrl().buscarCasillaPorUsuario(miId);

        if (miCasilla == null) {
            JButton btnRegistrar = new JButton("Reclamar Casilla Disponible");
            btnRegistrar.setFont(new Font("Arial", Font.BOLD, 16));
            TemaUI.estilizarBoton(btnRegistrar);
            btnRegistrar.addActionListener(e -> mostrarVentanaReclamo());
            panelCentro.add(btnRegistrar);
        } else {
            JButton btnAdministrar = new JButton("Administrar Mi Casilla (" + miCasilla.getIdCasilla() + ")");
            btnAdministrar.setFont(new Font("Arial", Font.BOLD, 16));
            TemaUI.estilizarBoton(btnAdministrar);
            btnAdministrar.addActionListener(e -> mostrarVentanaAdministracion(miCasilla));
            panelCentro.add(btnAdministrar);
        }
        panelCentro.revalidate();
        panelCentro.repaint();
    }

    private void mostrarVentanaReclamo() {
        List<String> opciones = new ArrayList<>();
        for (Casilla c : router.getUserCtrl().obtenerTodasLasCasillas()) {
            opciones.add(c.getIdCasilla() + (c.estaDisponible() ? " - DISPONIBLE" : " - OCUPADA"));
        }

        if (opciones.isEmpty()) {
            DialogoUI.mostrarMensaje(frame, "No hay casillas creadas en el sistema.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Usamos DialogoUI.mostrarCombo
        String seleccion = DialogoUI.mostrarCombo(
                frame, "Selecciona una casilla disponible:", "Inventario de Casillas", opciones.toArray()
        );

        if (seleccion != null) {
            if (seleccion.contains("OCUPADA")) {
                DialogoUI.mostrarMensaje(frame, "Esa casilla ya se encuentra ocupada.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String idCasilla = seleccion.split(" - ")[0].trim();
                try {
                    router.getUserCtrl().reclamarCasilla(idCasilla, router.getAuth().getUsuarioActual().getIdUsuario());
                    DialogoUI.mostrarMensaje(frame, "¡Casilla " + idCasilla + " asignada a tu cuenta!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarOpcionesCasilla();
                } catch (Exception ex) {
                    DialogoUI.mostrarMensaje(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void mostrarVentanaAdministracion(Casilla casilla) {
        String miId = router.getAuth().getUsuarioActual().getIdUsuario();
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
        TemaUI.estilizarBoton(btnAgregar);
        JButton btnQuitar = new JButton("Retirar Objeto");
        TemaUI.estilizarBoton(btnQuitar);
        JButton btnLiberar = new JButton("Liberar Casilla");
        TemaUI.estilizarBoton(btnLiberar);
        JButton btnCerrar = new JButton("Cerrar");
        TemaUI.estilizarBoton(btnCerrar);

        btnAgregar.addActionListener(e -> {
            JTextField txtNombre = new JTextField();
            JTextField txtCantidad = new JTextField("1");
            Object[] campos = {"Nombre del objeto:", txtNombre, "Cantidad:", txtCantidad};

            // El JOptionPane con formulario es el único que se nos resiste un poco,
            // pero lo mantenemos aquí y le aplicamos un ajuste rápido.
            if (JOptionPane.showConfirmDialog(dialog, campos, "Guardar Objeto", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    String nombre = txtNombre.getText().trim();
                    int cant = Integer.parseInt(txtCantidad.getText().trim());
                    if (!nombre.isEmpty()) {
                        Item nuevoItem = new Item(nombre, cant);
                        if (router.getUserCtrl().guardarItem(casilla.getIdCasilla(), nuevoItem, miId)) {
                            modeloLista.addElement(nuevoItem);
                        }
                    }
                } catch (NumberFormatException ex) {
                    DialogoUI.mostrarMensaje(dialog, "Ingrese una cantidad numérica válida.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnQuitar.addActionListener(e -> {
            Item seleccionado = listaItems.getSelectedValue();
            if (seleccionado != null) {
                if (router.getUserCtrl().retirarItem(casilla.getIdCasilla(), seleccionado.getIdItem(), miId)) {
                    modeloLista.removeElement(seleccionado);
                }
            } else {
                DialogoUI.mostrarMensaje(dialog, "Selecciona un objeto de la lista primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnLiberar.addActionListener(e -> {
            int confirm = DialogoUI.mostrarConfirmacion(dialog, "¿Seguro que deseas liberar esta casilla?\nTodos los objetos serán retirados.", "Confirmar Liberación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                router.getUserCtrl().liberarMiCasilla(casilla.getIdCasilla(), miId);
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