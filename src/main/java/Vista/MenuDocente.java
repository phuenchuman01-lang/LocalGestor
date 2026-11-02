package Vista;

import Modelo.Casilla;
import Modelo.Item;
import Modelo.RegistroHistorial;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MenuDocente {
    private final JFrame frame = new JFrame("Panel de Administración - LocalGestor");
    private final AppRouter router;
    private final JPanel panelCasillas;

    public MenuDocente(AppRouter router) {
        this.router = router;

        frame.setSize(850, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Panel de Control Admin: " + router.getAuth().getUsuarioActual().getNombre(), SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(titulo, BorderLayout.NORTH);

        panelCasillas = new JPanel(new GridLayout(0, 5, 10, 10));
        panelCasillas.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JScrollPane scrollMatrix = new JScrollPane(panelCasillas);
        scrollMatrix.setBorder(BorderFactory.createEmptyBorder());
        frame.add(scrollMatrix, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnAgregarModulo = new JButton("Agregar Módulo de Casillas");
        TemaUI.estilizarBoton(btnAgregarModulo);
        btnAgregarModulo.addActionListener(e -> dialogAgregarModulo());

        JButton btnVerHistorial = new JButton("Ver Historial de Auditoría");
        TemaUI.estilizarBoton(btnVerHistorial);
        btnVerHistorial.addActionListener(e -> mostrarVentanaHistorial());

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        TemaUI.estilizarBoton(btnCerrarSesion);
        btnCerrarSesion.addActionListener(e -> {
            router.getAuth().cerrarSesion();
            frame.dispose();
            router.irAlLogin();
        });

        panelInferior.add(btnAgregarModulo);
        panelInferior.add(btnVerHistorial);
        panelInferior.add(btnCerrarSesion);
        frame.add(panelInferior, BorderLayout.SOUTH);

        cargarVistaCasillas();
    }

    private void cargarVistaCasillas() {
        panelCasillas.removeAll();
        List<Casilla> casillas = router.getAdminCtrl().obtenerTodasLasCasillas();

        for (Casilla c : casillas) {
            JButton btnCasilla = new JButton();
            btnCasilla.setLayout(new BorderLayout());
            btnCasilla.setPreferredSize(new Dimension(130, 90));
            btnCasilla.setOpaque(true);

            JLabel lblId = new JLabel(c.getIdCasilla(), SwingConstants.CENTER);
            lblId.setFont(new Font("Arial", Font.BOLD, 14));

            JLabel lblEstado = new JLabel(
                    c.estaDisponible() ? "DISPONIBLE" : "Usuario: " + c.getIdUsuarioAsignado(),
                    SwingConstants.CENTER
            );

            if (c.estaDisponible()) {
                btnCasilla.setBackground(new Color(40, 100, 60));
                btnCasilla.setForeground(Color.WHITE);
                lblId.setForeground(Color.WHITE);
                lblEstado.setForeground(Color.WHITE);
            } else {
                btnCasilla.setBackground(new Color(120, 40, 50));
                btnCasilla.setForeground(Color.WHITE);
                lblId.setForeground(Color.WHITE);
                lblEstado.setForeground(Color.WHITE);
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
        Object[] campos = {"Prefijo del Módulo (Ej: LAB, BIO):", txtPrefijo, "Cantidad de Casillas a crear:", txtCantidad};

        int opcion = DialogoUI.mostrarFormulario(frame, "Nuevo Módulo de Casillas", campos);

        if (opcion == JOptionPane.OK_OPTION) {
            try {
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                String prefijo = txtPrefijo.getText().trim();
                router.getAdminCtrl().agregarModuloCasillas(cantidad, prefijo, router.getAuth().getUsuarioActual().getIdUsuario());
                cargarVistaCasillas();
            } catch (NumberFormatException ex) {
                DialogoUI.mostrarMensaje(frame, "La cantidad debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                DialogoUI.mostrarMensaje(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarOpcionesAdmin(String idCasilla) {
        Casilla c = router.getAdminCtrl().buscarCasillaPorId(idCasilla);
        if (c == null) return;

        String idAdmin = router.getAuth().getUsuarioActual().getIdUsuario();
        JDialog dialog = new JDialog(frame, "Gestión de Casilla " + idCasilla, true);
        dialog.setSize(460, 360);
        dialog.setLayout(new BorderLayout(5, 5));
        dialog.setLocationRelativeTo(frame);

        if (c.estaDisponible()) {
            JLabel lblInfo = new JLabel("La casilla " + idCasilla + " está actualmente disponible.", SwingConstants.CENTER);
            dialog.add(lblInfo, BorderLayout.CENTER);

            JPanel panelBotones = new JPanel();
            JButton btnAsignar = new JButton("Asignar a Usuario");
            TemaUI.estilizarBoton(btnAsignar);
            JButton btnCerrar = new JButton("Cerrar");
            TemaUI.estilizarBoton(btnCerrar);

            btnAsignar.addActionListener(e -> {
                String idUsuario = DialogoUI.mostrarInput(dialog, "Ingrese el ID del usuario a asignar:", "Asignar Casilla");
                if (idUsuario != null && !idUsuario.trim().isEmpty()) {
                    try {
                        router.getAdminCtrl().asignarCasilla(idCasilla, idUsuario.trim(), idAdmin);
                        DialogoUI.mostrarMensaje(dialog, "¡Casilla asignada exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        cargarVistaCasillas();
                    } catch (Exception ex) {
                        DialogoUI.mostrarMensaje(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            TemaUI.estilizarBoton(btnAgregar);
            JButton btnQuitar = new JButton("Retirar Item");
            TemaUI.estilizarBoton(btnQuitar);
            JButton btnLiberar = new JButton("Liberar Casilla");
            TemaUI.estilizarBoton(btnLiberar);
            JButton btnMover = new JButton("Mover Contenido");
            TemaUI.estilizarBoton(btnMover);
            JButton btnCerrar = new JButton("Cerrar");
            TemaUI.estilizarBoton(btnCerrar);

            btnAgregar.addActionListener(e -> {
                JTextField txtNombre = new JTextField();
                JTextField txtCant = new JTextField("1");
                Object[] form = {"Nombre del objeto:", txtNombre, "Cantidad:", txtCant};

                if (DialogoUI.mostrarFormulario(dialog, "Agregar Item", form) == JOptionPane.OK_OPTION) {
                    try {
                        String nombre = txtNombre.getText().trim();
                        int cant = Integer.parseInt(txtCant.getText().trim());
                        if (!nombre.isEmpty()) {
                            Item nuevo = new Item(nombre, cant);
                            router.getAdminCtrl().agregarItemComoAdmin(idCasilla, nuevo, idAdmin);
                            modeloLista.addElement(nuevo);
                        }
                    } catch (NumberFormatException ex) {
                        DialogoUI.mostrarMensaje(dialog, "Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            btnQuitar.addActionListener(e -> {
                Item seleccionado = listaItems.getSelectedValue();
                if (seleccionado != null) {
                    router.getAdminCtrl().retirarItemComoAdmin(idCasilla, seleccionado.getIdItem(), idAdmin);
                    modeloLista.removeElement(seleccionado);
                } else {
                    DialogoUI.mostrarMensaje(dialog, "Seleccione un item de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            });

            btnLiberar.addActionListener(e -> {
                int resp = DialogoUI.mostrarConfirmacion(dialog, "¿Seguro que desea liberar esta casilla y vaciar sus objetos?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    router.getAdminCtrl().liberarCasilla(idCasilla, idAdmin);
                    dialog.dispose();
                    cargarVistaCasillas();
                }
            });

            btnMover.addActionListener(e -> {
                String idDestino = DialogoUI.mostrarInput(dialog, "Ingrese el ID de la casilla destino (Ej: LAB-2):", "Mover Contenido");
                if (idDestino != null && !idDestino.trim().isEmpty()) {
                    try {
                        router.getAdminCtrl().moverContenidoCasilla(idCasilla, idDestino.trim(), idAdmin);
                        DialogoUI.mostrarMensaje(dialog, "Contenido trasladado con éxito a " + idDestino.trim(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        cargarVistaCasillas();
                    } catch (Exception ex) {
                        DialogoUI.mostrarMensaje(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        List<RegistroHistorial> registros = router.getAdminCtrl().verHistorial();
        if (registros.isEmpty()) {
            DialogoUI.mostrarMensaje(frame, "El historial está vacío.", "Auditoría", JOptionPane.INFORMATION_MESSAGE);
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

        // Usamos el nuevo método para mostrar componentes complejos
        DialogoUI.mostrarComponente(frame, "Auditoría - Historial de Cambios", scrollPane);
    }

    public void mostrarVentana() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}