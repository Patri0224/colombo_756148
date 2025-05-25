package graphics;

import bookRecommender.UtenteGestore;
import bookRecommender.eccezioni.Eccezione;

import javax.swing.*;
import java.awt.*;

public class Autenticazione extends JPanel {


    private BookRecommender gui;
    private JPanel panelLogin;
    private JPanel panelRegistrazione;
    private UtenteGestore utente;

    private JPasswordField inputPasswordRegistrazione;
    private JTextField inputEmailRegistrazione;
    private JTextField inputCodiceFiscaleRegistrazione;
    private JTextField inputNomeRegistrazione;
    private JTextField inputCognomeRegistrazione;
    private JLabel errEmailRegistrazione;
    private JLabel errPasswordRegistrazione;
    private JLabel errCodiceFiscaleRegistrazione;
    private JLabel errNomeRegistrazione;
    private JLabel errCognomeRegistrazione;

    private JTextField inputEmailLogin;
    private JLabel errEmailLogin;
    private JPasswordField inputPasswordLogin;
    private JLabel errPasswordLogin;


    public Autenticazione() {
        gui = BookRecommender.GetInstance();
        Config.setPanel1(this);
        setLayout(new BorderLayout(1, 1));
        JPanel menuRidotto = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Config.setPanel1(menuRidotto);
        JLabel t = new JLabel("Login e Registrazione");
        Config.setLabel1(t);
        menuRidotto.add(ComandoIndietro.getBottoneHome());
        menuRidotto.add(ComandoIndietro.getBottoneIndietro());
        menuRidotto.add(t);
        add(menuRidotto, BorderLayout.NORTH);
        JPanel main = new JPanel(new GridLayout(2, 3));
Config.setPanel1(main);
        panelLogin = new JPanel();
        panelRegistrazione = new JPanel();
        Config.setPanel2(panelLogin);
        Config.setPanel2(panelRegistrazione);
        panelLogin.setLayout(new BoxLayout(panelLogin, BoxLayout.Y_AXIS));
        panelRegistrazione.setLayout(new BoxLayout(panelRegistrazione, BoxLayout.Y_AXIS));

        JPanel em = new JPanel();
        JPanel p = new JPanel();
        JPanel b = new JPanel();
        Config.setPanel3(em);
        Config.setPanel3(p);
        Config.setPanel3(b);
        em.setLayout(new GridLayout(1, 3));
        p.setLayout(new GridLayout(1, 3));
        b.setLayout(new BorderLayout());
        JLabel email = new JLabel("Email o Id: ");
        JLabel password = new JLabel("Password: ");
        Config.setLabel1(email);
        Config.setLabel1(password);

        JPanel nomeRegistrazione = new JPanel();
        JPanel cognomeRegistrazione = new JPanel();
        JPanel codFiscaleRegistrazione = new JPanel();
        JPanel emailRegistrazione = new JPanel();
        JPanel passwordRegistrazione = new JPanel();
        JPanel btnRegistrazione = new JPanel();
        Config.setPanel3(nomeRegistrazione);
        Config.setPanel3(cognomeRegistrazione);
        Config.setPanel3(codFiscaleRegistrazione);
        Config.setPanel3(emailRegistrazione);
        Config.setPanel3(passwordRegistrazione);
        Config.setPanel3(btnRegistrazione);
        nomeRegistrazione.setLayout(new GridLayout(1, 3));
        cognomeRegistrazione.setLayout(new GridLayout(1, 3));
        codFiscaleRegistrazione.setLayout(new GridLayout(1, 3));
        emailRegistrazione.setLayout(new GridLayout(1, 3));
        passwordRegistrazione.setLayout(new GridLayout(1, 3));
        btnRegistrazione.setLayout(new BorderLayout());
        JLabel emailR = new JLabel("Email: ");
        JLabel passwordR = new JLabel("Password: ");
        JLabel nome = new JLabel("Nome: ");
        JLabel codFiscale = new JLabel("Cod. Fiscale: ");
        JLabel cognome = new JLabel("Cognome: ");
        Config.setLabel1(emailR);
        Config.setLabel1(passwordR);
        Config.setLabel1(nome);
        Config.setLabel1(cognome);
        Config.setLabel1(codFiscale);

        inputEmailRegistrazione = new JTextField();
        inputPasswordRegistrazione = new JPasswordField();
        inputCodiceFiscaleRegistrazione = new JTextField();
        inputNomeRegistrazione = new JTextField();
        inputCognomeRegistrazione = new JTextField();
        Config.setTextField1(inputEmailRegistrazione);
        Config.setTextField1(inputPasswordRegistrazione);
        Config.setTextField1(inputNomeRegistrazione);
        Config.setTextField1(inputCognomeRegistrazione);
        Config.setTextField1(inputCodiceFiscaleRegistrazione);

        errEmailRegistrazione = new JLabel();
        errPasswordRegistrazione = new JLabel();
        errCodiceFiscaleRegistrazione = new JLabel();
        errNomeRegistrazione = new JLabel();
        errCognomeRegistrazione = new JLabel();

        Config.setLabelError(errEmailRegistrazione);
        Config.setLabelError(errPasswordRegistrazione);
        Config.setLabelError(errCodiceFiscaleRegistrazione);
        Config.setLabelError(errNomeRegistrazione);
        Config.setLabelError(errCognomeRegistrazione);

        nomeRegistrazione.add(nome);
        nomeRegistrazione.add(inputNomeRegistrazione);
        nomeRegistrazione.add(errNomeRegistrazione);

        cognomeRegistrazione.add(cognome);
        cognomeRegistrazione.add(inputCognomeRegistrazione);
        cognomeRegistrazione.add(errCognomeRegistrazione);

        codFiscaleRegistrazione.add(codFiscale);
        codFiscaleRegistrazione.add(inputCodiceFiscaleRegistrazione);
        codFiscaleRegistrazione.add(errCodiceFiscaleRegistrazione);

        emailRegistrazione.add(emailR);
        emailRegistrazione.add(inputEmailRegistrazione);
        emailRegistrazione.add(errEmailRegistrazione);

        passwordRegistrazione.add(passwordR);
        passwordRegistrazione.add(inputPasswordRegistrazione);
        passwordRegistrazione.add(errPasswordRegistrazione);

        JButton bottoneRegistrazione = new JButton("Registrati");
        bottoneRegistrazione.addActionListener(e -> ControlloRegistrazione());
        btnRegistrazione.add(bottoneRegistrazione, BorderLayout.NORTH);
        panelRegistrazione.add(nomeRegistrazione);
        panelRegistrazione.add(cognomeRegistrazione);
        panelRegistrazione.add(codFiscaleRegistrazione);
        panelRegistrazione.add(emailRegistrazione);
        panelRegistrazione.add(passwordRegistrazione);
        panelRegistrazione.add(btnRegistrazione);
        main.add(new JLabel());
        main.add(panelRegistrazione);
        main.add(new JLabel());


        inputEmailLogin = new JTextField();
        inputPasswordLogin = new JPasswordField();
        Config.setTextField1(inputEmailLogin);
        Config.setTextField1(inputPasswordLogin);

        errEmailLogin = new JLabel();
        errPasswordLogin = new JLabel();
        inputEmailLogin.setEditable(true);
        inputPasswordLogin.setEditable(true);
        Config.setLabelError(errEmailLogin);
        Config.setLabelError(errPasswordLogin);

        em.add(email);
        em.add(inputEmailLogin);
        em.add(errEmailLogin);
        p.add(password);
        p.add(inputPasswordLogin);
        p.add(errPasswordLogin);
        JButton bottoneLogin = new JButton("Login");
        bottoneLogin.addActionListener(e -> controlloLogin());
        b.add(bottoneLogin, BorderLayout.NORTH);
        panelLogin.add(em);
        panelLogin.add(p);
        panelLogin.add(b);
        main.add(new JLabel());
        main.add(panelLogin);
        main.add(new JLabel());
        add(main, BorderLayout.CENTER);

    }


