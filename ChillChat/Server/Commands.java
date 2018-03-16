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
       switch (comms[0]) {
           case "updateusercolor":
               if(comms.length == 3)
               {
                   if(dbConnector.searchForUser(comms[1])){
                       dbConnector.updateUserColor(comms[1], Integer.parseInt(comms[2]));
                       Connection conn = broadcaster.getConnectionByLogin(comms[1]);
                       if(conn!=null) {
                           conn.updateColor(Integer.parseInt(comms[2]));
                           break;
                       }
                       break;
                   }
                   else
                       System.out.println(comms[1]+": пользователь не найден!");
                   break;
               }
               break;
           case "updateuserrole":
               if(comms.length == 3)
               {
                   if(dbConnector.searchForUser(comms[1])){
                       dbConnector.updateUserRole(comms[1], Integer.parseInt(comms[2]));
                       break;
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
                   break;
               }
               for (Connection connect: connections
                    ) {
                   System.out.println(connect.getLogin());
               }
               break;
           case "discall":
               broadcaster.disconnectAll();
               break;
               default:
                   System.out.println("Команда не найдена");
                   break;
       }
   }
}
