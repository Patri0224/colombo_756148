package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionProvider implements ConnectionManager {
    private LinkedBlockingQueue<Connection> connessioniDisponibili = new LinkedBlockingQueue<Connection>();
    private AtomicInteger connessioniFunzionanti = new AtomicInteger(0);
    private static final int maxTentativi = 100;
    private boolean inRicostruzione = false;


    public ConnectionProvider() throws IOException, InterruptedException {
        connectionProviderImpl();
    }

    private synchronized void connectionProviderImpl() throws IOException, InterruptedException {
        int targetFunzionanti = 50;
        int tentativi = 0;
        Connection c;
        while (connessioniFunzionanti.get() < targetFunzionanti && tentativi < maxTentativi) {
            try {
                tentativi++;
                c = Db.getConnection(1);
                if (c.isValid(1)) {
                    connessioniDisponibili.put(c);
                    connessioniFunzionanti.incrementAndGet();
                    System.out.println("Creazione connessione n." + tentativi + " riuscita");
                }
            } catch (SQLException e) {
                System.err.println("Creazione connessione n." + tentativi + " fallita, riprovo");
                System.err.println(e.getMessage());
            }
        }
        if (tentativi == maxTentativi) {
            throw new IOException("Troppi tentativi non riusciti. Riavviare il programma o il db");
        }
    }

    private synchronized void ricostruisci() throws IOException, InterruptedException {
        if (inRicostruzione) {
            return;
        }
        if (connessioniDisponibili.isEmpty() && connessioniFunzionanti.get() < 40) {
            inRicostruzione = true;
            try {
                connectionProviderImpl();
            } finally {
                inRicostruzione = false;
            }
        }
    }

    @Override
    public Connection getConnection() throws SQLException, InterruptedException, IOException {
        Connection c = null;
        for (int i = 0; i < 50; i++) {
            c = connessioniDisponibili.take();
            if (c.isValid(1)) {
                return c;
            }
            c.close();
            connessioniFunzionanti.decrementAndGet();
        }
        System.err.println("Nessuna connessione valida trovata. Ricostruzione buffer");
        ricostruisci();
        c = connessioniDisponibili.take();
        if (!c.isValid(1)) {
            c.close();
            connessioniFunzionanti.decrementAndGet();
            throw new SQLException("Riavviare il programma e il db");
        }
        return c;
    }


    @Override
    public void endConnection(Connection c) throws InterruptedException, SQLException {
        try {
            if (c.isValid(1)) {
                connessioniDisponibili.put(c);
            }
        } catch (SQLException ex) {
            System.err.println("Errore nella restituzione della connessione nel buffer");
            c.close();
            connessioniFunzionanti.decrementAndGet();
        }
    }
}
