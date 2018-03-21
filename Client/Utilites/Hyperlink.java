package ChillChat.Client.Utilites;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Hyperlink extends Text {

    private Application app;
    private String URL;
    private String currentDefaultColor;
    private String activeColor;
    private String clickedColor;
    private String defaultColor;

    private Boolean isSmooth;
    private Double colorChangeTime;
    private Timeline colorChanger;

    //Некоторые эффекты
    //Инициализируются, когда вызывается метод задающий эти эффекты
    private Glow glow;

    public Hyperlink(String url, Application application) {
        super(url);
        this.app = application;
        setURLonClick(url);
        activeColor = "#f06292";
        clickedColor = "#ba68c8";
        defaultColor = "#4fc3f7";
        currentDefaultColor = defaultColor;
        this.setStyle("-fx-fill: " + currentDefaultColor + ";");
        updateFomating();
    }

    private void setURLonClick(String url){
        URL = url;
        updateFomating();
    }

    public void setClickedColor(String webColor){
        clickedColor = webColor;
        updateFomating();
    }

    public void setDefaultColor(String webColor){
        defaultColor = webColor;
        updateFomating();
    }

    public void setActiveColor(String webColor){
        activeColor = webColor;
        updateFomating();
    }

    private void updateFomating() {

        setOnMouseClicked(e -> {
            app.getHostServices().showDocument(URL);
            currentDefaultColor = clickedColor;
        });

        setOnMouseEntered(e -> {
            if (!isSmooth)
                this.setFill(Color.web(activeColor));
            else
                smoothChangeToActive();
        });

        setOnMouseExited(e -> {
            if (!isSmooth)
                this.setFill(Color.web(currentDefaultColor));
            else
                smoothChangeToDefault();
        });

    }

    private void smoothChangeToDefault() {
        colorChanger.getKeyFrames().clear();
        colorChanger.getKeyFrames().add(
                new KeyFrame(Duration.seconds(0.0), new KeyValue(this.fillProperty(), this.getFill()))
        );
        colorChanger.getKeyFrames().add(
                new KeyFrame(Duration.seconds(colorChangeTime), new KeyValue(this.fillProperty(), Color.web(currentDefaultColor)))
        );
        colorChanger.playFromStart();
    }

    private void smoothChangeToActive() {
        colorChanger.getKeyFrames().clear();
        colorChanger.getKeyFrames().add(
                new KeyFrame(Duration.seconds(0.0), new KeyValue(this.fillProperty(), this.getFill()))
        );
        colorChanger.getKeyFrames().add(
                new KeyFrame(Duration.seconds(colorChangeTime), new KeyValue(this.fillProperty(), Color.web(activeColor)))
        );
        colorChanger.playFromStart();
    }

    public void refresh(){
        currentDefaultColor = defaultColor;
        updateFomating();
    }

    public void makeGlowing(double glowStrenght){
        glow = new Glow();
        this.setEffect(glow);
        glow.setLevel(glowStrenght);
    }

    public void makeSmooth(double colorChangeTime){
        isSmooth = true;
        this.colorChangeTime = colorChangeTime;
        colorChanger = new Timeline();
        updateFomating();
    }


}
