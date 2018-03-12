package ChillChat.Client;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;

class MessageSender extends Thread {
    private boolean stoped = false;

    private Scanner scanner;
    private PrintWriter out;



    public MessageSender(PrintWriter out, Scanner scanner) {
        this.scanner = scanner;
        this.out = out;
    }

    public void setStop() {
        stoped = true;
    }

    @Override
    public void run() {

        String text = "";

        while (!text.equals("ВЫЙТИ")){
            text = scanner.nextLine();
            sendMessage(text);
        }

    }

    private void sendMessage(String text) {
        out.println(JsonHandler.getString(text));
    }

    public String getTime() {
        Date date = new Date();
        return "[" + date + "]";
    }
}

