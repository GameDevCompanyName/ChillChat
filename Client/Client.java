package ChillChat.Client;

import ChillChat.Client.Utilites.Message;
import javafx.application.Application;
import javafx.stage.Stage;

public class Client extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Thread.currentThread().setName("MAIN");

        Message.loadFonts();

        primaryStage.setTitle("C   H   I   L   L");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();

        ClientWindow clientWindow = new ClientWindow(this, primaryStage);

        clientWindow.launch();

    }

}
