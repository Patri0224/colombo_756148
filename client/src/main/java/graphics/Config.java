package graphics;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Config {
    public final static Color COLORE_SFONDO = new Color(43, 43, 43);
    public final static Color COLORE_SFONDO_BOTTONE = new Color(50, 50, 50);
    public final static Color COLORE_HOVER_BOTTONE = new Color(80, 80, 80);
    public final static Color COLORE_TEXT_1 = new Color(255, 255, 255);
    public final static Color COLORE_TEXT_2 = new Color(200, 200, 200);
    private final static Color COLORE_BORDO1 = new Color(255, 255, 255);
    private final static Color COLORE_BORDO2 = new Color(255, 255, 255);
    public static final Border BORDO_OP1 = BorderFactory.createLineBorder(COLORE_BORDO1);
    public static final Border BORDO_OP2 = BorderFactory.createLineBorder(COLORE_BORDO2, 0, false);

    public static void setPanel1(JPanel panel) {
        panel.setBackground(COLORE_SFONDO);
        panel.setBorder(BORDO_OP1);
    }

    public static void setPanel2(JPanel panel) {
        panel.setBackground(COLORE_SFONDO);
        panel.setBorder(BORDO_OP2);
    }

    public static void setButton1(JButton button) {
        button.setBackground(COLORE_SFONDO_BOTTONE);
        button.setBorder(BORDO_OP1);
        button.setForeground(COLORE_TEXT_1);
    }

    public static void setButton2(JButton button) {
        button.setBackground(COLORE_SFONDO_BOTTONE);
        button.setBorder(BORDO_OP2);
        button.setForeground(COLORE_TEXT_2);
    }

    public static void setTextField1(JTextField textField) {
        textField.setBackground(COLORE_SFONDO_BOTTONE);
        textField.setBorder(BORDO_OP1);
        textField.setForeground(COLORE_TEXT_1);
    }

    public static void setTextField2(JTextField textField) {
        textField.setBackground(COLORE_SFONDO_BOTTONE);
        textField.setBorder(BORDO_OP2);
        textField.setForeground(COLORE_TEXT_2);
    }

}
