package ChillChat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;

import static ChillChat.GlobalParameters.PORT;


public class Server {

    Broadcaster broadcaster = new Broadcaster();

    DBConnector dbConnector;

    {
        try {
            dbConnector = new DBConnector();
            CommandLine cmd = new CommandLine(dbConnector, broadcaster); //Создаем поток ввода для сервера
            cmd.start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] ar) {

        Server server = new Server();

        try {
            ServerSocket ss = new ServerSocket(PORT);

            while (true) {

                System.out.println("Ожидаю нового клиента");
                Socket socket = ss.accept();

                System.out.println("Соединение установлено с клиентом: " + socket.getInetAddress());

                //Создаем соединение с клиентом
                Connection con = new Connection(socket, server.broadcaster, server.dbConnector);
                server.broadcaster.connectClient(con);
                con.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Серверу пиздец");
            System.exit(1);
        }

    }


}