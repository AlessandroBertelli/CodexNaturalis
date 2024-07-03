
package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.SocketAndRMI.Server;

import java.util.Arrays;

/**
 * Represents a message used by the server to reply to a login request,
 * providing information about the validity of the nickname and the status of the connection.
 */
public class ColorResponseMessage extends Message {
    private final boolean colorAccepted;       // Indicates whether the suggested nickname is accepted or not
    private String[] availableColor;

    /**
     * Constructs a new LoginResponseMessage object.
     * @param colorAccepted Indicates whether the suggested nickname is accepted.
     */
    public ColorResponseMessage(boolean colorAccepted, String [] availableColor) {
        super(Server.SERVER_NAME, MessageType.COLOR_RESPONSE);
        this.colorAccepted = colorAccepted;
        this.availableColor = availableColor;
    }

    /**
     * Checks if the suggested nickname is accepted.
     * @return {@code true} if the suggested nickname is valid and unique, {@code false} otherwise.
     */
    public boolean isColorAccepted() {
        return colorAccepted;
    }

    public String[] getAvailableColor() {
        return availableColor;
    }

    @Override
    public String toString() {
        return "ColorResponseMessage{" +
                "colorAccepted=" + colorAccepted +
                ", nickname='" + nickname + '\'' +
                Arrays.toString(availableColor) +
                ", messageType='" + messageType +'\''+
                '}';
    }
}
