package bookRecommender;


import bookRecommender.rmi.ServerBookRecommenderInterface;
import graphics.BookRecommender;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class Client {
    static String hostName = "127.0.0.1";
    static int PORT = 10001;
    static ServerBookRecommenderInterface stub;
    static UtenteGestore utenteGestore;
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
            UtenteGestore u = UtenteGestore.GetInstance();
            //u.Login("falafel@g","Ciao.987");
            //u.Registrazione("falafel@g","Ciao.987","FFL78C","Fabio", "Falafel");
            //crezione GUI
            gui = BookRecommender.CreateInstance();
            gui.creaGrafica(gui);


        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }

    }

}
