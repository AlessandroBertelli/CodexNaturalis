package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.SocketAndRMI.Server;

/**
 * Represents a message sent from the server to the client, requesting the number of players in the game.
 */
public class PlayersNumberRequest extends Message {

    /**
     * Constructs a new PlayersNumberRequest object.
     */
    public PlayersNumberRequest() {
        super(Server.SERVER_NAME, MessageType.PLAYERS_NUMBER_REQUEST);
    }

    @Override
    public String toString() {
        return "PlayersNumberRequest{" +
                "nickname='" + nickname + '\'' +
                '}';
    }
}
