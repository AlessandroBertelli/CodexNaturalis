package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.SocketAndRMI.Server;

/**
 * Represents a message used by the server to reply to a login request,
 * providing information about the validity of the nickname and the status of the connection.
 */
public class LoginResponseMessage extends Message {
    private final boolean nicknameAccepted;       // Indicates whether the suggested nickname is accepted or not
    private final boolean connectionEstablished;  // Indicates whether the connection is established or not

    /**
     * Constructs a new LoginResponseMessage object.
     * @param nicknameAccepted Indicates whether the suggested nickname is accepted.
     * @param connectionEstablished Indicates whether the connection is established.
     */
    public LoginResponseMessage(boolean nicknameAccepted, boolean connectionEstablished) {
        super(Server.SERVER_NAME, MessageType.LOGIN_REPLY);
        this.nicknameAccepted = nicknameAccepted;
        this.connectionEstablished = connectionEstablished;
    }

    /**
     * Checks if the suggested nickname is accepted.
     * @return {@code true} if the suggested nickname is valid and unique, {@code false} otherwise.
     */
    public boolean isNicknameAccepted() {
        return nicknameAccepted;
    }

    /**
     * Checks if a stable connection is established.
     * @return {@code true} if a stable connection is established, {@code false} otherwise.
     */
    public boolean isConnectionEstablished() {
        return connectionEstablished;
    }

    @Override
    public String toString() {
        return "LoginReply{" +
                "nicknameAccepted=" + nicknameAccepted +
                ", connectionEstablished=" + connectionEstablished +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
