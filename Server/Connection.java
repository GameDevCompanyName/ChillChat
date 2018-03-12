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

    String name;
    Integer userColor;

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

                String login = (String) loginAttempt.get("login");
                String password = (String) loginAttempt.get("password");

                int loginAttemptCode = dbConnector.checkLoginAttempt(login, password);
                //colorCode > 0 - удачно, пользователь существовал
                //colorCode > 0 - удачно, новый пользователь
                //-1 - неверный пароль

                if (loginAttemptCode > 0){
                    System.out.println(login + " удачно залогинился в чате.");
                    userColor = loginAttemptCode;
                    sendLoginMessage(loginAttemptCode);
                    name = login;
                    loggedIn = true;
                }

                sendMessage(new Message("Залогинится не удалось", "Server", loginAttemptCode, 3));

            }

            broadcaster.connectClient(this);

            boolean clientConnected = true;

            while (clientConnected){

                String incomingMessage = in.readLine();
                JSONObject jsonMessage = (JSONObject) JSONValue.parse(incomingMessage);

                Message message = new Message(
                        (String) jsonMessage.get("text"),
                        name,
                        userColor,
                        1);

                broadcaster.broadcastMessage(message);

            }


        } catch (IOException e) {
            System.out.println(name + " разорвал соединение.");
            broadcaster.disconnectClient(this);
        }

    }

    private void sendLoginMessage(int loginAttemptCode) {
        JSONObject object = new JSONObject();
        object.put("type", "3");
        object.put("response", Integer.toString(loginAttemptCode));
        out.println(object.toJSONString());
    }

    public void sendMessage(Message message){

        JSONObject object = new JSONObject();
        object.put("text", message.getText());
        object.put("name", message.getSenderName());
        object.put("color", Integer.toString(message.getColorCode()));
        object.put("type", "1");
        out.println(object.toJSONString());

    }

}
