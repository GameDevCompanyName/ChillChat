package ChillChat.Client;

import ChillChat.Client.Console.ConsoleClient;
import ChillChat.Client.Console.JsonHandler;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import static ChillChat.Client.Constants.DEBUG;

public class Messenger {

    Scene clientScene;

    ConsoleClient consoleClient;
    VBox messengerBox;

    CustomConsole console;
    TextField inputField;


    public Messenger(ConsoleClient consoleClient, StackPane centralPane, Scene clientScene) {

        this.clientScene = clientScene;

        clientScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)){
                    flushTextFromField();
                }
            }
        });

        this.consoleClient = consoleClient;
        messengerBox = new VBox();

        if (DEBUG)
            messengerBox.setStyle("-fx-border-color: yellow");

        messengerBox.prefHeightProperty().bind(centralPane.heightProperty());
        messengerBox.prefWidthProperty().bind(centralPane.widthProperty());
        messengerBox.maxHeightProperty().bind(centralPane.heightProperty());
        messengerBox.maxWidthProperty().bind(centralPane.widthProperty());

        StackPane consolePane = new StackPane();

        if (DEBUG)
            consolePane.setStyle("-fx-border-color: orange");

        inputField = new TextField();
        inputField.prefWidthProperty().bind(centralPane.widthProperty());

        consolePane.prefHeightProperty().bind(messengerBox.heightProperty().subtract(inputField.heightProperty()));
        consolePane.maxHeightProperty().bind(messengerBox.heightProperty().subtract(inputField.heightProperty()));
        consolePane.prefWidthProperty().bind(messengerBox.widthProperty());
        consolePane.maxWidthProperty().bind(messengerBox.widthProperty());

        console = new CustomConsole(consolePane);

        consolePane.getChildren().add(console.getBox());


        messengerBox.getChildren().addAll(consolePane, inputField);

    }

    private void flushTextFromField() {
        sendMessage(inputField.getText());
        inputField.setText("");
    }

    public Node getContainer() {
        return messengerBox;
    }

    public void displayMessage(String name, String text, Integer color) {
        console.textAppend(name, text, color);
    }

    private void sendMessage(String text) {
        consoleClient.sendMessage(JsonHandler.getString(text));
    }

}
