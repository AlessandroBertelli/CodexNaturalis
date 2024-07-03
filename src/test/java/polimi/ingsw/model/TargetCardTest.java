package polimi.ingsw.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class TargetCardTest {

/**
 * Test class for the TargetCard class. It contains unit tests to verify the creation
 * and behavior of TargetCard objects.
 */




    /**
     * Test the creation of a TargetCard object with specified attributes.
     */

    @Test
    public void testTargetCardCreation() {

        String ID = "001";
        String pathFrontImage = "/path/to/front/image";
        String pathBackImage = "/path/to/back/image";
        int point = 5;
        Corner[] frontCorners = new Corner[4];
        Corner[] backCorners = new Corner[4];
        String color = "red";
        String strategy = "AngleCovered";

        TargetCard targetCard = new TargetCard(ID, pathFrontImage, pathBackImage, point, frontCorners, backCorners, color, strategy);


        assertEquals(ID, targetCard.getID());
        assertEquals(pathFrontImage, targetCard.getPathFrontImage());
        assertEquals(pathBackImage, targetCard.getPathBackImage());
        assertEquals(point, targetCard.getPoint());
        assertArrayEquals(frontCorners, targetCard.getFrontCorner());
        assertArrayEquals(backCorners, targetCard.getBackCorner());

    }


    /**
     * Test the calculatePoint method of the TargetCard class.
     * It verifies that the calculated points are accurate based on the strategy.
     */

    @Test
    public void testCalculatePoint() {

        String ID = "002";
        String pathFrontImage = "/path/to/front/image2";
        String pathBackImage = "/path/to/back/image2";
        int point = 8;
        Corner[] frontCorners = new Corner[4];
        Corner[] backCorners = new Corner[4];
        String color = "blue";
        String strategy = "ThreePiece";

        TargetCard targetCard = new TargetCard(ID, pathFrontImage, pathBackImage, point, frontCorners, backCorners, color, strategy);


        assertEquals(point + 2, targetCard.calculatePoint());
    }
}
