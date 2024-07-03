package polimi.ingsw.model;

/**
 * extends abstract card with nothing relevant.
 */
public class TargetCard extends AbstractCard {

    private String color;   //Color = Resource type. It could be null for some cards, not a problem
    private String strategy;    //ID that identifies one of the 16 different target cards (and its own strategy to calculate points)

    public TargetCard(String ID, String pathFrontImage, String pathBackImage, int point, Corner[] frontCorner, Corner[] backCorner, String color, String strategy) {
        super(ID, pathFrontImage, pathBackImage, point, frontCorner, backCorner);
        this.color = color;
        this.strategy = strategy;
    }
    public boolean checkResourceRequested() {
        return true;
    }
    public int[] getCardResources() {
        return new int[0];
    }
    @Override
    public int calculatePoint() {return point+2;}
}//