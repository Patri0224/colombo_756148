package graphics;

import bookRecommender.LibriRicercaGestore;
import bookRecommender.UtenteGestore;
import bookRecommender.entita.Libri;

import javax.swing.*;
import java.awt.*;

public class PaginaLibro extends JPanel {
    private BookRecommender gui;
    private JPanel menu;
    private JPanel main;
    private Libri libro;
    private JScrollPane scrollLibrerie;
    private UtenteGestore utente;

    public PaginaLibro() {
        setLibro(-1);
    }

    public void setLibro(int id) {
        libro = LibriRicercaGestore.GetInstance().CercaLibro(id);
        if (libro == null) {
            libro = new Libri();
            libro.setTitolo("Libro non trovato");
        }
        removeAll();

        gui = BookRecommender.GetInstance();
        setBackground(Config.COLORE_SFONDO);
        setLayout(new BorderLayout(1, 1));
        scrollLibrerie = new JScrollPane();
        Config.setScrollPane(scrollLibrerie);
        scrollLibrerie.setVisible(false);
        menu = new menu("Pagina Libro " + libro.getTitolo(), scrollLibrerie);

        main = new JPanel();
        Config.setPanel1(main);
        if (UtenteGestore.GetInstance().UtenteLoggato()) {
            creaMainUtenteLoggato();
        } else {
            creaMainUtenteNonLoggato();
        }
        add(menu, BorderLayout.NORTH);
    }

    private void creaMainUtenteLoggato() {
    }

    private void creaMainUtenteNonLoggato() {
        main.setLayout(new GridBagLayout( ));
    }
}
