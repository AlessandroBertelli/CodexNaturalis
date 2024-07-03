package polimi.ingsw.model.strategy;

import org.junit.jupiter.api.Test;
import polimi.ingsw.model.*;

import static org.junit.Assert.assertEquals;
import static polimi.ingsw.main.App.createCornerArray;

public class DiagonalsTest {
    private Manuscript m;
    AbstractCard card1 = new ResourceCard("87","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.FUNGI);
    AbstractCard card2 = new ResourceCard("88","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.FUNGI);
    AbstractCard card3 = new ResourceCard("89","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.FUNGI);
    AbstractCard card4 = new ResourceCard("90","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.FUNGI);

    AbstractCard c1 = new ResourceCard("1","1","pathBack",1, createCornerArray(), createCornerArray(), Resources.FUNGI);
    AbstractCard c2 = new ResourceCard("2","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.FUNGI);

    AbstractCard c3 = new ResourceCard("3","1","pathBack",1, createCornerArray(), createCornerArray(), Resources.FUNGI);

    AbstractCard c4 = new ResourceCard("4","1","pathBack",1, createCornerArray(), createCornerArray(), Resources.ANIMAL);
    AbstractCard c5 = new ResourceCard("5","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.ANIMAL);

    AbstractCard c6 = new ResourceCard("6","1","pathBack",1, createCornerArray(), createCornerArray(), Resources.ANIMAL);
    AbstractCard c7 = new ResourceCard("7","1","pathBack",1, createCornerArray(), createCornerArray(), Resources.PLANT);
    AbstractCard c8 = new ResourceCard("8","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.PLANT);

    AbstractCard c9 = new ResourceCard("9","1","pathBack",1, createCornerArray(), createCornerArray(), Resources.PLANT);

    AbstractCard c10 = new ResourceCard("10","1","pathBack",1, createCornerArray(), createCornerArray(), Resources.INSECT);
    AbstractCard c11= new ResourceCard("11","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.INSECT);

    AbstractCard c12 = new ResourceCard("12","1","pathBack",1, createCornerArray(), createCornerArray(), Resources.INSECT);


    private void SetUp() {
        Player p = new Player("asd");
        AbstractCard startingCard = new StartingCard("START","1","pathBack",1, createCornerArray(), createCornerArray(),null);
        m = new Manuscript(startingCard);
    }

    @Test
    public void testCalculatePointFungi() {
        SetUp();
        m.placeCard(c1, 39, 41);
        m.placeCard(c2, 38, 42);
        m.placeCard(c3, 37, 43);
        int expectedPoint = 2;
        PointStrategy strategy = new Diagonals();
        int points = strategy.calculatePoint(m, card1);
        assertEquals(expectedPoint, points);
    }

    @Test
    public void testCalculatePointPlant() {
        SetUp();
        m.placeCard(c7, 39, 39);
        m.placeCard(c8, 38, 38);
        m.placeCard(c9, 37, 37);
        int expectedPoint = 2;
        PointStrategy strategy = new Diagonals();
        int points = strategy.calculatePoint(m, card2);
        assertEquals(expectedPoint, points);
    }
    @Test
    public void testCalculatePointAnimal() {
        SetUp();
        m.placeCard(c4, 39, 41);
        m.placeCard(c5, 38, 42);
        m.placeCard(c6, 37, 43);
        int expectedPoint = 2;
        PointStrategy strategy = new Diagonals();
        int points = strategy.calculatePoint(m, card3);
        assertEquals(expectedPoint, points);
    }

    @Test
    public void testCalculatePointInsect() {
        SetUp();
        m.placeCard(c10, 39, 39);
        m.placeCard(c11, 38, 38);
        m.placeCard(c12, 37, 37);
        int expectedPoint = 2;
        PointStrategy strategy = new Diagonals();
        int points = strategy.calculatePoint(m, card4);
        assertEquals(expectedPoint, points);
    }
}
