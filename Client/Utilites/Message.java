package ChillChat.Client.Utilites;

import ChillChat.Client.Client;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static ChillChat.Client.Utilites.Constants.*;

public class Message extends StackPane {

    public static Client client;
    public static Pane parent;

    public static Font commonFont;
    public static Font nameFont;
    public static Font serverNameFont;
    public static Font serverTextFont;

    private String senderName;
    private String senderColor;
    private MessageType type;
    private VBox content;

    private Boolean selected;

    private VBox textFlows;
    private ImageView imageView;
    private Paint backColor;
    private Paint selectedBackColor;
    private Color nameColor;
    private WebView video;
    private Rectangle backgroundRect;

    //private Glow glow;
    private Timeline clickedAnimation;

    public Message(String senderName, String senderColor){

        this.setAlignment(Pos.TOP_LEFT);

        this.content = new VBox();

        initRectangle();

        this.type = MessageType.COMMON_MESSAGE;

        createTextFlows();

        this.senderColor = senderColor;
        colorize(senderColor);

        Color defaultColor = Color.color(nameColor.getRed(), nameColor.getGreen(), nameColor.getBlue(), 0.1);
        Color selectedColor = Color.color(0.15, 0.25, 0.85, 0.4);

        backColor = defaultColor;
        selectedBackColor = selectedColor;

        this.senderName = senderName;

        if (DEBUG){
            this.setStyle("-fx-border-color: #FF765B");
            content.setStyle("-fx-border-color: RED");
        }

        setEffects();

    }

    public Message(String senderName, MessageType type){

        this.setAlignment(Pos.TOP_LEFT);

        this.content = new VBox();

        initRectangle();

        this.type = MessageType.SERVER_MESSAGE;

        createTextFlows();

        this.senderColor = "9";
        colorize(senderColor);

        Color defaultColor = Color.color(nameColor.getRed(), nameColor.getGreen(), nameColor.getBlue(), 0.1);
        Color selectedColor = Color.color(0.15, 0.25, 0.85, 0.4);

        backColor = defaultColor;
        selectedBackColor = selectedColor;

        this.senderName = "[SERVER]";

        if (DEBUG){
            this.setStyle("-fx-border-color: #FF765B");
            content.setStyle("-fx-border-color: RED");
        }

        setEffects();

    }

    private void initRectangle() {
        this.backgroundRect = new Rectangle();
        backgroundRect.widthProperty().bind(content.widthProperty());
        content.heightProperty().addListener(e -> {
            smoothResizeRectangle();
        });
        backgroundRect.setArcHeight(10);
        backgroundRect.setArcWidth(10);
        this.getChildren().add(backgroundRect);
    }

    private void smoothResizeRectangle() {
        Timeline resize = new Timeline();

        resize.getKeyFrames().add(
                new KeyFrame(Duration.seconds(0.0), new KeyValue(backgroundRect.heightProperty(), backgroundRect.getHeight())));

        resize.getKeyFrames().add(
                new KeyFrame(Duration.seconds(0.1), new KeyValue(backgroundRect.heightProperty(), content.getHeight())));

        resize.play();
    }

    public static void setParentNode(Pane parentNode) {
        parent = parentNode;
    }

