package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.SocketAndRMI.Server;

import java.util.Map;

/**
 * This message is used to transmit the final scoreboard containing the scores of each player.
 */
public class FinalScoreBoardMessage extends Message {
    /** Reference to the {@code Map} containing the scoreboard. */
    private final Map<String, Integer> scoreBoardMap;
    private final Map<String, Integer> completedObjectives;

    /**
     * @param scoreBoardMap {@code Map} containing the scoreboard.
     */
    public FinalScoreBoardMessage(Map<String, Integer> scoreBoardMap, Map<String, Integer> completedObjectives) {
        super(Server.SERVER_NAME, MessageType.FINAL_SCORE_BOARD);
        this.scoreBoardMap = scoreBoardMap;
        this.completedObjectives = completedObjectives;
    }

    /**
     * @return The {@code Map} representing the scoreboard.
     */
    public Map<String, Integer> getScoreBoardMap() {
        return scoreBoardMap;
    }

    /**
     * @return The map with the corresponding completed Objectives of each player.
     */
    public Map<String, Integer> getCompletedObjectives() {
        return completedObjectives;
    }
    @Override
    public String toString() {
        return "FinalScoreBoardMessage{" +
                "scoreBoardMap=" + scoreBoardMap +
                '}';
    }

}

