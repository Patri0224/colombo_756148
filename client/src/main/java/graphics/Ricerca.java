package graphics;

import bookRecommender.LibriRicercaGestore;
import bookRecommender.eccezioni.Eccezione;
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
            if (opzioneRicerca == 0)
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
        String titoloRicerca = titolo.getText().trim();
        String autoreRicerca = autore.getText().trim();
        String annoRicerca = anno.getText().trim();
        int annoR = -1;
        if (annoRicerca.equals("")) {
            annoR = -1; // Se l'anno non è specificato, lo consideriamo come 0
        } else if (!annoRicerca.matches("\\d+")) {
            anno.setText("valore non valido");
            annoR = -1;
            return;
        } else {
            annoR = Integer.parseInt(annoRicerca);
        }

        LibriRicercaGestore ricerca = LibriRicercaGestore.GetInstance();
        Libri[] libri = null;
        Eccezione ecc = ricerca.RicercaLibri(titoloRicerca, autoreRicerca, annoR);
        if (ecc.getErrorCode() == 0) {
            libri = ricerca.GetLibri();
            risultati.removeAll();
            if (libri.length == 0) {
                JLabel label = new JLabel("Nessun libro trovato");
                Config.setLabel1(label);
                risultati.add(label, BorderLayout.NORTH);
            } else {
                mostraLibri(libri);
            }
        } else {
            JLabel label = new JLabel(ecc.getErrorCode() + ecc.getMessage());
            Config.setLabel1(label);
            risultati.add(label, BorderLayout.NORTH);
        }
        risultati.revalidate();
        risultati.repaint();
    }

    public void mostraLibri(Libri[] libri) {
        JPanel spazioInterno = new JPanel();
        Config.setPanel1(spazioInterno);
        spazioInterno.setLayout(new GridLayout(libri.length, 1, 0, 5));

        for (Libri lib : libri) {
            JPanel libroPanel = new JPanel(new BorderLayout());
            libroPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Margine interno
            Config.setPanel1(libroPanel);
            JLabel titoloLabel = new JLabel(lib.getTitolo());
            Config.setLabel1(titoloLabel);
            libroPanel.add(titoloLabel, BorderLayout.CENTER);

            JButton button = new JButton("Apri");
            button.addActionListener(e -> gui.showLibro(lib.getId() + ""));
            Config.setButton1(button);
            libroPanel.add(button, BorderLayout.WEST);

            spazioInterno.add(libroPanel);
        }
        risultati.add(spazioInterno, BorderLayout.WEST);
    }
}
