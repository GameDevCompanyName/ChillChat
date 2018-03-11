package ChillChat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static ChillChat.Server.Parameters.PORT;


public class Server {

    Broadcaster broadcaster = new Broadcaster();

    DBConnector dbConnector = new DBConnector();

    public static void main(String[] ar) {

        Server server = new Server();

        try {
            ServerSocket ss = new ServerSocket(PORT);

            while (true) {

                System.out.println("Ожидаю нового клиента");

                Socket socket = ss.accept();

                System.out.println("Соединение установлено с клиентом: " + socket.getInetAddress());

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