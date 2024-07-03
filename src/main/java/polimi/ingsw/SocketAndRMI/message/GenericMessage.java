package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.SocketAndRMI.Server;

/**
 * Represents a message sent from the server to a client containing text information.
 * This class inherits from the {@code Message} class and encapsulates a string message.
 */
public class GenericMessage extends Message {
    private final String genericMessage;

    /**
     * Constructs a new GenericMessage object with the provided message.
     * @param genericMessage The text message to be sent.
     */
    public GenericMessage(String genericMessage) {
        super(Server.SERVER_NAME, MessageType.GENERIC_MESSAGE);
        this.genericMessage = genericMessage;
    }

    /**
     * Retrieves the generic message.
     * @return The text message.
     */
    public String getGenericMessage() {
        return genericMessage;
    }

    /**
     * Returns a string representation of the GenericMessage object.
     * @return A string representing the GenericMessage object, including its message content.
     */
    @Override
    public String toString() {
        return "GenericMessage{" +
                "genericMessage='" + genericMessage + '\'' +
                ", nickname='" + nickname + '\'' +
                ", messageType=" + messageType +
                '}';
    }
}
