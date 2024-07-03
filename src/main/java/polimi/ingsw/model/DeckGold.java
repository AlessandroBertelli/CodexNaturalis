package polimi.ingsw.model;

import java.util.List;

/**
 * Represents a deck of gold-themed cards, extending AbstractDeck.
 * Gold decks are made of a specified number and type of card.
 */
public class DeckGold extends AbstractDeck {

    /**
     * Constructs a gold deck with the given list of cards and maximum size.
     *
     * @param cards The list of deck's card.
     * @param maxSize The maximum size limit of the deck.
     */
    public DeckGold(List<AbstractCard> cards, int maxSize) {super(cards, maxSize);}

    /**
     * Constructs a gold deck with the given maximum size limit.
     * @param maxSize The maximum size limit of the deck.
     */
    public DeckGold(int maxSize) {
        super(maxSize);
    }

}
