package polimi.ingsw.SocketAndRMI.message;

import java.io.Serializable;

/**
 * This immutable class represents a message sent between clients and the server.
 * Each message includes the nickname of the sender/receiver and a type determined by the enumeration {@code MessageType}.
 */
public abstract class Message implements Serializable {
    /** The nickname of the message sender. */
    protected String nickname;
    /** The type of the message. */
    protected MessageType messageType;

    /**
     * Constructs a message with the specified sender's nickname and message type.
     * @param nickname The nickname of the client sender/receiver.
     * @param messageType The type of the message.
     */
    public Message(String nickname, MessageType messageType) {
        this.nickname = nickname;
        this.messageType = messageType;
    }

    /**
     * Gets the nickname of the client.
     * @return The nickname of the client.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Gets the type of the message.
     * @return The type of the message.
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Returns a string representation of the message.
     * @return A string representation of the message.
     */
    @Override
    public String toString() {
        return "Message{" +
                "nickname='" + nickname + '\'' +
                ", messageType=" + messageType +
                '}';
    }
}
