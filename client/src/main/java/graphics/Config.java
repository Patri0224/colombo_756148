package graphics;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Config {
    public final static Color COLORE_SFONDO = new Color(43, 43, 43);
    public final static Color COLORE_SFONDO1 = new Color(18, 18, 18);
    public final static Color COLORE_SFONDO2 = new Color(101, 101, 101);
    public final static Color COLORE_SFONDO_BOTTONE = new Color(46, 46, 76);
    public final static Color COLORE_HOVER_BOTTONE = new Color(93, 85, 158);
    public final static Font FONT = new Font("Arial", Font.PLAIN, 14);
    public final static Color COLORE_TEXT_1 = new Color(255, 255, 255);
    public final static Color COLORE_TEXT_2 = new Color(200, 200, 200);
    private final static Color COLORE_BORDO1 = new Color(71, 71, 71);
    private final static Color COLORE_BORDO2 = new Color(255, 255, 255);
    public static final Border BORDO_OP1 = BorderFactory.createLineBorder(COLORE_BORDO1);
    public static final Border BORDO_OP2 = BorderFactory.createEmptyBorder(4, 8, 4, 8);
    public static final Border BORDO_OP3 = BorderFactory.createEmptyBorder(1, 1, 1, 1);

    public static void setPanel1(JPanel panel) {
        panel.setBackground(COLORE_SFONDO);
        panel.setBorder(BORDO_OP3);
        panel.setOpaque(true);
    }

    public static void setPanel2(JPanel panel) {
        panel.setBackground(COLORE_SFONDO);
        panel.setBorder(BORDO_OP1);
        panel.setOpaque(true);
    }

    public static void setButton1(JButton button) {
        button.setBackground(COLORE_SFONDO_BOTTONE);
        button.setBorder(BORDO_OP2);
        button.setForeground(COLORE_TEXT_1);
        button.setFont(FONT);
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(COLORE_HOVER_BOTTONE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(COLORE_SFONDO_BOTTONE);
            }
        });
    }

    public static void setButton2(JButton button) {
        button.setBackground(COLORE_SFONDO_BOTTONE);
        button.setBorder(BORDO_OP2);
        button.setForeground(COLORE_TEXT_2);
        button.setFont(FONT);
    }

    public static void setTextField1(JTextField textField) {
        textField.setBackground(COLORE_SFONDO1);
        textField.setBorder(BORDO_OP1);
        textField.setForeground(COLORE_TEXT_1);
        textField.setCaretColor(COLORE_TEXT_1);
        textField.setFont(FONT);
    }

    public static void setTextField2(JTextField textField) {
        textField.setBackground(COLORE_SFONDO1);
        textField.setBorder(BORDO_OP2);
        textField.setForeground(COLORE_TEXT_2);
    }

    public static void setLabel1(JLabel label) {
        label.setForeground(COLORE_TEXT_1);
        label.setFont(FONT);
    }

    public static void setScrollPane(JScrollPane scrollPane) {
        scrollPane.setBackground(COLORE_SFONDO);
        scrollPane.setOpaque(true);
        scrollPane.setBorder(BORDO_OP1);
    }

    public static void setTextArea1(JTextArea textArea) {
        textArea.setBackground(COLORE_SFONDO);
        textArea.setBorder(BORDO_OP3);
        textArea.setForeground(COLORE_TEXT_1);
        textArea.setCaretColor(COLORE_TEXT_1);
        textArea.setFont(FONT);
        textArea.setOpaque(true);
    }

    public static void setTextArea2(JTextArea textArea) {
        textArea.setBackground(COLORE_SFONDO2);
        textArea.setBorder(BORDO_OP3);
        textArea.setForeground(COLORE_TEXT_1);
        textArea.setCaretColor(COLORE_TEXT_1);
        textArea.setFont(FONT);
        textArea.setOpaque(true);
    }
}
