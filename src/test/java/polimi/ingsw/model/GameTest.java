package polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import polimi.ingsw.exception.DuplicatePlayerException;
import polimi.ingsw.exception.NotPlayerTurnException;
import polimi.ingsw.main.App;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Game class.
 */
public class GameTest {

    private Game game;
    private Player player1;
    private Player player2;
    private AbstractCard resourceCard;
    private AbstractCard goldCard;

    /**
     * Sets up the test environment before each test.
     * Initializes the game, players, and cards to be used in the tests.
     */
    @BeforeEach
    public void setUp() {
        game = new Game(1, 2);
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        game.addPlayer(player1);
        game.addPlayer(player2);

        int array[] = {0, 1, 0, 2};

        AbstractCard[][] manuscript = new AbstractCard[2][2];
        AbstractCard startingCard = new AbstractCard("81", "/path/to/front/image", "/path/to/back/image", 10, App.createCornerArray(), App.createCornerArray()) {};

        Manuscript manuscript1 = new Manuscript(startingCard);
        player1.setManuscript(manuscript1);

        resourceCard = new ResourceCard("2", "/path/to/image", "pathBack", 1, App.createCornerArray(), App.createCornerArray(), null);
        goldCard = new GoldCard("TestCard", "/path/to/image", "pathBack", 1, App.createCornerArray(), App.createCornerArray(), array, Resources.FUNGI, "Quill");
    }

    /**
     * Tests the creation of the game.
     * Verifies that the game is correctly initialized with the provided ID and number of players.
     */
    @Test
    public void testCreateGame() {
        assertNotNull(game);
        assertEquals(1, game.getGAME_ID());
        assertEquals(2, game.getNumPlayer());
    }

    /**
     * Tests adding a player to the game.
     * Verifies that the player is correctly added and that the number of players is updated.
     */
    @Test
    public void testAddPlayer() {
        Player player3 = new Player("Player3");
        game.addPlayer(player3);
        assertEquals(3, game.getPlayers().size());
        assertEquals("Player3", game.getPlayers().get(2).getNickname());
    }

    /**
     * Tests adding a duplicate player to the game.
     * Verifies that an exception is thrown when trying to add a player that already exists.
     */
    @Test
    public void testAddDuplicatePlayerThrowsException() {
        Exception exception = assertThrows(DuplicatePlayerException.class, () -> {
            game.addPlayer(player1);
        });
        assertEquals("Player already exists", exception.getMessage());
    }

    /**
     * Tests setting and getting the active player by nickname.
     * Verifies that the active player is correctly set and retrieved.
     */
    @Test
    public void testSetAndGetActivePlayer() {
        game.setActivePlayerByNickname("Player1");
        assertEquals("Player1", game.getActivePlayerNickname());
    }

    /**
     * Tests starting the game.
     * Verifies that the game status is updated and the initial game setup is correct.
     */
    @Test
    public void testStartingGame() {
        game.startingGame();
        assertEquals(GameState.PLAY_CARD, game.getStatus());
        assertNotNull(game.getActivePlayerNickname());
        assertEquals(2, game.getResourceExposedCard().length);
        assertEquals(2, game.getGoldExposedCard().length);
        assertEquals(2, game.getCommonTargetCard().length);
    }

    /**
     * Tests playing a card.
     * Verifies that the card is correctly played and removed from the player's hand.
     */
    @Test
    public void testPlayCard() {
        game.startingGame();
        game.setActivePlayerByNickname("Player1");
        player1.addCardToHand(resourceCard);
        game.playCard(player1, resourceCard, 39, 39);
        assertEquals(GameState.PLAY_CARD, game.getStatus());
        game.removePlayerCardFromHand(player1, resourceCard);
        assertFalse(player1.getHandCards().contains(resourceCard));
    }

    /**
     * Tests playing a card when it's not the player's turn.
     * Verifies that an exception is thrown.
     */
    @Test
    public void testPlayCardNotPlayerTurnThrowsException() {
        game.startingGame();
        game.setActivePlayerByNickname("Player1");
        player2.addCardToHand(resourceCard);
        Exception exception = assertThrows(NotPlayerTurnException.class, () -> {
            game.playCard(player2, resourceCard, 0, 0);
        });
        assertEquals("It's not your turn now!", exception.getMessage());
    }

    /**
     * Tests drawing a card.
     * Verifies that the card is correctly drawn and added to the player's hand.
     */
    @Test
    public void testDrawCard() {
        game.setStatus(GameState.DRAW_CARD);
        game.setActivePlayerByNickname("Player1");
        game.drawingCard(player1, 1);
        assertEquals(1, player1.getHandCards().size());
    }

    /**
     * Tests ending the game.
     * Verifies that the game status is updated to ending.
     */
    @Test
    public void testEndingGame() {
        game.endingGame();
        assertEquals(GameState.ENDING, game.getStatus());
    }

    /**
     * Tests showing the game score.
     * Verifies that the scores are correctly calculated and returned.
     */
    @Test
    public void testShowScoreGame() {
        game.startingGame();
        player1.addPoints(10);
        player2.addPoints(20);
        var scores = game.showScoreGame();
        assertEquals(10, scores.get("Player1"));
        assertEquals(20, scores.get("Player2"));
    }

    /**
     * Tests setting the player's color.
     * Verifies that the player's color is correctly set and the color is no longer available.
     */
    @Test
    public void testSetPlayerColor() {
        game.setPlayerColor(player1, "RED");
        assertEquals(Color.RED, player1.getColor());
        assertFalse(game.getAvailableColors().contains(Color.RED));
    }

    /**
     * Tests setting an invalid player color.
     * Verifies that an exception is thrown when an invalid color is set.
     */
    @Test
    public void testSetPlayerInvalidColorThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            game.setPlayerColor(player1, "null");
        });
        assertEquals("Color null is not a valid color!", exception.getMessage());
    }
}

