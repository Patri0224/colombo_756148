package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Consente la connessione con il {@code bookrecommenderdb}
 */
public class ConnectionProvider implements ConnectionManager {
    private final LinkedBlockingQueue<Connection> connessioniDisponibili = new LinkedBlockingQueue<>();
    private final Thread supervisore;
    private final int targetFunzionanti = 50;
    private final int minimeFunzionanti = 40;
    private final ReentrantLock lockControllo = new ReentrantLock();

    private class PoolChecker implements Runnable{
        public void run(){
            try {
                controlloPool();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Controlla ogni 10 secondi che nel pool di connessioni ce ne siano almeno {@code minimeFunzionanti} funzionanti.
         * Se no chiude quelle che non funzionano e ne ricrea funzionanti in numero uguale, rimettendole infine nel pool
         */
        private void controlloPool() throws IOException{
            while(true){
                try{
                    Thread.sleep(10000);
                    lockControllo.lock();

                    try{
                        int valide = 0;
                        ArrayList<Connection> temp = new ArrayList<>();

                        connessioniDisponibili.drainTo(temp); //Non bloccante
                        for(Connection c : temp){
                            if(c.isValid(1)){
                                valide++;
                                connessioniDisponibili.put(c);
                            }
                            else{
                                c.close();
                            }
                        }
                        if(valide < minimeFunzionanti){
                            for(int i = valide; i<targetFunzionanti; i++){
                                aggiungiConnessione();
                            }
                        }
                    }
                    catch(SQLException e){
                        e.printStackTrace();
                    }
                }
                catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                    break;
                }
                finally{
                    lockControllo.unlock();
                }
            }
        }

    }

    /**
     * Costruisce un pool di n. {@code targetFunzionanti} connessioni e attiva il thread supervisore, che controlla periodicamente
     * la struttura dati
     *
     * @throws IOException propagata da {@link ConnectionProvider#aggiungiConnessione()}
     * @throws InterruptedException propagata da {@link ConnectionProvider#aggiungiConnessione()}
     */
    public ConnectionProvider() throws IOException, InterruptedException {
        for(int i=0; i<targetFunzionanti; i++){
            aggiungiConnessione();
            System.out.println("Creazione connessione n. "+(i+1)+" riuscita");
        }
        supervisore = new Thread(new PoolChecker());
        supervisore.start();
    }

    /**
     * Ottiene una connessione dal {@code bookrecommenderdb} e se è valida la inserisce nel pool
     *
     * @throws IOException propagata da {@link Db#getConnection(int)}
     * @throws InterruptedException causata da {@link LinkedBlockingQueue#put(Object)}
     */
    private void aggiungiConnessione() throws IOException, InterruptedException {
        try{
            Connection c = Db.getConnection(1);
            if(c.isValid(1)){
                connessioniDisponibili.put(c); //Bloccante
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Preleva connessioni dal pool finchè non ne trova una funzionante. Quelle non funzionanti vengono chiuse
     *
     * @return {@code Connection} funzionante per fare query
     * @throws InterruptedException causata da {@link LinkedBlockingQueue#take()}
     */
    public Connection getConnection() throws InterruptedException {
        lockControllo.lock();
        try{
            while(true){
                try{
                    Connection c = connessioniDisponibili.take();
                    if(c.isValid(1)){
                        return c;
                    }
                    else{
                        c.close();
                    }
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
        finally {
            lockControllo.unlock();
        }
    }

    /**
     * Restituisce al pool la connessione {@code c}, se funziona. Se no la chiude
     *
     * @param c la {@code Connection} che si vuole restituire
     * @throws InterruptedException causata da {@link LinkedBlockingQueue#put(Object)}
     */
    public void endConnection(Connection c) throws InterruptedException{
        lockControllo.lock();
        try{
            try{
                if(c.isValid(1)){
                    connessioniDisponibili.put(c);
                }
                else{
                    c.close();
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        finally{
            lockControllo.unlock();
        }
    }

}