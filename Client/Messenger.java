package ChillChat.Client;

import ChillChat.Client.Console.ConsoleClient;
import ChillChat.Client.Utilites.Constants;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Messenger {

    Scene clientScene;
    ConsoleClient consoleClient;
    ClientWindow clientWindow;

    VBox messengerBox;

    CustomConsole console;
    TextField inputField;


    public Messenger(ConsoleClient consoleClient, StackPane centralPane, Scene clientScene, Integer color, Client client, ClientWindow clientWindow) {

        this.clientWindow = clientWindow;
        this.clientScene = clientScene;

        clientScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)){
                    flushTextFromField();
                }

                if (event.getCode().equals(KeyCode.ESCAPE)){
                    clientWindow.goToLoginScreen(false);
                }
            }
        });

        this.consoleClient = consoleClient;
        messengerBox = new VBox();

        if (Constants.DEBUG)
            messengerBox.setStyle("-fx-border-color: yellow");

        messengerBox.prefHeightProperty().bind(centralPane.heightProperty());
        messengerBox.prefWidthProperty().bind(centralPane.widthProperty());
        messengerBox.maxHeightProperty().bind(centralPane.heightProperty());
        messengerBox.maxWidthProperty().bind(centralPane.widthProperty());

        StackPane consolePane = new StackPane();

        if (Constants.DEBUG)
            consolePane.setStyle("-fx-border-color: orange");

        inputField = new TextField();
        inputField.setFont(new Font("Courier New", 16));

        changeInterfaceColor(color);

        inputField.prefWidthProperty().bind(centralPane.widthProperty());

        consolePane.prefHeightProperty().bind(messengerBox.heightProperty().subtract(inputField.heightProperty()));
        consolePane.maxHeightProperty().bind(messengerBox.heightProperty().subtract(inputField.heightProperty()));
        consolePane.prefWidthProperty().bind(messengerBox.widthProperty());
        consolePane.maxWidthProperty().bind(messengerBox.widthProperty());

        console = new CustomConsole(consolePane, client);

        consolePane.getChildren().add(console.getBox());

        messengerBox.getChildren().addAll(consolePane, inputField);

    }

    private void changeInterfaceColor(Integer color) {

        switch (color){
            case 1:
                inputField.setStyle("-fx-background-color: transparent;-fx-text-inner-color: Crimson;");
                break;
            case 2:
                inputField.setStyle("-fx-background-color: transparent;-fx-text-inner-color: CornflowerBlue;");
                break;
            case 3:
                inputField.setStyle("-fx-background-color: transparent;-fx-text-inner-color: Cyan;");
                break;
            case 4:
                inputField.setStyle("-fx-background-color: transparent;-fx-text-inner-color: DarkOrange;");
                break;
            case 5:
                inputField.setStyle("-fx-background-color: transparent;-fx-text-inner-color: DarkSeaGreen;");
                break;
            case 6:
                inputField.setStyle("-fx-background-color: transparent;-fx-text-inner-color: ForestGreen;");
                break;
            case 7:
                inputField.setStyle("-fx-background-color: transparent;-fx-text-inner-color: Khaki;");
                break;
            case 8:
                inputField.setStyle("-fx-background-color: transparent;-fx-text-inner-color: HotPink;");
                break;
        }

    }

    private void flushTextFromField() {

        String text = inputField.getText();

        if (text.isEmpty())
            return;
        if (text.length() > 255)
            sendMessage(text.substring(0, 255));
        else
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
        consoleClient.sendMessage(text);
    }

    public void displayServerMessage(String text) {
        console.serverMessageAppend(text);
    }

}
