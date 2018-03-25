package ChillChat.Server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
Броадкастер работает с соединениями. Содержит в себе их список и методы для работы с ними.
 */
public class Broadcaster {

    //Список соединений
    private List<Connection> connections = new ArrayList();
    private String startText = Utilities.getStartText("Broadcaster");
    //Добавить нового клиента в список соединений
    public void connectClient(Connection client){
        connections.add(client);
        broadcastMessage(ServerMessage.serverUserLoginSend(client.getUserName()));
        printClients();
    }

    //Удалить клиента из списка
    public void disconnectClient(Connection client){
        connections.remove(client);
        broadcastMessage(ServerMessage.serverUserDisconnectSend(client.getUserName()));
        printClients();
    }

    private void printClients() {
        System.out.println(startText+"Клиентов: " + connections.size());
    }

    //Передача сообщения всем клиентам
    public void broadcastMessage(String message){
        try {
            for (Connection connection: connections) {
                connection.sendMessage(message);
            }
        } catch (Exception e){
            System.out.println(startText+"Ошибка при отправке сообщения");
            e.printStackTrace();
        }
    }

    public List<Connection> getConnections(){
        return connections;
    }

    //Получить соединение по логину
    public Connection getConnectionByLogin(String login){
        for (Connection conn: connections) {
            if(conn.getUserName()==null)
                continue;
            if(conn.getUserName().equals(login))
            {
                return conn;
            }
        }
        return null;
    }

    //Отключить всех
    public void disconnectAll(){

        Iterator<Connection> i = connections.iterator();
        while (i.hasNext()) {
            Connection value = i.next();
            value.disconnect("Сервер закрыл соединение");
            i.remove();
        }
        connections.clear();
        System.out.println(startText+"Соединения закрыты");
        printClients();
    }
}
