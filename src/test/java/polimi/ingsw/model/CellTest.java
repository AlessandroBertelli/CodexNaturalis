package polimi.ingsw.model;

import junit.framework.TestCase;


/**
 * Test class for the Cell class. It contains unit tests to verify the functionality
 * of various methods in the Cell class.
 */
public class CellTest extends TestCase {

    /**
     * Tests if the getRow method returns the correct row value.
     */
    public void testGetRow() {
        Cell cell = new Cell(0, 0);
        int x = cell.getRow();
        assertEquals(x, 0);
    }

    /**
     * Tests if the getColumn method returns the correct column value.
     */
    public void testGetColumn() {
        Cell cell = new Cell(0, 0);
        int y = cell.getColumn();
        assertEquals(y, 0);
    }

    /**
     * Tests the equals method for cells with different row and column values.
     * Verifies that equals returns false for cells with different coordinates.
     */
    public void testNegativeEquals() {
        Cell cell1 = new Cell(0, 0);
        Cell cell2 = new Cell(1, 1);
        assertFalse(cell2.equals(cell1));
    }

    /**
     * Tests the equals method for cells with the same row and column values.
     * Verifies that equals returns true for cells with identical coordinates.
     */
    public void testPositiveEquals() {
        Cell cell1 = new Cell(1, 1);
        Cell cell2 = new Cell(1, 1);
        assertTrue(cell1.equals(cell2));
    }
}
