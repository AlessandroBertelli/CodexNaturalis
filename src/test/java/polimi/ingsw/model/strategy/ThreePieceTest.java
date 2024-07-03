package polimi.ingsw.model.strategy;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import polimi.ingsw.model.*;

import static org.junit.Assert.assertEquals;

public class ThreePieceTest {
    Corner[] totalPiece = createCornerArrayQuill();
   


    AbstractCard startingCard = new StartingCard("START", "1", "pathBack", 1, createCornerArrayQuill(), createCornerArrayQuill(), null);

    private Manuscript  m = new Manuscript(startingCard);

    AbstractCard goldCard = new GoldCard("41", "pathFront", "pathBack", 1, totalPiece, totalPiece, new int[]{0, 0, 0, 0}, Resources.FUNGI, "Quill");
    AbstractCard goldCard1 = new GoldCard("42", "pathFront", "pathBack", 1, totalPiece, totalPiece, new int[]{0, 0, 0, 0}, Resources.FUNGI, "Quill");
    AbstractCard goldCard2 = new GoldCard("43", "pathFront", "pathBack", 1, totalPiece, totalPiece, new int[]{0, 0, 0, 0}, Resources.FUNGI, "Quill");

    AbstractCard target = new TargetCard("95", "pathFront", "pathBack", 1, totalPiece, totalPiece, "red", "ThreePiece");

    public static Corner[] createCornerArrayQuill() {
        Corner[] corners = new Corner[4];
        for (int i = 0; i < corners.length; i++) {
            corners[i] = new Corner("1", true, null, Piece.QUILL);
        }
        return corners;
    }



    @Test
    public void testCalculateInkwell() {

        goldCard.setFront(true);
        goldCard1.setFront(true);
        goldCard2.setFront(true);

        m.placeCard(goldCard, 39, 39);
        m.placeCard(goldCard1, 38, 38);
        m.placeCard(goldCard2, 41, 39);

        int expectedPoint = 9;
        PointStrategy strategy = new ThreePiece();
        int points = strategy.calculatePoint(m, goldCard2);
        assertEquals(expectedPoint, points);
    }

    @Test
    public void testCalculateStrategy() {



        m.placeCard(goldCard, 39, 39);
        m.placeCard(goldCard1, 38, 38);
        m.placeCard(goldCard2, 41, 39);

        int expectedPoint = 2;
        PointStrategy strategy = new ThreePiece();
        int points = strategy.calculatePoint(m, target);
        assertEquals(expectedPoint, points);
    }



}

