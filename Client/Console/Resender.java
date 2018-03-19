package ChillChat.Client.Console;

import ChillChat.Client.ClientWindow;
import javafx.application.Platform;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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
        this.interrupt();
    }

    @Override
    public void run() {
        try {
            while (!stoped) {

                String str = in.readLine();

                JSONObject message = (JSONObject) JSONValue.parse(str);

                Platform.runLater(() -> {

                    if (message.get("type").equals("1")){
                        String name = message.get("name").toString();
                        String text = message.get("text").toString();
                        Integer color = Integer.parseInt(message.get("color").toString());
                        clientWindow.displayMessage(name, text, color);
                    }

                    if (message.get("type").equals("2")){
                        String text = message.get("text").toString();
                        clientWindow.displayServerMessage(text);
                    }

                    if (message.get("type").equals("3")){
                        logIn.serverAnswer(message.get("response").toString());
                    }

                    if (message.get("type").equals("4")){
                        String reason = message.get("reason").toString();
                        clientWindow.displayServerMessage("Вы были отключены от сервера. Причина:");
                        clientWindow.displayServerMessage(reason);
                    }

                });

            }

        } catch (IOException e) {
            clientWindow.inputStreamProblem();
        }
    }

    public String getTime() {
        Date date = new Date();
        return "[" + date + "]";
    }
}
