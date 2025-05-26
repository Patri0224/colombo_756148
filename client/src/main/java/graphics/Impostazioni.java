/*
 * Autori del progetto:
 * Nome: Colombo     Cognome: Patrizio     Matricola: 756148     Sede: CO
 * Nome: Felitti     Cognome: Fabio        Matricola: 758058     Sede: CO
 * Nome: Franchi     Cognome: Matteo       Matricola: 757075     Sede: CO
 * Nome: Iacono      Cognome: Alessandro   Matricola: 758451     Sede: CO
 */
package graphics;

import bookRecommender.UtenteGestore;
import bookRecommender.eccezioni.Eccezione;

import javax.swing.*;
import java.awt.*;

public class Impostazioni extends JPanel {
    private BookRecommender gui;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JPanel cambiaPasswordPanel;
    private JPanel visualizzaDatiUtentePanel;
    private JPanel eliminaAccountPanel;
    private JPanel aspettoPanel;
    private boolean isLoggedIn = true;

    public Impostazioni() {
        reload();
    }

    public static JButton buttonImpostazioni() {
        JButton bottone = new JButton("Setting");
        bottone.addActionListener(e -> BookRecommender.CreateInstance().showImpostazioni());
        Config.setButton1(bottone);
        return bottone;
    }

    public void reload() {
        removeAll();
        gui = BookRecommender.GetInstance();
        setBackground(Config.COLORE_SFONDO);
        setLayout(new BorderLayout());
        JPanel menuRidotto = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Config.setPanel1(menuRidotto);
        JLabel t = new JLabel("Impostazioni");
        Config.setLabel1(t);
        menuRidotto.add(ComandoIndietro.getBottoneHome());
        menuRidotto.add(ComandoIndietro.getBottoneIndietro());
        menuRidotto.add(t);
        add(menuRidotto, BorderLayout.NORTH);


        JPanel panel = new JPanel();
        Config.setPanel1(panel);
        panel.setLayout(new GridLayout(4, 1));

        JButton btnModificaPassword = new JButton("Modifica Password");
        Config.setButton1(btnModificaPassword);
        btnModificaPassword.addActionListener(e -> showModPass());

        JButton btnAccount = new JButton("Account");
        Config.setButton1(btnAccount);
        btnAccount.addActionListener(e -> showAccount());

        JButton btnEliminaAccount = new JButton("Elimina Account");
        Config.setButton1(btnEliminaAccount);
        btnEliminaAccount.addActionListener(e -> showEliminaAccount());

        JButton btnAspetto = new JButton("Aspetto");
        Config.setButton1(btnAspetto);
        btnAspetto.addActionListener(e -> showAspetto());


        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        Config.setPanel1(cardPanel);
        if (UtenteGestore.GetInstance().UtenteLoggato()) {
            panel.add(btnAccount);
            panel.add(btnModificaPassword);
            panel.add(btnEliminaAccount);
            cambiaPasswordPanel = cambiaPasswordPanel();
            visualizzaDatiUtentePanel = visualizzaDatiUtentePanel();
            eliminaAccountPanel = eliminaAccountPanel();
            cardPanel.add(cambiaPasswordPanel(), "Cambia Password");
            cardPanel.add(visualizzaDatiUtentePanel(), "Visuallizza Dati Utente");
            cardPanel.add(eliminaAccountPanel(), "Elimina Account");
        }
        panel.add(btnAspetto);
        add(panel, BorderLayout.WEST);

        cardPanel.add(aspettoPanel(), "Cambia Aspetto");

        add(panel, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);
        showAccount();


    }

    private void showModPass() {
        if (UtenteGestore.GetInstance().UtenteLoggato()) {
            cambiaPasswordPanel = cambiaPasswordPanel();
        }
        cardLayout.show(cardPanel, "Cambia Password");
        revalidate();
        repaint();
    }

    private void showAccount() {
        if (UtenteGestore.GetInstance().UtenteLoggato()) {
            visualizzaDatiUtentePanel = visualizzaDatiUtentePanel();
        }
        cardLayout.show(cardPanel, "Visuallizza Dati Utente");
        revalidate();
        repaint();
    }

