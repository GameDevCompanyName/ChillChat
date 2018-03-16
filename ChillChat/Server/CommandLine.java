package ChillChat.Server;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandLine extends Thread{
    private String text;
    private Scanner scanner;
    private Broadcaster broadcaster;
    private Message msg;
    private Commands cmds;

    public  CommandLine(DBConnector dbConnector, Broadcaster broadcaster){
        this.scanner = new Scanner(System.in);
        this.broadcaster = new Broadcaster();
        this.cmds = new Commands(dbConnector, broadcaster);
        this.broadcaster = broadcaster;
    }

    @Override
    public void run() {
        Pattern pattern = Pattern.compile("^/[a-zA-Z0-9_\\s]+$");
        Matcher m;
        while (true){
            text = scanner.nextLine();
            m = pattern.matcher(text);
            if(m.matches()) {
                text = text.substring(1);
                String[] command = text.split(" ");
                cmds.invoke(command);

            }
            else {
                msg = new Message(text, "SERVER", 3, 1);
                broadcaster.broadcastMessage(msg);
            }
        }
    }
}
