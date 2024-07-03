package polimi.ingsw.SocketAndRMI.message;

/**
 * Represents a message sent from the client to the server, specifying the number of players in the game.
 */
public class PlayersNumberResponse extends Message {
    private final int playersNumber;        // Number of players in the game

    /**
     * Constructs a new PlayersNumberResponse object.
     * @param nickname The client sender of the information about the number of players.
     * @param playersNumber The number of players in the game.
     */
    public PlayersNumberResponse(String nickname, int playersNumber) {
        super(nickname, MessageType.PLAYERSNUMBER_REPLY);
        this.playersNumber = playersNumber;
    }

    /**
     * Retrieves the number of players in the game.
     * @return The number of players in the game.
     */
    public int getPlayersNumber() {
        return playersNumber;
    }

    @Override
    public String toString() {
        return "PlayersNumberReply{" +
                "playersNumber=" + playersNumber +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}

