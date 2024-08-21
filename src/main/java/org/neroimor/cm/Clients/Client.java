package org.neroimor.cm.Clients;

import java.io.*;
import java.net.Socket;

public class Client {
    private static Socket socketClient;
    private static String host = "127.0.0.1";
    private static int port = 8080;
    private static BufferedReader readerClient;
    private static BufferedReader in;
    private static BufferedWriter out;
    static boolean running = true;

    public static void main(String[] args) throws IOException {
        try {
            socketClient = new Socket(host, port);
            readerClient = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));

            // Запуск потоков
            inputMessages();
            outputMessages();
        } finally {
            System.out.println("Пока сервер");
            closeResources();
        }
    }

    private static String legendaryWord = "";

    private static void outputMessages() {
        String word;
        while (running) {
            try {
                word = readerClient.readLine();
                if (out != null && word != null) {
                    out.write(word + "\n");
                    out.flush();
                }
                if (legendaryWord.equals("stop") || word.equals("stop")) {
                    running = false;
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void inputMessages() {
        Thread t = new Thread(() -> {
            String word;
            try {
                while (running && (word = in.readLine()) != null) {
                    System.out.println(word);
                    legendaryWord = word;
                    if (word.equals("stop")) {
                        running = false;
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    private static void closeResources() {
        try {
            if (readerClient != null) readerClient.close();
            if (in != null) in.close();
            if (out != null) out.close();
            if (socketClient != null && !socketClient.isClosed()) socketClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
