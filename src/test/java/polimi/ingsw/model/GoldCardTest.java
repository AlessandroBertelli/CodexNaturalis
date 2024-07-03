package polimi.ingsw.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import polimi.ingsw.main.App;

import static org.junit.jupiter.api.Assertions.*;

public class GoldCardTest {

    private GoldCard goldCard;
    private Manuscript manuscript;
    private AbstractCard[][] manuscriptMatrix;

    private int[] numResourceRequested = {0, 0, 0, 0};

    @BeforeEach
    public void setUp() {

        manuscriptMatrix = new AbstractCard[2][2];
        AbstractCard startingCard = new AbstractCard("START", "1", "pathBack", 1, App.createCornerArray(), App.createCornerArray(), Resources.FUNGI) {
        };
        manuscript = new Manuscript(startingCard);


        manuscriptMatrix[0][0] = new AbstractCard("1", "path1", "path1", 1, new Corner[]{}, new Corner[]{}, Resources.PLANT) {
        };
        manuscriptMatrix[0][1] = new AbstractCard("2", "path2", "path2", 2, new Corner[]{}, new Corner[]{}, Resources.ANIMAL) {
        };
        manuscriptMatrix[1][0] = new AbstractCard ("3", "path3", "path3", 3, new Corner[]{}, new Corner[]{}, Resources.INSECT){

        };
        manuscriptMatrix[1][1] = new AbstractCard("4", "path4", "path4", 4, new Corner[]{}, new Corner[]{}, Resources.FUNGI){
        };

        // Creiamo un esempio di GoldCard
        goldCard = new GoldCard("G1", "pathFront", "pathBack", 5, new Corner[]{}, new Corner[]{}, new int[]{1, 1, 1, 1}, Resources.PLANT, "example");
    }

  /*  @Test
    public void testCheckResourceRequested_FrontCard() {
        // Impostiamo la carta come front
        goldCard.setFront(true);

        // Testiamo il metodo checkResourceRequested
        boolean result = goldCard.checkResourceRequested(manuscript);
        assertTrue(result, "Le risorse richieste dovrebbero essere sufficienti.");
    } */

    @Test
    public void testCheckResourceRequested_BackCard() {
        // Impostiamo la carta come back
        goldCard.setFront(false);

        // Testiamo il metodo checkResourceRequested
        boolean result = goldCard.checkResourceRequested(manuscript);
        assertTrue(result, "Una carta sul retro dovrebbe sempre restituire true.");
    }

    @Test
    public void testCalculateAvailablePiece() {

        int[] availablePieces = goldCard.calculateAvailablePiece(manuscriptMatrix);

        assertEquals(0, availablePieces[0], "Non dovrebbero esserci inkwells.");
        assertEquals(0, availablePieces[1], "Non dovrebbero esserci manuscripts.");
        assertEquals(0, availablePieces[2], "Non dovrebbero esserci quills.");
        assertEquals(0, availablePieces[3], "Non dovrebbero esserci altri pezzi.");
    }

    @Test
    public void testUpdateAvailableResources() {
        int[] availableResources = new int[4];
        goldCard.updateAvailableResources(Resources.PLANT, availableResources);

        assertEquals(1, availableResources[0], "Dovrebbe esserci una risorsa di tipo pianta.");
        assertEquals(0, availableResources[1], "Non dovrebbero esserci risorse di tipo animale.");
        assertEquals(0, availableResources[2], "Non dovrebbero esserci risorse di tipo insetto.");
        assertEquals(0, availableResources[3], "Non dovrebbero esserci risorse di tipo fungo.");
    }

    @Test
    public void testUpdateAvailablePiece() {
        int[] availablePieces = new int[4];
        goldCard.updateAvilablePiece(Piece.INKWELL, availablePieces);

        assertEquals(1, availablePieces[0], "Dovrebbe esserci un inkwell.");
        assertEquals(0, availablePieces[1], "Non dovrebbero esserci manuscripts.");
        assertEquals(0, availablePieces[2], "Non dovrebbero esserci quills.");
    }

    @Test
    public void testGetAttachedResource() {
        Resources resource = goldCard.getAttachedResource();
        assertEquals(Resources.PLANT, resource, "La risorsa associata dovrebbe essere di tipo pianta.");
    }

    @Test
    public void testGetForEach() {
        String forEach = goldCard.getForEach();
        assertEquals("example", forEach, "Il valore di forEach dovrebbe essere 'example'.");
    }
}
