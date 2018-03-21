package ChillChat.Client.Utilites;

import javafx.application.Application;
import javafx.scene.effect.Glow;
import javafx.scene.text.Text;

public class Hyperlink extends Text {

    private Application app;
    private String URL;
    private String currentDefaultColor;
    private String activeColor;
    private String clickedColor;
    private String defaultColor;

    //Некоторые эффекты
    //Инициализируются, когда вызывается метод задающий эти эффекты
    private Glow glow;

    public Hyperlink(String url, Application application) {
        super(url);
        this.app = application;
        setURLonClick(url);
        currentDefaultColor = "#73aae5";
        activeColor = "#3681FF";
        clickedColor = "#9270FF";
        defaultColor = "#73aae5";
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
            this.setStyle("-fx-fill: " + activeColor + ";");
        });

        setOnMouseExited(e -> {
            this.setStyle("-fx-fill: " + currentDefaultColor + ";");
        });

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


}
