package ChillChat.Client;

import ChillChat.Client.Utilites.Message;
import javafx.application.Application;
import javafx.stage.Stage;

public class Client extends Application {

    @Override
    public void start(Stage primaryStage) {

        Message.loadFonts();

        primaryStage.setTitle("C   H   I   L   L");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();

        ClientWindow clientWindow = new ClientWindow(this, primaryStage);

        clientWindow.launch();

    }

}
