package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.SocketAndRMI.Server;

import java.util.Map;

/**
 * This message is used to transmit the updated (current) scoreboard containing the scores of each player.
 */
public class ScoreBoardMessage extends Message {
    /** Reference to the {@code Map} containing the scoreboard. */
    private final Map<String, Integer> scoreBoardMap;

    /**
     * @param scoreBoardMap {@code Map} containing the scoreboard.
     */
    public ScoreBoardMessage(Map<String, Integer> scoreBoardMap) {
        super(Server.SERVER_NAME, MessageType.SCORE_BOARD);
        this.scoreBoardMap = scoreBoardMap;
    }

    /**
     * @return The {@code Map} representing the scoreboard.
     */
    public Map<String, Integer> getScoreBoardMap() {
        return scoreBoardMap;
    }

    @Override
    public String toString() {
        return "ScoreBoardMessage{" +
                "scoreBoardMap=" + scoreBoardMap +
                '}';
    }
}

