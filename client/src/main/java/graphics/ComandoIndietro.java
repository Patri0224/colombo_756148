package graphics;

import javax.swing.*;
import java.util.Stack;

public class ComandoIndietro {

    private static BookRecommender gui;
    private static Stack<oggettoIndietro> pila = new Stack<>();
    public static final String PAGINA_HOME = "Pagina Home";
    public static final String PAGINA_LIBRERIA = "Pagina Libreria";
    public static final String PAGINA_LIBRO = "Pagina Libro";
    public static final String PAGINA_AGGIUNGI_VALUTAZIONE = "Pagina Aggiungi Valutazione";
    public static final String PAGINA_AUTENTICAZIONE = "Pagina Autenticazione";

    public static void setGui() {
        gui = BookRecommender.GetInstance();
        pila.clear();
        pila.push(new oggettoIndietro(PAGINA_HOME, ""));
    }

    public static void indietro() {
        if (pila.size() > 2) {
            pila.pop();
            oggettoIndietro oggetto = pila.pop();
            azioneIndietro(oggetto);
        }
    }

    public static void aggiungiOggetto(String paginaCorrente, String opzione) {
        pila.push(new oggettoIndietro(paginaCorrente, opzione));
    }


    private static void azioneIndietro(oggettoIndietro oggetto) {
        switch (oggetto.getPagina()) {
            case PAGINA_HOME: {
                gui.showHome();
                break;
            }
            case PAGINA_LIBRERIA: {
                gui.showLibreria(oggetto.getOpzione());
                break;
            }
            case PAGINA_LIBRO: {
                gui.showLibro(oggetto.getOpzione());
                break;
            }
            case PAGINA_AGGIUNGI_VALUTAZIONE: {
                gui.showValutazione(oggetto.getOpzione());
                break;
            }
            case PAGINA_AUTENTICAZIONE: {
                gui.showLogin();
                break;
            }
        }

    }

    public static JButton getBottoneIndietro() {
        JButton bottone = new JButton("Back");
        bottone.addActionListener(e -> indietro());
        Config.setButton1(bottone);
        return bottone;
    }
    public static JButton getBottoneHome() {
        JButton bottone = new JButton("Home");
        bottone.addActionListener(e -> gui.showHome());
        Config.setButton1(bottone);
        return bottone;
    }
}


