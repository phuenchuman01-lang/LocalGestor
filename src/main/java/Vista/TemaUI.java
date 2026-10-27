package Vista;

import javax.swing.*;
import java.awt.*;

public class TemaUI {
    public static final Color FONDO_PRINCIPAL = new Color(43, 43, 54);
    public static final Color AZUL_OSCURO = new Color(55, 55, 75);
    public static final Color TEXTO_CLARO = new Color(230, 230, 230);
    public static final Color FONDO_INPUT = new Color(30, 30, 40);

    public static void aplicarTemaGlobal() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            // Paneles y fondos
            UIManager.put("Panel.background", FONDO_PRINCIPAL);
            UIManager.put("OptionPane.background", FONDO_PRINCIPAL);
            UIManager.put("OptionPane.messageForeground", TEXTO_CLARO);

            // Textos y etiquetas
            UIManager.put("Label.foreground", TEXTO_CLARO);

            // Botones
            UIManager.put("Button.background", AZUL_OSCURO);
            UIManager.put("Button.foreground", TEXTO_CLARO);
            UIManager.put("Button.focus", new Color(0, 0, 0, 0)); // Elimina el feo recuadro al hacer clic

            // Entradas de texto
            UIManager.put("TextField.background", FONDO_INPUT);
            UIManager.put("TextField.foreground", TEXTO_CLARO);
            UIManager.put("TextField.caretForeground", TEXTO_CLARO); // Color del cursor parpadeante

            UIManager.put("PasswordField.background", FONDO_INPUT);
            UIManager.put("PasswordField.foreground", TEXTO_CLARO);
            UIManager.put("PasswordField.caretForeground", TEXTO_CLARO);

            // Listas y Scroll
            UIManager.put("List.background", FONDO_INPUT);
            UIManager.put("List.foreground", TEXTO_CLARO);
            UIManager.put("ScrollPane.background", FONDO_PRINCIPAL);
            UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());

        } catch (Exception e) {
            System.err.println("No se pudo aplicar el tema personalizado.");
        }
    }
}