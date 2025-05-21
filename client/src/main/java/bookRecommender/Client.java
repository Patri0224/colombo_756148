package bookRecommender;


import bookRecommender.eccezioni.Eccezione;
import bookRecommender.entita.Libri;
import bookRecommender.rmi.ServerBookRecommenderInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class Client {
    static String hostName = "127.0.0.1";
    static int PORT = 10001;
    static ServerBookRecommenderInterface stub;
    static UtenteGestore utenteGestore;

    public static void main(String[] args) {
        try {
            Registry registry = java.rmi.registry.LocateRegistry.getRegistry(hostName, PORT);
            stub = (ServerBookRecommenderInterface) registry.lookup("BookRecommender");
            Libri[] libri = stub.RicercaLibri("poet", "la", 0);
            for (Libri lib : libri) {
                System.out.println(lib.toString());
            }
            System.out.println("output: errorCode 0,1,errorCode 0,dati utente,errorCode 0,errorCode 1,dati -1 e null");
            utenteGestore = new UtenteGestore(stub);
            Eccezione e = utenteGestore.Login("colombo@g", "Patrizio.24");
            System.out.println(e.toString());
            e = utenteGestore.RimuoviUtente();
            System.out.println(e.toString());
            e = utenteGestore.Registrazione("colombo@g", "Patrizio.24", "CMLPRZ04B416W", "patrizio", "colombo");
            System.out.println(e.toString());
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

        } catch (RemoteException | SQLException | NotBoundException e) {
            throw new RuntimeException(e);
        }

    }

}
