package ChillChat.Server;

import java.sql.SQLException;
import java.util.List;

public class Commands {
    private DBConnector dbConnector;
    private Broadcaster broadcaster;
   public Commands(DBConnector dbConnector, Broadcaster broadcaster)
   {
       this.dbConnector = dbConnector;
       this.broadcaster = broadcaster;
   }

    public void invoke(String[] comms) {
       String template = null;
       switch (comms[0]) {
           case "updateusercolor":
               template = "Wrong format: /updateusercolor <login> <color>";
               if(comms.length == 3)
               {
                   if(dbConnector.searchForUser(comms[1])){
                       dbConnector.updateUserColor(comms[1], Integer.parseInt(comms[2]));
                       Connection conn = broadcaster.getConnectionByLogin(comms[1]);
                       if(conn!=null) {
                           conn.updateColor(Integer.parseInt(comms[2]));
                           return;
                       }
                       System.out.println("Не удалось открыть соединение");
                       return;
                   }
                   else
                       System.out.println(comms[1]+": пользователь не найден!");
                   break;
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
                       System.out.println(comms[1]+": пользователь не найден!");
                    break;
               }
                break;
           case "clients":
               List<Connection> connections = broadcaster.getConnections();
               if(connections.isEmpty())
               {
                   System.out.println("Никого нет");
                   return;
               }
               for (Connection connect: connections
                    ) {
                   System.out.println(connect.getLogin());
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
                           Message msg = new Message(kickMsg, "SERVER", 3, 2);
                           broadcaster.broadcastMessage(msg);
                           conn.disconnect(comms[2]);
                           return;
                       }
                       System.out.println("Не удалось открыть соединение");
                       return;
                   }
               }
               break;
               default:
                   template ="Команда не найдена";
                   break;
       }
       System.out.println(template);
   }
}
