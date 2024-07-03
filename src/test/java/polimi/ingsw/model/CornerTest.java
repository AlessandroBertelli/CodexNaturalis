package polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Corner class. It contains unit tests to verify the functionality
 * of various methods in the Corner class.
 */
public class CornerTest {

    private Corner cornerWithResource;
    private Corner cornerWithPiece;
    private Corner freeCorner;
    private Corner nonExistentCorner;

    /**
     * Sets up the test environment. Initializes corners with various states.
     */
    @BeforeEach
    public void setUp() {
        cornerWithResource = new Corner("C1", true, Resources.PLANT, null);
        cornerWithPiece = new Corner("C2", true, null, Piece.INKWELL);
        freeCorner = new Corner("C3", true, null, null);
        nonExistentCorner = new Corner("C4", false, null, null);
    }

    /**
     * Tests if the existCorner method returns the correct existence state for corners.
     */
    @Test
    public void testExistCorner() {
        assertTrue(cornerWithResource.existCorner(), "Il corner dovrebbe esistere.");
        assertTrue(cornerWithPiece.existCorner(), "Il corner dovrebbe esistere.");
        assertTrue(freeCorner.existCorner(), "Il corner dovrebbe esistere.");
        assertFalse(nonExistentCorner.existCorner(), "Il corner non dovrebbe esistere.");
    }

    /**
     * Tests if the isFree method returns the correct free state for corners.
     */
    @Test
    public void testIsFree() {
        assertFalse(cornerWithResource.isFree(), "Il corner non dovrebbe essere libero (ha una risorsa).");
        assertFalse(cornerWithPiece.isFree(), "Il corner non dovrebbe essere libero (ha un oggetto).");
        assertTrue(freeCorner.isFree(), "Il corner dovrebbe essere libero.");
        assertFalse(nonExistentCorner.isFree(), "Un corner non esistente non dovrebbe essere libero.");
    }

    /**
     * Tests if the getAttachedResource method returns the correct resource for corners.
     */
    @Test
    public void testGetAttachedResource() {
        assertEquals(Resources.PLANT, cornerWithResource.getAttachedResource(), "La risorsa dovrebbe essere di tipo pianta.");
        assertNull(cornerWithPiece.getAttachedResource(), "Non dovrebbe esserci una risorsa.");
        assertNull(freeCorner.getAttachedResource(), "Non dovrebbe esserci una risorsa.");
        assertNull(nonExistentCorner.getAttachedResource(), "Non dovrebbe esserci una risorsa.");
    }

    /**
     * Tests if the getAttachedPiece method returns the correct piece for corners.
     */
    @Test
    public void testGetAttachedPiece() {
        assertNull(cornerWithResource.getAttachedPiece(), "Non dovrebbe esserci un oggetto.");
        assertEquals(Piece.INKWELL, cornerWithPiece.getAttachedPiece(), "L'oggetto dovrebbe essere di tipo inkwell.");
        assertNull(freeCorner.getAttachedPiece(), "Non dovrebbe esserci un oggetto.");
        assertNull(nonExistentCorner.getAttachedPiece(), "Non dovrebbe esserci un oggetto.");
    }

    /**
     * Tests if the setAttachedCard and getAttachedCard methods correctly set and return the attached card for corners.
     */
    @Test
    public void testSetAndGetAttachedCard() {
        AbstractCard card = new AbstractCard("E1", "path1", "path1", 1, new Corner[]{}, new Corner[]{}, Resources.ANIMAL) {};
        cornerWithResource.setAttachedCard(card);

        assertEquals(card, cornerWithResource.getAttachedCard(), "La carta allegata dovrebbe essere quella impostata.");
    }
}
