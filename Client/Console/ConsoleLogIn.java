package ChillChat.Client.Console;

public class ConsoleLogIn {

    ConsoleClient consoleClient;

    public ConsoleLogIn(ConsoleClient consoleClient) {
        this.consoleClient = consoleClient;
    }

    public void tryToLogIn() {
        System.out.println("---Попытка авторизации---");
        System.out.println("Введите логин: ");
        String login = consoleClient.scanner.nextLine();
        System.out.println("Введите пароль: ");
        String pass = consoleClient.scanner.nextLine();
        sendLogInAttempt(login, pass);
    }

    private void sendLogInAttempt(String login, String pass) {
        consoleClient.out.println(JsonHandler.getString(login, pass));
    }

    public void serverAnswer(String response){

        if (response.equals("-1")){
            System.out.println("Авторизация не удалась.");
            tryToLogIn();
            return;
        }

        System.out.println("Удачно авторизован.");
        consoleClient.loggedIn();

    }


}
