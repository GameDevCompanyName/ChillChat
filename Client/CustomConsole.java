package ChillChat.Client;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import static ChillChat.Client.Utilites.Constants.DEBUG;

public class CustomConsole {

    StackPane mainBox;
    ScrollPane scrollPane;
    VBox textBox;

    public CustomConsole(StackPane parentPane){

        mainBox = new StackPane();
        textBox = new VBox();
        scrollPane = new ScrollPane();

        mainBox.prefHeightProperty().bind(parentPane.heightProperty());
        mainBox.prefWidthProperty().bind(parentPane.widthProperty());
        mainBox.maxHeightProperty().bind(parentPane.heightProperty());
        mainBox.maxWidthProperty().bind(parentPane.widthProperty());

        if (DEBUG)
            mainBox.setStyle("-fx-border-color: red");
        mainBox.setAlignment(Pos.CENTER);

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

    }

    private void slowScrollToBottom() {
        Animation animation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(scrollPane.vvalueProperty(), 1.0)));
        animation.play();
    }

    public void textAppend(String name, String text, Integer color){

        TextFlow flow = new TextFlow();
        Font textFont = new Font("Courier New", 18);

        Text t1 = new Text();
        t1.setFont(textFont);
        t1.setText(name + ": ");

        Text t2 = new Text();
        t2.setStyle("-fx-fill: Lavender;");
        t2.setText(text);
        t2.setFont(textFont);

        switch (color){
            case 1:
                t1.setStyle("-fx-fill: Crimson;");
                break;
            case 2:
                t1.setStyle("-fx-fill: CornflowerBlue;");
                break;
            case 3:
                t1.setStyle("-fx-fill: Cyan;");
                break;
            case 4:
                t1.setStyle("-fx-fill: DarkOrange;");
                break;
            case 5:
                t1.setStyle("-fx-fill: DarkSeaGreen;");
                break;
            case 6:
                t1.setStyle("-fx-fill: ForestGreen;");
                break;
            case 7:
                t1.setStyle("-fx-fill: Khaki;");
                break;
            case 8:
                t1.setStyle("-fx-fill: HotPink;");
                break;
        }

        flow.getChildren().addAll(t1, t2);
        flow.setOpacity(0.0);

        textBox.getChildren().add(flow);

        Timeline shutUpAnimation = new Timeline();
        shutUpAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(0.0), new KeyValue(flow.opacityProperty(), 0)));
        shutUpAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(0.6), new KeyValue(flow.opacityProperty(), 1)));
        shutUpAnimation.play();

        slowScrollToBottom();

    }

    public Node getBox() {
        return mainBox;
    }

    public void serverMessageAppend(String text) {

        TextFlow flow = new TextFlow();
        Font textFont = new Font("Courier New", 20);

        Text t1 = new Text();
        t1.setFont(textFont);
        t1.setText("[SERVER]: " + text);
        t1.setStyle("-fx-fill: LightSkyBlue;");

        flow.getChildren().add(t1);
        textBox.getChildren().add(flow);

    }
}

