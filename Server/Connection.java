package ChillChat.Server;


import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection extends Thread {

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private Broadcaster broadcaster;
    private DBConnector dbConnector;

    public Connection(Socket socket, Broadcaster broadcaster, DBConnector dbConnector) {

        this.broadcaster = broadcaster;
        this.dbConnector = dbConnector;

        this.socket = socket;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        try {

            System.out.println("Обработка клиента " + socket.getInetAddress());

            boolean loggedIn = false;

            while (!loggedIn){

                System.out.println("Ожидание попытки залогинится.");

                String message = in.readLine();

                JSONObject loginAttempt = (JSONObject) JSONValue.parse(message);

                if (!((String) loginAttempt.get("type")).equals("3")){
                    System.out.println("Неудачная попытка логина у " + socket.getInetAddress());
                    continue;
                }

                String login = (String) loginAttempt.get("name");
                String password = (String) loginAttempt.get("text");

                int loginAttemptCode = dbConnector.checkLoginAttempt(login, password);
                //colorCode > 0 - удачно, пользователь существовал
                //colorCode > 0 - удачно, новый пользователь
                //-1 - неверный пароль

                if (loginAttemptCode > 0){
                    System.out.println(login + " удачно залогинился в чате.");
                    loggedIn = true;
                }

            }

            broadcaster.connectClient(this);

            boolean clientConnected = true;

            while (clientConnected){

                String incomingMessage = in.readLine();
                JSONObject jsonMessage = (JSONObject) JSONValue.parse(incomingMessage);

                if (((String) jsonMessage.get("type")).equals("4")){
                    broadcaster.disconnectClient(this);
                    clientConnected = false;
                    System.out.println(((String) jsonMessage.get("name")) + " задисконектился пиздец куда он ушёл ну и пиздуй нахуй долбаёб");
                    continue;
                }

                Message message = new Message(
                        (String) jsonMessage.get("text"),
                        (String) jsonMessage.get("name"),
                        Integer.parseInt((String) jsonMessage.get("color")),
                        Integer.parseInt((String) jsonMessage.get("type"))
                );

                broadcaster.broadcastMessage(message);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void sendMessage(Message message){

        JSONObject object = new JSONObject();
        object.put("text", message.getText());
        object.put("name", message.getSenderName());
        object.put("color", Integer.toString(message.getColorCode()));
        object.put("type", Integer.toString(message.getColorCode()));

        out.println(object.toJSONString());

    }

}
