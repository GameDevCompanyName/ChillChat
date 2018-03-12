package ChillChat.Server;

import java.util.ArrayList;
import java.util.List;

public class Broadcaster {

    List<Connection> connections = new ArrayList();

    public short connectClient(Connection client){

        System.out.println("Broadcaster: Клиент добавлен в пул соединений");

        if (connections.contains(client))
            return -1;

        connections.add(client);
        printClients();
        return 1;

    }

    public short disconnectClient(Connection client){

        System.out.println("Broadcaster: Клиент удалён из пула соединений");

        if (!connections.contains(client))
            return -1;

        connections.remove(client);
        printClients();
        return 1;

    }

    private void printClients() {
        System.out.println("Клиентов: " + connections.size());
    }

    public short broadcastMessage(Message message){

        System.out.println("Broadcaster: Отправляю всем сообщение");

        try {
            for (Connection connection: connections) {
                connection.sendMessage(message);
            }
        } catch (Exception e){
            System.out.println("Ошибка при отправке сообщения");
            e.printStackTrace();
        }

        return 1;

    }

}