    public void controlloLogin() {
        errEmailLogin.setText("");
        errPasswordLogin.setText("");
        UtenteGestore utenteGestore = UtenteGestore.GetInstance();
        if (inputEmailLogin.getText().trim().isEmpty()) {
            errEmailLogin.setText("Campo vuoto");
        }
        if (inputPasswordLogin.getText().trim().isEmpty()) {
            errPasswordLogin.setText("Campo vuoto");
        }
        if (isNumeroIntero(inputEmailLogin.getText())) {
            if (!UtenteGestore.ControlloCaratteriPassword(inputPasswordLogin.getText())) {
                errPasswordLogin.setText("Mancano caratteri");
                return;
            }
            Eccezione ecc = utenteGestore.Login(Integer.parseInt(inputEmailLogin.getText()), inputPasswordLogin.getText());
            if (ecc.getErrorCode() == 0) {
                gui.reloadHome();
                ComandoIndietro.indietro();
            } else if (ecc.getErrorCode() == 2) {
                errPasswordLogin.setText(ecc.getErrorCode() + ecc.getMessage());
            } else {
                errEmailLogin.setText(ecc.getErrorCode() + ecc.getMessage());
            }

        } else {
            if (!UtenteGestore.ControlloCaratteriPassword(inputPasswordLogin.getText())) {
                errPasswordLogin.setText("Mancano caratteri");
                return;
            }
        }
        Eccezione ecc = utenteGestore.Login(inputEmailLogin.getText(), inputPasswordLogin.getText());
        if (ecc.getErrorCode() == 0) {
            gui.reloadHome();
            ComandoIndietro.indietro();
        } else if (ecc.getErrorCode() == 2) {
            errPasswordLogin.setText(ecc.getErrorCode() + ecc.getMessage());
        } else {
            errEmailLogin.setText(ecc.getErrorCode() + ecc.getMessage());
        }
    }

