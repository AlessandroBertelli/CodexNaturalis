package polimi.ingsw.model;

import java.util.List;

/**
 * This class represents a starting card in the game.
 * It extends Abstract Card.
 */
public class StartingCard extends AbstractCard{

    private List<Resources> attachedResources;

    /**
     * Constructs a StartingCard with specified parameters.
     *
     * @param ID the ID of the card
     * @param pathFrontImage the path to the front image of the card
     * @param pathBackImage the path to the back image of the card
     * @param point the points assigned to the card
     * @param frontCorner the front corners of the card
     * @param backCorner the back corners of the card
     * @param attachedResources the resources attached to the card
     */
    public StartingCard(String ID, String pathFrontImage, String pathBackImage, int point, Corner[] frontCorner, Corner[] backCorner, List<Resources> attachedResources) {
        super(ID,pathFrontImage,pathBackImage,point,frontCorner,backCorner);
        this.attachedResources = attachedResources;
    }

    public List<Resources> getCardResources() {
        return attachedResources;
    }

    /**
     * Gets the actual resources available on the card.
     *
     * @return an array representing the count of each resource type
     */
    @Override
    public int[] getActualResource() {
        int[] availableResources = new int[4];

        if (isFront()) {
            List<Resources> cardResources = getCardResources();
            for (int i = 0; i < cardResources.size(); i++) {
                switch (getCardResources().get(i)) {
                    case PLANT:
                        availableResources[0]++;
                        break;
                    case ANIMAL:
                        //position 1
                        availableResources[1]++;
                        break;
                    case INSECT:
                        //position 2
                        availableResources[2]++;
                        break;
                    case FUNGI:
                        //position 3
                        availableResources[3]++;
                        break;
                    default:
                        break;
                }
            }
            int i = 0;
            for (Corner corner : getFrontCorner()) {
                if (corner.existCorner()&& (corner.getAttachedResource()!=null)) {
                    switch (corner.getAttachedResource()) {
                        case PLANT:
                            availableResources[0]++;
                            break;
                        case ANIMAL:
                            //position 1
                            availableResources[1]++;
                            break;
                        case INSECT:
                            //position 2
                            availableResources[2]++;
                            break;
                        case FUNGI:
                            //position 3
                            availableResources[3]++;
                            break;
                        default:
                            break;
                    }
                }
                i++;
            }
        } else {
            for (Corner corner : getBackCorner()) {
                if (corner.existCorner() && (corner.getAttachedResource()!=null)) {
                    switch (corner.getAttachedResource()) {
                        case PLANT:
                            availableResources[0]++;
                            break;
                        case ANIMAL:
                            //position 1
                            availableResources[1]++;
                            break;
                        case INSECT:
                            //position 2
                            availableResources[2]++;
                            break;
                        case FUNGI:
                            //position 3
                            availableResources[3]++;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return availableResources;
    }
    public StartingCard(String ID, String pathFrontImage, String pathBackImage) {
        super(ID,pathFrontImage,pathBackImage, 0, null,null);
    }

    @Override
    public int calculatePoint() {
        return point;
    }
}
//