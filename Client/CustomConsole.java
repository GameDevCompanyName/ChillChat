package ChillChat.Client;

import ChillChat.Client.Utilites.Message;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static ChillChat.Client.Utilites.Constants.DEBUG;
import static ChillChat.Client.Utilites.Constants.TEXT_APPEAR_TIME;

class CustomConsole {

    private Client client;
    private StackPane mainBox;
    private ScrollPane scrollPane;
    private VBox textBox;

    private Message lastMessage;
    private List<Message> selectedMessages;

    private Font commonFont;
    private Font serverFont;

    CustomConsole(StackPane parentPane, Client client){

        this.selectedMessages = new ArrayList<>();
        this.client = client;
        mainBox = new StackPane();
        textBox = new VBox();
        scrollPane = new ScrollPane();

        Message.setParentNode(textBox);

        textBox.setPadding(new Insets(13));
        textBox.setSpacing(5);

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

    void userTextAppend(String name, String text, String color){

        if (lastMessage != null)
        {
            if (lastMessage.getSenderName().equals(name)) {
                lastMessage.addText(text);
                slowScrollToBottom();
                return;
            }
        }

        Message message = new Message(name, color);

        lastMessage = message;

        message.addText(text);

        message.build();

        textBox.getChildren().add(message);

        message.setOpacity(0.0);
        animateAppear(message);

        message.setOnMouseClicked(e -> {

            message.playPressedAnimation();
            if (!message.isSelected()){
                message.select();
                selectedMessages.add(message);
            } else {
                message.unSelect();
                selectedMessages.remove(message);
            }

        });

        slowScrollToBottom();

    }

    public static void animateAppear(Node node) {

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

        if (lastMessage != null)
        {
            if (lastMessage.getSenderName().equals("[SERVER]")) {
                lastMessage.addText(text);
                slowScrollToBottom();
                return;
            }
        }

        Message message = new Message("[SERVER]", Message.MessageType.SERVER_MESSAGE);
        lastMessage = message;
        message.addText(text);

        message.setOpacity(0.0);
        message.build();

        textBox.getChildren().add(message);
        message.setOnMouseClicked(e -> {
            animateSlideRightDissapear(message);
        });

        animateAppear(message);

        slowScrollToBottom();

    }

    private void animateSlideRightDissapear(Message message) {

        Timeline slideRight = new Timeline();

        slideRight.getKeyFrames().add(
                new KeyFrame(Duration.seconds(0.0), new KeyValue(message.translateXProperty(), message.getTranslateX())));
        slideRight.getKeyFrames().add(
                new KeyFrame(Duration.seconds(TEXT_APPEAR_TIME), new KeyValue(message.translateXProperty(), message.getTranslateX() + 2000)));
        slideRight.setOnFinished(e -> {
            if (textBox.getChildren().contains(message))
                textBox.getChildren().remove(message);
        });
        slideRight.play();

    }

    public void deleteSelectedMessages() {
        if (selectedMessages.isEmpty())
            return;

        for (Message message: selectedMessages) {
            animateSlideRightDissapear(message);
        }

        if (selectedMessages.contains(lastMessage))
            lastMessage = null;

        selectedMessages.clear();
    }

}

