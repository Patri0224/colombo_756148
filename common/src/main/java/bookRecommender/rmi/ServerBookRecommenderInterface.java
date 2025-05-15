package bookRecommender.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerBookRecommenderInterface extends Remote {

    boolean login(String userId, String password) throws RemoteException;

}
