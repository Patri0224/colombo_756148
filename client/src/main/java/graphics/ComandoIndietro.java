/*
 * Autori del progetto:
 * Nome: Colombo     Cognome: Patrizio     Matricola: 756148     Sede: CO
 * Nome: Felitti     Cognome: Fabio        Matricola: 758058     Sede: CO
 * Nome: Franchi     Cognome: Matteo       Matricola: 757075     Sede: CO
 * Nome: Iacono      Cognome: Alessandro   Matricola: 758451     Sede: CO
 */
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
    public static final String PAGINA_IMPOSTAZIONI = "Pagina Impostazioni";
    public static final String PAGINA_AGGIUNGI_CONSIGLIO = "Pagina Aggiungi Consiglio";

    public static void setGui() {
        gui = BookRecommender.GetInstance();
        pila.clear();
        pila.push(new oggettoIndietro(PAGINA_HOME, ""));
    }

    public static void indietro() {
        System.out.println(pila.toString());
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
            case PAGINA_IMPOSTAZIONI: {
                gui.showImpostazioni();
                break;
            }
            case PAGINA_AGGIUNGI_CONSIGLIO: {
                gui.showAggiungiConsiglio(-1);
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

    public static void indietroI() {
        System.out.println(pila.toString());
        if (pila.size() > 2) {
            oggettoIndietro oggetto = pila.pop();
            azioneIndietro(oggetto);
        }
    }
}


