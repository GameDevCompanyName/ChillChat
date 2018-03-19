package ChillChat.Client.Console;

import ChillChat.Client.LogInInterface;

public class LogInProcedure {

    private LogInInterface logInInterface;
    private ConsoleClient consoleClient;

    private int color;

    LogInProcedure(ConsoleClient client) {
        this.consoleClient = client;
    }

    public void tryToLogIn(String login, String pass) {
        sendLogInAttempt(login, pass);
    }

    private void sendLogInAttempt(String login, String pass) {
        if (consoleClient.getOut() == null){
            logInInterface.serverIsUnavalable();
            return;
        }
        consoleClient.getOut().println(JsonHandler.getString(login, pass));
    }

    public void setLogInInterface(LogInInterface inInterface){
        logInInterface = inInterface;
    }

    void serverAnswer(String response){

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


    Integer getColor() {
        return color;
    }
}
