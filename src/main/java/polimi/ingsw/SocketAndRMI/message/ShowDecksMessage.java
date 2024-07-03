package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.SocketAndRMI.Server;

import java.util.ArrayList;

public class ShowDecksMessage extends Message {
    private final ArrayList<String> cards;

    public ShowDecksMessage(ArrayList<String> cards) {
        super(Server.SERVER_NAME, MessageType.DISPLAY_DECKS);
        this.cards = cards;
    }

    public ArrayList<String> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return "ShowDecksMessage{" +
                "cards=" + cards +
                 '\'' +
                '}';
    }
}
