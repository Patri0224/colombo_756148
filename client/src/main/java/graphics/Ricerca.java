package graphics;

import bookRecommender.entita.Libri;

import javax.swing.*;
import java.awt.*;

public class Ricerca extends JPanel {
    JPanel risultati;
    BookRecommender gui;
    String paginaCorrente;
    int opzioneRicerca;
    JTextField titolo;
    JTextField autore;
    JTextField anno;

    public Ricerca(String paginaCorrente, JPanel panel, int opzioneRicerca) {
        this.paginaCorrente = paginaCorrente;
        this.risultati = panel;
        this.gui = BookRecommender.GetInstance();
        this.opzioneRicerca = opzioneRicerca;
        JPanel spazioInterno = new JPanel();
        Config.setPanel1(spazioInterno);
        spazioInterno.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setLayout(new BorderLayout());
        spazioInterno.setLayout(new GridLayout(2, 4, 30, 0));
        Config.setPanel2(this);
        JLabel titoloL = new JLabel("Titolo");
        JLabel autoreL = new JLabel("Autore");
        JLabel annoL = new JLabel("Anno");
        titolo = new JTextField();
        autore = new JTextField();
        anno = new JTextField();
        Config.setLabel1(titoloL);
        Config.setLabel1(autoreL);
        Config.setLabel1(annoL);
        Config.setTextField1(titolo);
        Config.setTextField1(autore);
        Config.setTextField1(anno);
        JButton ricerca = new JButton("Cerca");
        Config.setButton1(ricerca);
        ricerca.addActionListener(e -> {
            cercaLibri();
        });
        spazioInterno.add(titoloL);
        spazioInterno.add(autoreL);
        spazioInterno.add(annoL);
        spazioInterno.add(new JLabel(""));
        spazioInterno.add(titolo);
        spazioInterno.add(autore);
        spazioInterno.add(anno);
        spazioInterno.add(ricerca);
        add(spazioInterno, BorderLayout.CENTER);

    }

    public void cercaLibri() {

    }

    public void mostraLibri(Libri[] libri) {
        for (Libri lib : libri) {
            JButton button = new JButton("Apri " + lib.getTitolo());
            button.addActionListener(e -> gui.showLibro(lib.getId() + ""));
            Config.setButton1(button);
            risultati.add(button);
        }
    }
}