    public static void loadFonts(){
        try {
            commonFont = Font.loadFont(new FileInputStream(new File("resources/commonFont.ttf")), 16);
            nameFont = Font.loadFont(new FileInputStream(new File("resources/nameFont.ttf")), 14);
            serverNameFont = Font.loadFont(new FileInputStream(new File("resources/nameFont.ttf")), 16);
            serverTextFont = Font.loadFont(new FileInputStream(new File("resources/nameFont.ttf")), 13);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void createTextFlows() {
        this.textFlows = new VBox();
        textFlows.setSpacing(3);
        textFlows.setPadding(new Insets(2));
    }

    private void setEffects() {

        clickedAnimation = new Timeline();

        clickedAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(0.0), new KeyValue(scaleXProperty(), 1.0)));
        clickedAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(MESSAGE_CLICK_ANIMATION_TIME/4), new KeyValue(scaleXProperty(), 0.965)));
        clickedAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(MESSAGE_CLICK_ANIMATION_TIME), new KeyValue(scaleXProperty(), 1.0)));
        clickedAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(0.0), new KeyValue(scaleYProperty(), 1.0)));
        clickedAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(MESSAGE_CLICK_ANIMATION_TIME/4), new KeyValue(scaleYProperty(), 0.965)));
        clickedAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(MESSAGE_CLICK_ANIMATION_TIME), new KeyValue(scaleYProperty(), 1.0)));

    }

    private TextFlow buildText(String text){

        Font font = commonFont;
        if (type == MessageType.SERVER_MESSAGE)
            font = serverTextFont;

        Font boldTextFont;

        try {
            boldTextFont = Font.loadFont(new FileInputStream(new File("resources/nameFont.ttf")), 16);
        } catch (FileNotFoundException e) {
            boldTextFont = commonFont;
        }

        TextFlow textFlow = new TextFlow();

        if (DEBUG) {
            textFlow.setStyle("-fx-border-color: #81ffd9");
        }

        String[] parsedText = text.split(" ");
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < parsedText.length; i++){
            String word = parsedText[i];
            if (word.contains("www.") || word.contains("https://") || word.contains("http://")){

                if (buffer.length() != 0){
                    Text bufferedText = new Text();
                    colorizeText(bufferedText);
                    bufferedText.setText(buffer.toString());
                    bufferedText.setFont(font);
                    textFlow.getChildren().add(bufferedText);
                    buffer = new StringBuilder();
                }


                if (word.contains(".png")
                        || word.contains(".jpg")
                        || word.contains(".gif")
                        || word.contains(".jpeg")
                        || word.contains(".bmp"))
                    this.tryToAddImage(word);
                else {
                    Hyperlink link = new Hyperlink(word, client);
                    link.setFont(font);
                    link.makeSmooth(LINK_COLOR_CHANGE_TIME);
                    textFlow.getChildren().add(link);
                }

                /*
                if (word.contains("youtube") || word.contains("youtu.be") || word.contains(".webm") || word.contains(".mp4") || word.contains(".flv"))
                    message.tryToAddVideo(word);
                */


            } else {
                if (word.charAt(0) == '@'){
                    if (buffer.length() != 0){
                        Text bufferedText = new Text();
                        colorizeText(bufferedText);
                        bufferedText.setText(buffer.toString());
                        bufferedText.setFont(font);
                        textFlow.getChildren().add(bufferedText);
                        buffer = new StringBuilder();
                    }

                    Text boldText = new Text(word.substring(1));
                    colorizeText(boldText);
                    boldText.setFont(boldTextFont);
                    textFlow.getChildren().add(boldText);

                } else
                    buffer.append(word);
            }


            if (i != parsedText.length - 1)
                buffer.append(" ");
        }

        if (buffer.length() != 0){
            Text bufferedText = new Text();
            if (type == MessageType.COMMON_MESSAGE)
                bufferedText.setStyle("-fx-fill: Lavender;");
            if (type == MessageType.SERVER_MESSAGE)
                bufferedText.setStyle("-fx-fill: LightSkyBlue");
            bufferedText.setText(buffer.toString());
            bufferedText.setFont(font);
            textFlow.getChildren().add(bufferedText);
        }

        return textFlow;

    }

    public void addText(String text){

        TextFlow newText = buildText(text);
        boopFromFlat(newText);
        textFlows.getChildren().add(newText);

    }

    private void boopFromFlat(TextFlow newText) {

        Timeline boop = new Timeline();

        boop.getKeyFrames().add(
                new KeyFrame(Duration.seconds(0.0), new KeyValue(newText.scaleYProperty(), 0.0)));

        boop.getKeyFrames().add(
                new KeyFrame(Duration.seconds(TEXT_APPEAR_TIME), new KeyValue(newText.scaleYProperty(), 1.0)));

        boop.play();

    }

    private void colorizeText(Text bufferedText) {
        if (type == MessageType.COMMON_MESSAGE)
            bufferedText.setStyle("-fx-fill: Lavender;");
        if (type == MessageType.SERVER_MESSAGE)
            bufferedText.setStyle("-fx-fill: LightSkyBlue;");
    }

    public void tryToAddImage(String path){

        imageView = new ImageView();
        Image image = new Image(path);
        imageView.setImage(image);
        double scaleCoef = 380/image.getWidth();
        imageView.setFitHeight(image.getHeight()*scaleCoef);
        imageView.setFitWidth(image.getWidth()*scaleCoef);

        StackPane imagePane = new StackPane();
        imagePane.maxWidthProperty().bind(imageView.fitWidthProperty());
        imagePane.maxHeightProperty().bind(imageView.fitHeightProperty());

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.color(0, 0, 0, 0.35));
        shadow.setOffsetX(5);
        shadow.setOffsetY(5);
        shadow.setRadius(5);
        imagePane.setEffect(shadow);
        imagePane.getChildren().add(imageView);
        textFlows.getChildren().add(imagePane);
        //createTextFlows();

    }

    public void build(){

        selected = false;

        Text nickname = new Text(senderName);
        nickname.setFont(nameFont);
        if (type == MessageType.SERVER_MESSAGE)
            nickname.setFont(serverNameFont);
        nickname.setFill(nameColor);



        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(2);
        dropShadow.setOffsetY(3);
        dropShadow.setOffsetX(3);
        dropShadow.setColor(Color.color(0.05, 0.05, 0.05, 0.6));
        //dropShadow.setInput(glow);
        nickname.setEffect(dropShadow);

        content.setPadding(new Insets(2,2, 2, 4));

        setDefaultBackground();

        content.setSpacing(2);
        content.prefWidthProperty().bind(textFlows.widthProperty());
        content.setAlignment(Pos.TOP_LEFT);

        StackPane nameBox = new StackPane();
        nameBox.setPadding(new Insets(1));
        nameBox.setAlignment(Pos.CENTER);
        nameBox.getChildren().add(nickname);
        nameBox.prefHeightProperty().set((
                nickname.getLayoutBounds().getMaxY() - nickname.getLayoutBounds().getMinY()
        ) + 6);
        nameBox.maxWidthProperty().set((
                nickname.getLayoutBounds().getMaxX() - nickname.getLayoutBounds().getMinX()
        ) + 10);
        content.getChildren().addAll(nameBox, textFlows);

        this.getChildren().add(content);

    }


    private void colorize(String nickname) {

        switch (senderColor){
            case "1":
                nameColor = Color.web("#f44336");
                break;
            case "2":
                nameColor = Color.web("#3f51b5");
                break;
            case "3":
                nameColor = Color.web("#29b6f6");
                break;
            case "4":
                nameColor = Color.web("#ff5722");
                break;
            case "5":
                nameColor = Color.web("#4caf50");
                break;
            case "6":
                nameColor = Color.web("#8bc34a");
                break;
            case "7":
                nameColor = Color.web("#ffeb3b");
                break;
            case "8":
                nameColor = Color.web("#ec407a");
                break;
            case "9":
                nameColor = Color.web("LightSkyBlue");
                break;
            default:
                nameColor = Color.web("#546e7a");
                break;
        }

    }

    public void tryToAddVideo(String word) {

        video = new WebView();
        video.getEngine().load(
                word
        );
        video.setPrefSize(840, 690);


    }

    public String getSenderName() {
        return senderName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void select() {
        setSelectedBackground();
        selected = true;
    }

    public void unSelect() {
        setDefaultBackground();
        selected = false;
    }

    private void smoothChangeBackColor(Paint backColor) {
        //TODO SOMEHOW
        backgroundRect.setFill(backColor);
    }

    private void setDefaultBackground() {
        smoothChangeBackColor(backColor);
    }

    private void setSelectedBackground() {
        smoothChangeBackColor(selectedBackColor);
    }

    public void playPressedAnimation() {
        clickedAnimation.playFromStart();
    }

    public enum MessageType{
        SERVER_MESSAGE, COMMON_MESSAGE, CLIENT_MESSAGE
    }

}
