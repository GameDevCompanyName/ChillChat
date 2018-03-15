package ChillChat.Client;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import static ChillChat.Client.Constants.DEBUG;

public class CustomConsole {

    StackPane mainBox;
    ScrollPane scrollPane;
    VBox textBox;
    Font font;

    public CustomConsole(StackPane parentPane){

        mainBox = new StackPane();
        textBox = new VBox();
        scrollPane = new ScrollPane();
        font = Font.font("Courier New", FontWeight.LIGHT, 16);

        mainBox.prefHeightProperty().bind(parentPane.heightProperty());
        mainBox.prefWidthProperty().bind(parentPane.widthProperty());
        mainBox.maxHeightProperty().bind(parentPane.heightProperty());
        mainBox.maxWidthProperty().bind(parentPane.widthProperty());

        if (DEBUG)
            mainBox.setStyle("-fx-border-color: red");
        mainBox.setAlignment(Pos.CENTER);

        /*
        textBox.heightProperty().addListener(event -> {
            slowScrollToBottom();
        });
        */

        scrollPane.setContent(textBox);
        if (DEBUG)
            scrollPane.setStyle("-fx-border-color: green");
        scrollPane.prefWidthProperty().bind(mainBox.widthProperty());
        scrollPane.prefHeightProperty().bind(mainBox.heightProperty());
        scrollPane.maxWidthProperty().bind(mainBox.widthProperty());
        scrollPane.maxHeightProperty().bind(mainBox.heightProperty());

        scrollPane.setBackground(Background.EMPTY);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;\n" +
                "-fx-background: transparent;");

        mainBox.getChildren().addAll(scrollPane);

        /*
        for (int i = 0; i < 100; i++){
            textAppend(GameTexts.randomLoadingText());
        }
        */

    }

    private void slowScrollToBottom() {
        Animation animation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(scrollPane.vvalueProperty(), 1.0)));
        animation.play();
    }

    public void textAppend(String name, String text, Integer color) {

        Label newText = new Label(name + ": " + text);

        newText.setFont(font);
        newText.setWrapText(true);
        if (DEBUG)
            newText.setStyle("-fx-border-color: orange");
        newText.maxWidthProperty().bind(scrollPane.widthProperty());

        textBox.getChildren().add(newText);

        slowScrollToBottom();

    }

    /*
    public void textAppend(String text) {

        textAppend(text, MessageType.DEFAULT);

    }
    */

    public Node getBox() {
        return mainBox;
    }

}

