package graphics;

import javax.swing.*;

public class PaginaLibro extends JPanel {
    private BookRecommender gui;
    public PaginaLibro() {
        gui = BookRecommender.GetInstance();
    }
}
