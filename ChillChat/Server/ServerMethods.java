package ChillChat.Server;

public class ServerMethods {
    public static void versionReceived(String version){

    }

    public static void loginAttemptReceived(String login, String password){

    }

    public static void messageReceived(String message){
        System.out.println("Получено "+message);
    }

    public static void disconnectReceived(String reason){

    }
}
