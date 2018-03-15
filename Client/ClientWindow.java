package ChillChat.Client;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

import static ChillChat.Client.Constants.LOGO_SHOW_TIME;

public class ClientWindow {

    private final Stage clientStage;
    private final Scene clientScene;
    private final Group totalGroup;
    private final MediaPlayer soundPlayer;

    private Pane activePane;


    public ClientWindow(Stage primaryStage) {

        clientStage = primaryStage;
        totalGroup = new Group();
        clientScene = new Scene(totalGroup,
                Screen.getPrimary().getBounds().getWidth(),
                Screen.getPrimary().getBounds().getHeight(),
                Color.BISQUE);

        clientScene.heightProperty();



        clientStage.setScene(clientScene);

        String uriString = new File("resources/music/ss13theme.mp3").toURI().toString();
        soundPlayer = new MediaPlayer(new Media(uriString));
        soundPlayer.setVolume(0.05);
        //soundPlayer.play();

    }


    public void launch() {

        ImageView logoImage = new ImageView(new Image(new File("resources/images/logo.png").toURI().toString()));

        logoImage.fitHeightProperty().bind(clientScene.heightProperty().divide(3));
        logoImage.fitWidthProperty().bind(clientScene.heightProperty().divide(3));
        logoImage.setOpacity(0);

        StackPane centralPane = new StackPane();

        centralPane.prefWidthProperty().bind(clientScene.widthProperty());
        centralPane.prefHeightProperty().bind(clientScene.heightProperty());

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



        LogInInterface logInInterface = new LogInInterface();
        VBox logInBox = logInInterface.getBox();
        centralPane.getChildren().add(logInBox);
        centralPane.setAlignment(logInBox, Pos.CENTER);



    }
}
