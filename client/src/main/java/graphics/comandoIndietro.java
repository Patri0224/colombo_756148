package graphics;

import java.util.Stack;


public class comandoIndietro {
    private static BookRecommender gui;
    private static Stack<oggettoIndietro> pila = new Stack<>();
    public static final String PAGINA_HOME = "Pagina Home";
    public static final String PAGINA_LIBRERIA = "Pagina Libreria";
    public static final String PAGINA_LIBRO = "Pagina Libro";
    public static final String PAGINA_AGGIUNGI_VALUTAZIONE = "Pagina Aggiungi Valutazione";
    public static final String PAGINA_AUTENTICAZIONE = "Pagina Autenticazione";

    private static void setGui(BookRecommender bookRecommender) {
        gui = bookRecommender;
        pila.clear();
        pila.push(new oggettoIndietro(PAGINA_HOME, ""));
    }

    public static void indietro() {
        if (pila.size() > 1) {
            oggettoIndietro oggetto = pila.pop();
            azioneIndietro(oggetto);
        }
    }

    public static void aggiungiOggetto(String paginaCorrente, String opzione) {
        pila.push(new oggettoIndietro(paginaCorrente, opzione));
    }


    private static void azioneIndietro(oggettoIndietro oggetto) {
        switch (oggetto.getPagina()) {
            case PAGINA_HOME : {

                break;
            }
            case PAGINA_LIBRERIA : {

                break;
            }
            case PAGINA_LIBRO : {

                break;
            }
            case PAGINA_AGGIUNGI_VALUTAZIONE : {

                break;
            }
            case PAGINA_AUTENTICAZIONE : {

                break;
            }
        }

    }


}
