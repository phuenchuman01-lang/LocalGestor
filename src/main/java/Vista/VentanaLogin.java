package Vista;

import Controlador.AdminController;
import Controlador.AuthController;
import Controlador.UserController;
import Modelo.RolUsuario;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin {
    private final JFrame frame;
    private final AuthController auth;
    private final AdminController adminCtrl;
    private final UserController userCtrl;

    private final JTextField txtUsuario;
    private final JPasswordField txtClave;

    public VentanaLogin(AuthController auth, AdminController adminCtrl, UserController userCtrl) {
        this.auth = auth;
        this.adminCtrl = adminCtrl;
        this.userCtrl = userCtrl;

        frame = new JFrame("LocalGestor - Inicio de Sesión");
        frame.setSize(450, 320);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        JPanel panelLogin = new JPanel(new GridLayout(4, 2, 10, 15));
        panelLogin.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelLogin.add(new JLabel("ID Usuario:"));
        txtUsuario = new JTextField(15);
        panelLogin.add(txtUsuario);

        panelLogin.add(new JLabel("Contraseña:"));
        txtClave = new JPasswordField(15);
        panelLogin.add(txtClave);

        JButton btnRegistrar = new JButton("Crear Usuario");
        panelLogin.add(btnRegistrar);

        JButton btnIngresar = new JButton("Ingresar");
        panelLogin.add(btnIngresar);

        frame.add(panelLogin);

        btnIngresar.addActionListener(e -> intentarLogin());
        btnRegistrar.addActionListener(e -> {
            frame.dispose();
            new VentanaRegistro(auth, adminCtrl, userCtrl).mostrarVentana();
        });
    }

    public void mostrarVentana() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void intentarLogin() {
        String user = txtUsuario.getText();
        String password = new String(txtClave.getPassword());

        try {
            auth.iniciarSesion(user, password);
            frame.dispose();

            if (auth.getUsuarioActual().getRol() == RolUsuario.ADMIN) {
                new MenuDocente(auth, adminCtrl, userCtrl).mostrarVentana();
            } else {
                new MenuEstudiante(auth, adminCtrl, userCtrl).mostrarVentana();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            txtClave.setText("");
        }
    }
}