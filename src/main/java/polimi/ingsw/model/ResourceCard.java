package polimi.ingsw.model;

/**
 * This class represents a resource card in the game.
 * It extends Abstract Card.
 */
public class ResourceCard extends AbstractCard {

    /**
     * Constructs a ResourceCard with specified parameters.
     *
     * @param ID the ID of the card
     * @param pathFrontImage the path to the front image of the card
     * @param pathBackImage the path to the back image of the card
     * @param point the points assigned to the card
     * @param frontCorner the front corners of the card
     * @param backCorner the back corners of the card
     * @param attachedResource the resource attached to the card
     */
    public ResourceCard(String ID, String pathFrontImage, String pathBackImage, int point, Corner[] frontCorner, Corner[] backCorner, Resources attachedResource) {
        super(ID, pathFrontImage, pathBackImage, point, frontCorner, backCorner);
        this.setAttachedResource(attachedResource);
    }


    @Override
    public int calculatePoint() {
        return point;
    }


}
