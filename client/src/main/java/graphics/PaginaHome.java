package graphics;

import bookRecommender.entita.Libri;

import javax.swing.*;
import java.awt.*;

public class PaginaHome extends JPanel {
    private BookRecommender gui;
    private JPanel menu;
    private JPanel main;
    private JPanel ricerca;
    private JScrollPane scroll;
    private String paginaPrecedente;
    private JScrollPane librerie;

    public PaginaHome(BookRecommender bookRecommender) {
        gui = bookRecommender;
        setLayout(new BorderLayout(1, 1));
        menu = new menu("Pagina Home", paginaPrecedente,librerie, bookRecommender);
        add(menu, BorderLayout.NORTH);
        main = new JPanel();
        main.setLayout(new BorderLayout());
        /*ricerca = new Ricerca("Pagina Home",scroll, bookRecommender);*/
        main.add(ricerca, BorderLayout.NORTH);
        scroll = new JScrollPane();

        main.add(scroll, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }


}
