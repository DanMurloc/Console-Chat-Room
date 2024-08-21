package org.neroimor.cm.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class CommunicationUsers extends Thread {
    Socket socket;
    BufferedReader in;
    BufferedWriter out;
    String helloMes = "Добро пожаловать на сервер. \n" +
            "Введите ваше имя: ";
    boolean statusName = false;
    String nameUser = "unknown";
    public static ArrayList<CommunicationUsers> serverList = new ArrayList<>();

    public CommunicationUsers(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        serverList.add(this);
        sendMessage(helloMes);
    }

    @Override
    public void run() {
        String word;
        try {
            while (true) {
                word = in.readLine();
                if (word.equals("stop")) {
                    SinglDataBase.updateUser(nameUser);
                    break;
                }

                bypass(word);
            }
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                socket.close();
                in.close();
                out.close();
                serverList.remove(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    synchronized private void bypass(String word){
        for (var serverElement : serverList) {

            if(!statusName && serverElement.equals(this)){
                nameUser = word;
                statusName = true;

                if (SinglDataBase.addAndGetUser(nameUser)){
                    serverElement.sendMessage("Вновь привет тебе: " + nameUser+".");
                }
                else{
                    serverElement.sendMessage("Ваше имя: " + nameUser+".");
                }

            }
            if (serverElement.equals(this)){
                //serverElement.sendMessage("Ввод: ");
            }
            else
                serverElement.sendMessage(nameUser +": "+word+"\nВвод: ");
        }
    }

    synchronized public void sendMessage(String message) {
        try {

            out.write(message+"\n");
            out.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
