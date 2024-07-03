package polimi.ingsw.model.strategy;

import polimi.ingsw.model.AbstractCard;
import polimi.ingsw.model.Manuscript;

/**
 * The EmptyStrategy class implements the PointStrategy interface and provides a simple point calculation method.
 * This strategy directly returns the point value of the given card without any additional calculations.
 */
public class EmptyStrategy implements PointStrategy {
    /**
     * Calculates points for the given manuscript and card.
     * This implementation simply returns the point value of the given card.
     * @param m the Manuscript object (not used in this strategy).
     * @param c the AbstractCard for which points are to be calculated.
     * It returns the point value of the given card.
     */
    public int calculatePoint(Manuscript m, AbstractCard c) {
        return c.getPoint();
    }
}
