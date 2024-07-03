package polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import polimi.ingsw.main.App;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the ResourceCard class. It contains unit tests to verify the functionality
 * of various methods in the ResourceCard class.
 */
class ResourceCardTest {
    private AbstractCard card;
    private Corner[] frontCorners = new Corner[4];
    private Corner[] backCorners = new Corner[4];
    private Manuscript m;
    private List<Resources> attachedResources;
    private StartingCard start;

    /**
     * Sets up the test environment. Initializes a ResourceCard and a Manuscript with a starting card before each test.
     */
    @BeforeEach
    void setUp() {
        // Initialize corners
        for (int i = 0; i < 4; i++) {
            frontCorners[i] = new Corner("1", true, null, null);
            backCorners[i] = new Corner("1", true, null, null);
        }
        // Initialize the card and manuscript
        card = new ResourceCard("TestCard", "/path/to/image", "pathBack", 1, App.createCornerArray(), App.createCornerArray(), null);
        AbstractCard startingCard = new StartingCard("START", "1", "pathBack", 1, App.createCornerArray(), App.createCornerArray(), attachedResources);
        m = new Manuscript(startingCard);
    }

    /**
     * Tests the calculatePoint method of the ResourceCard class.
     */
    @Test
    @DisplayName("Test calculatePoint method")
    void testCalculatePoint() {
        assertEquals(1, card.calculatePoint());
    }

    /**
     * Tests the checkResourceRequested method of the ResourceCard class.
     */
    @Test
    @DisplayName("Test checkResourceRequested method for ResourceCard")
    void testCheckResourceRequested() {
        assertTrue(card.checkResourceRequested(new Manuscript(start = new StartingCard("START", "1", "pathBack", 1, frontCorners, backCorners, attachedResources))));
    }

    /**
     * Tests the getTopLeftCard method of the ResourceCard class when no card is attached.
     */
    @Test
    @DisplayName("Test getTopLeftCard method with NO attached card")
    void testGetTopLeftCardNoAttached() {
        assertNull(card.getTopLeftCard());
    }

    /**
     * Tests the getTopLeftCard method of the ResourceCard class when a card is attached.
     */
    @Test
    @DisplayName("Test getTopLeftCard method WITH attached card")
    void testGetTopLeftCardAttached() {
        for (int i = 0; i < 4; i++) {
            frontCorners[i] = new Corner("1", true, null, null);
            backCorners[i] = new Corner("1", true, null, null);
        }
        ResourceCard card2 = new ResourceCard("card2", "/path/to/image", "pathBack", 1, App.createCornerArray(), App.createCornerArray(), null);
        m.placeCard(card, 39, 39);
        m.placeCard(card2, 38, 38);
        assertNotNull(card.getTopLeftCard());
        assertEquals(card2, card.getTopLeftCard());
        assertFalse(m.getCard(39, 39).existTopLeftCorner());
    }

    /**
     * Tests the getID method of the ResourceCard class.
     */
    @Test
    @DisplayName("Test getID method")
    void testGetID() {
        assertEquals("TestCard", card.getID());
    }

    /**
     * Tests the isFront and setFront methods of the ResourceCard class.
     */
    @Test
    @DisplayName("Test isFront and setFront methods")
    void testIsFrontAndSetFront() {
        assertFalse(card.isFront());
        card.setFront(true);
        assertTrue(card.isFront());
    }

    /**
     * Tests the getPathFrontImage method of the ResourceCard class.
     */
    @Test
    @DisplayName("Test getPathImage method")
    void testGetPathImage() {
        assertEquals("/path/to/image", card.getPathFrontImage());
    }

    /**
     * Tests the getPoint method of the ResourceCard class.
     */
    @Test
    @DisplayName("Test getPoint method")
    void testGetPoint() {
        assertEquals(1, card.getPoint());
    }
}
