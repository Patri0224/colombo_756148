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
import java.rmi.RemoteException;
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
        String consigliStr = "";
        short[] scores;
        datiLibro.setLayout(new GridLayout(2, 1));
        JTextArea valutazioni = new JTextArea();
        valutazioni.setEditable(false);
        ValutazioniGestore vG = ValutazioniGestore.GetInstance();
        ValutazioniLibri valutazioneLibro = vG.RicercaValutazioneMedia(libro.getId());
        scores = valutazioneLibro.getPunteggi();
        valutazioneStr += "Stile: " + creaStelle(scores[0]);
        valutazioneStr += "Contenuto: " + creaStelle(scores[1]);
        valutazioneStr += "Gradevolezza: " + creaStelle(scores[2]);
        valutazioneStr += "Originalità: " + creaStelle(scores[3]);
        valutazioneStr += "Edizione: " + creaStelle(scores[4]);
        valutazioneStr += "Media: " + creaStelle(scores[5]);
        valutazioni.setText(valutazioneStr);

        ConsigliGestore cG = ConsigliGestore.GetInstance();
        Libri[] libriConsigliati = cG.RicercaConsigliDatoLibro(libro.getId());

        // Conteggio delle occorrenze
        Map<Libri, Integer> conteggio = new HashMap<>();
        for (Libri lib : libriConsigliati) {
            conteggio.put(lib, conteggio.getOrDefault(lib, 0) + 1);
        }
        StringBuilder consigliStrBuilder = new StringBuilder();

        conteggio.forEach((libro, occorrenze) -> {
            consigliStrBuilder.append("Libro: ")
                    .append(libro.getTitolo())
                    .append(" -> ")
                    .append(occorrenze)
                    .append(" occorrenze\n");
        });

        JTextArea consigli = new JTextArea();
        consigli.setText(consigliStrBuilder.toString());
        return datiLibro;
    }


    private JPanel ValutazioniConsigliLibroPersonali() {
        JPanel datiLibro = new JPanel();
        datiLibro.setLayout(new GridLayout(2, 1));
        Config.setPanel1(datiLibro);
        if (id == -1) return datiLibro;

        JPanel valLibro = new JPanel();
        valLibro.setLayout(new GridLayout(2, 1));
        Config.setPanel1(valLibro);

        JLabel lVal = new JLabel("Valutazione");
        Config.setLabel1(lVal);
        valLibro.add(lVal);
        ValutazioniGestore v = ValutazioniGestore.GetInstance();
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
        JPanel conLibro = new JPanel();
        conLibro.setLayout(new GridLayout(2, 1));
        Config.setPanel1(conLibro);
        JLabel lCon = new JLabel("Consigli");
        Config.setLabel1(lCon);
        conLibro.add(lCon);
        ConsigliGestore c = ConsigliGestore.GetInstance();
        Libri[] lib = null;
        JLabel er = new JLabel();
        try {
            lib = c.RicercaConsigliDatoUtenteELibro(id);
        } catch (RemoteException e) {
            er.setText("errore");
            //launch error            popup
        }

        if (lib == null) {
            Config.setLabel1(er);
            lCon.add(er);
        } else if (lib.length == 0) {
            JButton aggButton1 = new JButton("primo Consiglio");
            JButton aggButton2 = new JButton("secondo Consiglio");
            JButton aggButton3 = new JButton("terzo Consiglio");
            Config.setButton1(aggButton1);
            Config.setButton1(aggButton2);
            Config.setButton1(aggButton3);
            aggButton1.addActionListener(e -> setConsiglio());
            aggButton2.addActionListener(e -> setConsiglio());
            aggButton3.addActionListener(e -> setConsiglio());
            JPanel consigli = new JPanel();
            consigli.setLayout(new GridLayout(3, 1));
            Config.setPanel1(consigli);
            consigli.add(aggButton1);
            consigli.add(aggButton2);
            consigli.add(aggButton3);
            conLibro.add(consigli);
        } else {
            JPanel consigli = new JPanel();
            consigli.setLayout(new GridLayout(3, 1));
            Config.setPanel1(consigli);
            Libri[] finalLib = lib;

            JButton aggButton1 = new JButton(lib[0].getTitolo());
            Config.setButton1(aggButton1);
            aggButton1.addActionListener(e -> gui.showLibro(finalLib[0].getId() + ""));
            consigli.add(aggButton1);

            JButton aggButton2 = new JButton("secondo Consiglio");
            Config.setButton1(aggButton2);
            aggButton2.addActionListener(e -> setConsiglio());
            if (finalLib.length == 2) {
                aggButton2.setText(finalLib[1].getTitolo());
                aggButton2.addActionListener(e -> gui.showLibro(finalLib[1].getId() + ""));
            }
            consigli.add(aggButton2);

            JButton aggButton3 = new JButton("terzo Consiglio");
            Config.setButton1(aggButton3);
            aggButton3.addActionListener(e -> setConsiglio());
            if (finalLib.length == 3) {
                aggButton3.setText(finalLib[2].getTitolo());
                aggButton3.addActionListener(e -> gui.showLibro(finalLib[2].getId() + ""));
            }
            consigli.add(aggButton3);
            conLibro.add(consigli);
        }
        return valLibro;


    }

    private void setConsiglio() {
    }

    private static String creaStelle(float voto) {
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
