package Vista;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin {
    private final JFrame frame;
    private final AppRouter router;
    private final JTextField txtUsuario;
    private final JPasswordField txtClave;

    public VentanaLogin(AppRouter router) {
        this.router = router;

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
        TemaUI.estilizarBoton(btnRegistrar);
        panelLogin.add(btnRegistrar);

        JButton btnIngresar = new JButton("Ingresar");
        TemaUI.estilizarBoton(btnIngresar);
        panelLogin.add(btnIngresar);

        frame.add(panelLogin);

        btnIngresar.addActionListener(e -> intentarLogin());
        btnRegistrar.addActionListener(e -> {
            frame.dispose();
            router.irAlRegistro();
        });
    }

    public void mostrarVentana() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void intentarLogin() {
        String user = txtUsuario.getText();
        String password = new String(txtClave.getPassword());

        if (router.getAuth().iniciarSesion(user, password)) {
            frame.dispose();
            router.irAlMenuPrincipal();
        } else {
            DialogoUI.mostrarMensaje(frame, "Usuario o contraseña incorrectos.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            txtClave.setText("");
        }
    }
}