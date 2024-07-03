package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.SocketAndRMI.CardsPlayed;
import polimi.ingsw.SocketAndRMI.Server;
import java.util.List;

public class ReconnectedManuscriptMessage extends Message {

    String playerNickname;
    List<CardsPlayed> cardsPlayedByPlayer;

    public ReconnectedManuscriptMessage(String playerNickname, List<CardsPlayed> cardsPlayedByPlayer) {
        super(Server.SERVER_NAME, MessageType.RECONNECTED_MANUSCRIPT_MESSAGE);
        this.playerNickname = playerNickname;
        this.cardsPlayedByPlayer = cardsPlayedByPlayer;
    }

    public List<CardsPlayed> getCardsPlayedByPlayer() { return cardsPlayedByPlayer; }

    @Override
    public String toString() {
        return "PlayerInformation{" +
                ", nickname='" + nickname + '\'' +
                ", cardsPlayedByPlayer='too large to display'" +
                '}';
    }
}