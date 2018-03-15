package ChillChat.Server;

public class Commands {
    private DBConnector dbConnector;
   public Commands(DBConnector dbConnector)
   {
       this.dbConnector = dbConnector;
   }

    public void invoke(String[] comms) {
       switch (comms[0]) {
           case "updateusercolor":
               if(comms.length == 3)
               {
                   if(dbConnector.searchForUser(comms[1])){
                       dbConnector.updateUserColor(comms[1], Integer.parseInt(comms[2]));
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
               default:
                   System.out.println("Команда не найдена");
                   break;
       }
   }
}
