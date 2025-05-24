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
        ricerca = new Ricerca("Pagina Home", libri, 0);
        main.add(ricerca, BorderLayout.NORTH);
        scroll = new JScrollPane();
        Config.setScrollPane(scroll);
        libri = new JPanel();
        Config.setPanel1(libri);
        scroll.setViewportView(libri);
        main.add(scroll, BorderLayout.CENTER);
        add(scrollLibrerie, BorderLayout.EAST);
        add(main, BorderLayout.CENTER);
    }


}
