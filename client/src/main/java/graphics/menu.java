package graphics;

import bookRecommender.LibrerieGestore;
import bookRecommender.UtenteGestore;
import bookRecommender.eccezioni.Eccezione;
import bookRecommender.entita.Librerie;

import javax.swing.*;
import java.awt.*;

public class menu extends JPanel {
    UtenteGestore utente;
    JScrollPane scrollLibrerie;
    BookRecommender gui;
    JLabel titolo;

    public menu(String titoloPagina, JScrollPane scrollLibrerie) {
        this.scrollLibrerie = scrollLibrerie;
        this.gui = BookRecommender.GetInstance();
        setLayout(new BorderLayout());
        Config.setPanel2(this);

        // Impostazione del titolo della pagina
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Config.setPanel1(panel);
        panel.add(Impostazioni.buttonImpostazioni());
        panel.add(ComandoIndietro.getBottoneHome());
        panel.add(ComandoIndietro.getBottoneIndietro());
        titolo = new JLabel(titoloPagina);
        Config.setLabel1(titolo);
        panel.add(titolo);
        add(panel, BorderLayout.WEST);

        //controllo login
        utente = UtenteGestore.GetInstance();
        JPanel panel1 = new JPanel();
        Config.setPanel1(panel1);
        panel1.setLayout(new FlowLayout(FlowLayout.RIGHT));
        scrollLibrerie.setMinimumSize(new Dimension(200, 0));
        if (utente.UtenteLoggato()) {
            // Se l'utente è loggato, mostra librerie non ancora caricate
            titolo.setText(titoloPagina + " di " + utente.GetNome() + ". Id utente: " + utente.GetIdUtente());
            JPanel panel2 = new JPanel();
            Config.setPanel1(panel2);
            panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
            JLabel benvenuto = new JLabel("Librerie non");
            JLabel benvenuto1 = new JLabel("ancora caricate");
            benvenuto.setAlignmentX(Component.CENTER_ALIGNMENT);
            benvenuto1.setAlignmentX(Component.CENTER_ALIGNMENT);
            Config.setLabel1(benvenuto);
            Config.setLabel1(benvenuto1);
            panel2.add(benvenuto);
            panel2.add(benvenuto1);
            panel2.setMinimumSize(new Dimension(300, 0));
            scrollLibrerie.setViewportView(panel2);
            //creazione dei bottoni
            JButton mostraLibreria = new JButton("Aggiorna libreria");
            mostraLibreria.addActionListener(e -> mostraLibrerie());
            Config.setButton1(mostraLibreria);
            JButton Logout = new JButton("Logout");
            Config.setButton1(Logout);
            Logout.addActionListener(e -> gui.logout(titoloPagina));
            panel1.add(Logout);
            panel1.add(mostraLibreria);
            scrollLibrerie.setVisible(true);
            mostraLibrerie();
        } else {
            // Se l'utente è non loggato, nasconde la parte delle librerie
            scrollLibrerie.setVisible(false);
            //creazione dei bottoni
            JButton Login = new JButton("Accedi");
            Config.setButton1(Login);
            Login.addActionListener(e -> gui.showLogin());
            panel1.add(Login);
            scrollLibrerie.setVisible(false);
        }
        add(panel1, BorderLayout.EAST);
    }

    public void mostraLibrerie() {
        // Se l'utente è loggato, mostra librerie non ancora caricate
        JPanel panel2 = new JPanel();
        Config.setPanel1(panel2);
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        JLabel benvenuto = new JLabel("Caricamento");
        JLabel benvenuto1 = new JLabel("in corso...");
        benvenuto.setAlignmentX(Component.CENTER_ALIGNMENT);
        benvenuto1.setAlignmentX(Component.CENTER_ALIGNMENT);
        Config.setLabel1(benvenuto);
        Config.setLabel1(benvenuto1);
        panel2.add(benvenuto);
        panel2.add(benvenuto1);
        panel2.setMinimumSize(new Dimension(300, 0));
        scrollLibrerie.setViewportView(panel2);

        LibrerieGestore libG = LibrerieGestore.GetInstance();
        Eccezione ecc = libG.caricaLibrerie();
        if (ecc.getErrorCode() > 0) {
            return;
        }
        Librerie[] libGs = libG.GetLibrerie();
        if (libGs.length == 0) {
            JPanel panel3 = new JPanel();
            Config.setPanel1(panel3);
            panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
            JLabel a = new JLabel("Non hai ancora");
            JLabel a1 = new JLabel("librerie");
            a.setAlignmentX(Component.CENTER_ALIGNMENT);
            a1.setAlignmentX(Component.CENTER_ALIGNMENT);
            Config.setLabel1(a);
            Config.setLabel1(a1);
            panel3.add(a);
            panel3.add(a1);
            panel3.setMinimumSize(new Dimension(300, 0));
            panel3.add(newLibreria());
            scrollLibrerie.setViewportView(panel3);
            return;
        }
        JPanel panelLibrerie = new JPanel();
        Config.setPanel1(panelLibrerie);
        scrollLibrerie.setViewportView(panelLibrerie);
        panelLibrerie.setLayout(new BoxLayout(panelLibrerie, BoxLayout.Y_AXIS));

        panelLibrerie.setMinimumSize(new Dimension(300, 0));
        for (Librerie libreria : libGs) {
            JButton b = new JButton(libreria.getNome());
            Config.setButton1(b);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, b.getPreferredSize().height));
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.addActionListener(e -> gui.showLibreria(libreria.getNome()));
            Config.setButton1(b);
            panelLibrerie.add(b);
        }
        panelLibrerie.add(newLibreria());
    }

    private JPanel newLibreria() {
        JPanel panel = new JPanel();
        Config.setPanel1(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Nome Libreria:");
        Config.setLabel1(label);
        JTextField textField = new JTextField();
        Config.setTextField1(textField);
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));
        JButton button = new JButton("Crea Libreria");
        Config.setButton1(button);
        button.addActionListener(e -> {
            String nomeLibreria = textField.getText();
            if (nomeLibreria.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Il nome della libreria non può essere vuoto.", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Eccezione ecc = LibrerieGestore.GetInstance().AggiungiLibreria(nomeLibreria);
            if (ecc.getErrorCode() > 0) {
                JOptionPane.showMessageDialog(this, ecc.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
            LibrerieGestore.GetInstance().caricaLibrerie();
        });
        panel.add(label);
        panel.add(textField);
        panel.add(button);

        return panel;
    }

    ;
}
