package ChillChat.Client.Utilites;

import ChillChat.Client.Client;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import static ChillChat.Client.Utilites.Constants.DEBUG;
import static ChillChat.Client.Utilites.Constants.LINK_COLOR_CHANGE_TIME;
import static ChillChat.Client.Utilites.Constants.TEXT_APPEAR_TIME;

public class Message extends VBox {

    public static Client client;
    public static Pane parent;

    public static Font commonFont = new Font("Courier New", 18);
    public static Font nameFont = new Font("Courier New Bold Italic", 19);
    public static Font serverFont = new Font("Courier New Italic", 22);

    private String senderName;
    private String senderColor;
    private MessageType type;

    private VBox textFlows;
    private ImageView imageView;
    private Color backColor;
    private Glow glow;
    private WebView video;

    public Message(String senderName, String senderColor){

        this.type = MessageType.COMMON_MESSAGE;

        createTextFlows();

        this.senderColor = senderColor;

        this.senderName = senderName;

        if (DEBUG)
            this.setStyle("-fx-border-color: #FF765B");


        setGlowEffect();

    }

    public Message(String senderName, MessageType type){

        this.type = type;

        createTextFlows();

        switch (type){
            case SERVER_MESSAGE:
                this.backColor = Color.TRANSPARENT;
                this.senderName = senderName;
                this.senderColor = "9";
                break;
                //TODO
        }

        if (DEBUG)
            this.setStyle("-fx-border-color: #FF765B");


        setGlowEffect();
    }

    public static void setParentNode(Pane parentNode) {
        parent = parentNode;
    }

    private void createTextFlows() {
        this.textFlows = new VBox();
        textFlows.setSpacing(5);
        textFlows.setPadding(new Insets(3));
    }

    private void setGlowEffect() {

        glow = new Glow();
        this.setEffect(glow);

        Timeline glowUp = new Timeline();
        glowUp.getKeyFrames().add(new KeyFrame(Duration.seconds(0.0), new KeyValue(glow.levelProperty(), 0.2)));
        glowUp.getKeyFrames().add(new KeyFrame(Duration.seconds(0.15), new KeyValue(glow.levelProperty(), 1.0)));

        Timeline glowDown = new Timeline();
        glowDown.getKeyFrames().add(new KeyFrame(Duration.seconds(0.0), new KeyValue(glow.levelProperty(), 0.7)));
        glowDown.getKeyFrames().add(new KeyFrame(Duration.seconds(0.15), new KeyValue(glow.levelProperty(), 0.0)));

        setOnMouseEntered(e -> {
            glowDown.stop();
            glowUp.play();
        });

        setOnMouseExited(e -> {
            glowUp.stop();
            glowDown.play();
        });

    }

    private TextFlow buildText(String text){

        Font font = commonFont;
        if (type == MessageType.SERVER_MESSAGE)
            font = serverFont;

        TextFlow textFlow = new TextFlow();
        textFlow.setEffect(glow);

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

                Hyperlink link = new Hyperlink(word, client);
                link.setFont(font);
                link.makeSmooth(LINK_COLOR_CHANGE_TIME);
                textFlow.getChildren().add(link);


                /*
                if (word.contains("youtube") || word.contains("youtu.be") || word.contains(".webm") || word.contains(".mp4") || word.contains(".flv"))
                    message.tryToAddVideo(word);

                if (word.contains(".png") || word.contains(".jpg") || word.contains(".gif") || word.contains(".jpeg"))
                    message.tryToAddImage(word);
                */

            } else
                buffer.append(word);

