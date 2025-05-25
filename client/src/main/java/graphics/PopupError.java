package graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PopupError extends JFrame {
    public PopupError(String str) {
        setSize(200, 200);
        setAlwaysOnTop(true);

        // Posiziona al centro dello schermo
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
        JTextArea textArea = new JTextArea(str);
        Config.setTextArea2(textArea);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        add(textArea);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                    setVisible(false);
                    dispose();
                }
            }
        });
        addWindowFocusListener(new java.awt.event.WindowAdapter() {
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                requestFocusInWindow();
            }
        });

        // Mostra il popup
        setVisible(true);
        requestFocus();
        setVisible(true);
    }

    public static void mostraErrore(String messaggio) {
        SwingUtilities.invokeLater(() -> new PopupError(messaggio));
    }
}
