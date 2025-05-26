package graphics;

import bookRecommender.UtenteGestore;
import bookRecommender.ValutazioniGestore;
import bookRecommender.eccezioni.Eccezione;
import bookRecommender.entita.Score;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class AggiungiValutazione extends JPanel {
    private BookRecommender gui;

    private JSlider sliderStile;
    private JSlider sliderEdizione;
    private JSlider sliderContenuto;
    private JSlider sliderGradevolezza;
    private JSlider sliderOriginalita;

    private JTextArea noteStile;
    private JTextArea noteEdizione;
    private JTextArea noteContenuto;
    private JTextArea noteGradevolezza;
    private JTextArea noteOriginalita;
    private JTextArea noteGenerali;


    public AggiungiValutazione() {
        reload("");

    }

    public void reload(String idLibro) {
        gui = BookRecommender.GetInstance();
        removeAll();
        System.out.println(idLibro);
        if (!idLibro.equals("")) {
            setBackground(Config.COLORE_SFONDO);
            setLayout(new BorderLayout());
            JPanel menuRidotto = new JPanel(new FlowLayout(FlowLayout.LEFT));
            Config.setPanel1(menuRidotto);
            JLabel t = new JLabel("Valutazione");
            Config.setLabel1(t);
            menuRidotto.add(ComandoIndietro.getBottoneHome());
            menuRidotto.add(ComandoIndietro.getBottoneIndietro());
            menuRidotto.add(t);
            add(menuRidotto, BorderLayout.NORTH);

            JPanel griglia = new JPanel(new GridLayout(6, 3, 30, 40));
            Config.setPanel1(griglia);

            JLabel stile = new JLabel("Stile");
            Config.setLabel1(stile);

            JPanel panelStile = new JPanel(new GridLayout(2, 1));
            Config.setPanel1(panelStile);
            sliderStile = new JSlider(JSlider.HORIZONTAL, 1, 5, 3);
            JLabel scoreStile = new JLabel(Integer.toString(sliderStile.getValue()), JLabel.CENTER);
            Config.setLabel1(scoreStile);
            sliderStile.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    scoreStile.setText(Integer.toString(sliderStile.getValue()));
                }
            });
            panelStile.add(scoreStile);
            panelStile.add(sliderStile);
            noteStile = new JTextArea();
            Config.setTextAreaNote(noteStile);
            griglia.add(stile);
            griglia.add(panelStile);
            griglia.add(noteStile);

            JPanel panelContenuto = new JPanel(new GridLayout(2, 1));
            Config.setPanel1(panelContenuto);
            sliderContenuto = new JSlider(JSlider.HORIZONTAL, 1, 5, 3);
            JLabel scoreContenuto = new JLabel(Integer.toString(sliderContenuto.getValue()), JLabel.CENTER);
            Config.setLabel1(scoreContenuto);
            sliderContenuto.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    scoreContenuto.setText(Integer.toString(sliderContenuto.getValue()));
                }
            });
            panelContenuto.add(scoreContenuto);
            panelContenuto.add(sliderContenuto);
            JLabel contenuto = new JLabel("Contenuto");
            Config.setLabel1(contenuto);
            noteContenuto = new JTextArea();
            Config.setTextAreaNote(noteContenuto);
            griglia.add(contenuto);
            griglia.add(panelContenuto);
            griglia.add(noteContenuto);

            JLabel gradevolezza = new JLabel("Gradevolezza");
            Config.setLabel1(gradevolezza);

            JPanel panelGradevolezza = new JPanel(new GridLayout(2, 1));
            Config.setPanel1(panelGradevolezza);

            sliderGradevolezza = new JSlider(JSlider.HORIZONTAL, 1, 5, 3);
            JLabel scoreGradevolezza = new JLabel(Integer.toString(sliderGradevolezza.getValue()), JLabel.CENTER);
            Config.setLabel1(scoreGradevolezza);
            sliderGradevolezza.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    scoreGradevolezza.setText(Integer.toString(sliderGradevolezza.getValue()));
                }
            });
            panelGradevolezza.add(scoreGradevolezza);
            panelGradevolezza.add(sliderGradevolezza);

            noteGradevolezza = new JTextArea();
            Config.setTextAreaNote(noteGradevolezza);
            griglia.add(gradevolezza);
            griglia.add(panelGradevolezza);
            griglia.add(noteGradevolezza);


            JLabel originalita = new JLabel("Originalità");
            Config.setLabel1(originalita);

            JPanel panelOriginalita = new JPanel(new GridLayout(2, 1));
            Config.setPanel1(panelOriginalita);

            sliderOriginalita = new JSlider(JSlider.HORIZONTAL, 1, 5, 3);
            JLabel scoreOriginalita = new JLabel(Integer.toString(sliderOriginalita.getValue()), JLabel.CENTER);
            Config.setLabel1(scoreOriginalita);
            sliderOriginalita.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    scoreOriginalita.setText(Integer.toString(sliderOriginalita.getValue()));
                }
            });
            panelOriginalita.add(scoreOriginalita);
            panelOriginalita.add(sliderOriginalita);
            noteOriginalita = new JTextArea();
            Config.setTextAreaNote(noteOriginalita);
            griglia.add(originalita);
            griglia.add(panelOriginalita);
            griglia.add(noteOriginalita);

            JLabel edizione = new JLabel("Edizione");
            Config.setLabel1(edizione);
            JPanel panelEdizione = new JPanel(new GridLayout(2, 1));
            Config.setPanel1(panelEdizione);

            sliderEdizione = new JSlider(JSlider.HORIZONTAL, 1, 5, 3);
            JLabel scoreEdizione = new JLabel(Integer.toString(sliderEdizione.getValue()), JLabel.CENTER);
            Config.setLabel1(scoreEdizione);
            sliderEdizione.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    scoreEdizione.setText(Integer.toString(sliderEdizione.getValue()));
                }
            });
            panelEdizione.add(scoreEdizione);
            panelEdizione.add(sliderEdizione);
            noteEdizione = new JTextArea();
            Config.setTextArea2(noteEdizione);
            griglia.add(edizione);
            griglia.add(panelEdizione);
            griglia.add(noteEdizione);

            JLabel generale = new JLabel("Note generali");
            Config.setLabel1(generale);
            noteGenerali = new JTextArea();
            Config.setTextArea2(noteGenerali);
            JButton salvaValutazione = new JButton("Salva Valutazione");
            salvaValutazione.addActionListener(e -> aggiungiValutazione(idLibro));
            griglia.add(generale);
            griglia.add(noteGenerali);
            griglia.add(salvaValutazione);
            add(griglia, BorderLayout.CENTER);
        }
    }

    private void aggiungiValutazione(String idLibro) {
        short[] punteggi = new short[5];
        String[] note = new String[6];

        punteggi[0] = (short) sliderStile.getValue();
        punteggi[1] = (short) sliderContenuto.getValue();
        punteggi[2] = (short) sliderGradevolezza.getValue();
        punteggi[3] = (short) sliderOriginalita.getValue();
        punteggi[4] = (short) sliderEdizione.getValue();
        note[0] = noteStile.getText();
        note[1] = noteContenuto.getText();
        note[2] = noteGradevolezza.getText();
        note[3] = noteOriginalita.getText();
        note[4] = noteEdizione.getText();
        note[5] = noteGenerali.getText();

        Score score = new Score(punteggi, note);
        ValutazioniGestore vG = ValutazioniGestore.GetInstance();
        Eccezione ecc = vG.AggiungiValutazione(Integer.parseInt(idLibro), score);
        if (ecc.getErrorCode() > 0) PopupError.mostraErrore(ecc.getErrorCode()+" "+ecc.getMessage());
        ComandoIndietro.indietro();
    }
}
