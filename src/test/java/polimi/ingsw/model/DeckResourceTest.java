package polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the DeckResources class.
 */
class DeckResourceTest {
    private DeckResources deck;
    private ResourceCard card1;
    private ResourceCard card2;
    private Corner[] frontCorner = new Corner[4];
    private Corner[] backCorner = new Corner[4];

    /**
     * Sets up the test environment before each test.
     * Initializes the deck and cards to be used in the tests.
     */
    @BeforeEach
    void setUp() {
        for (int i = 0; i < 4; i++) {
            frontCorner[i] = new Corner("1", true, null, null);
            backCorner[i] = new Corner("1", true, null, null);
        }
        deck = new DeckResources(5); // DeckResources is a concrete class that extends AbstractDeck
        card1 = new ResourceCard("card1", "1", "pathBack", 1, frontCorner, backCorner, null);
        card2 = new ResourceCard("card2", "1", "pathBack", 1, frontCorner, backCorner, null);
    }

    /**
     * Tests the addCard method of DeckResources.
     * Verifies that cards are correctly added and that the deck size is updated.
     * Also checks that an exception is thrown when trying to add a card to a full deck.
     */
    @Test
    void testAddCard() {
        deck.addCard(card1);
        assertEquals(1, deck.getSize());
        assertTrue(deck.getCards().contains(card1));

        // Additional test: verify that it's not possible to add a card when the deck is full
        assertThrows(IllegalStateException.class, () -> {
            for (int i = 0; i < 5; i++) {
                deck.addCard(new ResourceCard("Card" + (i + 3), "1", "pathBack", 1, frontCorner, backCorner, null));
            }
        });
    }

    /**
     * Tests the removeCard method of DeckResources.
     * Verifies that cards are correctly removed and that the deck size is updated.
     * Also checks the removal of a card that is not present in the deck.
     */
    @Test
    void testRemoveCard() {
        deck.addCard(card1);
        deck.addCard(card2);

        deck.removeCard(card1);
        assertEquals(1, deck.getSize());
        assertFalse(deck.getCards().contains(card1));
        assertTrue(deck.getCards().contains(card2));

        // Additional test: removing a card not present in the deck
        assertFalse(deck.getCards().contains(new ResourceCard("Card3", "1", "pathBack", 1, frontCorner, backCorner, null)));
        assertDoesNotThrow(() -> deck.removeCard(new ResourceCard("Card3", "1", "pathBack", 1, frontCorner, backCorner, null)));
    }

    /**
     * Tests the drawCard method of DeckResources.
     * Verifies that cards are correctly drawn and that the deck size is updated.
     * Also checks that an exception is thrown when trying to draw from an empty deck.
     */
    @org.testng.annotations.Test
    void testDrawCard() {
        deck.addCard(card1);
        deck.addCard(card2);

        AbstractCard drawnCard1 = deck.drawCard();
        assertEquals(1, deck.getSize());
        assertFalse(deck.getCards().contains(drawnCard1));

        AbstractCard drawnCard2 = deck.drawCard();
        assertEquals(0, deck.getSize());
        assertFalse(deck.getCards().contains(drawnCard2));

        assertFalse(deck.getCards().contains(card1));
        assertFalse(deck.getCards().contains(card2));

        // Additional test: attempting to draw a card from an empty deck
        assertThrows(IllegalStateException.class, () -> deck.drawCard());
    }

    /**
     * Tests the isEmpty method of DeckResources.
     * Verifies that the method correctly identifies when the deck is empty or not.
     */
    @Test
    void testIsEmpty() {
        assertTrue(deck.isEmpty());

        deck.addCard(card1);
        assertFalse(deck.isEmpty());

        deck.drawCard();
        assertTrue(deck.isEmpty());
    }

    /**
     * Tests the getMaxSize method of DeckResources.
     * Verifies that the method correctly returns the maximum size of the deck.
     */
    @Test
    void testGetMaxSize() {
        assertEquals(5, deck.getMaxSize());
    }
}
