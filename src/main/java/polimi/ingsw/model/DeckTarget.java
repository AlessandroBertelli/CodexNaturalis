package polimi.ingsw.model;

import java.util.List;

/**
 * Represents a target deck of cards, extending AbstractDeck.
 * Target decks can contain a specified number of cards and have a maximum size limit.
 */
public class DeckTarget extends AbstractDeck {

    /**
     * Constructs a target deck with the given list of cards and maximum size.
     *
     * @param cards The list of cards to initialize the deck with.
     * @param maxSize The maximum size limit of the deck.
     */
    public DeckTarget(List<AbstractCard> cards, int maxSize) {super(cards, maxSize);}

    /**
     * Constructs a target deck with the given maximum size limit.
     *
     * @param maxSize The maximum size limit of the deck.
     */
    public DeckTarget(int maxSize) {
        super(maxSize);
    }

}
