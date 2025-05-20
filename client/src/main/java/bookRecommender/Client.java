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

            utenteGestore = new UtenteGestore(stub);
            Eccezione ecc = utenteGestore.Registrazione("colombo@g", "Patrizio.24", "CMLPRZ04B416W", "patrizio", "colombo");
            System.out.println(ecc.toString());
            System.out.println(utenteGestore.GetIdUtente("colombo@g"));
            utenteGestore.GetDati();
            System.out.println(utenteGestore.toString());

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
