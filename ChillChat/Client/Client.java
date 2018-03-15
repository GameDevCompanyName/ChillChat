package ChillChat.Client;

import javafx.application.Application;
import javafx.stage.Stage;

public class Client extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("C   H   I   L   L");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();

        ClientWindow clientWindow = new ClientWindow(primaryStage);

        clientWindow.launch();

        /*
        ConsoleClient client = new ConsoleClient();
        client.start();
        */

    }
}
