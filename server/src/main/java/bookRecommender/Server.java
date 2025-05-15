package bookRecommender;

import bookRecommender.rmi.ServerBookRecommenderInterface;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements ServerBookRecommenderInterface {
    static int PORT = 10001;

    public Server() throws RemoteException {
//connect to server
        super();
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


    @Override
    public boolean login(String userId, String password) throws RemoteException {
        if (userId.equals("admin") && password.equals("admin")) {
            return true;
        } else {
            return false;
        }
    }
}


