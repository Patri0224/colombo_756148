package com.example;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("Hello from the client!");
            System.out.println("Server: " + in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
