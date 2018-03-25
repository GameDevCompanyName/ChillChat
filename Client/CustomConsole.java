package ChillChat.Client;

import ChillChat.Client.Utilites.Hyperlink;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import static ChillChat.Client.Utilites.Constants.DEBUG;
import static ChillChat.Client.Utilites.Constants.LINK_COLOR_CHANGE_TIME;
import static ChillChat.Client.Utilites.Constants.TEXT_APPEAR_TIME;

class CustomConsole {

    private Client client;
    private StackPane mainBox;
    private ScrollPane scrollPane;
    private VBox textBox;

    CustomConsole(StackPane parentPane, Client client){

        this.client = client;
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

    void textAppend(String name, String text, String color){

        TextFlow flow = new TextFlow();
        Font textFont = new Font("Courier New", 20);

        Text t1 = new Text();
        t1.setFont(textFont);
        t1.setText(name + ": ");

        switch (color){
            case "1":
                t1.setStyle("-fx-fill: #f44336;");
                break;
            case "2":
                t1.setStyle("-fx-fill: #3f51b5;");
                break;
            case "3":
                t1.setStyle("-fx-fill: #29b6f6;");
                break;
            case "4":
                t1.setStyle("-fx-fill: #ff5722;");
                break;
            case "5":
                t1.setStyle("-fx-fill: #4caf50;");
                break;
            case "6":
                t1.setStyle("-fx-fill: #8bc34a;");
                break;
            case "7":
                t1.setStyle("-fx-fill: #ffeb3b;");
                break;
            case "8":
                t1.setStyle("-fx-fill: #ec407a;");
                break;
            default:
                t1.setStyle("-fx-fill: #546e7a;");
                break;
        }

        flow.getChildren().addAll(t1);

        String[] parsedText = text.split(" ");
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < parsedText.length; i++){
            String word = parsedText[i];
            if (word.contains("www.") || word.contains("https://") || word.contains("http://")){

                if (buffer.length() != 0){
                    Text bufferedText = new Text();
                    bufferedText.setStyle("-fx-fill: Lavender;");
                    bufferedText.setText(buffer.toString());
                    bufferedText.setFont(textFont);
                    flow.getChildren().add(bufferedText);
                    buffer = new StringBuilder();
                }

                Hyperlink link = new Hyperlink(word, client);
                link.setFont(textFont);
                link.makeSmooth(LINK_COLOR_CHANGE_TIME);
                flow.getChildren().add(link);

            } else
                buffer.append(word);

            if (i != parsedText.length - 1)
                buffer.append(" ");
        }

        if (buffer.length() != 0){
            Text bufferedText = new Text();
            bufferedText.setStyle("-fx-fill: Lavender;");
            bufferedText.setText(buffer.toString());
            bufferedText.setFont(textFont);
            flow.getChildren().add(bufferedText);
        }

        flow.setOpacity(0.0);

        textBox.getChildren().add(flow);

        animateTextAppear(flow);


        slowScrollToBottom();

    }

    private void animateTextAppear(Node node) {

        GaussianBlur blur = new GaussianBlur();

        node.setEffect(blur);

        Timeline textAppear = new Timeline();
        textAppear.getKeyFrames().add(
                new KeyFrame(Duration.seconds(0.0), new KeyValue(blur.radiusProperty(), 25.0)));
        textAppear.getKeyFrames().add(
                new KeyFrame(Duration.seconds(TEXT_APPEAR_TIME), new KeyValue(blur.radiusProperty(), 0.0)));
        textAppear.getKeyFrames().add(
                new KeyFrame(Duration.seconds(0.0), new KeyValue(node.opacityProperty(), 0.0)));
        textAppear.getKeyFrames().add(
                new KeyFrame(Duration.seconds(TEXT_APPEAR_TIME), new KeyValue(node.opacityProperty(), 1)));
        textAppear.play();


        /*
        Timeline shutUpAnimation = new Timeline();
        shutUpAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(0.0), new KeyValue(flow.opacityProperty(), 0)));
        shutUpAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(0.5), new KeyValue(flow.opacityProperty(), 1)));
        shutUpAnimation.play();
        */

    }

    Node getBox() {
        return mainBox;
    }

    void serverMessageAppend(String text) {

        TextFlow flow = new TextFlow();
        Font textFont = new Font("Courier New", 20);

        Text t1 = new Text();
        t1.setFont(textFont);
        t1.setText(text);
        t1.setStyle("-fx-fill: LightSkyBlue;");

        flow.getChildren().add(t1);

        flow.setOpacity(0.0);
        textBox.getChildren().add(flow);
        animateTextAppear(flow);

        slowScrollToBottom();

    }

}

