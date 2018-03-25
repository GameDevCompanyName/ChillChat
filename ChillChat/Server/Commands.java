package ChillChat.Server;

import java.util.List;

/*
Класс команд, отвечает за выполнение команд сервера.
TODO: выполнять команды пользователей
 */

public class Commands {
    public static Broadcaster broadcaster;
    public static DBConnector dbConnector;

    public static void setBroadcaster(Broadcaster broadcaster) {
        Commands.broadcaster = broadcaster;
    }

    public static void setDbConnector(DBConnector dbConnector) {
        Commands.dbConnector = dbConnector;
    }

    //Пытаемся выполнить команду, разбитую на части
    //comms[0] - название команды
    //comms[1] - первый аргумент
    //comms[2] - второй аргумент
    public static void invoke(String[] comms) {
       String template = null;
       switch (comms[0]) {
           //TODO: делать вызов метода команды
           case "updateusercolor":
               template = "Wrong format: /updateusercolor <login> <color>";
               if(comms.length == 3)
               {
                   if(dbConnector.searchForUser(comms[1])){
                       dbConnector.updateUserColor(comms[1], comms[2]);
                       Connection conn = broadcaster.getConnectionByLogin(comms[1]);
                       if(conn!=null) {
                           conn.updateColor(comms[2]);
                           return;
                       }
                       System.out.println(Utilities.getStartText("Commands")+"Не удалось открыть соединение: пользователь не в сети");
                       return;
                   }
                   else
                       System.out.println(Utilities.getStartText("Commands")+comms[1]+" - пользователь не найден!");
                   return;

               }
               break;
           case "updateuserrole":
               template = "Wrong format: /updateuserrole <login> <role>";
               if(comms.length == 3)
               {
                   if(dbConnector.searchForUser(comms[1])){
                       dbConnector.updateUserRole(comms[1], Integer.parseInt(comms[2]));
                       return;
                   }
                   else
                       System.out.println(Utilities.getStartText("Commands")+comms[1]+" - пользователь не найден!");
                    return;
               }
                break;
           case "clients":
               List<Connection> connections = broadcaster.getConnections();
               if(connections.isEmpty())
               {
                   System.out.println(Utilities.getStartText("Commands")+"Никого нет");
                   broadcaster.broadcastMessage(ServerMessage.serverMessageSend("Никого нет"));
                   return;
               }
               System.out.println(Utilities.getStartText("Commands")+"В сети");
               broadcaster.broadcastMessage(ServerMessage.serverMessageSend("В сети:"));
               for (Connection connect: connections
                    ) {
                   System.out.println(Utilities.getStartText("Commands")+connect.getUserName());
                   broadcaster.broadcastMessage(ServerMessage.serverMessageSend(connect.getUserName()));
               }
               return;
           case "discall":
               broadcaster.disconnectAll();
               return;
           case "kick":
               template = "Wrong format: /kick <login> <reason>";
               if(comms.length == 3){
                   if(dbConnector.searchForUser(comms[1])){
                       Connection conn = broadcaster.getConnectionByLogin(comms[1]);
                       if(conn!=null) {
                           String kickMsg = comms[1]+" был кикнут сервером по причине: "+comms[2];
                           System.out.println(Utilities.getStartText("Commands")+kickMsg);
                           broadcaster.broadcastMessage(ServerMessage.serverUserKickedSend(comms[1], comms[2]));
                           conn.disconnect("kicked");
                           return;
                       }
                       System.out.println(Utilities.getStartText("Commands")+"Не удалось открыть соединение");
                       return;
                   }
               }
               break;
               default:
                   template ="Команда не найдена";
                   break;
       }
       System.out.println(Utilities.getStartText("Commands")+template);
   }
}
