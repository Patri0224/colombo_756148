package graphics;

import bookRecommender.entita.Libri;

import javax.swing.*;
import java.awt.*;

public class Ricerca extends JPanel {
    JScrollPane scroll;
    BookRecommender gui;
    String paginaCorrente;
    int opzioneRicerca;

    public Ricerca(String paginaCorrente, JScrollPane scrollPanel, BookRecommender bookRecommender, int opzioneRicerca) {
        this.paginaCorrente = paginaCorrente;
        this.scroll = scrollPanel;
        this.gui = bookRecommender;
        this.opzioneRicerca = opzioneRicerca;
        setLayout(new GridLayout(2, 3));
    }

    public void mostraLibri(Libri[] libri) {
        for (Libri lib : libri) {
            JButton button = new JButton("Apri " + lib.getTitolo());
            button.addActionListener(e -> gui.showLibro(lib, paginaCorrente));
            scroll.add(button);
        }
    }
}
