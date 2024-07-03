package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.SocketAndRMI.Server;

import java.util.List;

/**
 * Represents a message used to send a list of all players connected to the game to each player.
 * This list is utilized, for example, to set the appropriate layout on the GUI scene.
 */
public class PlayersListMessage extends Message {
    /** List of player nicknames. */
    private final List<String> players;

    /**
     * Constructs a new PlayersListMessage object.
     * @param players List of player nicknames.
     */
    public PlayersListMessage(List<String> players) {
        super(Server.SERVER_NAME, MessageType.PLAYERS_LIST);
        this.players = players;
    }

    /**
     * Retrieves the list of player nicknames.
     * @return List of player nicknames.
     */
    public List<String> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return "PlayersListMessage{" +
                "players=" + players +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
