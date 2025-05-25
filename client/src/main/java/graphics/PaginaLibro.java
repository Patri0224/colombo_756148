package graphics;

import bookRecommender.ConsigliGestore;
import bookRecommender.LibriRicercaGestore;
import bookRecommender.UtenteGestore;
import bookRecommender.ValutazioniGestore;
import bookRecommender.entita.ConsigliLibri;
import bookRecommender.entita.Libri;
import bookRecommender.entita.ValutazioniLibri;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PaginaLibro extends JPanel {
    private BookRecommender gui;
    private JPanel menu;
    private JPanel main;
    private Libri libro;
    private JScrollPane scrollLibrerie;
    private UtenteGestore utente;
    private int id = -1;

    public PaginaLibro() {
        setLibro(-1);
    }

    public void setLibro(int id) {
        this.id = id;

        if (id != -1) {
            libro = LibriRicercaGestore.GetInstance().CercaLibro(id);
            if (libro == null) {
                libro = new Libri();
                libro.setTitolo("Libro non trovato");
            }
        } else {
            libro = new Libri();
            libro.setTitolo("Nessun Libro");
        }

        removeAll();

        gui = BookRecommender.GetInstance();

        setBackground(Config.COLORE_SFONDO);

        setLayout(new BorderLayout(1, 1));
        scrollLibrerie = new

                JScrollPane();
        Config.setScrollPane(scrollLibrerie);
        scrollLibrerie.setVisible(false);
        menu = new

                menu("Pagina Libro " + libro.getTitolo(), scrollLibrerie);

        main = new

                JPanel();
        Config.setPanel1(main);
        if (UtenteGestore.GetInstance().

                UtenteLoggato()) {
            creaMainUtenteLoggato();
        } else {
            creaMainUtenteNonLoggato();
        }

        add(menu, BorderLayout.NORTH);
    }

    private void creaMainUtenteLoggato() {
        main.setLayout(new GridLayout(1, 3));
        JPanel datiLibro = creaDatiLibro();
        JPanel valConLibro = ValutazioniConsigliLibro();
        JPanel valConUtente = ValutazioniConsigliLibroPersonali();
    }

    private void creaMainUtenteNonLoggato() {
        main.setLayout(new GridLayout(1, 2));
        JPanel datiLibro = creaDatiLibro();
        JPanel valConLibro = ValutazioniConsigliLibro();


    }

    private JPanel creaDatiLibro() {
        JPanel datiLibro = new JPanel();
        datiLibro.setLayout(new GridLayout(7, 2));
        Config.setPanel1(datiLibro);
        JLabel lTitolo = new JLabel("Titolo");
        JLabel lAutore = new JLabel("Autore");
        JLabel lAnno = new JLabel("Pubblicazione");
        JLabel lCategoria = new JLabel("Categoria");
        JLabel lEditore = new JLabel("Editore");
        JLabel lPrezzo = new JLabel("Prezzo");
        JLabel lDescrizione = new JLabel("Descrizione");
        Config.setLabel1(lTitolo);
        Config.setLabel1(lAutore);
        Config.setLabel1(lAnno);
        Config.setLabel1(lCategoria);
        Config.setLabel1(lEditore);
        Config.setLabel1(lPrezzo);
        Config.setLabel1(lDescrizione);
        JTextArea tTitolo = new JTextArea(libro.getTitolo());
        JTextArea tAutore = new JTextArea(libro.getAutori());
        JTextArea tAnno = new JTextArea(libro.getMesePubblicazione() + " " + libro.getAnnoPubblicazione());
        JTextArea tCategoria = new JTextArea(libro.getCategorie());
        JTextArea tEditore = new JTextArea(libro.getEditore());
        JTextArea tPrezzo = new JTextArea(libro.getPrezzoPartenzaEuro() + "");
        JTextArea tDescrizione = new JTextArea(libro.getDescrizione());
        Config.setTextArea1(tTitolo);
        Config.setTextArea1(tAutore);
        Config.setTextArea1(tAnno);
        Config.setTextArea1(tCategoria);
        Config.setTextArea1(tEditore);
        Config.setTextArea1(tPrezzo);
        Config.setTextArea1(tDescrizione);
        datiLibro.add(lTitolo);
        datiLibro.add(tTitolo);
        datiLibro.add(lAutore);
        datiLibro.add(tAutore);
        datiLibro.add(lAnno);
        datiLibro.add(tAnno);
        datiLibro.add(lCategoria);
        datiLibro.add(tCategoria);
        datiLibro.add(lEditore);
        datiLibro.add(tEditore);
        datiLibro.add(lPrezzo);
        datiLibro.add(tPrezzo);
        datiLibro.add(lDescrizione);
        datiLibro.add(tDescrizione);
        return datiLibro;
    }

    private JPanel ValutazioniConsigliLibro() {
        JPanel datiLibro = new JPanel();
        Config.setPanel1(datiLibro);
        if (id == -1) return datiLibro;
        String valutazioneStr = "";
        short[] scores;
        datiLibro.setLayout(new GridLayout(2, 1));
        JTextArea valutazioni = new JTextArea();
        valutazioni.setEditable(false);
        ValutazioniGestore vG = ValutazioniGestore.GetInstance();
        ValutazioniLibri valutazioneLibro = vG.RicercaValutazioneMedia(libro.getId());
        scores = valutazioneLibro.getPunteggi();
        valutazioneStr += "Stile: " + Short.toString(scores[0]);
        valutazioneStr += "Contenuto: " + Short.toString(scores[1]);
        valutazioneStr += "Gradevolezza: " + Short.toString(scores[2]);
        valutazioneStr += "Originalità: " + Short.toString(scores[3]);
        valutazioneStr += "Edizione: " + Short.toString(scores[4]);
        valutazioneStr += "Media: " + Short.toString(scores[5]);
        valutazioni.setText(valutazioneStr);

        ConsigliGestore cG = ConsigliGestore.GetInstance();
        Libri[] libriConsigliati = cG.RicercaConsigliDatoLibro(libro.getId());

        // Conteggio delle occorrenze
        Map<Libri, Integer> conteggio = new HashMap<>();
        for (Libri lib : libriConsigliati) {
            conteggio.put(lib, conteggio.getOrDefault(lib, 0) + 1);
        }

        return null;
    }

    private JPanel ValutazioniConsigliLibroPersonali() {
        JPanel valLibro = new JPanel();
        valLibro.setLayout(new GridLayout(2, 1));
        Config.setPanel1(valLibro);
        if (id == -1) return valLibro;
        JLabel lVal = new JLabel();
        Config.setLabel1(lVal);
        valLibro.add(lVal);
        ValutazioniGestore v = ValutazioniGestore.GetInstance();
        ConsigliGestore c = ConsigliGestore.GetInstance();
        ValutazioniLibri valutazioniLibri = v.RicercaValutazione(id);
        if (valutazioniLibri == null) {
            JButton aggButton = new JButton("Aggiungi");
            Config.setButton1(aggButton);
            aggButton.addActionListener(e -> gui.showValutazione(id + ""));
            valLibro.add(aggButton);
        } else {
            JTextArea valutazioni = new JTextArea();
            valutazioni.setEditable(false);
            Config.setTextArea1(valutazioni);
            valutazioni.setText(valutazioniLibri.toString());
            valLibro.add(valutazioni);
        }
        return valLibro;
    }

    private static String creaStelle(double voto) {
        int piene = (int) voto;
        boolean mezza = (voto - piene) >= 0.5;
        int vuote = 5 - piene - (mezza ? 1 : 0);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < piene; i++) sb.append("★");
        if (mezza) sb.append("⯨");
        for (int i = 0; i < vuote; i++) sb.append("☆");
        return sb.toString();
    }
}
