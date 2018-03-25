package ChillChat.Client.Console;

import ChillChat.Client.LogInInterface;
import ChillChat.Client.Utilites.ClientMessage;

import static ChillChat.Client.Utilites.Constants.DESKTOP_VERSION;

public class LogInProcedure {

    private LogInInterface logInInterface;
    private ConsoleClient consoleClient;

    private String color;

    LogInProcedure(ConsoleClient client) {
        this.consoleClient = client;
    }

    public void tryToLogIn(String login, String pass) {
        consoleClient.sendMessage(ClientMessage.versionSend(DESKTOP_VERSION));
        consoleClient.sendMessage(ClientMessage.loginAttemptSend(login, pass));
    }

    public void setLogInInterface(LogInInterface inInterface){
        logInInterface = inInterface;
    }

    public String getColor() {
        return color;
    }

    public void passWrong() {
        logInInterface.wrongPass();
    }

    public void userAlreadyOnline() {
        logInInterface.userAlreadyExists();
    }

    public void loginSuccess() {
        logInInterface.loggedIn();
    }

}
