package Vista;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

public class TemaUI {
    public static final Color FONDO_PRINCIPAL = new Color(43, 43, 55);
    public static final Color AZUL_OSCURO = new Color(65, 75, 105);
    public static final Color AZUL_CLARO_BORDE = new Color(100, 120, 160);
    public static final Color TEXTO_CLARO = new Color(230, 230, 230);
    public static final Color FONDO_INPUT = new Color(55, 55, 70);

    public static void aplicarTemaGlobal() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("swing.boldMetal", Boolean.FALSE);

            UIManager.put("Panel.background", new ColorUIResource(FONDO_PRINCIPAL));
            UIManager.put("OptionPane.background", new ColorUIResource(FONDO_PRINCIPAL));
            UIManager.put("OptionPane.messageForeground", new ColorUIResource(TEXTO_CLARO));
            UIManager.put("Label.foreground", new ColorUIResource(TEXTO_CLARO));

            UIManager.put("Button.gradient", null);
            UIManager.put("Button.rollover", Boolean.FALSE);
            UIManager.put("Button.select", new ColorUIResource(AZUL_OSCURO.darker()));
            UIManager.put("Button.background", new ColorUIResource(AZUL_OSCURO));
            UIManager.put("Button.foreground", new ColorUIResource(TEXTO_CLARO));
            UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
            UIManager.put("Button.border", BorderFactory.createCompoundBorder(
                    new LineBorder(AZUL_CLARO_BORDE, 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
            ));

            UIManager.put("TextField.background", new ColorUIResource(FONDO_INPUT));
            UIManager.put("TextField.foreground", new ColorUIResource(TEXTO_CLARO));
            UIManager.put("TextField.caretForeground", new ColorUIResource(TEXTO_CLARO));
            UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
                    new LineBorder(AZUL_CLARO_BORDE, 1),
                    BorderFactory.createEmptyBorder(4, 8, 4, 8)
            ));

            UIManager.put("PasswordField.background", new ColorUIResource(FONDO_INPUT));
            UIManager.put("PasswordField.foreground", new ColorUIResource(TEXTO_CLARO));
            UIManager.put("PasswordField.caretForeground", new ColorUIResource(TEXTO_CLARO));
            UIManager.put("PasswordField.border", BorderFactory.createCompoundBorder(
                    new LineBorder(AZUL_CLARO_BORDE, 1),
                    BorderFactory.createEmptyBorder(4, 8, 4, 8)
            ));

            UIManager.put("List.background", new ColorUIResource(FONDO_INPUT));
            UIManager.put("List.foreground", new ColorUIResource(TEXTO_CLARO));
            UIManager.put("ScrollPane.background", new ColorUIResource(FONDO_PRINCIPAL));
            UIManager.put("ScrollPane.border", new LineBorder(AZUL_CLARO_BORDE, 1));
        } catch (Exception e) {
            System.err.println("Error al aplicar TemaUI: " + e.getMessage());
        }
    }

    // NUEVO: Método centralizado para botones
    public static void estilizarBoton(JButton btn) {
        btn.setOpaque(true);
        btn.setBackground(AZUL_OSCURO);
        btn.setForeground(TEXTO_CLARO);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}