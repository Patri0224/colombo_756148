package bookRecommender;

import bookRecommender.rmi.ServerBookRecommenderInterface;
import db.ConnectionProvider;
import db.DbBuilder;
import db.ConnectionManager;
import db.QueryList;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.SQLException;

public class Server extends UnicastRemoteObject {
    static int PORT = 10001;
    private QueryList queryList;
    private DbBuilder db;
    private ConnectionManager conMgr;

    private void inizializzaDatabase(){
        try{
            db = DbBuilder.getDbInstance();
        }
        catch(IOException | SQLException e){
            e.printStackTrace();
        }
    }
    private void ottieniConnessioni(){
        try{
            conMgr = new ConnectionProvider();
        }
        catch(IOException e){
        }
        catch(InterruptedException iex){
        }
    }
    private void query(){
        queryList = new QueryList(conMgr);
    }


    public Server() throws RemoteException {
        inizializzaDatabase();
        ottieniConnessioni();
    }

    private void startServer() throws RemoteException {
        try {
            ImpServer impServer = new ImpServer(queryList);
            Registry registry = java.rmi.registry.LocateRegistry.createRegistry(PORT);
            registry.rebind("BookRecommender", impServer);
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


