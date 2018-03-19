package ChillChat.Client;

import ChillChat.Client.Console.ConsoleClient;
import ChillChat.Client.Utilites.MusicPlayer;
import ChillChat.Client.Utilites.Utils;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

import static ChillChat.Client.Utilites.Constants.*;

public class ClientWindow {

    private final Client client;
    private final Stage clientStage;
    private final Scene clientScene;
    private final Group totalGroup;

    private ConsoleClient consoleClient;
    private Messenger messenger;

    private MusicPlayer musicPlayer;

    private StackPane activeNode;
    private LogInInterface logInInterface;


    ClientWindow(Client client, Stage primaryStage) {

        this.client = client;
        clientStage = primaryStage;
        totalGroup = new Group();
        clientScene = new Scene(totalGroup,
                Screen.getPrimary().getBounds().getWidth(),
                Screen.getPrimary().getBounds().getHeight(),
                Color.BLACK);

        clientStage.setScene(clientScene);

        musicPlayer = new MusicPlayer();
        musicPlayer.start();

        clientStage.setOnCloseRequest(e -> closeAllThreads());

    }

    private void closeAllThreads() {
        if (consoleClient != null)
            consoleClient.closeAllThreads();
        System.exit(1);
    }

    void launch() {

        ImageView logoImage = new ImageView(new Image(new File("resources/images/logo.png").toURI().toString()));

        logoImage.fitHeightProperty().bind(clientScene.heightProperty().divide(3));
        logoImage.fitWidthProperty().bind(clientScene.heightProperty().divide(3));
        logoImage.setOpacity(0);

        StackPane centralPane = new StackPane();

        centralPane.getChildren().add(logoImage);
        StackPane.setAlignment(logoImage, Pos.CENTER);

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
            launchLogIn(centralPane, false);
        });

        totalGroup.getChildren().add(centralPane);

        centralPane.prefWidthProperty().bind(clientScene.widthProperty());
        centralPane.prefHeightProperty().bind(clientScene.heightProperty());
        centralPane.maxWidthProperty().bind(clientScene.widthProperty());
        centralPane.maxHeightProperty().bind(clientScene.heightProperty());

        fadeIn.play();

    }

    private void launchLogIn(StackPane centralPane, boolean connectionError) {

        centralPane.getChildren().clear();
        centralPane.setOpacity(1);

        ImageView background = new ImageView(Utils.getRandomLogInBackground());
        double scaleCoef = (350*500)/background.getImage().getWidth();
        background.scaleXProperty().bind(centralPane.widthProperty().divide(scaleCoef));
        background.scaleYProperty().bind(centralPane.widthProperty().divide(scaleCoef));
        background.setOpacity(0);

        consoleClient = new ConsoleClient(this);

        LogInInterface logInInterface = new LogInInterface(this, centralPane, consoleClient.getLogIn());
        this.logInInterface = logInInterface;
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

        StackPane.setAlignment(logInBox, Pos.CENTER);
        StackPane.setAlignment(background, Pos.CENTER);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(LOGIN_FADE_TIME), logInBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition backFadeIn = new FadeTransition(Duration.seconds(LOGIN_FADE_TIME*2), background);
        backFadeIn.setFromValue(0);
        backFadeIn.setToValue(1);

        centralPane.prefWidthProperty().bind(clientScene.widthProperty());
        centralPane.prefHeightProperty().bind(clientScene.heightProperty());
        centralPane.maxWidthProperty().bind(clientScene.widthProperty());
        centralPane.maxHeightProperty().bind(clientScene.heightProperty());

        backFadeIn.play();
        fadeIn.play();

        if (connectionError) {
            logInInterface.serverIsUnavalable();
            musicPlayer.slowPlayFromStart();
        }


    }

    void loggedIn() {

        //this.logInInterface = null;

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(LOGIN_FADE_TIME), activeNode);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> changeToMessenger());
        fadeOut.play();

    }

    private void changeToMessenger() {

        musicPlayer.slowShutUp();

        StackPane centralPane = new StackPane();

        centralPane.prefWidthProperty().bind(clientScene.widthProperty());
        centralPane.prefHeightProperty().bind(clientScene.heightProperty());
        centralPane.maxWidthProperty().bind(clientScene.widthProperty());
        centralPane.maxHeightProperty().bind(clientScene.heightProperty());

        Messenger messenger = new Messenger(consoleClient, centralPane, clientScene, consoleClient.getColor(), client, this);
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

    public void displayServerMessage(String text) {
        if (messenger != null)
            messenger.displayServerMessage(text);
    }

    public void inputStreamProblem() {
        connectionProblem();
    }

    private void connectionProblem() {
        goToLoginScreen(true);
    }

    void goToLoginScreen(boolean becauseOfError) {
        closeConnection();
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(LOGIN_FADE_TIME), activeNode);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.play();
        fadeOut.setOnFinished(e -> launchLogIn(activeNode, becauseOfError));
    }

    private void closeConnection() {
        consoleClient.closeAllThreads();
    }

    private void fadeOut() {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(LOGIN_FADE_TIME), activeNode);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.play();
    }

    private void fadeIn() {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(LOGIN_FADE_TIME), activeNode);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    public void outputStreamError() {
        connectionProblem();
    }

    void startConsoleClient() {
        if (!consoleClient.isInitiated())
            consoleClient.start();
    }

    public void unableToConnect() {
        reconnect();
    }

    private void reconnect() {
        consoleClient = new ConsoleClient(this);
        logInInterface.updateConsoleClient(consoleClient);
        consoleClient.getLogIn().setLogInInterface(logInInterface);
    }

}
