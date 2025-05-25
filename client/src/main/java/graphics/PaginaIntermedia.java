package graphics;

import javax.swing.*;

public class PaginaIntermedia extends JPanel {
    public PaginaIntermedia() {
        reload();
    }
    public void reload() {
        removeAll();
        Config.setPanel1(this);
        add(new JLabel("Pagina Intermedia, questa pagina non dovrebbe essere visibile"));
        revalidate();
        repaint();
    }
}
