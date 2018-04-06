package ChillChat.Client;

import ChillChat.Client.Console.ConsoleClient;
import ChillChat.Client.Console.LogInProcedure;
import ChillChat.Client.Utilites.Constants;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LogInInterface {

    private LogInProcedure logIn;

    private VBox box;
    private TextField loginField;
    private PasswordField passwordField;
    private Button acceptButton;

    private Label titleText;
    private Label inputLogin;
    private Label inputPassword;

    private Label loginState;

    private ClientWindow clientWindow;
    private Scene clientScene;

    LogInInterface(ClientWindow clientWindow, StackPane parentPane, LogInProcedure logIn, Scene scene) {

        this.clientScene = scene;
        clientScene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)){
                tryToLogIn();
            }

            if (event.getCode().equals(KeyCode.ESCAPE)){
                clientWindow.goToLoginScreen(false);
            }
        });

        this.clientWindow = clientWindow;
        this.logIn = logIn;



        Font title = null;
        Font common = null;
        Font input = null;
        Font error = null;

        try {
            common = Font.loadFont(new FileInputStream("resources/commonFont.ttf"), 24);
            title = Font.loadFont(new FileInputStream("resources/nameFont.ttf"), 54);
            input = new Font("Courier New", 24);
            error = Font.loadFont(new FileInputStream("resources/commonFont.ttf"), 28);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.box = new VBox();
        loginField = new TextField();
        passwordField = new PasswordField();
        acceptButton = new Button("CHILLAX");

        Background background = new Background(new BackgroundFill(Color.rgb(35, 35, 45, 0.7), CornerRadii.EMPTY, Insets.EMPTY));
        box.setBackground(background);

        titleText = new Label( "ChillChat");
        inputLogin = new Label("L O G I N");
        inputPassword = new Label("P A S S");
        loginState = new Label("");

        loginField.setStyle("-fx-background-color: transparent; -fx-text-fill: #d1cefa");
        loginField.setFont(input);
        loginField.setScaleX(1.5);
        loginField.setScaleY(1.5);
        loginField.maxWidthProperty().bind(titleText.widthProperty());

        passwordField.setStyle("-fx-background-color: transparent; -fx-text-fill: #d1cefa");
        passwordField.setFont(input);
        passwordField.setScaleX(1.5);
        passwordField.setScaleY(1.5);
        passwordField.maxWidthProperty().bind(titleText.widthProperty());

        titleText.setFont(title);

        inputLogin.setFont(common);
        inputPassword.setFont(common);
        loginState.setFont(error);

        Glow glow = new Glow(1.0);
        loginField.setEffect(glow);
        passwordField.setEffect(glow);

        loginState.setTextFill(Color.RED);
        inputLogin.setStyle("-fx-text-fill: #80a8f0");
        inputPassword.setStyle("-fx-text-fill: #80a8f0");
        titleText.setStyle("-fx-text-fill: rgba(191,190,255,0.95)");

        acceptButton.setOnMouseClicked(e -> tryToLogIn());

        /*box.getChildren().addAll(titleText, inputLogin, loginField,
                inputPassword, passwordField, acceptButton, loginState);
                */
        box.getChildren().addAll(titleText, inputLogin, loginField,
                inputPassword, passwordField, loginState);
        box.setAlignment(Pos.CENTER);
        //box.prefWidthProperty().bind(interfacePane.widthProperty());
        box.setSpacing(10);
        box.setPadding(new Insets(10));
        box.maxWidthProperty().bind(titleText.widthProperty().multiply(1.6));
        box.maxHeightProperty().bind(loginState.layoutYProperty().subtract(titleText.layoutYProperty().subtract(15)));

        //interfacePane.prefWidthProperty().bind(parentPane.widthProperty().divide(2.5));
        //interfacePane.prefHeightProperty().bind(parentPane.heightProperty().divide(2.5));
        //interfacePane.setMaxSize(350, 250);

        //loginField.prefWidthProperty().bind(box.widthProperty().divide(2));
        //passwordField.prefWidthProperty().bind(box.widthProperty().divide(2));
        //loginField.maxWidthProperty().bind(box.widthProperty().divide(2));
        //passwordField.maxWidthProperty().bind(box.widthProperty().divide(2));

        loginField.setAlignment(Pos.CENTER);
        passwordField.setAlignment(Pos.CENTER);

        StackPane.setAlignment(box, Pos.CENTER);

        if (Constants.DEBUG){
            box.setStyle("-fx-border-color: blue");
        }



        smoothAppear(box, 1.5);



    }

    private void smoothAppear(Node node, Double secs) {

        Blend blend = new Blend();
        blend.setMode(BlendMode.MULTIPLY);

        GaussianBlur gaussianBlur = new GaussianBlur(10.0);
        blend.setTopInput(gaussianBlur);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(8.0);
        dropShadow.setOffsetX(10.0);
        dropShadow.setOffsetY(10.0);
        Color color = Color.color(0.1, 0.1, 0.1, 0.4);
        dropShadow.setColor(color);
        dropShadow.setInput(box.getEffect());

        blend.setBottomInput(dropShadow);

        node.setEffect(blend);

        Timeline appear = new Timeline();
        appear.getKeyFrames().add(new KeyFrame(Duration.seconds(0.0), new KeyValue(gaussianBlur.radiusProperty(), 30.0)));
        appear.getKeyFrames().add(new KeyFrame(Duration.seconds(secs), new KeyValue(gaussianBlur.radiusProperty(), 0)));
        appear.play();

    }

    Pane getContainer() {
        return box;
    }

    void setTextColor(Color color){
        titleText.setTextFill(color);
        inputLogin.setTextFill(color);
        inputPassword.setTextFill(color);
    }

    public void wrongPass(){
        loginState.setText("Неверный пароль");
    }

    public void loggedIn() {
        clientWindow.loggedIn();
    }

    private void tryToLogIn(){

        clientWindow.startConsoleClient();

        if (passwordField.getText().isEmpty() || loginField.getText().isEmpty()){
            wrongInput();
            return;
        }

        if (passwordField.getText().length() > 20 || loginField.getText().length() > 20){
            tooLongInput();
            return;
        }

        logIn.tryToLogIn(loginField.getText(),
                passwordField.getText());

        clearErrorField();

    }

    private void clearErrorField() {
        loginState.setText("");
    }

    private void tooLongInput() {
        loginState.setText("Пароль или логин\nслишком длинный");
    }

    private void wrongInput() {
        loginState.setText("Некорректные данные");
    }

    public void userAlreadyExists() {
        loginState.setText("Юзер с таким логином\nуже подключен");
    }

    public void serverIsUnavalable() {
        loginState.setText("Соединение разорвано.");
    }

    void updateConsoleClient(ConsoleClient consoleClient) {
        this.logIn = consoleClient.getLogIn();
    }

}
