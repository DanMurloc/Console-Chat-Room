package org.neroimor.cm.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class MainServer {
    private static final int PORT = 8080;
    public static void main(String[] args) throws IOException {
        //ServerSocket server = new ServerSocket(PORT);
        InetAddress addr = InetAddress.getByName("127.0.0.1");
        ServerSocket server = new ServerSocket(PORT,50,addr);
        try {
            System.out.println("Сервер запущен");
            while (true) {
                Socket socket = server.accept();
                try {
                    Thread communicationUsers = new CommunicationUsers(socket);
                    communicationUsers.start();


                } catch (IOException e){
                    socket.close();
                }

            }
        }
        finally {
            server.close();
        }
    }
}
