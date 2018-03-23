package ChillChat.Server;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ServerMessage {

    //Читаем входное сообщение от клиента
    public static void read(String input){
        JSONObject incomingMessage = (JSONObject) JSONValue.parse(input);
        int incMsgSize = incomingMessage.size();
        String methodName = incomingMessage.get("type") + "Received";
        ServerMethods serverMethods = new ServerMethods();
        switch (incMsgSize) {
            case 1:
                try {
                    Method method = serverMethods.getClass().getMethod(methodName);
                    method.invoke(null);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    Method method = serverMethods.getClass().getMethod(methodName, String.class);
                    method.invoke(null, incomingMessage.get("first"));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    Method method = serverMethods.getClass().getMethod(methodName, String.class, String.class);
                    method.invoke(null, incomingMessage.get("first"), incomingMessage.get("second"));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    //Все статичные методы описаны в документации
    public static String clientVersionRequestSend(){
        JSONObject object = new JSONObject();
        object.put("type", "clientVersionRequest");
        return object.toJSONString();
    }

    public static String loginWrongErrorSend(){
        JSONObject object = new JSONObject();
        object.put("type", "loginWrongError");
        return object.toJSONString();
    }

    public static String loginAlreadyErrorSend(){
        JSONObject object = new JSONObject();
        object.put("type", "loginAlreadyError");
        return object.toJSONString();
    }

    public static String loginSuccessSend(){
        JSONObject object = new JSONObject();
        object.put("type", "loginSuccess");
        return object.toJSONString();
    }

    public static String userRegistrationSuccessSend(){
        JSONObject object = new JSONObject();
        object.put("type", "userRegistrationSuccess");
        return object.toJSONString();
    }
    public static String userColorSend(String login, String color){
        JSONObject object = new JSONObject();
        object.put("type", "userColor");
        object.put("first", login);
        object.put("second", color);
        return object.toJSONString();
    }
    public static String userMessageSend(String login, String message){
        JSONObject object = new JSONObject();
        object.put("type", "userMessage");
        object.put("first", login);
        object.put("second", message);
        return object.toJSONString();
    }
    public static String userActionSend (String login, String action){
        JSONObject object = new JSONObject();
        object.put("type", "userAction");
        object.put("first", login);
        object.put("second", action);
        return object.toJSONString();
    }
    public static String serverMessageSend(String message){
        JSONObject object = new JSONObject();
        object.put("type", "serverMessage");
        object.put("first", message);
        return object.toJSONString();
    }
    public static String serverEventSend(String event){
        JSONObject object = new JSONObject();
        object.put("type", "serverEvent");
        object.put("first", event);
        return object.toJSONString();
    }
    public static String serverUserKickedSend(String login, String reason){
        JSONObject object = new JSONObject();
        object.put("type", "serverUserKicked");
        object.put("first", login);
        object.put("second", reason);
        return object.toJSONString();
    }
    public static String userDisconnectSend(String reason){
        JSONObject object = new JSONObject();
        object.put("type", "userDisconnect");
        object.put("first", reason);
        return object.toJSONString();
    }
    public static String serverUserLoginSend(String login){
        JSONObject object = new JSONObject();
        object.put("type", "serverUserLogin");
        object.put("first", login);
        return object.toJSONString();
    }
    public static String serverUserDisconnectSend(String login){
        JSONObject object = new JSONObject();
        object.put("type", "serverUserDisconnect");
        object.put("first", login);
        return object.toJSONString();
    }
}
