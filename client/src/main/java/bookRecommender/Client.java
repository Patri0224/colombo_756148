package bookRecommender;


import bookRecommender.eccezioni.Eccezione;
import bookRecommender.entita.Libri;
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

            //crezione GUI
            gui = new BookRecommender();
            gui.setVisible(true);

//test

            LibriRicercaGestore libriGestore = LibriRicercaGestore.GetInstance();
            Eccezione ecc = libriGestore.RicercaLibri("poet", "la", -1);
            int numLibri = libriGestore.GetNumeroLibri();
            System.out.println(ecc.toString());
            Libri[] libri = libriGestore.GetLibri();
            /*for (Libri lib : libri) {
                System.out.println(lib.toString());
            }*/

            ecc = libriGestore.RicercaLibri("poet", "la", -1, null, "poetry", -1, -1);
            System.out.println(ecc.toString());
            libri = libriGestore.GetLibri();
            for (Libri lib : libri) {
                System.out.println(lib.toString());
            }
            System.out.println("old:" + numLibri + "new:" + libriGestore.GetNumeroLibri());


            System.out.println("output: errorCode 0,1,errorCode 0,dati utente,errorCode 0,errorCode 1,dati -1 e null");
            UtenteGestore.CreateInstance(stub);
            utenteGestore = UtenteGestore.GetInstance();
            Eccezione e = utenteGestore.Login("colombo@g", "Patrizio.24");
            System.out.println(e.toString());
            /*e = utenteGestore.RimuoviUtente();
            System.out.println(e.toString());
            e = utenteGestore.Registrazione("colombo@g", "Patrizio.24", "CMLPRZ04B416W", "patrizio", "colombo");
            System.out.println(e.toString());
            if (!utenteGestore.UtenteLoggato()) {
                utenteGestore.Login("colombo@g", "Patrizio.24");
            }*/
            LibrerieGestore.CreateInstance(stub);
            LibrerieGestore librerieGestore = LibrerieGestore.GetInstance();
            ecc = librerieGestore.caricaLibrerie();
            System.out.println(ecc.toString());
            ecc = librerieGestore.AggiungiLibreria("prova1");
            System.out.println(ecc.toString());
            ecc = librerieGestore.AggiungiLibreria("prova2");
            System.out.println(ecc.toString());
            ecc = librerieGestore.caricaLibrerie();
            System.out.println(ecc.toString());
            Libri libro = libriGestore.GetLibri()[1];
            System.out.println(libro.toString());
            ecc = librerieGestore.AggiungiLibroALibreria("prova1", libro.getId());
            System.out.println(ecc.toString());
            System.out.println(librerieGestore.toString());
            /*Eccezione ecc = utenteGestore.Registrazione("colombo@g", "Patrizio.24", "CMLPRZ04B416W", "patrizio", "colombo");
            System.out.println(ecc.toString());
            System.out.println(utenteGestore.GetIdUtente("colombo@g"));
            ecc = utenteGestore.Login("colombo@g", "Patrizio.24");
            System.out.println(ecc.toString());
            System.out.println(utenteGestore.toString());
            utenteGestore.RimuoviUtente();
            ecc = utenteGestore.Login("colombo@g", "Patrizio.24");
            System.out.println(ecc.toString());
            System.out.println(utenteGestore.toString());*/


//fine test
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }

    }

}
