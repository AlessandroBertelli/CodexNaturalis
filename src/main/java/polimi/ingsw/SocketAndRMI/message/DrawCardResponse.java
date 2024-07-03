package polimi.ingsw.SocketAndRMI.message;

/**
 * This message is sent from the server to the client in response to a draw card request,
 * containing the drawn card.
 */
public class DrawCardResponse extends Message {

    private final int deckChoice;

    /**
     * @param deckChoice The deck where you draw your card from.
     */
    public DrawCardResponse( String nickname, int deckChoice) {
        super(nickname,MessageType.DRAW_CARD_RESPONSE);
        this.deckChoice = deckChoice;
    }

    /**
     * @return The drawn card.
     */
    public int getDrawnCard() {
        return deckChoice;
    }


    @Override
    public String toString() {
        return "DrawCardResponse{" +
                "drawnCard=" + deckChoice +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
