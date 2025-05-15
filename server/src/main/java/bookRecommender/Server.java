package bookRecommender;

import bookRecommender.rmi.ServerBookRecommenderInterface;
import db.DbMethods;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import static db.DbBuilder.getDbInstance;

public class Server extends UnicastRemoteObject implements ServerBookRecommenderInterface {
    static int PORT = 10001;
    private DbMethods dbm;

    public Server() throws RemoteException {
        //connect to server
        //connectionToDB();
    }

    private void startServer() throws RemoteException {
        try {
            Registry registry = java.rmi.registry.LocateRegistry.createRegistry(PORT);
            registry.rebind("BookRecommender", this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String args[]) {
        System.out.println("Server started on port " + PORT);
        try {
            new Server().startServer();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("Server ready");


    }

    private void connectionToDB() {
        try {
            dbm=getDbInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean login(String userId, String password) throws RemoteException {
        if (userId.equals("admin") && password.equals("admin")) {
            return true;
        } else {
            return false;
        }
    }
}


