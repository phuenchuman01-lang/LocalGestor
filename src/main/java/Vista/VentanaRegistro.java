package Vista;

import Modelo.RolUsuario;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistro {
    private final JFrame frame;
    private final AppRouter router;
    private final JTextField txtIdUsuario;
    private final JTextField txtNombre;
    private final JPasswordField txtClave;

    public VentanaRegistro(AppRouter router) {
        this.router = router;

        frame = new JFrame("LocalGestor - Registro de Usuario");
        frame.setSize(420, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.add(new JLabel("ID Usuario (Username):"));
        txtIdUsuario = new JTextField(15);
        panel.add(txtIdUsuario);

        panel.add(new JLabel("Nombre Completo:"));
        txtNombre = new JTextField(15);
        panel.add(txtNombre);

        panel.add(new JLabel("Contraseña:"));
        txtClave = new JPasswordField(15);
        panel.add(txtClave);

        JButton btnVolver = new JButton("Volver");
        TemaUI.estilizarBoton(btnVolver);
        panel.add(btnVolver);

        JButton btnRegistrar = new JButton("Registrar");
        TemaUI.estilizarBoton(btnRegistrar);
        panel.add(btnRegistrar);

        frame.add(panel);

        btnVolver.addActionListener(e -> {
            frame.dispose();
            router.irAlLogin();
        });

        btnRegistrar.addActionListener(e -> intentarRegistro());
    }

    public void mostrarVentana() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void intentarRegistro() {
        try {
            String id = txtIdUsuario.getText();
            String nombre = txtNombre.getText();
            String pass = new String(txtClave.getPassword());

            router.getAuth().registrarUsuario(id, nombre, pass, RolUsuario.USUARIO);
            JOptionPane.showMessageDialog(frame, "¡Usuario registrado exitosamente!");
            frame.dispose();
            router.irAlLogin();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error de Registro", JOptionPane.ERROR_MESSAGE);
        }
    }
}