package bookRecommender;

import bookRecommender.rmi.ServerBookRecommenderInterface;
import db.ConnectionManager;
import db.ConnectionProvider;
import db.DbBuilder;
import db.QueryList;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class Server extends UnicastRemoteObject {
    static int PORT = 10001;
    private QueryList queryList;
    private DbBuilder db;
    private ConnectionManager conMgr;

    private void inizializzaDatabase() {
        try {
            db = DbBuilder.getDbInstance();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void ottieniConnessioni() {
        try {
            conMgr = new ConnectionProvider();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public Server() throws RemoteException {
        inizializzaDatabase();
        ottieniConnessioni();
    }

    private void startServer() throws RemoteException {
        try {
            queryList = new QueryList(conMgr);
            ImpServer impServer = new ImpServer(queryList);
            ServerBookRecommenderInterface stub = (ServerBookRecommenderInterface) UnicastRemoteObject.exportObject(impServer, 0);
            Registry registry = java.rmi.registry.LocateRegistry.createRegistry(PORT);
            registry.rebind("BookRecommender", stub);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Server started on port " + PORT);
        try {
            new Server().startServer();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("Server ready");

    }
}