    private void showEliminaAccount() {
        if (UtenteGestore.GetInstance().UtenteLoggato()) {
            eliminaAccountPanel = cambiaPasswordPanel();
        }
        cardLayout.show(cardPanel, "Elimina Account");
        revalidate();
        repaint();
    }

    private void showAspetto() {
        if (aspettoPanel == null) {
            aspettoPanel = aspettoPanel();
        }
        cardLayout.show(cardPanel, "Cambia Aspetto");
        revalidate();
        repaint();
    }

    private JPanel cambiaPasswordPanel() {

        cambiaPasswordPanel = new JPanel();
        Config.setPanel1(cambiaPasswordPanel);
        cambiaPasswordPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        cambiaPasswordPanel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Modifica Password", SwingConstants.CENTER);
        Config.setLabel1(label);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        cambiaPasswordPanel.add(label, BorderLayout.NORTH);

        // Pannello centrale con i campi
        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 100, 70));
        Config.setPanel2(centerPanel);
        JLabel vecchiaLabel = new JLabel("Vecchia Password:", SwingConstants.RIGHT);
        JPasswordField vecchiaPassword = new JPasswordField();
        Config.setLabel1(vecchiaLabel);
        Config.setPasswordField1(vecchiaPassword);

        JLabel nuovaLabel = new JLabel("Nuova Password:", SwingConstants.RIGHT);
        JPasswordField nuovaPassword = new JPasswordField();
        Config.setLabel1(nuovaLabel);
        Config.setPasswordField1(nuovaPassword);

        JLabel confermaLabel = new JLabel("Conferma Nuova Password:", SwingConstants.RIGHT);
        JPasswordField confermaPassword = new JPasswordField();
        Config.setLabel1(confermaLabel);
        Config.setPasswordField1(confermaPassword);

        centerPanel.add(vecchiaLabel);
        centerPanel.add(vecchiaPassword);
        centerPanel.add(nuovaLabel);
        centerPanel.add(nuovaPassword);
        centerPanel.add(confermaLabel);
        centerPanel.add(confermaPassword);

        cambiaPasswordPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottone conferma
        JButton confermaButton = new JButton("Conferma");
        Config.setButton1(confermaButton);
        confermaButton.setBackground(new Color(7, 44, 0));
        cambiaPasswordPanel.add(confermaButton, BorderLayout.SOUTH);

        // Azione sul click
        confermaButton.addActionListener(e -> {
            String vecchia = new String(vecchiaPassword.getPassword());
            String nuova = new String(nuovaPassword.getPassword());
            String conferma = new String(confermaPassword.getPassword());

            UtenteGestore utente = UtenteGestore.GetInstance();

            if (!nuova.equals(conferma)) {
                PopupError.mostraErrore("Le nuove password non corrispondono.");
                return;
            }

            if (!UtenteGestore.ControlloCaratteriPassword(nuova)) {
                PopupError.mostraErrore("La nuova password non rispetta i criteri.");
                return;
            }

            Eccezione ecc = utente.Login(utente.GetIdUtente(), vecchia);
            if (ecc.getErrorCode() != 0) {
                PopupError.mostraErrore("Vecchia password errata.");
                return;
            }

            Eccezione cambio = utente.ModificaPassword(nuova);
            if (cambio.getErrorCode() == 0) {
                JOptionPane.showMessageDialog(cambiaPasswordPanel, "Password modificata con successo!");
            } else {
                PopupError.mostraErrore("Errore durante la modifica: " + cambio.getMessage());
            }
        });

        return cambiaPasswordPanel;
    }

    private JPanel visualizzaDatiUtentePanel() {

        visualizzaDatiUtentePanel = new JPanel();
        Config.setPanel1(visualizzaDatiUtentePanel);
        visualizzaDatiUtentePanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        visualizzaDatiUtentePanel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Dati Utente", SwingConstants.CENTER);
        Config.setLabel1(title);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        visualizzaDatiUtentePanel.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(4, 2, 10, 10));
        Config.setPanel2(center);

        JLabel nomeLabel = new JLabel("Nome:", SwingConstants.RIGHT);
        JLabel nomeVal = new JLabel();
        Config.setLabel1(nomeLabel);
        Config.setLabel1(nomeVal);

        JLabel cognomeLabel = new JLabel("Cognome:", SwingConstants.RIGHT);
        JLabel cognomeVal = new JLabel();
        Config.setLabel1(cognomeLabel);
        Config.setLabel1(cognomeVal);

        JLabel emailLabel = new JLabel("Email:", SwingConstants.RIGHT);
        JLabel emailVal = new JLabel();
        Config.setLabel1(emailLabel);
        Config.setLabel1(emailVal);

        JLabel cfLabel = new JLabel("Codice Fiscale:", SwingConstants.RIGHT);
        JLabel cfVal = new JLabel();
        Config.setLabel1(cfLabel);
        Config.setLabel1(cfVal);

        center.add(nomeLabel);
        center.add(nomeVal);
        center.add(cognomeLabel);
        center.add(cognomeVal);
        center.add(emailLabel);
        center.add(emailVal);
        center.add(cfLabel);
        center.add(cfVal);

        visualizzaDatiUtentePanel.add(center, BorderLayout.CENTER);

        // Preleva i dati dell’utente corrente
        try {
            UtenteGestore u = UtenteGestore.GetInstance();
            u.GetDati(); // aggiorna i dati lato client
            nomeVal.setText(u.GetNome());
            cognomeVal.setText(u.GetCognome());
            emailVal.setText(u.GetEmail());
            cfVal.setText(u.GetCF());
        } catch (Exception e) {
            PopupError.mostraErrore("Impossibile recuperare i dati utente: " + e.getMessage());
        }

        return visualizzaDatiUtentePanel;
    }

    private JPanel eliminaAccountPanel() {

        eliminaAccountPanel = new JPanel();
        Config.setPanel1(eliminaAccountPanel);
        eliminaAccountPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        eliminaAccountPanel.setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Elimina Account", SwingConstants.CENTER);
        Config.setLabel1(title);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        eliminaAccountPanel.add(title, BorderLayout.NORTH);

        JTextArea infoArea = new JTextArea("ATTENZIONE: questa azione è irreversibile.\nTutti i dati associati al tuo account verranno eliminati.");
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setOpaque(false);
        Config.setTextArea1(infoArea);
        eliminaAccountPanel.add(infoArea, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        Config.setPanel1(buttonPanel);
        JButton conferma = new JButton("Conferma Eliminazione");
        JButton annulla = new JButton("Annulla");
        Config.setButton1(conferma);
        conferma.setBackground(new Color(117, 0, 0));
        Config.setButton1(annulla);

        buttonPanel.add(conferma);
        buttonPanel.add(annulla);
        eliminaAccountPanel.add(buttonPanel, BorderLayout.CENTER);

        conferma.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    eliminaAccountPanel,
                    "Sei sicuro di voler eliminare definitivamente il tuo account?",
                    "Conferma Eliminazione",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                Eccezione ecc = UtenteGestore.GetInstance().RimuoviUtente();
                if (ecc.getErrorCode() == 0) {
                    PopupError.mostraErrore("Account eliminato correttamente.");
                    // eventualmente chiudi l'app o torna alla schermata iniziale
                } else {
                    PopupError.mostraErrore("Errore durante l'eliminazione: " + ecc.getMessage());
                }
            }
        });

        annulla.addActionListener(e -> {
            showAccount();
        });

        return eliminaAccountPanel;
    }

    private JPanel aspettoPanel() {
        if (aspettoPanel == null) {
            aspettoPanel = new JPanel(new BorderLayout(10, 10));
            Config.setPanel1(aspettoPanel);

            JLabel title = new JLabel("Personalizza Aspetto", SwingConstants.CENTER);
            Config.setLabel1(title);
            aspettoPanel.add(title, BorderLayout.NORTH);

            JPanel centerPanel = new JPanel(new GridLayout(5, 2, 10, 10));
            Config.setPanel2(centerPanel);

            // Selettori colore
            JButton sfondoBtn = new JButton("Colore Sfondo");
            JButton sfondo2Btn = new JButton("Colore Sfondo 2");
            JButton sfondo3Btn = new JButton("Colore Sfondo 3");
            JButton testoBtn = new JButton("Colore Testo");
            JButton bottoneBtn = new JButton("Colore Bottone");
            JButton hoverBtn = new JButton("Colore Hover Bottone");
            Config.setButton1(sfondoBtn);
            Config.setButton1(sfondo2Btn);
            Config.setButton1(sfondo3Btn);
            Config.setButton1(testoBtn);
            Config.setButton1(bottoneBtn);
            Config.setButton1(hoverBtn);


            // Font selector
            String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            JComboBox<String> fontCombo = new JComboBox<>(fontNames);
            fontCombo.setSelectedItem(Config.FONT.getFamily());
            Config.setComboBox1(fontCombo);

            JSpinner fontSizeSpinner = new JSpinner(new SpinnerNumberModel(Config.FONT.getSize(), 8, 48, 1));
            Config.setSpinner1(fontSizeSpinner);
            // Aggiunta componenti
            JLabel f=new JLabel("Font:");
            Config.setLabel1(f);
            JLabel d=new JLabel("Grandezza Font:");
            Config.setLabel1(d);
            centerPanel.add(f);
            centerPanel.add(fontCombo);
            centerPanel.add(d);
            centerPanel.add(fontSizeSpinner);
            centerPanel.add(sfondoBtn);
            centerPanel.add(sfondo2Btn);
            centerPanel.add(sfondo3Btn);
            centerPanel.add(testoBtn);
            centerPanel.add(bottoneBtn);
            centerPanel.add(hoverBtn);

            aspettoPanel.add(centerPanel, BorderLayout.CENTER);

            JButton salvaBtn = new JButton("Applica Modifiche");
            Config.setButton1(salvaBtn);
            aspettoPanel.add(salvaBtn, BorderLayout.SOUTH);

            // Azioni
            sfondoBtn.addActionListener(e -> {
                Color newColor = JColorChooser.showDialog(aspettoPanel, "Seleziona Colore Sfondo", Config.COLORE_SFONDO);
                if (newColor != null) Config.COLORE_SFONDO = newColor;
            });
            sfondo2Btn.addActionListener(e -> {
                Color newColor = JColorChooser.showDialog(aspettoPanel, "Seleziona Colore Sfondo", Config.COLORE_SFONDO);
                if (newColor != null) Config.COLORE_SFONDO1 = newColor;
            });
            sfondo3Btn.addActionListener(e -> {
                Color newColor = JColorChooser.showDialog(aspettoPanel, "Seleziona Colore Sfondo", Config.COLORE_SFONDO);
                if (newColor != null) Config.COLORE_SFONDO2 = newColor;
            });

            testoBtn.addActionListener(e -> {
                Color newColor = JColorChooser.showDialog(aspettoPanel, "Seleziona Colore Testo", Config.COLORE_TEXT_1);
                if (newColor != null) Config.COLORE_TEXT_1 = newColor;
            });

            bottoneBtn.addActionListener(e -> {
                Color newColor = JColorChooser.showDialog(aspettoPanel, "Seleziona Colore Bottone", Config.COLORE_SFONDO_BOTTONE);
                if (newColor != null) Config.COLORE_SFONDO_BOTTONE = newColor;
            });

            hoverBtn.addActionListener(e -> {
                Color newColor = JColorChooser.showDialog(aspettoPanel, "Seleziona Colore Hover Bottone", Config.COLORE_HOVER_BOTTONE);
                if (newColor != null) Config.COLORE_HOVER_BOTTONE = newColor;
            });

            salvaBtn.addActionListener(e -> {
                String selectedFont = (String) fontCombo.getSelectedItem();
                int fontSize = (int) fontSizeSpinner.getValue();
                Config.FONT = new Font(selectedFont, Font.PLAIN, fontSize);
                JOptionPane.showMessageDialog(aspettoPanel, "Modifiche applicate. Riavvia per vederle ovunque.");
                gui.reloadAll();
            });
        }
        return aspettoPanel;
    }

}
