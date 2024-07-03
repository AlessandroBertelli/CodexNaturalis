package polimi.ingsw.model;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link StartingCard} class.
 * These tests verify the behavior and functionality of the StartingCard class.
 */
public class StartingCardTest {

    private StartingCard startingCard;
    private Corner[] frontCorners;
    private Corner[] backCorners;

    /**
     * Sets up the test environment before each test method runs.

     * and attached resources.
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

        List<Resources> attachedResources = Arrays.asList(Resources.PLANT, Resources.ANIMAL);
        startingCard = new StartingCard("start", "pathFront", "pathBack", 10, frontCorners, backCorners, attachedResources);
    }

    /**

     * Verifies that the getActualResource method correctly calculates the number of resources.
     */
    @Test
    public void testGetActualResourceFront() {
        startingCard.setFront(true);
        int[] expectedResources = {2, 2, 1, 1};
        assertArrayEquals(expectedResources, startingCard.getActualResource());
    }

    /**

     * Verifies that the getActualResource method correctly calculates the number of resources.
     */
    @Test
    public void testGetActualResourceBack() {
        startingCard.setFront(false);
        int[] expectedResources = {1, 1, 1, 1};
        assertArrayEquals(expectedResources, startingCard.getActualResource());
    }

    /**

     * Verifies that the calculatePoint method returns the expected point value.
     */
    @Test
    public void testCalculatePoint() {
        assertEquals(10, startingCard.calculatePoint());
    }

    /**

     * Verifies that the getCardResources method returns the expected list of attached resources.
     */
    @Test
    public void testGetCardResources() {
        List<Resources> expectedResources = Arrays.asList(Resources.PLANT, Resources.ANIMAL);
        assertEquals(expectedResources, startingCard.getCardResources());
    }
}

