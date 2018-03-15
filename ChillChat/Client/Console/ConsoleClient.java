package ChillChat.Client.Console;

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
    ConsoleMessenger consoleMessenger;

    String name;

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
        consoleMessenger = new ConsoleMessenger(this);
        consoleMessenger.start();

    }

    private void initStreams() {

        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8"))), true);
            resender = new ConsoleResender(in, logIn);
            resender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ConsoleLogIn getLogIn() {
        return logIn;
    }
}