package graphics;

import bookRecommender.LibrerieGestore;
import bookRecommender.UtenteGestore;

import javax.swing.*;
import java.awt.*;

public class AggiungiConsiglio extends JFrame {
    public int idLibro;
    private JScrollPane scroll;

    public AggiungiConsiglio(int idLibro) {
        this.idLibro = idLibro;

        JFrame aggiungi = new JFrame("Aggiungi consiglio a " + idLibro);
        aggiungi.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // chiusura finestra

        JPanel panel = new JPanel(new BorderLayout(5, 5));

        LibrerieGestore librerieGestore = LibrerieGestore.GetInstance();
        UtenteGestore g = UtenteGestore.GetInstance();

        JPanel p1 = new JPanel();
        JPanel ric = new Ricerca(p1, 1, this);
        scroll = new JScrollPane();
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        Config.setScrollPane(scroll);
        Config.setPanel1(p1);
        JViewport viewport = new JViewport();
        viewport.setView(p1);
        scroll.setViewport(viewport);
        scroll.getVerticalScrollBar().setUnitIncrement(7);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        p1.setLayout(new BorderLayout());
        panel.add(ric, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        aggiungi.setContentPane(panel);
        aggiungi.setSize(500, 400); // oppure aggiungi.pack();
        aggiungi.setLocationRelativeTo(null); // centra sullo schermo
        aggiungi.setVisible(true);
    }


}