            if (i != parsedText.length - 1)
                buffer.append(" ");
        }

        if (buffer.length() != 0){
            Text bufferedText = new Text();
            bufferedText.setStyle("-fx-fill: Lavender;");
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

        Image image = new Image(path);
        imageView = new ImageView(image);
        double scaleCoef = 380/image.getWidth();
        imageView.setFitHeight(image.getHeight()*scaleCoef);
        imageView.setFitWidth(image.getWidth()*scaleCoef);

    }

    public void build(){

        Text nickname = new Text(senderName);
        nickname.setFont(nameFont);
        if (type == MessageType.SERVER_MESSAGE)
            nickname.setFont(serverFont);
        colorize(nickname);

        nickname.setEffect(glow);

        this.setPadding(new Insets(4,4, 4, 4));

        if (backColor != null)
            this.setBackground(
                    new Background(
                            new BackgroundFill(
                                    backColor, CornerRadii.EMPTY, Insets.EMPTY)));
        else
            this.setBackground(
                    new Background(
                            new BackgroundFill(
                                    Color.rgb(100, 100, 100, 0.15), CornerRadii.EMPTY, Insets.EMPTY)));

        this.setSpacing(2);
        this.prefWidthProperty().bind(textFlows.widthProperty());
        this.setAlignment(Pos.TOP_LEFT);

        VBox nameBox = new VBox();
        nameBox.setPadding(new Insets(1));
        nameBox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        nameBox.setAlignment(Pos.CENTER);
        nameBox.getChildren().add(nickname);
        nameBox.prefHeightProperty().set((
                nickname.getLayoutBounds().getMaxY() - nickname.getLayoutBounds().getMinY()
        ) + 6);
        nameBox.maxWidthProperty().set((
                nickname.getLayoutBounds().getMaxX() - nickname.getLayoutBounds().getMinX()
        ) + 10);
        this.getChildren().addAll(nameBox, textFlows);

//        if (type == MessageType.COMMON_MESSAGE){
//
//            VBox nameBox = new VBox();
//            nameBox.setPadding(new Insets(1));
//            nameBox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
//            nameBox.setAlignment(Pos.CENTER);
//            nameBox.getChildren().add(nickname);
//            nameBox.prefHeightProperty().set((
//                    nickname.getLayoutBounds().getMaxY() - nickname.getLayoutBounds().getMinY()
//                    ) + 6);
//            nameBox.maxWidthProperty().set((
//                    nickname.getLayoutBounds().getMaxX() - nickname.getLayoutBounds().getMinX()
//            ) + 10);
//            this.getChildren().addAll(nameBox, textFlows);
//        }
//        if (type == MessageType.SERVER_MESSAGE){
//            TextFlow textFlow = new TextFlow();
//            textFlow.getChildren().addAll(nickname, this.textFlows);
//            this.getChildren().add(textFlow);
//        }

        if (imageView != null || video != null){

            VBox images = new VBox();
            images.setPadding(new Insets(10));

            if (imageView != null){
                StackPane imagePane = new StackPane();
                imagePane.maxWidthProperty().bind(imageView.fitWidthProperty());
                imagePane.maxHeightProperty().bind(imageView.fitHeightProperty());
                imagePane.setStyle("-fx-border-color: #3b1319;\n" +
                        "    -fx-border-style: solid;\n" +
                        "    -fx-border-width: 1.5;");

                DropShadow shadow = new DropShadow();
                shadow.setColor(Color.color(0, 0, 0, 0.35));
                shadow.setOffsetX(5);
                shadow.setOffsetY(5);
                shadow.setRadius(5);
                imagePane.setEffect(shadow);
                imagePane.getChildren().add(imageView);
                images.getChildren().add(imagePane);
            }

            if (video != null){
                StackPane videoPane = new StackPane();
                videoPane.maxWidthProperty().bind(video.widthProperty());
                videoPane.maxHeightProperty().bind(video.heightProperty());
                videoPane.setStyle("-fx-border-color: #3b1319;\n" +
                        "    -fx-border-style: solid;\n" +
                        "    -fx-border-width: 1.5;");

                DropShadow shadow = new DropShadow();
                shadow.setColor(Color.color(0, 0, 0, 0.35));
                shadow.setOffsetX(5);
                shadow.setOffsetY(5);
                shadow.setRadius(5);
                videoPane.setEffect(shadow);
                videoPane.getChildren().add(video);
                images.getChildren().add(videoPane);
            }



            images.setSpacing(10);
            if (DEBUG)
                images.setStyle("-fx-border-color: BLUE");
            this.getChildren().add(images);

        }

    }

    private void colorize(Text nickname) {

        switch (senderColor){
            case "1":
                nickname.setStyle("-fx-fill: #f44336;");
                break;
            case "2":
                nickname.setStyle("-fx-fill: #3f51b5;");
                break;
            case "3":
                nickname.setStyle("-fx-fill: #29b6f6;");
                break;
            case "4":
                nickname.setStyle("-fx-fill: #ff5722;");
                break;
            case "5":
                nickname.setStyle("-fx-fill: #4caf50;");
                break;
            case "6":
                nickname.setStyle("-fx-fill: #8bc34a;");
                break;
            case "7":
                nickname.setStyle("-fx-fill: #ffeb3b;");
                break;
            case "8":
                nickname.setStyle("-fx-fill: #ec407a;");
                break;
            case "9":
                nickname.setStyle("-fx-fill: LightSkyBlue;");
                break;
            default:
                nickname.setStyle("-fx-fill: #546e7a;");
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

    public enum MessageType{
        SERVER_MESSAGE, COMMON_MESSAGE, CLIENT_MESSAGE
    }

}
