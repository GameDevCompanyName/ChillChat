package ChillChat.Client;

import ChillChat.Client.Console.ConsoleClient;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

import static ChillChat.Client.Constants.*;

public class ClientWindow {

    private final Stage clientStage;
    private final Scene clientScene;
    private final Group totalGroup;
    private final MediaPlayer soundPlayer;

    private ConsoleClient consoleClient;
    private Messenger messenger;

    private Node activeNode;


    public ClientWindow(Stage primaryStage) {

        clientStage = primaryStage;
        totalGroup = new Group();
        clientScene = new Scene(totalGroup,
                Screen.getPrimary().getBounds().getWidth(),
                Screen.getPrimary().getBounds().getHeight(),
                Color.BLACK);

        clientStage.setScene(clientScene);

        String uriString = new File("resources/music/ss13theme.mp3").toURI().toString();
        soundPlayer = new MediaPlayer(new Media(uriString));
        soundPlayer.setVolume(0.05);
        if (PLAY_MUSIC_ON_START)
            soundPlayer.play();

    }


    public void launch() {

        ImageView logoImage = new ImageView(new Image(new File("resources/images/logo.png").toURI().toString()));

        logoImage.fitHeightProperty().bind(clientScene.heightProperty().divide(3));
        logoImage.fitWidthProperty().bind(clientScene.heightProperty().divide(3));
        logoImage.setOpacity(0);

        StackPane centralPane = new StackPane();

        centralPane.prefWidthProperty().bind(clientScene.widthProperty());
        centralPane.prefHeightProperty().bind(clientScene.heightProperty());
        centralPane.maxWidthProperty().bind(clientScene.widthProperty());
        centralPane.maxHeightProperty().bind(clientScene.heightProperty());

        centralPane.getChildren().add(logoImage);
        centralPane.setAlignment(logoImage, Pos.CENTER);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(LOGO_SHOW_TIME * 0.2), logoImage);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition stay = new FadeTransition(Duration.seconds(LOGO_SHOW_TIME * 0.6), logoImage);
        stay.setFromValue(1);
        stay.setToValue(1);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(LOGO_SHOW_TIME * 0.2), logoImage);
        stay.setFromValue(1);
        stay.setToValue(0);

        fadeIn.setOnFinished(e -> stay.play());
        stay.setOnFinished(e -> fadeOut.play());
        fadeOut.setOnFinished(e -> {
            centralPane.getChildren().remove(logoImage);
            launchLogIn(centralPane);
        });

        totalGroup.getChildren().add(centralPane);

        fadeIn.play();

    }

    private void launchLogIn(StackPane centralPane) {

        ImageView background = new ImageView(new Image(new File("resources/images/chill.gif").toURI().toString()));
        background.scaleXProperty().bind(centralPane.widthProperty().divide(350));
        background.scaleYProperty().bind(centralPane.widthProperty().divide(350));
        background.setOpacity(0);

        consoleClient = new ConsoleClient(this);
        consoleClient.start();

        LogInInterface logInInterface = new LogInInterface(this, centralPane, consoleClient.getLogIn());
        Pane logInBox = logInInterface.getContainer();
        logInBox.setOpacity(0);

        consoleClient.getLogIn().setLogInInterface(logInInterface);

        logInBox.maxWidthProperty().bind(centralPane.widthProperty());
        logInBox.maxHeightProperty().bind(centralPane.heightProperty());

        if (DEBUG){
            logInBox.setStyle("-fx-border-color: green");
        }

        logInInterface.setTextColor(Color.WHITESMOKE);

        activeNode = centralPane;

        centralPane.getChildren().addAll(background, logInBox);

        centralPane.setAlignment(logInBox, Pos.CENTER);
        centralPane.setAlignment(background, Pos.CENTER);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(LOGIN_FADE_TIME), logInBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition backFadeIn = new FadeTransition(Duration.seconds(LOGIN_FADE_TIME*2), background);
        backFadeIn.setFromValue(0);
        backFadeIn.setToValue(1);

        backFadeIn.play();
        fadeIn.play();

    }

    public void loggedIn() {

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(LOGIN_FADE_TIME), activeNode);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> changeToMessenger());
        fadeOut.play();

    }

    private void changeToMessenger() {

        StackPane centralPane = new StackPane();

        centralPane.prefWidthProperty().bind(clientScene.widthProperty());
        centralPane.prefHeightProperty().bind(clientScene.heightProperty());
        centralPane.maxWidthProperty().bind(clientScene.widthProperty());
        centralPane.maxHeightProperty().bind(clientScene.heightProperty());

        Messenger messenger = new Messenger(consoleClient, centralPane, clientScene);
        this.messenger = messenger;

        totalGroup.getChildren().remove(activeNode);
        centralPane.getChildren().add(messenger.getContainer());
        activeNode = centralPane;
        totalGroup.getChildren().add(activeNode);

    }

    public void displayMessage(String name, String text, Integer color) {
        if (messenger != null)
            messenger.displayMessage(name, text, color);
    }

}
