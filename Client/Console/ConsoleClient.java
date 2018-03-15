package ChillChat.Client.Console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
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