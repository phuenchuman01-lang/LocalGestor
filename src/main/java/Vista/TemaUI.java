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

            UIManager.put("Panel.background", new ColorUIResource(FONDO_PRINCIPAL));
            UIManager.put("OptionPane.background", new ColorUIResource(FONDO_PRINCIPAL));
            UIManager.put("OptionPane.messageForeground", new ColorUIResource(TEXTO_CLARO));
            UIManager.put("Label.foreground", new ColorUIResource(TEXTO_CLARO));

            // Botones: Eliminando el degradado nativo y forzando color sólido
            UIManager.put("Button.background", new ColorUIResource(AZUL_OSCURO));
            UIManager.put("Button.foreground", new ColorUIResource(TEXTO_CLARO));
            UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
            UIManager.put("Button.contentAreaFilled", false); // Clave para quitar el degradado
            UIManager.put("Button.setOpaque", true);

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
            UIManager.put("TextArea.background", new ColorUIResource(FONDO_INPUT));
            UIManager.put("TextArea.foreground", new ColorUIResource(TEXTO_CLARO));
            UIManager.put("ScrollPane.background", new ColorUIResource(FONDO_PRINCIPAL));
            UIManager.put("ScrollPane.border", new LineBorder(AZUL_CLARO_BORDE, 1));

        } catch (Exception e) {
            System.err.println("Error al aplicar TemaUI: " + e.getMessage());
        }
    }
}