package polimi.ingsw.model.strategy;

import polimi.ingsw.model.AbstractCard;
import polimi.ingsw.model.Manuscript;

/**
 * The AnglesCovered class implements the PointStrategy interface and calculates points for a given manuscript and card.
 * Points are calculated based on the presence of adjacent cards in the manuscript.
 */
public class AnglesCovered implements PointStrategy {
    /**
     * Calculates points for the given manuscript and card.
     * Points are awarded based on specific criteria involving the IDs of adjacent cards in the manuscript matrix.
     * @param m the Manuscript object containing the matrix of AbstractCards.
     * @param c the AbstractCard for which points are to be calculated.
     * it returns the calculated points.
     */
    public int calculatePoint(Manuscript m, AbstractCard c) {

        AbstractCard[][] matrix = m.getCompressedManuscript();
        int rows = matrix.length;
        int col = matrix[0].length;
        int points = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < col; j++) {
                if(matrix[i][j] != null) {

                    if ( matrix[i][j].getID().equals(c.getID()) ) {

                        if (!matrix[i +1 ][j +1 ].getID().equals("SI") && !matrix[i + 1][j +1].getID().equals("NO")) {
                            points += 2;
                        }


                        if (!matrix[i +1 ][j -1].getID().equals("SI") && !matrix[i +1][j-1].getID().equals("NO")) {
                            points += 2;
                        }


                        if (!matrix[i -1 ][j +1].getID().equals("SI") && !matrix[i-1][j+1].getID().equals("NO")) {
                           points += 2;
                        }


                        if (!matrix[i-1][j-1].getID().equals("SI") && !matrix[i-1][j-1].getID().equals("NO")) {
                            points += 2;
                        }
                    }
                }
            }
        }
        return points;
    }


    private boolean isValid(AbstractCard[][] matrix, int i, int j) {
        return i >= 0 && i < matrix.length && j >= 0 && j < matrix[0].length && matrix[i][j].getID() != null;
    }
}





