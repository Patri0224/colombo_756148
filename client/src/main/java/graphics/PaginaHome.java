package graphics;

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

    public PaginaHome() {
        gui = BookRecommender.GetInstance();
        setLayout(new BorderLayout(1, 1));
        menu = new menu("Pagina Home",librerie);
        add(menu, BorderLayout.NORTH);
        main = new JPanel();
        main.setLayout(new BorderLayout());
        ricerca = new Ricerca("Pagina Home",scroll,0);
        main.add(ricerca, BorderLayout.NORTH);
        scroll = new JScrollPane();

        main.add(scroll, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }


}
