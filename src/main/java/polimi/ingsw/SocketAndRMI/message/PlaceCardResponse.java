package polimi.ingsw.SocketAndRMI.message;

/**
 * Message sent from the client to the server specifying a card to place on the manuscript.
 */
public class PlaceCardResponse extends Message {
    private int choice;                   // Card to place on the board.
    private int row;                      // Row on the board.
    private int column;                   // Column on the board.
    private boolean side;

    /**
     * @param nickname Nickname of the message sender.
     * @param choice   position of the Card to place on the board.
     * @param row      Row on the board.
     * @param column   Column on the board.
     */
    public PlaceCardResponse(String nickname, int choice, int row, int column, boolean side) {
        super(nickname, MessageType.PLACE_CARD_RESPONSE);
        this.choice = choice;
        this.row = row;
        this.column = column;
        this.side = side;
    }

    /**
     * @return int to place on the board.
     */
    public int getCardPositionToPlace() {
        return choice;
    }

    /**
     * @return Row on the board.
     */
    public int getRow() {
        return row;
    }

    /**
     * @return Column on the board.
     */
    public int getColumn() {
        return column;
    }

    public boolean getSide() {
        return side;
    }

    @Override
    public String toString() {
        return "PlaceCardResponse{" +
                "cardToPlace=" + choice +
                ", row=" + row +
                ", column=" + column +
                ", side=" + ((side) ? ("front") : ("rear")) +
                '}';
    }
}
