package polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an abstract deck of cards.
 */
public abstract class AbstractDeck implements Serializable {
    protected List<AbstractCard> cards;
    protected int maxSize;

    /**
     * Constructs an abstract deck with the specified maximum size.
     * @param maxSize The maximum size of the deck.
     */
    public AbstractDeck(int maxSize) {
        this.cards = new ArrayList<>();
        this.maxSize = maxSize;
    }

    /**
     * Constructs an abstract deck with the specified list of cards and maximum size.
     * @param cards   The list of cards to initialize the deck with.
     * @param maxSize The maximum size of the deck.
     */
    public AbstractDeck(List<AbstractCard> cards, int maxSize) {
        this.cards = cards;
        this.maxSize = maxSize;
    }

    /**
     * Gets the list of cards in the deck.
     * @return The list of cards in the deck.
     */
    public List<AbstractCard> getCards() {
        return cards;
    }

    /**
     * Retrieves the ID of the top card in the list.
     * @return the ID of the top card if the list is not empty; otherwise, returns "null"
     */
    public String getTopCardID(){
        if (cards.isEmpty()){
            return "null";
        }
        else{
            return cards.get(0).getID();
        }
    }

    /**
     * Gets the maximum size of the deck.
     * @return The maximum size of the deck.
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Adds a card to the deck.
     *
     * @param card The card to add to the deck.
     * @throws IllegalStateException if the deck is full.
     */
    public void addCard(AbstractCard card) {
        if (cards.size() < maxSize) {
            cards.add(card);
        } else {
            throw new IllegalStateException("Deck is full");
        }
    }

    /**
     * Removes a card from the deck.
     *
     * @param card The card to remove from the deck.
     */
    public void removeCard(AbstractCard card) {
        cards.remove(card);
    }

    /**
     * Draws a card from the deck.
     *
     * @return The drawn card.
     * @throws IllegalStateException if the deck is empty.
     */
    public AbstractCard drawCard() throws IllegalStateException {
        if (!isEmpty()) {
            return cards.remove(0);
        } else {
            throw new IllegalStateException("Deck is empty!");
        }
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise.
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Gets the current size of the deck.
     *
     * @return The current size of the deck.
     */
    public int getSize() {
        return cards.size();
    }
}

