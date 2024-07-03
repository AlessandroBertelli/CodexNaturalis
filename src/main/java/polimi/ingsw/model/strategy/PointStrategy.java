package polimi.ingsw.model.strategy;


import polimi.ingsw.model.AbstractCard;
import polimi.ingsw.model.Manuscript;
/**
 * The PointStrategy interface defines a method for calculating points based on a manuscript and a card.
 * This interface is part of the Strategy pattern, which allows for different point calculation strategies
 * to be used interchangeably.
 * Implementing classes will provide specific point calculation logic. Examples of implementing classes include:
 *  -AnglesCovered
 *  -Diagonals
 *  -EmptyStrategy
 *  -LconfigurationFour/Three/two/one
 *  -ThreePiece
 * The Strategy pattern is used here to encapsulate the point calculation algorithms, allowing the system to choose
 * the appropriate algorithm at runtime without modifying the client code.
 */
public interface PointStrategy {
    /**
     * Calculates points for the given manuscript and card.
     * @param m the Manuscript object containing the matrix of AbstractCards.
     * @param c the AbstractCard for which points are to be calculated.
     * It returns the calculated points.
     */
    int calculatePoint(Manuscript m, AbstractCard c);
}

