package ChillChat.Client;

import ChillChat.Client.Console.ConsoleClient;
import ChillChat.Client.Utilites.ChillTextPane;
import ChillChat.Client.Utilites.ClientMessage;
import ChillChat.Client.Utilites.Constants;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static ChillChat.Client.Utilites.Constants.TEXT_DISAPPEAR_TIME;

public class Messenger {

    private Scene clientScene;
    private ConsoleClient consoleClient;
    private ClientWindow clientWindow;
    private StackPane centralPane;

    private VBox messengerBox;

    private CustomConsole console;
    private ChillTextPane inputField;
    private GaussianBlur textBlur;


    Messenger(ConsoleClient consoleClient, StackPane centralPane, Scene clientScene, Client client, ClientWindow clientWindow, Client client1) {

        this.centralPane = centralPane;
        this.clientWindow = clientWindow;
        this.clientScene = clientScene;

        clientScene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)){
                clientWindow.goToLoginScreen(false);
            }

            if (event.getCode().equals(KeyCode.DELETE)){
                console.deleteSelectedMessages();
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


        Glow textGlow = new Glow();
        textGlow.setLevel(1);
        textBlur = new GaussianBlur();
        textBlur.setRadius(0.0);

        Font inputFieldFont = null;
        try {
            inputFieldFont = Font.loadFont(new FileInputStream(new File("resources/commonFont.ttf")), 16);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        textBlur.setInput(textGlow);

        inputField = new ChillTextPane(inputFieldFont, this);
        inputField.prefWidthProperty().bind(centralPane.widthProperty());
        inputField.prefWidthProperty().bind(centralPane.widthProperty());

        consolePane.prefHeightProperty().bind(messengerBox.heightProperty().subtract(inputField.heightProperty()));
        consolePane.maxHeightProperty().bind(messengerBox.heightProperty().subtract(inputField.heightProperty()));
        consolePane.prefWidthProperty().bind(messengerBox.widthProperty());
        consolePane.maxWidthProperty().bind(messengerBox.widthProperty());

        console = new CustomConsole(consolePane, client);

        consolePane.getChildren().add(console.getBox());

        messengerBox.getChildren().addAll(consolePane, inputField);

    }

    public void changeInterfaceColor(String color) {

        inputField.changeColor(color);



    }

    public void flushTextFromField() {

        String text = inputField.getText();

        if (text.isEmpty())
            return;
        if (text.length() > 1000)
            sendMessage(text.substring(0, 1000));
        else
            sendMessage(text);

        animatedEreaseText();

    }

    private void animatedEreaseText() {

        Timeline textDissapear = new Timeline();
        textDissapear.getKeyFrames().add(new KeyFrame(Duration.seconds(0.0), new KeyValue(textBlur.radiusProperty(), 0.0)));
        textDissapear.getKeyFrames().add(new KeyFrame(Duration.seconds(TEXT_DISAPPEAR_TIME), new KeyValue(textBlur.radiusProperty(), 20.0)));
        textDissapear.play();
        textDissapear.setOnFinished(e -> {
            inputField.clear();
            textBlur.setRadius(0);
        });

    }

    public StackPane getCentralPane() {
        return centralPane;
    }

    Node getContainer() {
        return messengerBox;
    }

    void displayMessage(String name, String text, String color) {
        console.userTextAppend(name, text, color);
    }

    private void sendMessage(String text) {
        consoleClient.sendMessage(ClientMessage.messageSend(text));
    }

    void displayServerMessage(String text) {
        console.serverMessageAppend(text);
    }

    public void displayUserKicked(String login, String reason) {
        console.serverMessageAppend(login + " отключился.\nПричина: " + reason);
    }

    public void displayDisconnectedByReason(String reason) {
        console.serverMessageAppend("Вы отключены от сервера.\nПричина: " + reason);
    }

    public void displayNewUserConnected(String login) {
        console.serverMessageAppend(login + " подключился.");
    }

    public void displayUserDisconnected(String login) {
        console.serverMessageAppend(login + " отключился.");
    }

}
