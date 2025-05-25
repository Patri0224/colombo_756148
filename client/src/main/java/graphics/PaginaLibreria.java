package graphics;

import bookRecommender.LibrerieGestore;
import bookRecommender.eccezioni.Eccezione;
import bookRecommender.entita.Libri;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PaginaLibreria extends JPanel {
    String nomeLibreria;
    private BookRecommender gui;
    private JPanel menu;
    private JPanel main;
    private JScrollPane scroll;
    private JPanel listaLibri;
    private JScrollPane scrollLibrerie;
    private JTextArea[] textAreas = new JTextArea[0];
    private JPanel info;

    public PaginaLibreria() {
        setLibreria("");

    }

    public void setLibreria(String nomeLibreria) {
        removeAll();
        this.nomeLibreria = nomeLibreria;
        gui = BookRecommender.GetInstance();
        setBackground(Config.COLORE_SFONDO);
        setLayout(new BorderLayout(1, 1));
        scrollLibrerie = new JScrollPane();
        Config.setScrollPane(scrollLibrerie);
        scrollLibrerie.setVisible(false);

        menu = new menu("Pagina Libreria: "+nomeLibreria, scrollLibrerie);
        add(menu, BorderLayout.NORTH);
        main = new JPanel();
        Config.setPanel1(main);
        main.setLayout(new BorderLayout());
        scroll = new JScrollPane();
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        Config.setScrollPane(scroll);
        listaLibri = new JPanel();
        listaLibri.setLayout(new BorderLayout());
        Config.setPanel1(listaLibri);
        JViewport viewport = new JViewport();
        viewport.setView(listaLibri);
        scroll.setViewport(viewport);
        scroll.getVerticalScrollBar().setUnitIncrement(7);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        info = new JPanel();
        Config.setPanel1(info);
        info.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel lblLibri = new JLabel("Libri presenti nella libreria: " + nomeLibreria);
        Config.setLabel1(lblLibri);
        info.add(lblLibri);
        main.add(info, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        add(scrollLibrerie, BorderLayout.EAST);
        add(main, BorderLayout.CENTER);
        info.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int larghezza = info.getWidth();
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
        LibrerieGestore libG = LibrerieGestore.GetInstance();
        if (!nomeLibreria.isEmpty() && libG.ControlloEsisteLibreria(nomeLibreria)) {

            cercaLibriInLibreria();
        }

    }

    public void cercaLibriInLibreria() {
        Libri[] libri = new Libri[0];
        LibrerieGestore ricerca = LibrerieGestore.GetInstance();
        Eccezione ecc = ricerca.caricaLibrerie();
        if (ecc.getErrorCode() > 0) {
            JLabel label = new JLabel(ecc.getErrorCode() + ecc.getMessage());
            Config.setLabel1(label);
            listaLibri.add(label, BorderLayout.NORTH);
            listaLibri.revalidate();
            listaLibri.repaint();
            return;
        }
        libri = ricerca.GetLibriDaLibreria(nomeLibreria);

        listaLibri.removeAll();

        if (libri.length == 0) {
            JLabel label = new JLabel("Nessun libro trovato");
            Config.setLabel1(label);
            listaLibri.add(label, BorderLayout.NORTH);
        } else {
            mostraLibri(libri);
        }


        listaLibri.revalidate();
        listaLibri.repaint();
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
        listaLibri.add(interno, BorderLayout.CENTER);
    }

}


