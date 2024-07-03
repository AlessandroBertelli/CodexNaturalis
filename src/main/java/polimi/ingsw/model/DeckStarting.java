package polimi.ingsw.model;

import java.util.List;

/**
 * Represents a starting deck of cards, extending AbstractDeck.
 */
public class DeckStarting extends AbstractDeck{
    /**
     * Constructs a starting deck with the given list of cards and maximum size.
     *
     * @param cards The list of cards to initialize the deck with.
     * @param maxSize The maximum size limit of the deck.
     */
    public DeckStarting(List<AbstractCard> cards, int maxSize) {super(cards, maxSize);}

    /**
     * Constructs a starting deck with the given maximum size limit.
     *
     * @param maxSize The maximum size limit of the deck.
     */
    public DeckStarting(int maxSize) {
        super(maxSize);
    }
}
