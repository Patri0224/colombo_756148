package graphics;

import bookRecommender.*;
import bookRecommender.entita.Libri;

import javax.swing.*;
import java.awt.*;

public class BookRecommender extends JFrame {
    private UtenteGestore utenteGestore;
    private LibriRicercaGestore libriRicercaGestore;
    private LibrerieGestore librerieGestore;
    private ConsigliGestore consigliGestore;
    private ValutazioniGestore valutazioniGestore;

    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private PaginaHome paginaHome;
    private Autenticazione autenticazione;
    private PaginaLibro paginaLibro;
    private PaginaLibreria paginaLibreria;
    private AggiungiValutazione aggiungiValutazione;

    public BookRecommender() {
        utenteGestore = UtenteGestore.GetInstance();
        libriRicercaGestore = LibriRicercaGestore.GetInstance();
        librerieGestore = LibrerieGestore.GetInstance();
        consigliGestore = ConsigliGestore.GetInstance();
        valutazioniGestore = ValutazioniGestore.GetInstance();

        frame = new JFrame("Book Recommender");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        paginaHome = new PaginaHome(this);
        paginaLibro = new PaginaLibro(this);
        paginaLibreria = new PaginaLibreria(this);
        autenticazione = new Autenticazione(this);
        aggiungiValutazione = new AggiungiValutazione(this);

        cardPanel.add(paginaHome, "HOME");
        cardPanel.add(paginaLibro, "LIBRO");
        cardPanel.add(paginaLibreria, "LIBRERIA");
        cardPanel.add(autenticazione, "AUTENTICAZIONE");
        cardPanel.add(aggiungiValutazione, "VALUTAZIONE");
        cardLayout.show(cardPanel, "HOME");
        frame.add(cardPanel);
        frame.setVisible(true);
    }

    public void showLibro(Libri libro, String PaginaPrecedente) {
        paginaLibro.setLibro(libro);
        cardLayout.show(cardPanel, "LIBRO");
    }

    public void showLogin(String paginaPrecende) {
        autenticazione.setRitorno(paginaPrecende);
        cardLayout.show(cardPanel, "LOGIN");
    }

    public void logout() {

    }

}
