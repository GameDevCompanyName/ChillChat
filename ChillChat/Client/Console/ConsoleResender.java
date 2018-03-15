package ChillChat.Client.Console;

import ChillChat.Client.ClientWindow;
import javafx.application.Platform;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

class ConsoleResender extends Thread {
    private boolean stoped = false;

    private BufferedReader in;
    private ConsoleLogIn logIn;
    private ClientWindow clientWindow;

    public ConsoleResender(BufferedReader in, ConsoleLogIn logIn, ClientWindow clientWindow) {
        this.in = in;
        this.logIn = logIn;
        this.clientWindow = clientWindow;
    }

    public void setStop() {
        stoped = true;
    }

    @Override
    public void run() {
        try {
            while (!stoped) {

                String str = in.readLine();

                JSONObject message = (JSONObject) JSONValue.parse(str);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (message.get("type").equals("3")){
                            System.out.println(message.get("response"));
                            System.out.println(message.get("response").toString());
                            System.out.println((String) message.get("response"));

                            logIn.serverAnswer(message.get("response").toString());
                        }

                        if (message.get("type").equals("1")){

                            String name = message.get("name").toString();
                            String text = message.get("text").toString();

                            Integer color = Integer.parseInt(message.get("color").toString());

                            clientWindow.displayMessage(name, text, color);

                        }
                    }
                });

            }

        } catch (IOException e) {
            System.err.println("Ошибка при получении сообщения.");
            e.printStackTrace();
        }
    }

    public String getTime() {
        Date date = new Date();
        return "[" + date + "]";
    }
}
