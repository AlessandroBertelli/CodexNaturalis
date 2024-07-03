package polimi.ingsw.model.strategy;

import polimi.ingsw.model.AbstractCard;
import polimi.ingsw.model.GoldCard;
import polimi.ingsw.model.Manuscript;
import polimi.ingsw.model.Piece;
/**
 * The ThreePiece class implements the PointStrategy interface and calculates points based on the resources and pieces available in the manuscript.
 * Points are awarded based on specific card IDs and their associated resources or pieces.
 */
public class ThreePiece implements PointStrategy {
    /**
     * Calculates points for the given manuscript and card.
     * Points are awarded based on specific configurations of resources or pieces within the manuscript matrix and the card ID.
     * @param m the Manuscript object containing the matrix of AbstractCards.
     * @param c the AbstractCard for which points are to be calculated.
     * It returns the calculated points.
     */
    public int calculatePoint(Manuscript m, AbstractCard c) {
        int cardIdInt = Integer.parseInt(c.getID());
        int points = 0;




       int [] totalResources = Manuscript.getAvailableResource(m.getCompressedManuscript());
       int []totalPiece = Manuscript.getAvailablePiece(m.getCompressedManuscript());

        if(cardIdInt <= 80){
            GoldCard goldCard = (GoldCard) c;
            String s = goldCard.getForEach();
            if(s.equals("Quill")){
                points = totalPiece[1];
            }
            if(s.equals("Manuscript")){
                points = totalPiece[2];
            }
            if(s.equals("Inkwell")){
                points = totalPiece[0];
            }
            return points;
        }

        if (c.getID().equals("95")) {
            points = (totalResources[3] / 3 ) * 2;
        } else if (c.getID().equals("96")) {
            points = (totalResources[0] / 3 ) * 2;
        } else if (c.getID().equals("97")) {
            points = (totalResources[1] / 3 ) * 2;
        } else if (c.getID().equals("98")) {
            points = (totalResources[2] / 3 ) * 2;
        } else if (c.getID().equals("99")) {

            int min = totalPiece[0];

            for (int i = 0; i < 3; i++) {
                if (totalPiece[i] < min) {
                    min = totalPiece[i];
                }
            }
            points = min * 3;
        } else if (c.getID().equals("100")) {
            points = (totalPiece[2] / 2);
        } else if (c.getID().equals("101")) {
            points = (totalPiece[0] / 2);
        } else if (c.getID().equals("102")) {
            points = (totalPiece[1] / 2);
        }

        return points;
    }
}
