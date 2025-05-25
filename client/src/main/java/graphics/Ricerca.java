package graphics;

import bookRecommender.LibrerieGestore;
import bookRecommender.LibriRicercaGestore;
import bookRecommender.eccezioni.Eccezione;
import bookRecommender.entita.Libri;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Ricerca extends JPanel {
    JPanel risultati;
    BookRecommender gui;
    String paginaCorrente;
    int opzioneRicerca;
    JTextField titolo;
    JTextField autore;
    JTextField anno;
    JPanel spazioInterno;
    JTextArea[] textAreas = new JTextArea[0];

    public Ricerca(String paginaCorrente, JPanel panel, int opzioneRicerca) {
        this.paginaCorrente = paginaCorrente;
        this.risultati = panel;
        this.gui = BookRecommender.GetInstance();
        this.opzioneRicerca = opzioneRicerca;
        spazioInterno = new JPanel();
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
        titolo.addActionListener(e -> cercaLibri());
        autore.addActionListener(e -> cercaLibri());
        anno.addActionListener(e -> cercaLibri());
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
        spazioInterno.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int larghezza = spazioInterno.getWidth();
                System.out.println("Spazio interno larghezza: " + larghezza);
                for (JTextArea area : textAreas) {
                    Dimension nuovaDimensione = new Dimension(larghezza - 20, area.getPreferredSize().height);
                    area.setMaximumSize(nuovaDimensione);
                    area.setPreferredSize(nuovaDimensione);
                    area.setSize(nuovaDimensione);
                    area.revalidate();
                }
            }

            @Override
            public void componentShown(ComponentEvent e) {
                componentResized(e);
            }
        });

    }

    public void cercaLibri() {
        String titoloRicerca = titolo.getText().trim();
        String autoreRicerca = autore.getText().trim();
        String annoRicerca = anno.getText().trim();
        if (titoloRicerca.length() < 3 && autoreRicerca.length() < 3 && annoRicerca.length() < 4) {
            return;
        }
        int annoR;
        if (annoRicerca.isEmpty()) {
            annoR = -1; // Se il campo anno è vuoto, non filtrare per anno
        } else if (!annoRicerca.matches("\\d+")) {
            anno.setText("valore non valido");
            return;
        } else {
            annoR = Integer.parseInt(annoRicerca);
        }
        Libri[] libri = new Libri[0];
        if (opzioneRicerca == 0) {
            LibriRicercaGestore ricerca = LibriRicercaGestore.GetInstance();
            Eccezione ecc = ricerca.RicercaLibri(titoloRicerca, autoreRicerca, annoR);
            if (ecc.getErrorCode() > 0) {
                JLabel label = new JLabel(ecc.getErrorCode() + ecc.getMessage());
                Config.setLabel1(label);
                risultati.add(label, BorderLayout.NORTH);
            }
            libri = ricerca.GetLibri();
            risultati.removeAll();
        } else if (opzioneRicerca == 1) {
            LibrerieGestore ricerca = LibrerieGestore.GetInstance();
            try {
                libri = ricerca.GetLibriDaTutteLibrerie(titoloRicerca, autoreRicerca, annoR);
            } catch (Exception e) {
                JLabel label = new JLabel(e.getMessage());
                Config.setLabel1(label);
                risultati.add(label, BorderLayout.NORTH);
            }
        }
        if (libri.length == 0) {
            JLabel label = new JLabel("Nessun libro trovato");
            Config.setLabel1(label);
            risultati.add(label, BorderLayout.NORTH);
        } else {
            mostraLibri(libri);
        }


        risultati.revalidate();
        risultati.repaint();

    }


    public void mostraLibri(Libri[] libri) {
        JPanel interno = new JPanel();
        Config.setPanel1(interno);
        interno.setLayout(new GridLayout(libri.length, 1, 0, 5));
        interno.setBorder(Config.BORDO_OP2);
        textAreas = new JTextArea[libri.length];

        int i = 0;
        for (Libri lib : libri) {

            JTextArea textArea = new JTextArea(lib.getTitolo() + "\nDi " + lib.getAutori() + " (" + lib.getAnnoPubblicazione() + ")");
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setOpaque(false); // Sfondo trasparente
            textArea.setBorder(null);  // Nessun bordo
            textArea.setFocusable(false); // Come JLabel
            Config.setTextArea1(textArea); // Se usi questa funzione anche per JTextArea
            textArea.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    gui.showLibro(lib.getId() + "");
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    textArea.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    Config.setTextArea2(textArea);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    textArea.setCursor(Cursor.getDefaultCursor());
                    Config.setTextArea1(textArea);
                }
            });
            textAreas[i] = textArea;


            interno.add(textArea);
            i++;
        }
        risultati.add(interno, BorderLayout.CENTER);
    }

}
