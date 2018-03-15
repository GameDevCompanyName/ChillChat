package ChillChat.Server;

import java.util.ArrayList;
import java.util.List;

public class Broadcaster {

    List<Connection> connections = new ArrayList();


    public short connectClient(Connection client){


        if (connections.contains(client))
            return -1;

        connections.add(client);
        System.out.println("Broadcaster: Клиент добавлен в пул соединений");
        printClients();
        return 1;

    }

    public short disconnectClient(Connection client){

        if (!connections.contains(client))
            return -1;

        connections.remove(client);
        System.out.println("Broadcaster: Клиент удалён из пула соединений");
        printClients();
        return 1;

    }

    private void printClients() {
        System.out.println("Клиентов: " + connections.size());
    }

    public short broadcastMessage(Message message){

        try {
            for (Connection connection: connections) {
                System.out.println("Broadcaster: Отправляю всем сообщение");
                connection.sendMessage(message);
            }
        } catch (Exception e){
            System.out.println("Ошибка при отправке сообщения");
            e.printStackTrace();
        }

        return 1;

    }

}