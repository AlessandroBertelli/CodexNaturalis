package polimi.ingsw.model.strategy;

import polimi.ingsw.model.AbstractCard;
import polimi.ingsw.model.Manuscript;
import polimi.ingsw.model.Resources;
/**
 * The Diagonals class implements the PointStrategy interface and calculates points for a given manuscript and card.
 * Points are calculated based on the presence of specific diagonal patterns within the manuscript matrix.
 */

public class Diagonals implements PointStrategy {
    /**
     * Calculates points for the given manuscript and card.
     * Points are awarded based on the specific diagonal patterns and resource types attached to cards in the manuscript matrix.
     * @param m the Manuscript object containing the matrix of AbstractCards.
     * @param c the AbstractCard for which points are to be calculated.
     * It returns the calculated points.
     */
    public int calculatePoint(Manuscript m, AbstractCard c) {
        AbstractCard[][] matrix = m.getCompressedManuscript();
        int rows = matrix.length;
        int cols = matrix[0].length;
        int id = Integer.parseInt(c.getID());
        int sum_1 = 0;
        int sum_2 = 0;
        int points = 0;

        if (id == 88) {
            for (int i = cols -1 ; i >= 0; i--) {
                sum_1 = 0;
                int r = rows - 1;
                int h = i;
                 while (r >= 0 && h >= 0) {
                    if (matrix[r][h] != null) {
                        if (matrix[r][h].getAttachedResource() == Resources.PLANT) {
                            sum_1 += 1;
                        }
                    }
                    r--;
                    h--;
                }
                if (sum_1 >= 3) {
                    points += 2;
                }
            }

            for (int j = rows -2 ; j >= 0 ; j--) {
                sum_2 = 0;
                int r = j;
                int h = cols - 1;
                while (r >= 0 && h >= 0){
                    if (matrix[r][h] != null) {
                        if (matrix[r][h].getAttachedResource() == Resources.PLANT) {
                            sum_2 += 1;
                        }
                    }
                    r--;
                    h--;
                }
                if (sum_2 >= 3) {
                    points += 2;
                }
            }
        }

        if (id == 90) {
            for (int i = cols -1 ; i >= 0; i--) {
                sum_1 = 0;
                int r = rows - 1;
                int h = i;
                while (r >= 0 && h >= 0) {
                    if (matrix[r][h] != null) {
                        if (matrix[r][h].getAttachedResource() == Resources.INSECT) {
                            sum_1 += 1;
                        }
                    }
                    r--;
                    h--;
                }
                if (sum_1 >= 3) {
                    points += 2;
                }
            }

            for (int j = rows -2 ; j >= 0 ; j--) {
                sum_2 = 0;
                int r = j;
                int h = cols - 1;
                while (r >= 0 && h >= 0){
                    if (matrix[r][h] != null) {
                        if (matrix[r][h].getAttachedResource() == Resources.INSECT) {
                            sum_2 += 1;
                        }
                    }
                    r--;
                    h--;
                }
                if (sum_2 >= 3) {
                    points += 2;
                }
            }
        }

        if (id == 87) {
            for (int i = 0; i < cols; i++) {
                int r = rows - 1;
                int h = i;
                sum_1 = 0;
                while (r >= 0 && h < cols) {
                    if (matrix[r][h] != null) {
                        if (matrix[r][h].getAttachedResource() == Resources.FUNGI) {
                            sum_1 += 1;
                        }
                    }
                    r--;
                    h++;
                }
                if (sum_1 >= 3) {
                    points += 2;
                }
            }

            for (int j = rows - 2; j >= 0; j--) {
                sum_2 = 0;
                int r = j;
                int h = 0;
                while (r >= 0 && h < cols) {
                    if (matrix[r][j] != null) {
                        if (matrix[r][j].getAttachedResource() == Resources.FUNGI) {
                            sum_2 += 1;
                        }
                    }
                    r--;
                    h++;
                }
                if (sum_2 >= 3) {
                    points += 2;
                }
            }
        }

        if (id == 89) {
            for (int i = 0; i < cols; i++) {
                int r = rows - 1;
                int h = i;
                sum_1 = 0;
                while (r >= 0 && h < cols) {
                    if (matrix[r][h] != null) {
                        if (matrix[r][h].getAttachedResource() == Resources.ANIMAL) {
                            sum_1 += 1;
                        }
                    }
                    r--;
                    h++;
                }
                if (sum_1 >= 3) {
                    points += 2;
                }
            }

            for (int j = rows - 2; j >= 0; j--) {
                sum_2 = 0;
                int r = j;
                int h = 0;
                while (r >= 0 && h < cols) {
                    if (matrix[r][h] != null) {
                        if (matrix[r][h].getAttachedResource() == Resources.ANIMAL) {
                            sum_2 += 1;
                        }
                    }
                    r--;
                    h++;
                }
                if (sum_2 >= 3) {
                    points += 2;
                }
            }
        }
        return points;
    }



}
