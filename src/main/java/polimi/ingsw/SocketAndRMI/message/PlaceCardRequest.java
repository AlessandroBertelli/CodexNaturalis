package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.SocketAndRMI.Server;


/**
 * This message prompts the client to select a position on the game board and the column.
 */
public class PlaceCardRequest extends Message {
    public PlaceCardRequest() {
        super(Server.SERVER_NAME, MessageType.PLACE_CARD_REQUEST);
    }
}


