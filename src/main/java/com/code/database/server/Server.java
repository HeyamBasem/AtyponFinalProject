package com.code.database.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    Server(){

    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(5000);
            serverSocket.setReuseAddress(true);
            while (true) {
                Socket connection = serverSocket.accept();
                System.out.println("New connection" + connection.getInetAddress().getHostAddress());
                ServletRequestHandler DBhandler = new ServletRequestHandler(connection);
                (new Thread(DBhandler)).start();
            }

        } catch (IOException e) {
            System.out.println("error 1");
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
