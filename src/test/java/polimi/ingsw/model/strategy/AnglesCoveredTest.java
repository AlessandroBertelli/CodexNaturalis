package polimi.ingsw.model.strategy;

import org.junit.jupiter.api.Test;
import polimi.ingsw.main.App;
import polimi.ingsw.model.*;

import static org.junit.Assert.assertEquals;
import static polimi.ingsw.main.App.createCornerArray;

public class AnglesCoveredTest {
    private Manuscript m;
    AbstractCard card = new ResourceCard("3","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.ANIMAL);
    AbstractCard c1 = new ResourceCard("1","1","pathBack",1, createCornerArray(), createCornerArray(), Resources.ANIMAL);
    AbstractCard c2 = new ResourceCard("2","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.ANIMAL);

    AbstractCard c3 = new ResourceCard("1","1","pathBack",1, createCornerArray(), createCornerArray(), Resources.ANIMAL);
    AbstractCard test = new ResourceCard("2","1","pathBack",1, createCornerArrayTest(),createCornerArrayTest(), Resources.ANIMAL);


    public static Corner[] createCornerArrayTest() {
        Corner[] corners = new Corner[4];
        for (int i = 0; i < 3; i++) {
            corners[i] = new Corner("1", true, null, null);
        }
        corners[3] = new Corner("1", false, null, null);
        return corners;
    }


    private void SetUp() {
        Player p = new Player("asd");
        AbstractCard startingCard = new StartingCard("START","1","pathBack",1, createCornerArray(), createCornerArray(),null);
        m = new Manuscript(startingCard);
    }

    @Test
    public void testCalculatePoint() {
        SetUp();
        m.placeCard(c1, 39, 39);
        m.placeCard(c2, 41, 39);
        m.placeCard(card, 40, 38);
        int expectedPoint = 4;
        PointStrategy strategy = new AnglesCovered();
        int points = strategy.calculatePoint(m, card);
        assertEquals(expectedPoint, points);
    }

    @Test
    public void testCalculatePoint2() {
        SetUp();

        m.placeCard(c2, 41, 39);
        m.placeCard(card, 40, 38);
        int expectedPoint = 2;
        PointStrategy strategy = new AnglesCovered();
        int points = strategy.calculatePoint(m, card);
        assertEquals(expectedPoint, points);
    }

    @Test
    public void testCalculatePoint4() {
        SetUp();

        m.placeCard(test, 39, 41);
        m.placeCard(c2, 38, 42);
        m.placeCard(card, 39, 43);
        int expectedPoint = 2;
        PointStrategy strategy = new AnglesCovered();
        int points = strategy.calculatePoint(m, card);
        System.out.println(Manuscript.printManuscript(m.getCompressedManuscript()));
        assertEquals(expectedPoint, points);
    }

}

