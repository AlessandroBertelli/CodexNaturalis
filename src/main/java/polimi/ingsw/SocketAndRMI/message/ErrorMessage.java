package polimi.ingsw.SocketAndRMI.message;

/**
 * This class represents an error message.
 */
public class ErrorMessage extends Message {
    /** Description of the error. */
    private final String errorDescription;

    /**
     * Constructs an error message with the specified sender's nickname and error description.
     * @param nickname The nickname of the sender.
     * @param errorDescription Description of the error.
     */
    public ErrorMessage(String nickname, String errorDescription) {
        super(nickname, MessageType.ERROR_MESSAGE);
        this.errorDescription = errorDescription;
    }

    /**
     * Gets the description of the error.
     * @return The description of the error.
     */
    public String getErrorDescription() {
        return errorDescription;
    }

    /**
     * Returns a string representation of the error message.
     * @return A string representation of the error message.
     */
    @Override
    public String toString() {
        return "ErrorMessage{" +
                "errorDescription='" + errorDescription + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
