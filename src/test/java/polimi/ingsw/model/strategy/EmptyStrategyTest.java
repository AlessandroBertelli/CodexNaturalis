package polimi.ingsw.model.strategy;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import polimi.ingsw.main.App;
import polimi.ingsw.model.AbstractCard;
import polimi.ingsw.model.Manuscript;
import polimi.ingsw.model.ResourceCard;

public class EmptyStrategyTest {

    private Manuscript manuscript;

    AbstractCard startingCard = new AbstractCard("81", "/path/to/front/image", "/path/to/back/image", 10, App.createCornerArray(), App.createCornerArray()) {

    };

    Manuscript manuscript1 = new Manuscript(startingCard);
    AbstractCard card = new ResourceCard("2", "/path/to/image", "pathBack",4, App.createCornerArray(), App.createCornerArray(), null);


    @Test
    public void testCalculatePoint() {
        int expectedPoint = 4;
        PointStrategy strategy = new EmptyStrategy();
        int points = strategy.calculatePoint(manuscript1, card);
        assertEquals(expectedPoint, points);
    }
}
