package ChillChat.Server;


import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class Connection extends Thread {

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private Broadcaster broadcaster;
    private DBConnector dbConnector;
    boolean clientConnected = true;


    String name;  //Имя пользователя
    Integer userColor;  //Цвет пользователя

    public Connection(Socket socket, Broadcaster broadcaster, DBConnector dbConnector) {

        this.broadcaster = broadcaster;
        this.dbConnector = dbConnector;
        this.socket = socket;

        try {

            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8"))), true);

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
                String message = in.readLine();  //Ждем сообщение от клиента

                //Распарсили сообщение в стринги
                JSONObject loginAttempt = (JSONObject) JSONValue.parse(message);
                String login = (String) loginAttempt.get("login");
                String password = (String) loginAttempt.get("password");
                if(broadcaster.getConnectionByLogin(login)!=null){
                    sendLoginMessage(-2);
                    continue;
                }
                int loginAttemptCode = dbConnector.checkLoginAttempt(login, password);
                //colorCode > 0 - удачно, пользователь существовал
                //colorCode > 0 - удачно, новый пользователь
                //-1 - неверный пароль

                if (loginAttemptCode > 0){
                    System.out.println(login + " удачно залогинился в чате.");
                    userColor = loginAttemptCode;
                    name = login;
                    loggedIn = true;
                }

                sendLoginMessage(loginAttemptCode);
                Message msg = new Message(login+" зашел в чат","SERVER", 3, 2);
                broadcaster.broadcastMessage(msg);

            }

            broadcaster.connectClient(this);


            while (clientConnected){

                String incomingMessage = in.readLine();
                if(incomingMessage == null || !clientConnected)
                    continue;

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
    public void disconnectMessage(String reason){
        JSONObject object = new JSONObject();
        object.put("reason", reason);
        object.put("type", "4");
        out.println(object.toJSONString());
    }
    public void updateColor(Integer color)
    {
        userColor = dbConnector.getUserColor(name);
        System.out.println("Цвет пользователя "+name+" изменен на "+userColor.toString());
    }

    public String getLogin(){
        return name;
    }

    public void disconnect(String reason) {
            disconnectMessage(reason);
            clientConnected = false;
            this.interrupt();
    }
}
