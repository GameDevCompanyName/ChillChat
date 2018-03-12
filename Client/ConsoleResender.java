package ChillChat.Client;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

class ConsoleResender extends Thread {
    private boolean stoped = false;

    private BufferedReader in;
    private ConsoleLogIn logIn;

    public ConsoleResender(BufferedReader in, ConsoleLogIn logIn) {
        this.in = in;
        this.logIn = logIn;

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

                if (message.get("type").equals("3")){
                    logIn.serverAnswer(message.get("response").toString());
                }

                if (message.get("type").equals("1")){


                    String name = message.get("name").toString();
                    String text = message.get("text").toString();

                    Integer color = Integer.parseInt(message.get("color").toString());

                    String colorizer = "\u001B[0m";

                    switch (color){
                        case 2:
                            colorizer = "\u001B[31m";
                            break;
                        case 3:
                            colorizer = "\u001B[32m";
                            break;
                        case 4:
                            colorizer = "\u001B[33m";
                            break;
                        case 5:
                            colorizer = "\u001B[34m";
                            break;
                        case 6:
                            colorizer = "\u001B[35m";
                            break;
                        case 7:
                            colorizer = "\u001B[36m";
                            break;
                        case 8:
                            colorizer = "\u001B[37m";
                            break;

                    }

                    System.out.println("\n" + getTime() + " " + colorizer + name + ": " + text + "\u001B[0m");
                }

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
