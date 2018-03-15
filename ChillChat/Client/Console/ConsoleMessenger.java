package ChillChat.Client.Console;

public class ConsoleMessenger {

    ConsoleClient client;

    public ConsoleMessenger(ConsoleClient consoleClient) {
        client = consoleClient;
    }

    public void start() {

        System.out.println("Подключаюсь к чату...");

        MessageSender messageSender = new MessageSender(client.out, client.scanner);
        messageSender.start();

        System.out.println("Подключен к чату");

    }

    /*
    private void sendMessage(String text) {
        client.out.println(JsonHandler.getString(text));
    }
    */

}
