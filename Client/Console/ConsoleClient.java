package ChillChat.Client.Console;

import ChillChat.Client.ClientWindow;
import ChillChat.Client.Utilites.ClientMessage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

import static ChillChat.Client.Utilites.Constants.IP;
import static ChillChat.Client.Utilites.Constants.PORT;

public class ConsoleClient extends Thread {

    private Socket socket;

    private BufferedReader in;
    private PrintWriter out;
    private Resender resender;
    private LogInProcedure logIn;
    private ClientWindow clientWindow;

    private boolean initiated = false;

    public ConsoleClient(ClientWindow clientWindow) {
        this.clientWindow = clientWindow;
        logIn = new LogInProcedure(this);
    }

    @Override
    public void start(){

        initiated = true;

        try {
            socket = new Socket(IP, PORT);
            initStreams();
        } catch (IOException e) {
            clientWindow.unableToConnect();
            e.printStackTrace();
        }

    }

    private void initStreams() {

        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")), true);
            resender = new Resender(in, logIn, clientWindow);
            resender.start();
        } catch (Exception e) {
            clientWindow.unableToConnect();
            e.printStackTrace();
        }

    }

    public LogInProcedure getLogIn() {
        return logIn;
    }

    public void sendMessage(String message){
        out.println(message);
    }

    public boolean isInitiated(){
        return initiated;
    }

    public void closeAllThreads() {

        if (out != null)
            out.println(ClientMessage.disconnectSend("Закрыл соединение."));

        try {
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (resender != null)
            resender.setStop();

    }

    public String getColor() {
        return logIn.getColor();
    }

    PrintWriter getOut() {
        return out;
    }

    public void passWrong() {
        logIn.passWrong();
    }

    public void userAlreadyOnline() {
        logIn.userAlreadyOnline();
    }

    public void loggedIn() {
        logIn.loginSuccess();
    }

}