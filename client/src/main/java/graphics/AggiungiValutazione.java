package graphics;

import javax.swing.*;

public class AggiungiValutazione extends JPanel {
    private BookRecommender gui;

    public AggiungiValutazione() {
        reload();
    }
    public void reload(){
        gui = BookRecommender.GetInstance();
    }
}
