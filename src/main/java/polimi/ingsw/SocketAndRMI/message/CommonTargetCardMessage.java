package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.model.AbstractCard;
import polimi.ingsw.SocketAndRMI.Server;

/**
 * This class represents a message that contains an exposed target card.
 * This message is sent from the server to one or more clients.
 */
public class CommonTargetCardMessage extends Message {

    /** The {@code ExposedTargetCard} contained in the message. */
    private final AbstractCard firstCommonTargetCard;
    private final AbstractCard secondCommonTargetCard;


    public CommonTargetCardMessage(AbstractCard firstCommonTargetCard, AbstractCard secondCommonTargetCard) {
        super(Server.SERVER_NAME, MessageType.COMMON_TARGET_CARD);
        this.secondCommonTargetCard = firstCommonTargetCard;
        this.firstCommonTargetCard = secondCommonTargetCard;
    }

    /**
     * @return The {@code ExposedTargetCard} contained in the message.
     */
    public AbstractCard getSecondCommonTargetCard() {
        return secondCommonTargetCard;
    }
    public AbstractCard getFirstCommonTargetCard() {
        return firstCommonTargetCard;
    }



    @Override
    public String toString() {
        return "CommonTargetCardMessage{" +
                "firstCommonTargetCard=" + firstCommonTargetCard +
                ", secondCommonTargetCard=" + secondCommonTargetCard +
                '}';
    }

}
