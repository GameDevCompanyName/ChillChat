package ChillChat.Client.Utilites;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ClientMessage {

    public static void read(String input){

        JSONObject incomingMessage = (JSONObject) JSONValue.parse(input);
        System.out.println(incomingMessage);
        String methodName = (String) incomingMessage.get("type");

        switch (methodName){
            case "loginWrongError":
                ClientMethods.loginWrongErrorReceived();
                break;
            case "loginAlreadyError":
                ClientMethods.loginAlreadyErrorReceived();
                break;
            case "loginSuccess":
                ClientMethods.loginSuccessReceived();
                break;
            case "userColor":
                ClientMethods.userColorReceived(
                        incomingMessage.get("first").toString(),
                        incomingMessage.get("second").toString()
                );
                break;
            case "userMessage":
                ClientMethods.userMessageReceived(
                        incomingMessage.get("first").toString(),
                        incomingMessage.get("second").toString(),
                        incomingMessage.get("third").toString()
                );
                break;
            case "serverMessage":
                ClientMethods.serverMessageReceived(
                        incomingMessage.get("first").toString()
                );
                break;
            case "serverUserKicked":
                ClientMethods.serverUserKickedReceived(
                        incomingMessage.get("first").toString(),
                        incomingMessage.get("second").toString()
                );
                break;
            case "userDisconnect":
                ClientMethods.userDisconnectReceived(
                        incomingMessage.get("first").toString()
                );
                break;
            case "serverUserLogin":
                ClientMethods.serverUserLoginReceived(
                        incomingMessage.get("first").toString()
                );
                break;
            case "serverUserDisconnect":
                ClientMethods.serverUserDisconnectReceived(
                        incomingMessage.get("first").toString()
                );
                break;
        }

    }


    public static String messageSend(String message){
        JSONObject object = new JSONObject();
        object.put("type", "message");
        object.put("first", message);
        return object.toJSONString();
    }

    public static String versionSend(String version){
        JSONObject object = new JSONObject();
        object.put("type", "version");
        object.put("first", version);
        return object.toJSONString();
    }

    public static String loginAttemptSend(String login, String password){
        JSONObject object = new JSONObject();
        object.put("type", "loginAttempt");
        object.put("first", login);
        object.put("second", password);
        return object.toJSONString();
    }

    public static String disconnectSend(String reason){
        JSONObject object = new JSONObject();
        object.put("type", "disconnect");
        object.put("first", reason);
        return object.toJSONString();
    }

}