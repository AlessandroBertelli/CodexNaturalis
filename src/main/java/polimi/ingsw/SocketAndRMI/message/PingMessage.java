package polimi.ingsw.SocketAndRMI.message;

/**
 * This message is used for ping purposes to maintain the connection alive between the server and the client.
 */
public class PingMessage extends Message {

    /**
     * Constructs a ping message with the specified sender's nickname.
     * @param sender The nickname of the sender.
     */
    public PingMessage(String sender) {
        super(sender, MessageType.PING_MESSAGE);
    }
}
