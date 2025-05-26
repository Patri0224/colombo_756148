package graphics;

import bookRecommender.*;

import javax.swing.*;
import java.awt.*;

public class BookRecommender extends JFrame {
    private UtenteGestore utenteGestore;
    private LibriRicercaGestore libriRicercaGestore;
    private LibrerieGestore librerieGestore;
    private ConsigliGestore consigliGestore;
    private ValutazioniGestore valutazioniGestore;

    private static BookRecommender instance = null;

    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private PaginaHome paginaHome;
    private Autenticazione autenticazione;
    private PaginaLibro paginaLibro;
    private PaginaLibreria paginaLibreria;
    private AggiungiValutazione aggiungiValutazione;
    private PaginaIntermedia paginaIntermedia;
    private Impostazioni paginaImpostazioni;

    public BookRecommender() {

    }

    public static BookRecommender CreateInstance() {
        if (instance == null) {
            instance = new BookRecommender();
        }
        return instance;
    }

    public static BookRecommender GetInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    public void creaGrafica(BookRecommender bookRecommender) {
        utenteGestore = UtenteGestore.GetInstance();
        libriRicercaGestore = LibriRicercaGestore.GetInstance();
        librerieGestore = LibrerieGestore.GetInstance();
        consigliGestore = ConsigliGestore.GetInstance();
        valutazioniGestore = ValutazioniGestore.GetInstance();

        frame = new JFrame("Book Recommender");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.getContentPane().setBackground(Config.COLORE_SFONDO);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        Config.setPanel1(cardPanel);
        ComandoIndietro.setGui();
        paginaHome = new PaginaHome();
        paginaLibro = new PaginaLibro();
        paginaLibreria = new PaginaLibreria();
        autenticazione = new Autenticazione();
        aggiungiValutazione = new AggiungiValutazione();
        paginaIntermedia = new PaginaIntermedia();
        paginaImpostazioni = new Impostazioni();

        cardPanel.add(paginaHome, "HOME");
        cardPanel.add(paginaLibro, "LIBRO");
        cardPanel.add(paginaLibreria, "LIBRERIA");
        cardPanel.add(autenticazione, "AUTENTICAZIONE");
        cardPanel.add(aggiungiValutazione, "VALUTAZIONE");
        cardPanel.add(paginaIntermedia, "I");
        cardPanel.add(paginaImpostazioni, "IMPOSTAZIONI");
        showHome();
        frame.add(cardPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void reloadHome() {
        paginaHome.reload();
    }

    public void showHome() {
        showI();
        ComandoIndietro.aggiungiOggetto(ComandoIndietro.PAGINA_HOME, "");
        paginaHome.reload();
        cardLayout.show(cardPanel, "HOME");
    }

    public void showLibro(String idlibro) {
        showI();//inserito per permettere il passaggio da pagina alla stessa pagina//inserito per permettere il passaggio da pagina libro a un altra pagina libro
        ComandoIndietro.aggiungiOggetto(ComandoIndietro.PAGINA_LIBRO, idlibro);
        paginaLibro.setLibro(Integer.parseInt(idlibro));
        cardLayout.show(cardPanel, "LIBRO");
    }

    public void showLogin() {
        showI();
        ComandoIndietro.aggiungiOggetto(ComandoIndietro.PAGINA_AUTENTICAZIONE, "");
        cardLayout.show(cardPanel, "AUTENTICAZIONE");
    }

    public void showLibreria(String nomeLibreria) {
        showI();//inserito per permettere il passaggio da pagina alla stessa pagina
        ComandoIndietro.aggiungiOggetto(ComandoIndietro.PAGINA_LIBRERIA, nomeLibreria);
        paginaLibreria.setLibreria(nomeLibreria);
        cardLayout.show(cardPanel, "LIBRERIA");
    }

    public void showValutazione(String idlibro) {
        showI();
        ComandoIndietro.aggiungiOggetto(ComandoIndietro.PAGINA_LIBRERIA, idlibro);
        cardLayout.show(cardPanel, "LIBRERIA");
    }

    public void showImpostazioni() {
        showI();
        paginaImpostazioni.reload();
        ComandoIndietro.aggiungiOggetto(ComandoIndietro.PAGINA_IMPOSTAZIONI, "");
        cardLayout.show(cardPanel, "IMPOSTAZIONI");
    }

    public void showI() {
        cardLayout.show(cardPanel, "I");
    }


    public void logout(String titoloPagina) {
        utenteGestore.Logout();
        showI();//inserito per permettere il passaggio da pagina alla stessa pagina
        if (titoloPagina == ComandoIndietro.PAGINA_LIBRERIA || titoloPagina == ComandoIndietro.PAGINA_AGGIUNGI_VALUTAZIONE) {
            ComandoIndietro.setGui();
        } else {
            ComandoIndietro.indietroI();
        }
    }


    public void reloadAll() {
        paginaImpostazioni.reload();
        reloadHome();
        int idLibro = paginaLibro.getLibro();
        paginaLibro.setLibro(idLibro);

        String idLibreria = paginaLibreria.getNomeLibreria();
        paginaLibreria.setLibreria(idLibreria);

        autenticazione.reload();
    }

    public void refresh() {

    }
}
