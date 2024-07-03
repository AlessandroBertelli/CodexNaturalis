package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.model.AbstractCard;
import polimi.ingsw.SocketAndRMI.Server;

public class HandsResponseMessage extends Message{

    private AbstractCard handCard_1;
    private  AbstractCard handCard_2;
    private  AbstractCard handCard_3;


    public HandsResponseMessage(AbstractCard handCard_1, AbstractCard handCard_2, AbstractCard handCard_3) {
        super(Server.SERVER_NAME, MessageType.HANDS_RESPONSE_CARD);
        this.handCard_1 = handCard_1;
        this.handCard_2 = handCard_2;
        this.handCard_3 = handCard_3;
    }
    public AbstractCard getFirstHandCard() {
        return handCard_1;
    }
    public AbstractCard getSecondHandCard() {
        return handCard_2;
    }
    public AbstractCard getThirdHandCard() {
        return handCard_3;
    }

    public String toString() {
        return "HandleCard{" +
                "firstHandleCard=" + handCard_1 +
                ", secondHandleCard=" + handCard_2 +
                ", thirdHandleCard=" + handCard_3 +
                '}';
    }
}
