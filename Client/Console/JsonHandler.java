package ChillChat.Client.Console;

import org.json.simple.JSONObject;

class JsonHandler {

    static String getString(String login, String pass) {
        JSONObject object = new JSONObject();
        object.put("login", login);
        object.put("password", pass);
        return object.toJSONString();
    }

    static String getString(String text) {
        JSONObject object = new JSONObject();
        object.put("text", text);
        return object.toJSONString();
    }

}
