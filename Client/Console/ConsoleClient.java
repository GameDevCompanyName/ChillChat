package ChillChat.Client.Console;

import ChillChat.Client.ClientWindow;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

import static ChillChat.Client.Utilites.Constants.IP;
import static ChillChat.Client.Utilites.Constants.PORT;

public class ConsoleClient extends Thread {

    private Socket socket;

    BufferedReader in;
    PrintWriter out;
    Resender resender;
    LogInProcedure logIn;
    ClientWindow clientWindow;

    public ConsoleClient(ClientWindow clientWindow) {
        this.clientWindow = clientWindow;
        logIn = new LogInProcedure(this);
    }

    @Override
    public void start(){

        try {
            socket = new Socket(IP, PORT);
            initStreams();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loggedIn(){

        System.out.println("Подключаюсь к общему чату...");

    }

    private void initStreams() {

        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")), true);
            resender = new Resender(in, logIn, clientWindow);
            resender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public LogInProcedure getLogIn() {
        return logIn;
    }

    public void sendMessage(String formedMessage){
        out.println(formedMessage);
    }

    public void closeAllThreads() {
        try {
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (resender != null)
            resender.setStop();
        System.exit(1);
    }

    public Integer getColor() {
        return logIn.getColor();
    }
}