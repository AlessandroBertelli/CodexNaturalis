package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.model.AbstractCard;
import polimi.ingsw.SocketAndRMI.Server;

/**
 * This class represents public information about a single player,
 * including their {@code Manuscript} and other relevant details.
 */
public class PlayerInformation extends Message {
    /** Matrix of {@code ShelfCell} containing the shelf content of the player. */
    private final AbstractCard[][] manuscript;
    /** Nickname of the player. */
    private final String nickname;

    private final String stringManuscript;

    /**
     * @param nickname Nickname of the player.
     * @param manuscript Manuscript representing the player's information.
     */
    public PlayerInformation(String nickname, AbstractCard[][] manuscript, String stringManuscript) {
        super(Server.SERVER_NAME, MessageType.PLAYER_INFORMATION);
        this.nickname = nickname;
        this.manuscript = manuscript;
        this.stringManuscript = stringManuscript;
    }

    /**
     * @return Manuscript representing the player's information.
     */
    public AbstractCard[][] getManuscript() {
        return manuscript;
    }

    /**
     * @return Nickname of the player.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     *
     * @return String ready to be printed contain the manuscript with correct index
     */
    public String getStringManuscript() {
        return stringManuscript;
    }

    @Override
    public String toString() {
        return "PlayerInformation{" +
                "manuscript=" + manuscript +
                ", nickname='" + nickname + '\'' +
                ", stringManuscript='too large to display'" +
                '}';
    }
}
