package ChillChat.Client;

import ChillChat.Client.Console.ConsoleLogIn;
import ChillChat.Client.Utilites.Constants;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LogInInterface {

    private ConsoleLogIn logIn;

    StackPane interfacePane;
    VBox box;
    private TextField loginField;
    private PasswordField passwordField;
    private Button acceptButton;

    private Label titleText;
    private Label inputLogin;
    private Label inputPassword;

    private Label loginState;

    private ClientWindow clientWindow;

    public LogInInterface(ClientWindow clientWindow, StackPane parentPane, ConsoleLogIn logIn) {

        this.clientWindow = clientWindow;
        this.logIn = logIn;

        Font title = new Font("Century Gothic", 30);
        Font common = new Font("Century Gothic", 12);
        Font error = new Font("Century Gothic", 22);

        this.box = new VBox();
        loginField = new TextField();
        passwordField = new PasswordField();
        interfacePane = new StackPane();
        acceptButton = new Button("CHILLAX");

        titleText = new Label( "ChillChat");
        inputLogin = new Label("L O G I N");
        inputPassword = new Label("P A S S");
        loginState = new Label("");

        titleText.setFont(title);
        inputLogin.setFont(common);
        inputPassword.setFont(common);
        loginState.setFont(error);
        loginState.setTextFill(Color.RED);

        acceptButton.setOnMouseClicked(e -> tryToLogIn());

        box.getChildren().addAll(titleText, inputLogin, loginField, inputPassword, passwordField, acceptButton, loginState);
        box.setAlignment(Pos.CENTER);
        box.prefWidthProperty().bind(interfacePane.widthProperty());
        box.setSpacing(10);

        interfacePane.prefWidthProperty().bind(parentPane.widthProperty().divide(2.5));
        interfacePane.prefHeightProperty().bind(parentPane.heightProperty().divide(2.5));
        interfacePane.setMaxSize(350, 250);

        loginField.prefWidthProperty().bind(box.widthProperty().divide(2));
        passwordField.prefWidthProperty().bind(box.widthProperty().divide(2));
        loginField.maxWidthProperty().bind(box.widthProperty().divide(2));
        passwordField.maxWidthProperty().bind(box.widthProperty().divide(2));

        loginField.setAlignment(Pos.CENTER);
        passwordField.setAlignment(Pos.CENTER);


        interfacePane.getChildren().add(box);
        interfacePane.setAlignment(box, Pos.CENTER);

        if (Constants.DEBUG){
            interfacePane.setStyle("-fx-border-color: red");
            box.setStyle("-fx-border-color: blue");

        }

    }

    public Pane getContainer() {
        return interfacePane;
    }

    public void setTextColor(Color color){
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

    public void tryToLogIn(){

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
}
