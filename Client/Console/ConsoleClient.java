package ChillChat.Client.Console;

import ChillChat.Client.ClientWindow;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;

import static ChillChat.GlobalParameters.IP;
import static ChillChat.GlobalParameters.PORT;

public class ConsoleClient extends Thread {

    private Socket socket;

    BufferedReader in;
    PrintWriter out;
    ConsoleResender resender;
    Scanner scanner;
    ConsoleLogIn logIn;
    ClientWindow clientWindow;

    String name;

    public ConsoleClient(ClientWindow clientWindow) {
        this.clientWindow = clientWindow;
    }

    @Override
    public void start(){

        try {
            socket = new Socket(IP, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logIn = new ConsoleLogIn(this);

        initStreams();

    }

    public void loggedIn(){

        System.out.println("Подключаюсь к общему чату...");

    }

    private void initStreams() {

        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")), true);
            resender = new ConsoleResender(in, logIn, clientWindow);
            resender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ConsoleLogIn getLogIn() {
        return logIn;
    }

    public void sendMessage(String formedMessage){
        out.println(formedMessage);
    }

}