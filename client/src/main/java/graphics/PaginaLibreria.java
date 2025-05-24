package graphics;

import javax.swing.*;

public class PaginaLibreria extends JPanel {
    private BookRecommender gui;
    public PaginaLibreria() {
        gui = BookRecommender.GetInstance();
    }
}
