package com.example;


import java.io.*;
import java.net.Socket;

import static java.lang.System.exit;
public class Client {
    static String hostName = "localhost";
    static int portNumber = 1234;
    //static PrintWriter out;
    static ObjectOutputStream out;
    static String clientName;

    public static void main( String[] args ) {
        clientName = "CLIENT";
        System.out.println("Client " +clientName + " started");
        ObjectInputStream in = setup();
        if (in == null){
            exit(0);
        }
        readFromNetworkThreaded(in);
        sendNumbers();
    }

    static void sendNumbers(){
        Thread th = new Thread(
                () -> {
                    int[] array = new int[100];

                    for (int i = 0; i < 100; i++) {
                        array[i] = i;
                    }
                    try {
                        out.writeObject(array);
                        out.writeObject(null);//fine della mia comunicazione
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }// end of λ
        );
        th.start();
    }

    static ObjectInputStream setup() {
        //BufferedReader in = null;
        ObjectInputStream in = null;
        Socket kkSocket = null;
        try {
            kkSocket = new Socket(hostName, portNumber);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            //out = new PrintWriter(kkSocket.getOutputStream(), true);
            out = new ObjectOutputStream(kkSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            //in = new BufferedReader(
            //        new InputStreamReader(kkSocket.getInputStream()));
            in = new ObjectInputStream(kkSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return in;
    }

    static void readFromNetworkThreaded( ObjectInputStream in ) {
        Thread th = new Thread(
                () -> {
                    readFromNetwork(in);
                }// end of λ
        );
        th.start();
    }

    static void readFromNetwork( ObjectInputStream in ){
        int[] receivedArray = null;

        while (true) {
            try {
                //leggi array ricevuto
                receivedArray = (int[]) in.readObject();
                if (receivedArray == null)
                    break;
                for(int i = 0; i < receivedArray.length; i++) {
                    System.out.print(receivedArray[i] + " ");
                }
                System.out.println();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
