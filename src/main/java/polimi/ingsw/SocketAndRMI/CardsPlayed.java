package polimi.ingsw.SocketAndRMI;

import polimi.ingsw.model.Cell;
import java.io.Serializable;

public class CardsPlayed implements Serializable {
    private final String playerNickname;
    private final String id;
    private final Cell cell;
    private final boolean isFront;

    public CardsPlayed(String playerNickname, String id, Cell cell, boolean isFront) {
        this.playerNickname = playerNickname;
        this.id = id;
        this.cell = cell;
        this.isFront = isFront;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public String getId() {
        return id;
    }

    public Cell getCell() {
        return cell;
    }

    public boolean isFront() { return isFront; }

    @Override
    public String toString() {
        return "CardsPlayed{" +
                "playerNickname='" + playerNickname + '\'' +
                "id='" + id + '\'' +
                ", cell=" + cell +
                ", isFront=" + isFront +
                '}';
    }
}