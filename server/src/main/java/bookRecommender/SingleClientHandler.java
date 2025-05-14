package bookRecommender;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SingleClientHandler implements Runnable {
    private Socket socket;

    public SingleClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {

            //Scanner in = new Scanner(socket.getInputStream());
            //PrintWriter out = new PrintWriter(socket.getOutputStream());
            ObjectInputStream in = null;
            ObjectOutputStream out = null;

            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            int[] receivedArray = null;
            int[] evenArray = null;

            while (true) {
                int evenCounter = 0;

                //leggi array ricevuto
                receivedArray = (int[]) in.readObject();
                if (receivedArray == null)
                    break;
                for (int i = 0; i < receivedArray.length; i++) {
                    System.out.print(receivedArray[i] + " ");

                    if (receivedArray[i] % 2 == 0)
                        evenCounter++; //conta i pari
                }
                System.out.println();

                //manda i valori pari
                evenArray = new int[evenCounter];
                for (int i = 0, j = 0; i < receivedArray.length; i++) {
                    if (receivedArray[i] % 2 == 0) {
                        evenArray[j++] = receivedArray[i];
                    }
                }
                out.writeObject(evenArray);
                out.writeObject(null); //fine della mia comunicazione
                out.flush();
                break;
            }
            // Chiudo gli stream e il socket
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

