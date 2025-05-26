package graphics;

import bookRecommender.LibrerieGestore;
import bookRecommender.UtenteGestore;

import javax.swing.*;
import java.awt.*;

public class AggiungiConsiglio extends JPanel {
    public int idLibro;
    private JScrollPane scroll;

    public AggiungiConsiglio(int idLibro) {
        this.reload(idLibro);
    }

    public void reload(int idLibro) {
        removeAll();
        if (idLibro == -1) {
            return;
        }
        this.idLibro = idLibro;
        setLayout(new BorderLayout(5, 5));
        JPanel menuRidotto = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Config.setPanel1(menuRidotto);
        JLabel t = new JLabel("Aggiungi consiglio");
        Config.setLabel1(t);
        menuRidotto.add(ComandoIndietro.getBottoneHome());
        menuRidotto.add(ComandoIndietro.getBottoneIndietro());
        menuRidotto.add(t);
        add(menuRidotto, BorderLayout.NORTH);


        JPanel main= new JPanel(new BorderLayout(5, 5));
        Config.setPanel1(main);
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
        main.add(ric, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);

    }


}
