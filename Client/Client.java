package ChillChat.Client;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

import static ChillChat.Server.Parameters.PORT;

public class Client {

    private Socket socket;
    static BufferedReader in;
    PrintWriter out;
    Resender resender;

    public static void main(String[] args) {

        Client client = new Client();

        Scanner scan = new Scanner(System.in);

        System.out.println("Введите IP для подключения к серверу.");
        System.out.println("Формат: xxx.xxx.xxx.xxx");

        String ip = scan.nextLine();

        try {
            client.socket = new Socket(ip, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            client.in = new BufferedReader(new InputStreamReader(client.socket.getInputStream()));
            client.out = new PrintWriter(client.socket.getOutputStream(), true);
            client.resender = new Resender();
            client.resender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject object = new JSONObject();
        object.put("type", "3");
        System.out.println("Введите имя: ");
        String login = scan.nextLine();
        object.put("name", login);
        System.out.println("Введите пароль: ");
        String pass = scan.nextLine();
        object.put("text", pass);
        client.out.println(object.toJSONString());

        String message = "";

        while (message != "ВЫЙТИ"){

            System.out.println("Введите сообщение: ");
            message = scan.nextLine();

            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put("type", "1");
            jsonMessage.put("text", message);
            jsonMessage.put("name", login);
            jsonMessage.put("color", "1");
            client.out.println(jsonMessage.toJSONString());

        }

    }

    private static class Resender extends Thread {
        private boolean stoped;

        public void setStop() {
            stoped = true;
        }
        @Override
        public void run() {
            try {
                while (!stoped) {
                    String str = in.readLine();

                    JSONObject loginAttempt = (JSONObject) JSONValue.parse(str);

                    String name = (String) loginAttempt.get("name");
                    String text = (String) loginAttempt.get("text");

                    System.out.println(getTime() + " " + name + ": " + text);
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

}
