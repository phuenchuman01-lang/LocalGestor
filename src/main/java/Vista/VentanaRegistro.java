package Vista;

import Controlador.AdminController;
import Controlador.AuthController;
import Controlador.UserController;
import Modelo.RolUsuario;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistro {
    private final JFrame frame;
    private final AuthController auth;
    private final AdminController adminCtrl;
    private final UserController userCtrl;

    private final JTextField txtIdUsuario;
    private final JTextField txtNombre;
    private final JPasswordField txtClave;

    public VentanaRegistro(AuthController auth, AdminController adminCtrl, UserController userCtrl) {
        this.auth = auth;
        this.adminCtrl = adminCtrl;
        this.userCtrl = userCtrl;

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
        btnVolver.setOpaque(true);
        btnVolver.setBackground(TemaUI.AZUL_OSCURO);
        panel.add(btnVolver);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setOpaque(true);
        btnRegistrar.setBackground(TemaUI.AZUL_OSCURO);
        panel.add(btnRegistrar);

        frame.add(panel);

        btnVolver.addActionListener(e -> volverAlLogin());
        btnRegistrar.addActionListener(e -> intentarRegistro());
    }

    public void mostrarVentana() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void volverAlLogin() {
        frame.dispose();
        new VentanaLogin(auth, adminCtrl, userCtrl).mostrarVentana();
    }

    private void intentarRegistro() {
        try {
            String id = txtIdUsuario.getText();
            String nombre = txtNombre.getText();
            String pass = new String(txtClave.getPassword());

            auth.registrarUsuario(id, nombre, pass, RolUsuario.USUARIO);
            JOptionPane.showMessageDialog(frame, "¡Usuario registrado exitosamente!");
            volverAlLogin();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error de Registro", JOptionPane.ERROR_MESSAGE);
        }
    }
}