package graphics;

import javax.swing.*;
import java.awt.*;

public class PaginaHome extends JPanel {
    private BookRecommender gui;
    private JPanel menu;
    private JPanel main;
    private JPanel ricerca;
    private JScrollPane scroll;
    private JPanel libri;

    public PaginaHome() {
        gui = BookRecommender.GetInstance();
        setBackground(Config.COLORE_SFONDO);
        setLayout(new BorderLayout(1, 1));
        JScrollPane scrollLibrerie = new JScrollPane();
        Config.setScrollPane(scrollLibrerie);
        scrollLibrerie.setVisible(false);

        menu = new menu("Pagina Home", scrollLibrerie);
        add(menu, BorderLayout.NORTH);
        main = new JPanel();
        Config.setPanel1(main);
        main.setLayout(new BorderLayout());
        scroll = new JScrollPane();
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        Config.setScrollPane(scroll);
        libri = new JPanel();
        libri.setLayout(new BorderLayout());
        Config.setPanel1(libri);
        JViewport viewport = new JViewport();
        viewport.setView(libri);
        scroll.setViewport(viewport);
        scroll.getVerticalScrollBar().setUnitIncrement(7);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        ricerca = new Ricerca("Pagina Home", libri, 0);
        main.add(ricerca, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        add(scrollLibrerie, BorderLayout.EAST);
        add(main, BorderLayout.CENTER);
    }


}
