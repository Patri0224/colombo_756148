package bookRecommender;


import bookRecommender.rmi.ServerBookRecommenderInterface;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import static java.lang.System.exit;
public class Client {
    static String hostName = "127.0.0.1";
    static int PORT = 10001;


    public static void main( String[] args ) {
        try {
            Registry registry=java.rmi.registry.LocateRegistry.getRegistry(hostName,PORT);
            ServerBookRecommenderInterface stub = (ServerBookRecommenderInterface) registry.lookup("BookRecommender");
            System.out.println(stub.login("admin", "admin"));
            System.out.println(stub.login("admin", "admi"));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }

    }

}
