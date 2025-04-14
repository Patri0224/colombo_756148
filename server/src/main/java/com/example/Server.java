package com.example;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is listening on port 12345...");
            try (Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                String clientMessage = in.readLine();
                System.out.println("Received from client: " + clientMessage);
                out.println("Hello from the server!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
