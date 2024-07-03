package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.SocketAndRMI.Server;

public class ChatMessage extends Message {
    private final String senderNickname;
    private final String recipientNickname;
    private final String text;

    public ChatMessage(String senderNickname, String recipientNickname, String text) {
        super(Server.SERVER_NAME, MessageType.CHAT_MESSAGE);
        this.senderNickname = senderNickname;
        this.recipientNickname = recipientNickname;
        this.text = text;
    }

    public String getSenderNickname() {
        return senderNickname;
    }
    public String getRecipientNickname() {
        return recipientNickname;
    }
    public String getText() { return text; }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "sender=" + senderNickname +
                ", recipient=" + recipientNickname +
                ", text='" + text +
                '\'' +
                '}';
    }
}
