package polimi.ingsw.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for the DeckStarting class. It contains unit tests to verify the functionality
 * of various methods in the DeckStarting class.
 */
public class DeckStartingTest {

    private DeckStarting emptyDeck;
    private DeckStarting filledDeck;
    private List<AbstractCard> cards;

    /**
     * Inner class that extends AbstractCard for testing purposes.
     */
    private class TestCard extends AbstractCard {
        public TestCard(String ID, String pathFrontImage, String pathBackImage, int point, Corner[] frontCorner, Corner[] backCorner) {
            super(ID, pathFrontImage, pathBackImage, point, frontCorner, backCorner);
        }
    }

    /**
     * Sets up the test environment. Initializes emptyDeck and filledDeck with test cards.
     */
    @Before
    public void setUp() {

        cards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Corner[] corners = new Corner[]{
                    new Corner("front_" + i, true, Resources.PLANT, null),
                    new Corner("front_" + i, true, Resources.ANIMAL, null),
                    new Corner("front_" + i, true, Resources.INSECT, null),
                    new Corner("front_" + i, true, Resources.FUNGI, null)
            };
            cards.add(new TestCard("card" + i, "pathFront" + i, "pathBack" + i, i, corners, corners));
        }
        emptyDeck = new DeckStarting(20);
        filledDeck = new DeckStarting(cards, 20);
    }

    /**
     * Tests if the empty deck is correctly identified as empty and has a size of 0.
     */
    @Test
    public void testEmptyDeck() {
        assertTrue(emptyDeck.isEmpty());
        assertEquals(0, emptyDeck.getSize());
    }

    /**
     * Tests if the filled deck is correctly identified as not empty and has the correct size.
     */
    @Test
    public void testFilledDeck() {
        assertFalse(filledDeck.isEmpty());
        assertEquals(10, filledDeck.getSize());
    }

    /**
     * Tests if a card can be added to an empty deck, and if the deck size and contents are updated correctly.
     */
    @Test
    public void testAddCard() {
        AbstractCard newCard = cards.get(0);
        emptyDeck.addCard(newCard);
        assertFalse(emptyDeck.isEmpty());
        assertEquals(1, emptyDeck.getSize());
        assertEquals(newCard, emptyDeck.getCards().get(0));
    }

    /**
     * Tests if adding a card to a full deck throws an IllegalStateException.
     */
    @Test(expected = IllegalStateException.class)
    public void testAddCardToFullDeck() {
        AbstractDeck fullDeck = new DeckStarting(cards, 10);
        AbstractCard newCard = cards.get(0);
        fullDeck.addCard(newCard);
    }

    /**
     * Tests if a card can be removed from a filled deck, and if the deck size and contents are updated correctly.
     */
    @Test
    public void testRemoveCard() {
        AbstractCard cardToRemove = filledDeck.getCards().get(0);
        filledDeck.removeCard(cardToRemove);
        assertEquals(9, filledDeck.getSize());
        assertFalse(filledDeck.getCards().contains(cardToRemove));
    }

    /**
     * Tests if the deck can be shuffled and if the order of cards changes while containing the same elements.
     */
    @Test
    public void testShuffleDeck() {
        List<AbstractCard> originalOrder = new ArrayList<>(filledDeck.getCards());
        filledDeck.shuffle();
        List<AbstractCard> shuffledOrder = filledDeck.getCards();

        assertNotEquals(originalOrder, shuffledOrder);
        // Checks that all cards all still there
        assertTrue(filledDeck.getCards().containsAll(originalOrder));
    }

    /**
     * Tests if a card can be drawn from the top of a filled deck and if the deck size decreases correctly.
     */
    @Test
    public void testDrawCard() {
        AbstractCard topCard = filledDeck.getCards().get(0);
        AbstractCard drawnCard = filledDeck.drawCard();
        assertEquals(topCard, drawnCard);
        assertEquals(9, filledDeck.getSize());
    }

    /**
     * Tests if drawing a card from an empty deck throws an IllegalStateException.
     */
    @Test(expected = IllegalStateException.class)
    public void testDrawCardFromEmptyDeck() {
        emptyDeck.drawCard();
    }

    /**
     * Tests if the ID of the top card in the deck is returned correctly, and if the deck is empty, "null" is returned.
     */
    @Test
    public void testGetTopCardID() {
        assertEquals("card0", filledDeck.getTopCardID());
        assertEquals("null", emptyDeck.getTopCardID());
    }
}
