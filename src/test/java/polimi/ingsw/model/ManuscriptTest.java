package polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Manuscript class. It contains unit tests to verify the functionality
 * of various methods in the Manuscript class.
 */
public class ManuscriptTest {
    private Manuscript manuscript;
    private AbstractCard startingCard;

    /**
     * Sets up the test environment. Initializes a Manuscript with a starting card before each test.
     */
    @BeforeEach
    public void setUp() {
        Corner[] frontCorner = new Corner[4];
        Corner[] backCorner = new Corner[4];
        for (int i = 0; i< frontCorner.length; i++) {
            frontCorner[i] = new Corner("1", true, null, null);
            backCorner[i] = new Corner("1", true, null, null);
        }
        startingCard = new ResourceCard("Starting", "Description", "pathBack", 1, frontCorner, backCorner, null);
        manuscript = new Manuscript(startingCard);
    }

    /**
     * Tests if the starting card is correctly returned when queried.
     */
    @Test
    public void testGetStartingCard() {
        assertEquals(startingCard, manuscript.getCard(40, 40));
    }

    /**
     * Tests if a new card can be placed correctly in the manuscript.
     */
    @Test
    public void testPlaceCard() {
        Corner[] frontCorner = new Corner[4];
        Corner[] backCorner = new Corner[4];
        for (int i = 0; i< frontCorner.length; i++) {
            frontCorner[i] = new Corner("1", true, null, null);
            backCorner[i] = new Corner("1", true, null, null);
        }
        frontCorner[0] = new Corner("1", false, null, null);
        backCorner[0] = new Corner("1", false, null, null);
        AbstractCard newCard = new ResourceCard("New Card", "Description", "pathBack", 2, frontCorner, backCorner, null);
        manuscript.placeCard(newCard, 39, 39);
        assertEquals(newCard, manuscript.getCard(39, 39));
    }

    /**
     * Tests if the available cells are correctly updated after placing a card.
     */
    @Test
    public void testAvailableCellsAfterPlacingCard() {
        Corner[] frontCorner = new Corner[4];
        Corner[] backCorner = new Corner[4];
        for (int i = 0; i< frontCorner.length; i++) {
            frontCorner[i] = new Corner("1", true, null, null);
            backCorner[i] = new Corner("1", true, null, null);
        }
        frontCorner[0] = new Corner("1", false, null, null);
        backCorner[0] = new Corner("1", false, null, null);
        AbstractCard newCard = new ResourceCard("New Card", "Description", "pathBack", 2, frontCorner, backCorner, null);
        manuscript.placeCard(newCard, 39, 39);
        assertFalse(manuscript.getAvailableCells().contains(new Cell(39, 39)));
    }

    /**
     * Tests if the compressed manuscript is correctly returned and contains the new card.
     */
    @Test
    public void testGetCompressedManuscript() {
        Corner[] frontCorner = new Corner[4];
        Corner[] backCorner = new Corner[4];
        for (int i = 0; i< frontCorner.length; i++) {
            frontCorner[i] = new Corner("1", true, null, null);
            backCorner[i] = new Corner("1", true, null, null);
        }
        frontCorner[0] = new Corner("1", false, null, null);
        backCorner[0] = new Corner("1", false, null, null);
        AbstractCard newCard = new ResourceCard("New Card", "Description", "pathBack", 2, frontCorner, backCorner, null);
        manuscript.placeCard(newCard, 39, 39);

        AbstractCard[][] compressedManuscript = manuscript.getCompressedManuscript();
        assertNotNull(compressedManuscript);
        assertEquals(4, compressedManuscript.length);
        assertEquals(4, compressedManuscript[0].length);
        assertEquals(newCard, compressedManuscript[1][1]);
    }

    /**
     * Tests if the manuscript can be printed without throwing an exception.
     */
    @Test
    public void testPrintManuscript() {
        assertDoesNotThrow(() -> {
            String printedManuscript = manuscript.toString();
            assertNotNull(printedManuscript);
            assertFalse(printedManuscript.isEmpty());
        });
    }



    /**
     * Tests if the printing variables are correctly updated when new coordinates are provided.
     */
    @Test
    public void testUpdatePrintingVariables() {
        manuscript.updatePrintingVariables(35, 35);
        assertEquals(41, manuscript.getTopRow());
        assertEquals(34, manuscript.getBottomRow());
        assertEquals(34, manuscript.getLeftColumn());
        assertEquals(41, manuscript.getRightColumn());
    }
}