    public void ControlloRegistrazione() {
        errEmailRegistrazione.setText("");
        errNomeRegistrazione.setText("");
        errCognomeRegistrazione.setText("");
        errCodiceFiscaleRegistrazione.setText("");
        errPasswordRegistrazione.setText("");
        UtenteGestore utenteGestore = UtenteGestore.GetInstance();
        if (inputEmailRegistrazione.getText().trim().isEmpty()) {
            errEmailRegistrazione.setText("Campo vuoto");
        }
        if (inputPasswordRegistrazione.getText().trim().isEmpty()) {
            errPasswordRegistrazione.setText("Campo vuoto");
        }
        if (inputNomeRegistrazione.getText().trim().isEmpty()) {
            errNomeRegistrazione.setText("Campo vuoto");
        }
        if (inputCognomeRegistrazione.getText().trim().isEmpty()) {
            errCognomeRegistrazione.setText("Campo vuoto");
        }
        if (inputCodiceFiscaleRegistrazione.getText().trim().isEmpty()) {
            errCodiceFiscaleRegistrazione.setText("Campo vuoto");
        }
        if (!UtenteGestore.ControlloCaratteriPassword(inputPasswordRegistrazione.getText())) {
            errPasswordRegistrazione.setText("Mancano caratteri");
        }
        Eccezione e = utenteGestore.Registrazione(inputEmailRegistrazione.getText(),inputPasswordRegistrazione.getText(),inputCodiceFiscaleRegistrazione.getText(),inputNomeRegistrazione.getText(),inputCognomeRegistrazione.getText());
        if (e.getErrorCode() == 0) {
            gui.reloadHome();
            ComandoIndietro.indietro();
        } else if (e.getErrorCode() == 1) {
            errEmailRegistrazione.setText(e.getErrorCode() + " " + e.getMessage());
        }
    }


    public boolean isNumeroIntero(String str) {
        return str.matches("\\d+");
    }
}
