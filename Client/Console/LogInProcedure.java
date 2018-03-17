package ChillChat.Client.Console;

import ChillChat.Client.LogInInterface;

public class LogInProcedure {

    LogInInterface logInInterface;
    ConsoleClient consoleClient;

    int color;

    public LogInProcedure(ConsoleClient client) {
        this.consoleClient = client;
    }

    public void tryToLogIn(String login, String pass) {
        sendLogInAttempt(login, pass);
    }

    private void sendLogInAttempt(String login, String pass) {
        consoleClient.out.println(JsonHandler.getString(login, pass));
    }

    public void setLogInInterface(LogInInterface inInterface){
        logInInterface = inInterface;
    }

    public void serverAnswer(String response){

        if (response.equals("-1")){
            logInInterface.wrongPass();
            return;
        }

        if (response.equals("-2")) {
            logInInterface.userAlreadyExists();
            return;
        }

        color = Integer.parseInt(response);
        System.out.println("Удачно авторизован.");
        logInInterface.loggedIn();

    }


    public Integer getColor() {
        return color;
    }
}
