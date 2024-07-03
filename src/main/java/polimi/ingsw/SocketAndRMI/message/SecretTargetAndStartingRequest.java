package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.model.AbstractCard;
import polimi.ingsw.SocketAndRMI.Server;

/**
 * Represents a message sent from the server to the client, requesting the number of players in the game.
 */
public class SecretTargetAndStartingRequest extends Message {


    AbstractCard startingCard;
    AbstractCard firstPossibleTargetCard;
    AbstractCard secondPossibleTargetCard;
    /**
     * Constructs a new PlayersNumberRequest object.
     */
    public SecretTargetAndStartingRequest(AbstractCard startingCard, AbstractCard firstPossibleTargetCard, AbstractCard secondPossibleTargetCard) {
        super(Server.SERVER_NAME, MessageType.SECRET_STARTING_REQUEST);
        this.startingCard = startingCard;
        this.firstPossibleTargetCard = firstPossibleTargetCard;
        this.secondPossibleTargetCard = secondPossibleTargetCard;
    }

    public AbstractCard getStartingCard() {
        return startingCard;
    }

    public AbstractCard getFirstPossibleTargetCard() {
        return firstPossibleTargetCard;
    }

    public AbstractCard getSecondPossibleTargetCard() {
        return secondPossibleTargetCard;
    }

    @Override
    public String toString() {
        return "SecretTargetAndStartingRequest{" +
                "nickname='" + nickname + '\'' +
                ", startingCard=" + startingCard.getID() +
                ", firstPossibleTargetCard=" + firstPossibleTargetCard.getID() +
                ", secondPossibleTargetCard=" + secondPossibleTargetCard.getID() +
                '}';
    }
}
