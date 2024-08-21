package org.neroimor.cm.Clients;

import java.io.*;
import java.net.Socket;

public class Client {
    private static Socket socketClient;
    private static String host = "127.0.0.1";
    private static int port= 8080;
    private static BufferedReader readerClient;
    private static BufferedReader in;
    private static BufferedWriter out;
    public static void main(String[] args) throws IOException {
        try{
            socketClient = new Socket(host,port);
            readerClient = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
            inputMessages();
            outputMessages();
        }
        finally {
            System.out.println("Пока сервер");
            socketClient.close();
            in.close();
            out.close();
            readerClient.close();
        }

    }
    private static void outputMessages() {
        String word;
        while (true){
            try {
                word = readerClient.readLine();
                out.write(word+"\n");
                out.flush();
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    private static void inputMessages(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String word;
                StringBuilder builder = new StringBuilder();
                int character;
                try{
                    while(( word = in.readLine())!=null){
                        System.out.println(word);
                        //System.out.println("\nВвод: ");
                        if(word.equals("stop"))
                            break;
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}
