
package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.model.AbstractCard;
import polimi.ingsw.SocketAndRMI.Server;

/**
 * This message represents a secret target card sent from the server to a client.
 */
public class SecretTargetCardMessage extends Message {
    /**
     * Reference to the {@code SecretTargetCard}.
     */
    private final AbstractCard secretTargetCard;
    /**
     * String representing the owner of the card.
     */
    private final String cardOwner;

    /**
     * @param secretTargetCard Reference to the {@code SecretTargetCard}
     * @param owner            String representing the owner of the card.
     */
    public SecretTargetCardMessage(AbstractCard secretTargetCard, String owner) {
        super(Server.SERVER_NAME, MessageType.SECRET_TARGET_CARD);
        this.secretTargetCard = secretTargetCard;
        this.cardOwner = owner;
    }

    /**
     * @return Reference to the {@code SecretTargetCard}
     */
    public AbstractCard getSecretTargetCard() {
        return secretTargetCard;
    }

    /**
     * @return String representing the owner of the card
     */
    public String getCardOwner() {
        return cardOwner;
    }

    @Override
    public String toString() {
        return "SecretTargetCardMessage{" +
                "secretTargetCard=" + secretTargetCard +
                ", cardOwner='" + cardOwner + '\'' +
                '}';
    }
}


