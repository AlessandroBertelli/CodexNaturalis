package polimi.ingsw.model;

import org.junit.Before;
import org.junit.Test;
import polimi.ingsw.main.App;

import static org.junit.Assert.*;

/**
 * Unit tests for the Player class.
 * These tests verify the behavior and functionality of the Player class.
 */
public class PlayerTest {

    private Player player;
    private AbstractCard startingCard;
    private AbstractCard[] possibleTargetCards;

    /**
     * Sets up the test environment before each test method runs.
     * Initializes a Player instance with a starting card, possible target cards,
     * and sets the secret target card.
     */
    public void setUp() {
        player = new Player("TestPlayer");

        startingCard = new AbstractCard("001", "/path/to/front/image", "/path/to/back/image", 10, App.createCornerArray(), App.createCornerArray()) {
            @Override
            public int calculatePoint() {
                return point; // Just return the point for simplicity
            }
        };
        player.setStartingCard(startingCard);

        possibleTargetCards = new AbstractCard[2];
        possibleTargetCards[0] = new TargetCard("001", "/path/to/front/image", "/path/to/back/image", 20, App.createCornerArray(), App.createCornerArray(), "RED", "STRATEGY001");
        possibleTargetCards[1] = new TargetCard("002", "/path/to/front/image", "/path/to/back/image", 30, App.createCornerArray(), App.createCornerArray(), "BLUE", "STRATEGY002");
        player.setPossibleTargetCard(possibleTargetCards[0], possibleTargetCards[1]);

        player.setSecretTargetCard(possibleTargetCards[0]);
    }

    /**
     * Test case for player creation and initialization.
     * Verifies that the player's nickname, starting card, possible target cards, secret target card,
     * and initial state are correctly set up.
     */
    @Test
    public void testPlayerCreation() {
        setUp();
        assertEquals("TestPlayer", player.getNickname());
        assertEquals(startingCard, player.getStartingCard());
        assertTrue(player.isPlayerReady());
        assertEquals(0, player.getPoints());
        assertEquals(2, player.getPossibleTargetCard().length);
        assertEquals(possibleTargetCards[0], player.getPossibleTargetCard()[0]);
        assertEquals(possibleTargetCards[1], player.getPossibleTargetCard()[1]);
        assertEquals(possibleTargetCards[0], player.getSecretTargetCard());
        assertTrue(player.isOnlinePlayer());
        assertFalse(player.isFirstPlayer());
    }

    /**
     * Verifies that points are correctly added to the player.
     */
    @Test
    public void testAddPoints() {
        setUp();
        player.addPoints(50);
        assertEquals(50, player.getPoints());
    }

    /**
     * Verifies that the first player flag is correctly set.
     */
    @Test
    public void testSetFirstPlayer() {
        setUp();
        player.setFirstPlayer(true);
        assertTrue(player.isFirstPlayer());
    }

    /**

     * Verifies that setting an invalid secret target card throws an IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSecretTargetCardWithInvalidCard() {
        setUp();
        AbstractCard invalidCard = new TargetCard("InvalidCard", "/path/to/front/image", "/path/to/back/image", 15, new Corner[4], new Corner[4], "GREEN", "STRATEGY003");
        player.setSecretTargetCard(invalidCard);
    }

    /**
     * Test case for adding and removing cards from the player's hand.
     * Verifies that cards can be added and removed from the hand correctly.
     */
    @Test
    public void testAddAndRemoveCardFromHand() {
        setUp();
        AbstractCard cardToAdd = new AbstractCard("001", "/path/to/front/image", "/path/to/back/image", 5, new Corner[4], new Corner[4]) {
            @Override
            public int calculatePoint() {
                return point;
            }
        };

        player.addCardToHand(cardToAdd);
        assertEquals(1, player.getHandCards().size());
        assertTrue(player.getHandCards().contains(cardToAdd));

        player.removeCardFromHand(cardToAdd);
        assertEquals(0, player.getHandCards().size());
        assertFalse(player.getHandCards().contains(cardToAdd));
    }

    /**
     * Verifies the dimensions of the player's matrix manuscript.
     */
    @Test
    public void testGetMatrixManuscript() {
        setUp();
        AbstractCard[][] manuscript = player.getMatrixManuscript();
        assertNotNull(manuscript);
        assertEquals(81, manuscript.length);
        assertEquals(81, manuscript[0].length);
    }

    /**

     * Verifies that the compressed manuscript is not null.
     */
    @Test
    public void testGetCompressedManuscript() {
        setUp();
        AbstractCard[][] compressedManuscript = player.getCompressedManuscript();
        assertNotNull(compressedManuscript);
    }

    /**

     * Verifies that a card is placed correctly in the player's manuscript at the specified position.
     */
    @Test
    public void testPlaceCard() {
        setUp();
        AbstractCard cardToPlace = new AbstractCard("001", "/path/to/front/image", "/path/to/back/image", 7, App.createCornerArray(), App.createCornerArray()) {
            @Override
            public int calculatePoint() {
                return point;
            }
        };

        player.placeCard(cardToPlace, 39, 39);
        AbstractCard[][] manuscript = player.getMatrixManuscript();
        assertEquals(cardToPlace, manuscript[39][39]);
    }

    /**

     * Verifies that the correct card is retrieved from the player's hand based on its position.
     */
    @Test
    public void testGetHandCardByHandPosition() {
        setUp();
        AbstractCard card1 = new AbstractCard("001", "/path/to/front/image", "/path/to/back/image", 8, App.createCornerArray(), App.createCornerArray()) {
            @Override
            public int calculatePoint() {
                return point;
            }
        };
        AbstractCard card2 = new AbstractCard("002", "/path/to/front/image", "/path/to/back/image", 12, App.createCornerArray(), App.createCornerArray()) {
            @Override
            public int calculatePoint() {
                return point;
            }
        };

        player.addCardToHand(card1);
        player.addCardToHand(card2);

        assertEquals(card1, player.getHandCardByHandPosition(0));
        assertEquals(card2, player.getHandCardByHandPosition(1));
    }

    /**

     * Verifies that the string representation of the player object is correct.
     */
    @Test
    public void testToString() {
        setUp();
        String expectedString = "Player{'TestPlayer'}";
        assertEquals(expectedString, player.toString());
    }
}
