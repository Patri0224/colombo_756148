package bookRecommender;
/*
 * Autori del progetto:
 * Nome: Colombo     Cognome: Patrizio     Matricola: 756148     Sede: CO
 * Nome: Felitti     Cognome: Fabio        Matricola: 758058     Sede: CO
 * Nome: Franchi     Cognome: Matteo       Matricola: 757075     Sede: CO
 * Nome: Iacono      Cognome: Alessandro   Matricola: 758451     Sede: CO
 */


import bookRecommender.rmi.ServerBookRecommenderInterface;
import graphics.BookRecommender;
import graphics.PopupError;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

/**
 * Classe Client, contiene il main per l'avvio del client.
 * Collega il client al server RMI e crea le istanze dei gestori e della GUI.
 */
public class Client {
    static String hostName = "127.0.0.1";
    static int PORT = 10001;
    static ServerBookRecommenderInterface stub;
    static BookRecommender gui;

    public static void main(String[] args) {
        try {
            //collegamento con il server
            Registry registry = java.rmi.registry.LocateRegistry.getRegistry(hostName, PORT);
            stub = (ServerBookRecommenderInterface) registry.lookup("BookRecommender");
            //creazione componenti client
            LibriRicercaGestore.CreateInstance(stub);
            UtenteGestore.CreateInstance(stub);
            LibrerieGestore.CreateInstance(stub);
            ConsigliGestore.CreateInstance(stub);
            ValutazioniGestore.CreateInstance(stub);

            //creazione GUI
            gui = BookRecommender.CreateInstance();
            gui.creaGrafica();


        } catch (RemoteException | NotBoundException e) {
            PopupError.mostraErrore(e.getMessage());
            throw new RuntimeException(e);
        }

    }

}
