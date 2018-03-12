package ChillChat.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static ChillChat.Server.Parameters.PORT;

public class ConsoleClient {

    private Socket socket;

    BufferedReader in;
    PrintWriter out;
    ConsoleResender resender;
    Scanner scanner;
    ConsoleLogIn logIn;
    ConsoleMessenger consoleMessenger;


    String name;

    public ConsoleClient() {

        this.scanner = new Scanner(System.in);

    }

    public void start(){

        System.out.println("Введите IP для подключения к серверу.");
        System.out.println("Формат: xxx.xxx.xxx.xxx");

        String ip = scanner.nextLine();

        try {
            socket = new Socket(ip, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logIn = new ConsoleLogIn(this);
        initStreams();
        logIn.tryToLogIn();

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

}