package ChillChat.Client;

import org.json.simple.JSONObject;

public class JsonHandler {

    public static String getString(String login, String pass) {
        JSONObject object = new JSONObject();
        object.put("login", login);
        object.put("password", pass);
        return object.toJSONString();
    }

    public static String getString(String text) {
        JSONObject object = new JSONObject();
        object.put("text", text);
        return object.toJSONString();
    }

}
