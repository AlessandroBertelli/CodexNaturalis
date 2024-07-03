package polimi.ingsw.model;

import java.util.List;
/**
 * Represents a deck of resource-themed cards, extending AbstractDeck.
 * Resource decks are made of a specif type and number of card.
 */
public class DeckResources extends AbstractDeck{

    /**
     * Constructs a resource deck with the given list of cards and maximum size.
     *
     * @param cards The list of cards to initialize the deck with.
     * @param maxSize The maximum size limit of the deck.
     */
    public DeckResources(List<AbstractCard> cards, int maxSize) {super(cards, maxSize);}

    /**
     * Constructs a resource deck with the given maximum size limit.
     *
     * @param maxSize The maximum size limit of the deck.
     */
    public DeckResources(int maxSize) {
        super(maxSize);
    }

}

