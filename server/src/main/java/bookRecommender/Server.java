package bookRecommender;

import bookRecommender.rmi.ServerBookRecommenderInterface;
import db.DbBuilder;
import db.DbUtilityMethods;
import db.QueryList;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.SQLException;

public class Server extends UnicastRemoteObject implements ServerBookRecommenderInterface {
    static int PORT = 10001;
    private Connection CONNESSIONE_CHE_DOVETE_USARE;
    private QueryList queryList;

    public Server() throws RemoteException {
        inizializzaServerGetConnection();
    }

    private void startServer() throws RemoteException {
        try {
            Registry registry = java.rmi.registry.LocateRegistry.createRegistry(PORT);
            registry.rebind("BookRecommender", this);
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

    public Connection getCONNESSIONE_CHE_DOVETE_USARE() {
        return CONNESSIONE_CHE_DOVETE_USARE;
    }

    private void inizializzaServerGetConnection() {
        try {
            DbUtilityMethods dbm = DbBuilder.getDbInstance();
            this.CONNESSIONE_CHE_DOVETE_USARE = dbm.getconnBookRecommenderDB();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        queryList = new QueryList(CONNESSIONE_CHE_DOVETE_USARE);
    }
//kkkk

    @Override
    public boolean login(String userId, String password) throws RemoteException {
        if (userId.equals("admin") && password.equals("admin")) {
            return true;
        } else {
            return false;
        }
    }
}


