package graphics;

import javax.swing.*;
import java.awt.*;

public class PaginaLibro extends JPanel {
    private BookRecommender gui;
    private JPanel menu;
    public PaginaLibro() {
        gui = BookRecommender.GetInstance();
        setBackground(Config.COLORE_SFONDO);
        setLayout(new BorderLayout(1, 1));
        JScrollPane scrollLibrerie = new JScrollPane();
        Config.setScrollPane(scrollLibrerie);
        scrollLibrerie.setVisible(false);

        menu = new menu("Pagina Libro", scrollLibrerie);
        add(menu, BorderLayout.NORTH);
    }
}
