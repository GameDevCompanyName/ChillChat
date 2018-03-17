package ChillChat.Server;

import java.util.ArrayList;
import java.util.Iterator;
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
    public List<Connection> getConnections(){
        return connections;
    }
    public Connection getConnectionByLogin(String login){
        for (Connection conn: connections) {
            if(conn.getLogin()==null)
                continue;
            if(conn.getLogin().equals(login))
            {
                return conn;
            }
        }
        return null;
    }
    public void disconnectAll(){

        Iterator<Connection> i = connections.iterator();
        while (i.hasNext()) {
            Connection value = i.next();
            value.disconnect("Сервер закрыл соединение");
            i.remove();
        }
        connections.clear();
        System.out.println("Соединения закрыты");
        printClients();
    }

}
