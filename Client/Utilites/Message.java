package ChillChat.Client.Utilites;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import static ChillChat.Client.Utilites.Constants.DEBUG;

public class Message extends VBox {

    private String senderName;
    private TextFlow text;
    private ImageView imageView;
    private Color backColor;
    private Glow glow;
    private WebView video;

    public Message(String senderName){

        this.senderName = senderName;

        if (DEBUG)
            this.setStyle("-fx-border-color: #FF765B");
    }

    private void setGlowEffect() {

        glow = new Glow();
        this.setEffect(glow);

        text.setEffect(glow);

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

    public Message(String senderName, Color color){

        this.backColor = color;

        this.senderName = senderName;

        if (DEBUG)
            this.setStyle("-fx-border-color: #FF765B");
    }

    public void addText(TextFlow text){
        this.text = text;
    }

    public void tryToAddImage(String path){

        Image image = new Image(path);
        imageView = new ImageView(image);
        double scaleCoef = 380/image.getWidth();
        imageView.setFitHeight(image.getHeight()*scaleCoef);
        imageView.setFitWidth(image.getWidth()*scaleCoef);

    }

    public void build(){

        //this.setPadding(new Insets(5,5, 5, 5));

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

        this.setSpacing(5);
        this.getChildren().add(text);
        this.prefWidthProperty().bind(text.widthProperty());

        if (imageView != null || video != null){

            VBox images = new VBox();
            images.setPadding(new Insets(10, 10, 10, 10));

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

        setGlowEffect();

    }

    public void tryToAddVideo(String word) {

        video = new WebView();
        video.getEngine().load(
                word
        );
        video.setPrefSize(840, 690);


    }

}
