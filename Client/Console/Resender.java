package ChillChat.Client.Console;

import ChillChat.Client.ClientWindow;
import ChillChat.Client.Utilites.ClientMessage;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

class Resender extends Thread {
    private boolean stoped = false;

    private BufferedReader in;
    private LogInProcedure logIn;
    private ClientWindow clientWindow;

    Resender(BufferedReader in, LogInProcedure logIn, ClientWindow clientWindow) {
        this.in = in;
        this.logIn = logIn;
        this.clientWindow = clientWindow;
    }

    void setStop() {

        stoped = true;
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //this.interrupt();
    }

    @Override
    public void run() {

        while (!stoped) {

            try {
                String message = in.readLine();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ClientMessage.read(message);
                    }
                });
            } catch (IOException e) {
                System.out.println("Выключение входного потока");
            }


        }

    }

    public String getTime() {
        Date date = new Date();
        return "[" + date + "]";
    }
}
