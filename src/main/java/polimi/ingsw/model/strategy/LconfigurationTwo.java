package polimi.ingsw.model.strategy;

import polimi.ingsw.model.AbstractCard;
import polimi.ingsw.model.Manuscript;
import polimi.ingsw.model.Resources;
/**
 * The LconfigurationTwo class implements the PointStrategy interface and calculates points for a given manuscript and card.
 * Points are calculated based on specific L-shaped configurations of resources within the manuscript matrix.
 */
public class LconfigurationTwo implements PointStrategy{
    /**
     * Calculates points for the given manuscript and card.
     * Points are awarded based on specific L-shaped configurations of resources within the manuscript matrix.
     * @param m the Manuscript object containing the 2D matrix of AbstractCards.
     * @param c the AbstractCard for which points are to be calculated.
     * It returns the calculated points.
     */
    public int calculatePoint(Manuscript m, AbstractCard c){
        AbstractCard[][] matrix = m.getCompressedManuscript();
        int rows = matrix.length;
        int col = matrix[0].length;
        int points = 0;
        int i, j;
        int[][] array = new int[80][2];
        int index = 0;

        for(i = 0; i < rows; i++) {
            for (j = 0; j < col; j++) {
                if(matrix[i][j]!= null) {
                    if (matrix[i][j].getAttachedResource() == Resources.PLANT) {
                        boolean check = check(array, i, j);
                        if(i < rows - 2 && check) {
                            if(matrix[i+2][j] != null) {
                                if (matrix[i + 2][j].getAttachedResource() == Resources.PLANT) {
                                    if (j > 0) {
                                        if(matrix[i + 3][j - 1] != null) {
                                            if (matrix[i + 3][j - 1].getAttachedResource() == Resources.INSECT) {
                                                points += 3;
                                                array[index][0] = i - 2;
                                                array[index][1] = j;
                                                index += 1;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        return points;

    }

    private boolean check(int[][] array, int x, int y) {
        for(int i = 0; i < array.length; i++) {
            if (array[i][0] == x && array[i][1] == y) {
                return false;
            }
        }
        return true;
    }
}

