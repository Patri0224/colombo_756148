package graphics;

import bookRecommender.*;
import bookRecommender.entita.Libri;
import bookRecommender.entita.ValutazioniLibri;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

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
        removeAll();

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


        gui = BookRecommender.GetInstance();

        setBackground(Config.COLORE_SFONDO);

        setLayout(new BorderLayout(1, 1));
        scrollLibrerie = new JScrollPane();
        Config.setScrollPane(scrollLibrerie);
        scrollLibrerie.setVisible(false);
        add(scrollLibrerie, BorderLayout.EAST);
        menu = new menu("Pagina Libro " + libro.getTitolo(), scrollLibrerie);

        main = new JPanel();
        Config.setPanel1(main);
        if (UtenteGestore.GetInstance().UtenteLoggato()) {
            creaMainUtenteLoggato();
        } else {
            creaMainUtenteNonLoggato();
        }

        add(menu, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
    }

    private void creaMainUtenteLoggato() {
        main.setLayout(new GridLayout(1, 3));
        JPanel datiLibro = creaDatiLibro();
        Config.setPanel2(datiLibro);
        JPanel valConLibro = ValutazioniConsigliLibro();
        Config.setPanel2(valConLibro);
        JPanel valConUtente = ValutazioniConsigliLibroPersonali();
        Config.setPanel2(valConUtente);
        main.add(datiLibro);
        main.add(valConLibro);
        main.add(valConUtente);
    }

    private void creaMainUtenteNonLoggato() {
        main.setLayout(new GridLayout(1, 2));
        JPanel datiLibro = creaDatiLibro();
        Config.setPanel2(datiLibro);
        JPanel valConLibro = ValutazioniConsigliLibro();
        Config.setPanel2(valConLibro);
        main.add(datiLibro);
        main.add(valConLibro);


    }

    private JPanel creaDatiLibro() {
        JPanel datiLibro = new JPanel(new GridBagLayout());
        Config.setPanel2(datiLibro);

        int row = 0;
        datiLibro.add(creaCampo("Titolo", libro.getTitolo()), createGbc(row++));
        datiLibro.add(creaCampo("Autore", libro.getAutori()), createGbc(row++));
        datiLibro.add(creaCampo("Pubblicazione", libro.getMesePubblicazione() + " " + libro.getAnnoPubblicazione()), createGbc(row++));
        datiLibro.add(creaCampo("Categoria", libro.getCategorie()), createGbc(row++));
        datiLibro.add(creaCampo("Editore", libro.getEditore()), createGbc(row++));
        datiLibro.add(creaCampo("Prezzo", String.valueOf(libro.getPrezzoPartenzaEuro())), createGbc(row++));
        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.X_AXIS));
        Config.setPanel2(pannello);
        JLabel label = new JLabel("Descrizione");
        Config.setLabel1(label);
        JTextArea campo = new JTextArea(libro.getDescrizione());
        campo.setEditable(false);
        campo.setPreferredSize(new Dimension(300,100));
        campo.setLineWrap(true);
        campo.setWrapStyleWord(true);
        Config.setTextArea1(campo); // Supponendo che la funzione funzioni anche con JTextField
        JScrollPane scroll = new JScrollPane();
        Config.setScrollPane(scroll);
        scroll.setViewportView(campo);
        scroll.setPreferredSize(new Dimension(600, 400));

        JPanel container = new JPanel(new BorderLayout(5,5)); // altezza maggiore
        container.add(scroll, BorderLayout.CENTER);
        Config.setPanel1(container);
        pannello.add(label);
        pannello.add(container);
        datiLibro.add(pannello, createGbc(row++));

        return datiLibro;
    }

    private GridBagConstraints createGbc(int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        return gbc;
    }

    private JPanel creaCampo(String etichetta, String valore) {
        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.X_AXIS));
        Config.setPanel2(pannello);
        JLabel label = new JLabel(etichetta);
        Config.setLabel1(label);

        JTextArea campo = new JTextArea(valore);
        campo.setEditable(false);
        campo.setLineWrap(true);
        campo.setWrapStyleWord(true);
        Config.setTextArea1(campo); // Supponendo che la funzione funzioni anche con JTextField
        JPanel container = new JPanel(new BorderLayout(5, 5)); // altezza maggiore
        container.add(campo, BorderLayout.CENTER);
        Config.setPanel1(container);
        pannello.add(label);
        pannello.add(container);
        return pannello;
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
        valutazioneStr += "\nStile: " + creaStelle(scores[0]);
        valutazioneStr += "\nContenuto: " + creaStelle(scores[1]);
        valutazioneStr += "\nGradevolezza: " + creaStelle(scores[2]);
        valutazioneStr += "\nOriginalità: " + creaStelle(scores[3]);
        valutazioneStr += "\nEdizione: " + creaStelle(scores[4]);
        valutazioneStr += "\nMedia: " + creaStelle(scores[5]);
        Config.setTextArea1(valutazioni);
        valutazioni.setText(valutazioneStr);
        valutazioni.setFont(new Font("DejaVu Sans", Font.PLAIN, Math.min(Config.FONT.getSize() + 5, 40)));

        datiLibro.add(valutazioni);
        ConsigliGestore cG = ConsigliGestore.GetInstance();
        Libri[] libriConsigliati = cG.RicercaConsigliDatoLibro(libro.getId());
        JTextArea consigli = new JTextArea();
        if (libriConsigliati == null) {
            consigli.setText("Nessun libro è stato consigliato per questo libro.");
            Config.setTextArea1(consigli);

        } else {
            // Conteggio delle occorrenze
            Map<Libri, Integer> conteggio = new HashMap<>();
            for (Libri lib : libriConsigliati) {
                conteggio.put(lib, conteggio.getOrDefault(lib, 0) + 1);
            }
            StringBuilder consigliStrBuilder = new StringBuilder();

            conteggio.forEach((libro, occorrenze) -> {
                consigliStrBuilder.append("- Libro: ")
                        .append(libro.getTitolo())
                        .append(" -> ")
                        .append(occorrenze)
                        .append(" occorrenze\n");
            });

            consigli.setText(consigliStrBuilder.toString());
            Config.setTextArea1(consigli);
            consigli.setBorder(BorderFactory.createEmptyBorder(20,30,30,30));
            consigli.setLineWrap(true);
            consigli.setWrapStyleWord(true);
        }
        datiLibro.add(consigli);
        return datiLibro;
    }


    private JPanel ValutazioniConsigliLibroPersonali() {
        if (!LibrerieGestore.GetInstance().ControlloLibroInLibrerie(libro.getId())) {
            JPanel panel = aggiungiALibreria();
            return panel;
        }
        JPanel datiLibro = new JPanel();
        datiLibro.setLayout(new GridLayout(2, 1));
        Config.setPanel1(datiLibro);
        if (id == -1) return datiLibro;

        JPanel valLibro = new JPanel();
        valLibro.setLayout(new GridLayout(2, 1));
        Config.setPanel1(valLibro);
        valLibro.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

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
            JPanel panelValutazioni = new JPanel(new GridLayout(6, 3));
            Config.setPanel1(panelValutazioni);
            JLabel stile = new JLabel("Stile");
            Config.setLabel1(stile);
            JLabel scoreStile = new JLabel(creaStelle(valutazioniLibri.getPunteggio(0)));
            Config.setLabel2(scoreStile);

            JScrollPane scrollStile = new JScrollPane();
            JTextArea noteStile = new JTextArea(valutazioniLibri.getNota(0));
            Config.setScrollPane(scrollStile);
            scrollStile.setViewportView(noteStile);
            scrollStile.setPreferredSize(new Dimension(600, 400));
            Config.setTextArea1(noteStile);
            noteStile.setEditable(false);
            panelValutazioni.add(stile);
            panelValutazioni.add(scoreStile);
            panelValutazioni.add(scrollStile);

            JLabel contenuto = new JLabel("Contenuto");
            Config.setLabel1(contenuto);
            JLabel scoreContenuto = new JLabel(creaStelle(valutazioniLibri.getPunteggio(1)));
            Config.setLabel2(scoreContenuto);
            JScrollPane scrollContenuto = new JScrollPane();
            JTextArea noteContenuto = new JTextArea(valutazioniLibri.getNota(1));
            Config.setScrollPane(scrollContenuto);
            scrollContenuto.setViewportView(noteContenuto);
            scrollContenuto.setPreferredSize(new Dimension(600, 400));
            Config.setTextArea1(noteContenuto);
            noteContenuto.setEditable(false);
            panelValutazioni.add(contenuto);
            panelValutazioni.add(scoreContenuto);
            panelValutazioni.add(scrollContenuto);

            JLabel gradevolezza = new JLabel("Gradevolezza");
            Config.setLabel1(gradevolezza);
            JLabel scoreGradevolezza = new JLabel(creaStelle(valutazioniLibri.getPunteggio(2)));
            Config.setLabel2(scoreGradevolezza);
            JScrollPane scrollGradevolezza = new JScrollPane();
            JTextArea noteGradevolezza = new JTextArea(valutazioniLibri.getNota(2));
            Config.setScrollPane(scrollGradevolezza);
            scrollGradevolezza.setViewportView(noteGradevolezza);
            Config.setTextArea1(noteGradevolezza);
            noteGradevolezza.setEditable(false);
            panelValutazioni.add(gradevolezza);
            panelValutazioni.add(scoreGradevolezza);
            panelValutazioni.add(scrollGradevolezza);

            JLabel originalita = new JLabel("Originalità");
            Config.setLabel1(originalita);
            JLabel scoreOriginalita = new JLabel(creaStelle(valutazioniLibri.getPunteggio(3)));
            Config.setLabel2(scoreOriginalita);
            JScrollPane scrollOriginalita = new JScrollPane();
            JTextArea noteOriginalita = new JTextArea(valutazioniLibri.getNota(3));
            Config.setScrollPane(scrollOriginalita);
            scrollOriginalita.setViewportView(noteOriginalita);
            Config.setTextArea1(noteOriginalita);
            noteOriginalita.setEditable(false);
            panelValutazioni.add(originalita);
            panelValutazioni.add(scoreOriginalita);
            panelValutazioni.add(scrollOriginalita);

            JLabel edizione = new JLabel("Edizione");
            Config.setLabel1(edizione);
            JLabel scoreEdizione = new JLabel(creaStelle(valutazioniLibri.getPunteggio(4)));
            Config.setLabel2(scoreEdizione);
            JScrollPane scrollEdizione = new JScrollPane();
            JTextArea noteEdizione = new JTextArea(valutazioniLibri.getNota(4));
            Config.setScrollPane(scrollEdizione);
            scrollEdizione.setViewportView(noteEdizione);
            Config.setTextArea1(noteEdizione);
            noteEdizione.setEditable(false);
            panelValutazioni.add(edizione);
            panelValutazioni.add(scoreEdizione);
            panelValutazioni.add(scrollEdizione);

            JLabel media = new JLabel("Media");
            Config.setLabel1(media);
            JLabel scoreMedia = new JLabel(creaStelle(valutazioniLibri.getPunteggio(5)));
            Config.setLabel2(scoreMedia);
            panelValutazioni.add(media);
            panelValutazioni.add(scoreMedia);
            valLibro.add(panelValutazioni);

        }
        datiLibro.add(valLibro);
        JPanel conLibro = new JPanel();
        conLibro.setLayout(new GridLayout(2, 1));
        Config.setPanel1(conLibro);
        conLibro.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
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
            if (finalLib.length >= 2) {
                aggButton2.setText(finalLib[1].getTitolo());
                aggButton2.addActionListener(e -> gui.showLibro(finalLib[1].getId() + ""));
            } else {
                aggButton2.addActionListener(e -> setConsiglio());
            }
            consigli.add(aggButton2);

            JButton aggButton3 = new JButton("terzo Consiglio");
            Config.setButton1(aggButton3);
            if (finalLib.length == 3) {
                aggButton3.setText(finalLib[2].getTitolo());
                aggButton3.addActionListener(e -> gui.showLibro(finalLib[2].getId() + ""));
            } else {
                aggButton3.addActionListener(e -> setConsiglio());

            }
            consigli.add(aggButton3);
            conLibro.add(consigli);
        }
        datiLibro.add(conLibro);
        return datiLibro;


    }

    private JPanel aggiungiALibreria() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 20));
        Config.setPanel1(panel);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        JTextArea info;
        boolean nonHaLibs = LibrerieGestore.GetInstance().GetLibrerie().length == 0;
        if (nonHaLibs) {
            info = new JTextArea("Crea prima una libreria");
        } else {
            info = new JTextArea("Il libro non è presente in nessuna delle librerie, aggiungilo per dare una valutazione e aggiungiere consigli");
        }
        info.setEditable(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        Config.setTextArea1(info);
        panel.add(info);
        if (nonHaLibs) return panel;
        LibrerieGestore.GetInstance().caricaLibrerie();
        String[] nomeLibrerie = LibrerieGestore.GetInstance().getNomeLibrerie();
        JComboBox<String> libCombo = new JComboBox<>(nomeLibrerie);
        Config.setComboBox1(libCombo);
        libCombo.setSelectedItem(nomeLibrerie[0]);
        panel.add(libCombo);
        JButton aggiungiButton = new JButton("aggiungi");
        Config.setButton1(aggiungiButton);
        aggiungiButton.addActionListener(e -> aggiungiLibroaLibreria(libCombo));
        panel.add(aggiungiButton);
        return panel;
    }

    private void setConsiglio() {
        gui.showAggiungiConsiglio(libro.getId());
    }

    private static String creaStelle(float voto) {
        int piene = Math.round(voto);
        int vuote = 5 - piene;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%.1f", voto));
        for (int i = 0; i < piene; i++) sb.append("★");
        for (int i = 0; i < vuote; i++) sb.append("☆");
        return sb.toString();
    }

    private void aggiungiLibroaLibreria(JComboBox<String> libCombo) {
        LibrerieGestore.GetInstance().AggiungiLibroALibreria((String) libCombo.getSelectedItem(), libro.getId());
        gui.reloadAll();
    }

    public int getLibro() {
        return libro.getId();
    }
}
