package Vista;

import javax.swing.*;
import java.awt.*;

public class DialogoUI {

    // Para mensajes de información, error o advertencia
    public static void mostrarMensaje(Component padre, String mensaje, String titulo, int tipoMensaje) {
        JOptionPane pane = new JOptionPane(mensaje, tipoMensaje);
        JDialog dialog = pane.createDialog(padre, titulo);
        estilizarComponentes(dialog.getContentPane());
        dialog.setVisible(true);
    }

    // Para pedir un texto simple
    public static String mostrarInput(Component padre, String mensaje, String titulo) {
        JTextField txt = new JTextField(15);
        Object[] msg = {mensaje, txt};
        JOptionPane pane = new JOptionPane(msg, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = pane.createDialog(padre, titulo);
        estilizarComponentes(dialog.getContentPane());
        dialog.setVisible(true);

        if (pane.getValue() != null && (int) pane.getValue() == JOptionPane.OK_OPTION) {
            return txt.getText();
        }
        return null;
    }

    // Para confirmaciones (¿Estás seguro?)
    public static int mostrarConfirmacion(Component padre, String mensaje, String titulo, int opcionTipo) {
        JOptionPane pane = new JOptionPane(mensaje, JOptionPane.QUESTION_MESSAGE, opcionTipo);
        JDialog dialog = pane.createDialog(padre, titulo);
        estilizarComponentes(dialog.getContentPane());
        dialog.setVisible(true);

        if (pane.getValue() != null) {
            return (int) pane.getValue();
        }
        return JOptionPane.CLOSED_OPTION;
    }

    // Para listas desplegables
    public static String mostrarCombo(Component padre, String mensaje, String titulo, Object[] opciones) {
        JComboBox<Object> combo = new JComboBox<>(opciones);
        combo.setBackground(TemaUI.FONDO_INPUT);
        combo.setForeground(TemaUI.TEXTO_CLARO);
        combo.setBorder(BorderFactory.createLineBorder(TemaUI.AZUL_CLARO_BORDE));

        Object[] msg = {mensaje, combo};
        JOptionPane pane = new JOptionPane(msg, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = pane.createDialog(padre, titulo);
        estilizarComponentes(dialog.getContentPane());
        dialog.setVisible(true);

        if (pane.getValue() != null && (int) pane.getValue() == JOptionPane.OK_OPTION) {
            return (String) combo.getSelectedItem();
        }
        return null;
    }

    // NUEVO: Para formularios con múltiples campos (Object[])
    public static int mostrarFormulario(Component padre, String titulo, Object[] campos) {
        JOptionPane pane = new JOptionPane(campos, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = pane.createDialog(padre, titulo);
        estilizarComponentes(dialog.getContentPane());
        dialog.setVisible(true);

        if (pane.getValue() != null && (int) pane.getValue() == JOptionPane.OK_OPTION) {
            return JOptionPane.OK_OPTION;
        }
        return JOptionPane.CANCEL_OPTION;
    }

    // NUEVO: Para mostrar componentes complejos (como un JScrollPane con el historial)
    public static void mostrarComponente(Component padre, String titulo, Component componente) {
        JOptionPane pane = new JOptionPane(componente, JOptionPane.PLAIN_MESSAGE);
        JDialog dialog = pane.createDialog(padre, titulo);
        estilizarComponentes(dialog.getContentPane());
        dialog.setVisible(true);
    }

    // El "Escáner" que encuentra los botones rebeldes y los estiliza
    private static void estilizarComponentes(Component comp) {
        if (comp instanceof JButton) {
            TemaUI.estilizarBoton((JButton) comp);
        } else if (comp instanceof Container) {
            for (Component hijo : ((Container) comp).getComponents()) {
                estilizarComponentes(hijo);
            }
        }
    }
}