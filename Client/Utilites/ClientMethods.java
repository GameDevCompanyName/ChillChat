package ChillChat.Client.Utilites;

import ChillChat.Client.ClientWindow;

public class ClientMethods {

    static ClientWindow clientWindow;

    public static void setClientWindow(ClientWindow clientWindow) {
        ClientMethods.clientWindow = clientWindow;
    }

    public static void clientVersionRequestReceived(){
        //TODO
    }

    public static void loginWrongErrorReceived(){
        clientWindow.passWrongError();
    }

    public static void loginAlreadyErrorReceived(){
        clientWindow.userAlreadyOnline();
    }

    public static void loginSuccessReceived(){
        clientWindow.loginSuccess();
    }

    public static void userRegistrationSuccessReceived(){
        //TODO
    }

    public static void userColorReceived(String login, String color){
        clientWindow.userColorRecieved(login, color);
    }

    public static void userMessageReceived(String login, String message, String color){
        clientWindow.displayMessage(login, message, color);
    }

    public static void userActionReceived(String login, String action){
        //TODO
    }

    public static void serverMessageReceived(String message){
        clientWindow.displayServerMessage(message);
    }

    public static void serverEventReceived(String event){
        //TODO
    }

    public static void serverUserKickedReceived(String login, String reason){
        clientWindow.userKickedRecieved(login, reason);
    }

    public static void userDisconnectReceived(String reason){
        clientWindow.disconnectedByReason(reason);
    }

    public static void serverUserLoginReceived(String login){
        clientWindow.userConnectedRecieved(login);
    }

    public static void serverUserDisconnectReceived(String login){
        clientWindow.userDisconnectedRecieved(login);
    }

    public static void serverPingRequest() {
        clientWindow.sendPong();
    }
}
