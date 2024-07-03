package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.SocketAndRMI.Server;

/**
 * This message is sent from the server to the client to request drawing a card.
 */
public class DrawCardRequest extends Message {

    /**
     */
    public DrawCardRequest() {
        super(Server.SERVER_NAME, MessageType.DRAW_CARD_REQUEST);
    }


    @Override
    public String toString() {
        return "DrawCardRequest{" +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
