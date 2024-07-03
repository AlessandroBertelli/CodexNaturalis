package polimi.ingsw.SocketAndRMI.message;

/**
 * Message used by the client to send login information to the server.
 */
public class ColorRequestMessage extends Message {

    String color;

    /**
     * The Constructor of the message sends information about the nickname of the client.
     * @param nickname Nickname suggested from the client.
     */
    public ColorRequestMessage(String nickname, String color) {
        super(nickname, MessageType.COLOR_REQUEST);
        this.color=color;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "ColorRequestMessage{" +
                "color='" + color + '\'' +
                ", messageType=" + messageType +
                '}';
    }
}
