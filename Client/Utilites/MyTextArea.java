package ChillChat.Client.Utilites;

import ChillChat.Client.Messenger;
import javafx.geometry.Bounds;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import static ChillChat.Client.Utilites.Constants.DEBUG;

public class MyTextArea extends TextArea {

    Messenger messenger;

    public MyTextArea(Messenger messenger){

        if (DEBUG)
            this.setStyle("-fx-border-color: Yellow");

        this.messenger = messenger;

        this.setMaxHeight(100);

        setWrapText(true);

        KeyCombination keyComb = new KeyCodeCombination(KeyCode.ENTER,
                KeyCombination.CONTROL_DOWN);

        setEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (keyComb.match(event)){
                appendText("\n");
                event.consume();
            } else {
                if (event.getCode().equals(KeyCode.ENTER)){
                    messenger.flushTextFromField();
                    event.consume();
                }
            }
        });

        textProperty().addListener(e -> {
            Bounds bounds = getTextBounds();
            setPrefHeight(bounds.getHeight());
            if (bounds.getHeight() > 100)
                setMaxHeight(100);
            else
                setMaxHeight(bounds.getHeight());
        });

    }


    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        ScrollBar scrollBarv = (ScrollBar) this.lookup(".scroll-bar:vertical");
        if (scrollBarv != null) {
            ((ScrollPane) scrollBarv.getParent()).setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        }
        ScrollBar scrollBarh = (ScrollBar) this.lookup(".scroll-bar:horizontal");
        if (scrollBarh != null) {
            ((ScrollPane) scrollBarh.getParent()).setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        }
    }

    @Override
    protected double computePrefHeight(double height) {
        Bounds bounds = getTextBounds();
        double h = Math.ceil(bounds.getHeight());
        return h;
    }

    //from https://stackoverflow.com/questions/15593287/binding-textarea-height-to-its-content/19717901#19717901
    public Bounds getTextBounds() {
        //String text = (textArea.getText().equals("")) ? textArea.getPromptText() : textArea.getText();
        String text = "";
        text = this.getParagraphs().stream().map((p) -> p + "W\n").reduce(text, String::concat);
        text += "W";
        Text helper = new Text();
        helper.setText(text);
        helper.setFont(this.getFont());
        // Note that the wrapping width needs to be set to zero before
        // getting the text's real preferred width.
        //helper.setWrappingWidth(0);
        return helper.getLayoutBounds();
    }

}
