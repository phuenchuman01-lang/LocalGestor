package Vista;

import Controlador.AdminController;
import Controlador.AuthController;
import Controlador.UserController;
import Modelo.Casilla;
import Modelo.Item;
import Modelo.RegistroHistorial;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MenuDocente {
    private final JFrame frame = new JFrame("Panel de Administración - LocalGestor");
    private final AuthController auth;
    private final AdminController adminCtrl;
    private final UserController userCtrl;
    private final JPanel panelCasillas;

    public MenuDocente(AuthController auth, AdminController adminCtrl, UserController userCtrl) {
        this.auth = auth;
        this.adminCtrl = adminCtrl;
        this.userCtrl = userCtrl;

        frame.setSize(850, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Panel de Control Admin: " + auth.getUsuarioActual().getNombre(), SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(titulo, BorderLayout.NORTH);

        panelCasillas = new JPanel(new GridLayout(0, 5, 10, 10));
        panelCasillas.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        frame.add(new JScrollPane(panelCasillas), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnAgregarModulo = new JButton("Agregar Módulo de Casillas");
        btnAgregarModulo.addActionListener(e -> dialogAgregarModulo());

        JButton btnVerHistorial = new JButton("Ver Historial de Auditoría");
        btnVerHistorial.addActionListener(e -> mostrarVentanaHistorial());

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(e -> {
            auth.cerrarSesion();
            frame.dispose();
            new VentanaLogin(auth, adminCtrl, userCtrl).mostrarVentana();
        });

        panelInferior.add(btnAgregarModulo);
        panelInferior.add(btnVerHistorial);
        panelInferior.add(btnCerrarSesion);
        frame.add(panelInferior, BorderLayout.SOUTH);

        cargarVistaCasillas();
    }

    private void cargarVistaCasillas() {
        panelCasillas.removeAll();
        List<Casilla> casillas = adminCtrl.obtenerTodasLasCasillas();

        for (Casilla c : casillas) {
            JButton btnCasilla = new JButton();
            btnCasilla.setLayout(new BorderLayout());
            btnCasilla.setPreferredSize(new Dimension(130, 90));

            JLabel lblId = new JLabel(c.getIdCasilla(), SwingConstants.CENTER);
            lblId.setFont(new Font("Arial", Font.BOLD, 14));

            JLabel lblEstado = new JLabel(
                    c.estaDisponible() ? "DISPONIBLE" : "Usuario: " + c.getIdUsuarioAsignado(),
                    SwingConstants.CENTER
            );

            if (c.estaDisponible()) {
                btnCasilla.setBackground(new Color(144, 238, 144));
            } else {
                btnCasilla.setBackground(new Color(255, 182, 193));
            }

            btnCasilla.add(lblId, BorderLayout.NORTH);
            btnCasilla.add(lblEstado, BorderLayout.CENTER);
            btnCasilla.addActionListener(e -> mostrarOpcionesAdmin(c.getIdCasilla()));

            panelCasillas.add(btnCasilla);
        }

        panelCasillas.revalidate();
        panelCasillas.repaint();
    }

    private void dialogAgregarModulo() {
        JTextField txtPrefijo = new JTextField("LAB");
        JTextField txtCantidad = new JTextField("5");
        Object[] campos = {
                "Prefijo del Módulo (Ej: LAB, BIO):", txtPrefijo,
                "Cantidad de Casillas a crear:", txtCantidad
        };

        int opcion = JOptionPane.showConfirmDialog(frame, campos, "Nuevo Módulo de Casillas", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                String prefijo = txtPrefijo.getText().trim();
                adminCtrl.agregarModuloCasillas(cantidad, prefijo, auth.getUsuarioActual().getIdUsuario());
                cargarVistaCasillas();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "La cantidad debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarOpcionesAdmin(String idCasilla) {
        Casilla c = adminCtrl.buscarCasillaPorId(idCasilla);
        if (c == null) return;

        String idAdmin = auth.getUsuarioActual().getIdUsuario();
        JDialog dialog = new JDialog(frame, "Gestión de Casilla " + idCasilla, true);
        dialog.setSize(460, 360);
        dialog.setLayout(new BorderLayout(5, 5));
        dialog.setLocationRelativeTo(frame);

        if (c.estaDisponible()) {
            JLabel lblInfo = new JLabel("La casilla " + idCasilla + " está actualmente disponible.", SwingConstants.CENTER);
            dialog.add(lblInfo, BorderLayout.CENTER);

            JPanel panelBotones = new JPanel();
            JButton btnAsignar = new JButton("Asignar a Usuario");
            JButton btnCerrar = new JButton("Cerrar");

            btnAsignar.addActionListener(e -> {
                String idUsuario = JOptionPane.showInputDialog(dialog, "Ingrese el ID del usuario a asignar:");
                if (idUsuario != null && !idUsuario.trim().isEmpty()) {
                    try {
                        adminCtrl.asignarCasilla(idCasilla, idUsuario.trim(), idAdmin);
                        JOptionPane.showMessageDialog(dialog, "¡Casilla asignada exitosamente!");
                        dialog.dispose();
                        cargarVistaCasillas();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            btnCerrar.addActionListener(e -> dialog.dispose());
            panelBotones.add(btnAsignar);
            panelBotones.add(btnCerrar);
            dialog.add(panelBotones, BorderLayout.SOUTH);
        } else {
            JPanel panelTop = new JPanel(new GridLayout(2, 1));
            panelTop.add(new JLabel("Asignada a: " + c.getIdUsuarioAsignado(), SwingConstants.CENTER));
            panelTop.add(new JLabel("Items almacenados:", SwingConstants.CENTER));
            dialog.add(panelTop, BorderLayout.NORTH);

            DefaultListModel<Item> modeloLista = new DefaultListModel<>();
            for (Item item : c.getItems()) {
                modeloLista.addElement(item);
            }
            JList<Item> listaItems = new JList<>(modeloLista);
            dialog.add(new JScrollPane(listaItems), BorderLayout.CENTER);

            JPanel panelBotones = new JPanel(new GridLayout(3, 2, 5, 5));
            JButton btnAgregar = new JButton("Agregar Item");
            JButton btnQuitar = new JButton("Retirar Item");
            JButton btnLiberar = new JButton("Liberar Casilla");
            JButton btnMover = new JButton("Mover Contenido");
            JButton btnCerrar = new JButton("Cerrar");

            btnAgregar.addActionListener(e -> {
                JTextField txtNombre = new JTextField();
                JTextField txtCant = new JTextField("1");
                Object[] form = {"Nombre del objeto:", txtNombre, "Cantidad:", txtCant};
                if (JOptionPane.showConfirmDialog(dialog, form, "Agregar Item", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    try {
                        String nombre = txtNombre.getText().trim();
                        int cant = Integer.parseInt(txtCant.getText().trim());
                        if (!nombre.isEmpty()) {
                            Item nuevo = new Item(nombre, cant);
                            adminCtrl.agregarItemComoAdmin(idCasilla, nuevo, idAdmin);
                            modeloLista.addElement(nuevo);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            btnQuitar.addActionListener(e -> {
                Item seleccionado = listaItems.getSelectedValue();
                if (seleccionado != null) {
                    adminCtrl.retirarItemComoAdmin(idCasilla, seleccionado.getIdItem(), idAdmin);
                    modeloLista.removeElement(seleccionado);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Seleccione un item de la lista.");
                }
            });

            btnLiberar.addActionListener(e -> {
                int resp = JOptionPane.showConfirmDialog(dialog,
                        "¿Seguro que desea liberar esta casilla y vaciar sus objetos?",
                        "Confirmar", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    adminCtrl.liberarCasilla(idCasilla, idAdmin);
                    dialog.dispose();
                    cargarVistaCasillas();
                }
            });

            btnMover.addActionListener(e -> {
                String idDestino = JOptionPane.showInputDialog(dialog, "Ingrese el ID de la casilla destino (Ej: LAB-2):");
                if (idDestino != null && !idDestino.trim().isEmpty()) {
                    try {
                        adminCtrl.moverContenidoCasilla(idCasilla, idDestino.trim(), idAdmin);
                        JOptionPane.showMessageDialog(dialog, "Contenido trasladado con éxito a " + idDestino.trim());
                        dialog.dispose();
                        cargarVistaCasillas();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            btnCerrar.addActionListener(e -> dialog.dispose());

            panelBotones.add(btnAgregar);
            panelBotones.add(btnQuitar);
            panelBotones.add(btnLiberar);
            panelBotones.add(btnMover);
            panelBotones.add(btnCerrar);

            dialog.add(panelBotones, BorderLayout.SOUTH);
        }

        dialog.setVisible(true);
    }

    private void mostrarVentanaHistorial() {
        List<RegistroHistorial> registros = adminCtrl.verHistorial();
        if (registros.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "El historial está vacío.", "Auditoría", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        DefaultListModel<String> model = new DefaultListModel<>();
        for (RegistroHistorial r : registros) {
            model.addElement(r.toString());
        }

        JList<String> listaVisual = new JList<>(model);
        listaVisual.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(listaVisual);
        scrollPane.setPreferredSize(new Dimension(680, 400));

        JOptionPane.showMessageDialog(frame, scrollPane, "Auditoría - Historial de Cambios", JOptionPane.PLAIN_MESSAGE);
    }

    public void mostrarVentana() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}