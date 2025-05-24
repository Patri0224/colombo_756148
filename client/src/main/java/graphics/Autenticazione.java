package graphics;

import bookRecommender.UtenteGestore;
import bookRecommender.eccezioni.Eccezione;

import javax.swing.*;
import java.awt.*;

public class Autenticazione extends JPanel {
    private BookRecommender gui;
    private String paginaPrecedente;
    private JPanel panelLogin;
    private JPanel panelRegistrazione;
    private UtenteGestore utente;

    private JLabel email;
    private JTextField inputEmail;
    private JLabel eccezioneEmail;

    private JLabel password;
    private JTextField inputPassword;
    private JLabel eccezionePassword;
    private JButton bottoneLogin;


    public Autenticazione() {
        gui = BookRecommender.GetInstance();
        Config.setPanel1(this);
        setLayout(new BorderLayout(1, 1));
        JPanel menuRidotto = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Config.setPanel1(menuRidotto);
        menuRidotto.add(ComandoIndietro.getBottoneHome());
        menuRidotto.add(ComandoIndietro.getBottoneIndietro());
        menuRidotto.add(new JLabel("Login e Regitrazione"));
        add(menuRidotto, BorderLayout.NORTH);
        panelRegistrazione = new JPanel();
        panelLogin = new JPanel();
        Config.setPanel1(panelLogin);
        panelLogin.setLayout(new GridLayout(3, 3));
        email = new JLabel("Email o Id: ");
        password = new JLabel("Password: ");
        bottoneLogin = new JButton("Login");
        bottoneLogin.addActionListener(e -> controlloLogin());


    }

    public void setRitorno(String paginaPrecedente) {
        this.paginaPrecedente = paginaPrecedente;
    }

    public void controlloLogin() {
        UtenteGestore utenteGestore = UtenteGestore.GetInstance();
        if (email.getText() == null) {
            eccezioneEmail.setText("Campo vuoto");
            return;
        }
        if (password.getText() == null) {
            eccezionePassword.setText("Campo vuoto");
            return;
        }
        if (isNumeroIntero(email.getText())) {
            if (!UtenteGestore.ControlloCaratteriPassword(password.getText())) {
                eccezionePassword.setText("Mancano caratteri");
                return;
            }
            Eccezione ecc = utenteGestore.Login(Integer.parseInt(email.getText()), password.getText());
            if (ecc.getErrorCode() == 0) {
                ComandoIndietro.indietro();
            }

        } else {
            if (!UtenteGestore.ControlloCaratteriPassword(password.getText())) {
                eccezionePassword.setText("Mancano caratteri");
                return;
            }
        }

    }

    public boolean isNumeroIntero(String str) {
        return str.matches("\\d+");
    }
}
