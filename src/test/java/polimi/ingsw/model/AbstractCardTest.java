package polimi.ingsw.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Test class for the AbstractCard class. It contains unit tests to verify the functionality
 * of various methods in the AbstractCard class.
 */
public class AbstractCardTest {

    private AbstractCard testCard;
    private Corner[] frontCorners;
    private Corner[] backCorners;

    /**
     * Inner class that extends AbstractCard for testing purposes.
     */
    private class TestCard extends AbstractCard {
        public TestCard(String ID, String pathFrontImage, String pathBackImage, int point, Corner[] frontCorner, Corner[] backCorner) {
            super(ID, pathFrontImage, pathBackImage, point, frontCorner, backCorner);
        }
    }

    /**
     * Sets up the test environment. Initializes testCard and its front and back corners.
     */
    @Before
    public void setUp() {
        frontCorners = new Corner[]{
                new Corner("front_1", true, Resources.PLANT, null),
                new Corner("front_2", true, Resources.ANIMAL, null),
                new Corner("front_3", true, Resources.INSECT, null),
                new Corner("front_4", true, Resources.FUNGI, null)
        };

        backCorners = new Corner[]{
                new Corner("back_1", true, Resources.PLANT, null),
                new Corner("back_2", true, Resources.ANIMAL, null),
                new Corner("back_3", true, Resources.INSECT, null),
                new Corner("back_4", true, Resources.FUNGI, null)
        };

        testCard = new TestCard("test", "pathFront", "pathBack", 10, frontCorners, backCorners);
        testCard.setAttachedResource(Resources.FUNGI);
    }


    /**
     * Tests if the actual resources for the front side of the card are returned correctly.
     */
    @Test
    public void testGetActualResourceFront() {
        testCard.setFront(true);
        int[] expectedResources = {1, 1, 1, 1};
        assertArrayEquals(expectedResources, testCard.getActualResource());
    }

    /**
     * Tests if the actual resources for the back side of the card are returned correctly.
     */
    @Test
    public void testGetActualResourceBack() {
        testCard.setFront(false);
        int[] expectedResources = {0, 0, 0, 0};
        assertArrayEquals(expectedResources, testCard.getActualResource());
    }

    /**
     * Tests if the actual pieces of the card are returned correctly.
     */
    @Test
    public void testGetActualPiece() {
        testCard.setFront(true);
        int[] expectedPieces = {0, 0, 0};
        assertArrayEquals(expectedPieces, testCard.getActualPiece());
    }

    /**
     * Tests if the ID of the card is returned correctly.
     */
    @Test
    public void testGetID() {
        assertEquals("test", testCard.getID());
    }

    /**
     * Tests if the front state of the card can be set and retrieved correctly.
     */
    @Test
    public void testSetFront() {
        testCard.setFront(false);
        assertFalse(testCard.isFront());
        testCard.setFront(true);
        assertTrue(testCard.isFront());
    }

    /**
     * Tests if the path to the front image of the card is returned correctly.
     */
    @Test
    public void testGetPathFrontImage() {
        assertEquals("pathFront", testCard.getPathFrontImage());
    }

    /**
     * Tests if the path to the back image of the card is returned correctly.
     */
    @Test
    public void testGetPathBackImage() {
        assertEquals("pathBack", testCard.getPathBackImage());
    }

    /**
     * Tests if the point value of the card is returned correctly.
     */
    @Test
    public void testGetPoint() {
        assertEquals(10, testCard.getPoint());
    }

    /**
     * Tests if the corners of the card are returned correctly based on the front/back state.
     */
    @Test
    public void testCorners() {
        testCard.setFront(true);
        assertEquals(frontCorners[0], testCard.getTopLeftCorner());
        assertEquals(frontCorners[1], testCard.getTopRightCorner());
        assertEquals(frontCorners[2], testCard.getBottomLeftCorner());
        assertEquals(frontCorners[3], testCard.getBottomRightCorner());

        testCard.setFront(false);
        assertEquals(backCorners[0], testCard.getTopLeftCorner());
        assertEquals(backCorners[1], testCard.getTopRightCorner());
        assertEquals(backCorners[2], testCard.getBottomLeftCorner());
        assertEquals(backCorners[3], testCard.getBottomRightCorner());
    }

    /**
     * Tests if an attached card can be set and retrieved correctly.
     */
    @Test
    public void testGetAttachedCard() {
        AbstractCard attachedCard = new TestCard("attached", "pathFront", "pathBack", 5, frontCorners, backCorners);
        testCard.setTopLeftCard(attachedCard);
        assertEquals(attachedCard, testCard.getTopLeftCard());
    }

    /**
     * Tests if an attached resource can be set and retrieved correctly.
     */
    @Test
    public void testSetAttachedResource() {
        testCard.setAttachedResource(Resources.ANIMAL);
        assertEquals(Resources.ANIMAL, testCard.getAttachedResource());
    }
}
