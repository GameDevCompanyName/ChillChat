package ChillChat.Server;

public class Message {

    //1 - обычное сообщение юзера
    //2 - сообщение сервера
    //3 - попытка логина
    //4 - клиент отключатеся

    private String text;
    private String senderName;
    private int colorCode;
    private int messageType;

    public Message(String text, String senderName, int colorCode, int messageType) {
        this.text = text;
        this.senderName = senderName;
        this.colorCode = colorCode;
        this.messageType = messageType;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setColorCode(short colorCode) {
        this.colorCode = colorCode;
    }

    public void setMessageType(short messageType) {
        this.messageType = messageType;
    }

    public String getText() {
        return text;
    }

    public String getSenderName() {
        return senderName;
    }

    public int getColorCode() {
        return colorCode;
    }

    public int getMessageType() {
        return messageType;
    }

}
