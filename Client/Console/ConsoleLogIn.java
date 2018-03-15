package ChillChat.Client.Console;

import ChillChat.Client.LogInInterface;

public class ConsoleLogIn {

    LogInInterface logInInterface;
    ConsoleClient consoleClient;

    public ConsoleLogIn(ConsoleClient client) {
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

        System.out.println("Удачно авторизован.");
        logInInterface.loggedIn();

    }


}
