package ChillChat.Client;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClientMessage {

    public static void read(String input){
        JSONObject incomingMessage = (JSONObject) JSONValue.parse(input);
        String methodName = incomingMessage.get("type") + "Received";
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
