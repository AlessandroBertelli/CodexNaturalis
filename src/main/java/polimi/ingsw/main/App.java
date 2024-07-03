package polimi.ingsw.main;


import polimi.ingsw.model.*;
import polimi.ingsw.model.strategy.*;

public class App
{
    public static void main( String[] args )
    {

        Player p = new Player("asd");


        AbstractCard startingCard = new StartingCard("START","1","pathBack",1, createCornerArray(), createCornerArray(),null);
        Manuscript m = new Manuscript(startingCard);

        AbstractCard c1 = new GoldCard("95", "pathFront", "pathBack", 5, new Corner[]{}, new Corner[]{}, new int[]{1, 1, 1, 1}, Resources.PLANT, "example");

        AbstractCard c2 = new ResourceCard("SEC2","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.ANIMAL);
        AbstractCard c3 = new ResourceCard("SEC3","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.ANIMAL);
        AbstractCard c4 = new ResourceCard("SEC4","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.ANIMAL);
        AbstractCard c5 = new ResourceCard("SEC5","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.FUNGI);
        AbstractCard c6 = new ResourceCard("SEC6","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.FUNGI);
        AbstractCard c7 = new ResourceCard("SEC4","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.FUNGI);
        AbstractCard c8 = new ResourceCard("SEC5","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.FUNGI);
        AbstractCard c9 = new ResourceCard("SEC6","1","pathBack",1, createCornerArray(),createCornerArray(), Resources.FUNGI);
















        System.out.println("Compressed Manuscript:");
        System.out.println(Manuscript.printManuscript(m.getCompressedManuscript()));


        PointStrategy strategy = new ThreePiece();
        int points = strategy.calculatePoint(m, c1);
        System.out.println("points: " + points);



    }
    public static Corner[] createCornerArray() {
        Corner[] corners = new Corner[4];
        for (int i = 0; i < corners.length; i++) {
            corners[i] = new Corner("1", true, null, null);
        }
        return corners;
    }
}

