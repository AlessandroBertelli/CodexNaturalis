package polimi.ingsw.model;

import polimi.ingsw.model.strategy.AnglesCovered;
import polimi.ingsw.model.strategy.EmptyStrategy;
import polimi.ingsw.model.strategy.PointStrategy;
import polimi.ingsw.model.strategy.ThreePiece;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Abstract class representing a generic card in the game.
 * This class provides methods and properties common to all types of cards.
 */
public abstract class AbstractCard implements Serializable {

    private final String ID;
    private final Corner[] frontCorner;
    private final Corner[] backCorner;
    private boolean front;
    private final String pathFrontImage;
    private final String pathBackImage;
    final int point;
    protected Resources attachedResource;

    /**
     * Constructor for creating an AbstractCard object with specified attributes.
     *
     * @param ID             The unique identifier of the card.
     * @param pathFrontImage The file path to the front image of the card.
     * @param pathBackImage  The file path to the back image of the card.
     * @param point          The point value of the card.
     * @param frontCorner    Array of Corner objects representing corners on the front side of the card.
     * @param backCorner     Array of Corner objects representing corners on the back side of the card.
     */
    public AbstractCard(String ID, String pathFrontImage, String pathBackImage, int point, Corner[] frontCorner, Corner[] backCorner) {
        this.ID = ID;
        this.pathFrontImage = pathFrontImage;
        this.pathBackImage = pathBackImage;
        this.point = point;
        this.frontCorner = frontCorner;
        this.backCorner = backCorner;
    }

    /**
     * Constructor for creating an AbstractCard object with specified attributes including attached resources.
     *
     * @param ID             The unique identifier of the card.
     * @param pathFrontImage The file path to the front image of the card.
     * @param pathBackImage  The file path to the back image of the card.
     * @param point          The point value of the card.
     * @param frontCorner    Array of Corner objects representing corners on the front side of the card.
     * @param backCorner     Array of Corner objects representing corners on the back side of the card.
     * @param resources      The attached resource enum value of the card.
     */
    public AbstractCard(String ID, String pathFrontImage, String pathBackImage, int point, Corner[] frontCorner, Corner[] backCorner, Resources resources) {
        this.ID = ID;
        this.pathFrontImage = pathFrontImage;
        this.pathBackImage = pathBackImage;
        this.point = point;
        this.frontCorner = frontCorner;
        this.backCorner = backCorner;
        this.attachedResource = resources;
    }

    /**
     * @return The ID of the card.
     */
    public String getID() {
        return ID;
    }


    /**
     * @return true if the card is facing the front side, false otherwise.
     */
    public boolean isFront() {
        return front;
    }

    /**
     * @param front true to set the card facing the front side, false to set it facing the back side.
     */
    public void setFront(boolean front) {
        this.front = front;
    }

    /**
     * @return The file path to the front image.
     */
    public String getPathFrontImage() {
        return pathFrontImage;
    }

    /**
     * @return The file path to the back image.
     */
    public String getPathBackImage() {
        return pathBackImage;
    }

    /**
     * @return The card's point
     */
    public int getPoint() {
        return point;
    }

    /**
     * @return The card's attached resources
     */
    public Resources getAttachedResource() {
        return attachedResource;
    }

    /**
     * Sets the attached resource enum value of the card.
     *
     * @param attachedResource The attached resource enum value to set.
     */
    public void setAttachedResource(Resources attachedResource) {
        this.attachedResource = attachedResource;
    }

    /**
     * Calculates and returns the point value of the card.
     *
     * @return The calculated point value of the card.
     */
    public int calculatePoint() {
        return point;
    }

    /**
     * Dummy method to be overridden in subclasses.
     * Checks if the required resource for a manuscript matches the card's attached resource.
     *
     * @param manuscript The manuscript object to check against.
     * @return Always returns true.
     */
    boolean checkResourceRequested(Manuscript manuscript) {
        return true;
    }

     /**
      *  @return The top left attached card.
     */
    public AbstractCard getTopLeftCard() {
        if (front) return frontCorner[0].getAttachedCard();
        else return backCorner[0].getAttachedCard();
    }

    /**
     *  @return The top right attached card.
     */
    public AbstractCard getTopRightCard() {
        if (front) return frontCorner[1].getAttachedCard();
        else return backCorner[1].getAttachedCard();
    }

    /**
     *  @return The bottom left attached card.
     */
    public AbstractCard getBottomLeftCard() {
        if (front) return frontCorner[2].getAttachedCard();
        else return backCorner[2].getAttachedCard();
    }

    /**
     *  @return The bottom left attached card.
     */
    public AbstractCard getBottomRightCard() {
        if (front) return frontCorner[3].getAttachedCard();
        else return backCorner[3].getAttachedCard();
    }

    /**
     *  Set the top left attached card.
     */
    public void setTopLeftCard(AbstractCard card) {
        if (front) {
            frontCorner[0].setAttachedCard(card);
            frontCorner[0].setExistence(false);
        } else {
            backCorner[0].setAttachedCard(card);
            backCorner[0].setExistence(false);
        }
    }

    /**
     *  Set the top right attached card.
     */
    public void setTopRightCard(AbstractCard card) {
        if (front) {
            frontCorner[1].setAttachedCard(card);
            frontCorner[1].setExistence(false);
        } else {
            backCorner[1].setAttachedCard(card);
            backCorner[1].setExistence(false);
        }
    }

    /**
     *  Set the bottom left attached card.
     */
    public void setBottomLeftCard(AbstractCard card) {
        if (front) {
            frontCorner[2].setAttachedCard(card);
            frontCorner[2].setExistence(false);
        } else {
            backCorner[2].setAttachedCard(card);
            frontCorner[2].setExistence(false);
        }
    }

    /**
     *  Set the bottom right attached card.
     */
    public void setBottomRightCard(AbstractCard card) {
        if (front) {
            frontCorner[3].setAttachedCard(card);
            frontCorner[3].setExistence(false);
        } else {
            backCorner[3].setAttachedCard(card);
            frontCorner[3].setExistence(false);
        }
    }

    /**
     *  @return  the top left corner.
     */
    public Corner getTopLeftCorner() {
        if (front) return frontCorner[0];
        else return backCorner[0];
    }

    /**
     *  @return  the top right corner.
     */
    public Corner getTopRightCorner() {
        if (front) return frontCorner[1];
        else return backCorner[1];
    }

    /**
     *  @return  the bottom left corner.
     */
    public Corner getBottomLeftCorner() {
        if (front) return frontCorner[2];
        else return backCorner[2];
    }

    /**
     *  @return  the bottom right corner.
     */
    public Corner getBottomRightCorner() {
        if (front) return frontCorner[3];
        else return backCorner[3];
    }

    /**
     * @return true if the top left corner exists.
     */
    public boolean existTopLeftCorner() {
        if (front) return frontCorner[0].existCorner();
        else return backCorner[0].existCorner();
    }

    /**
     * @return true if the top right corner exists.
     */
    public boolean existTopRightCorner() {
        if (front) return frontCorner[1].existCorner();
        else return backCorner[1].existCorner();
    }

    /**
     * @return true if the bottom left corner exists.
     */
    public boolean existBottomLeftCorner() {
        if (front) return frontCorner[2].existCorner();
        else return backCorner[2].existCorner();
    }

    /**
     * @return true if the bottom right corner exists.
     */
    public boolean existBottomRightCorner() {
        if (front) return frontCorner[3].existCorner();
        else return backCorner[3].existCorner();
    }

    /**
     * Returns an array counting the available resources (PLANT, ANIMAL, INSECT, FUNGI) on the card.
     * The counts are indexed as follows:
     * Index 0: PLANT
     * Index 1: ANIMAL
     * Index 2: INSECT
     * Index 3: FUNGI
     * @return An array of integers representing the count of each resource type on the card.
     */
    public int[] getActualResource() {
        // Counter of manuscript available resources
        int[] availableResources = new int[4];

        if (!isFront()) {
            switch (getAttachedResource()) {
                case PLANT:    //position 0
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

            }
        } else {
            for (Corner corner : getFrontCorner()) {
                if (corner.existCorner() && (corner.getAttachedResource() != null)) {
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

    /**
     * Returns an array counting the available pieces (INKWELL, QUILL, MANUSCRIPT) on the front corners of the card.
     * The counts are indexed as follows:
     * Index 0: INKWELL
     * Index 1: QUILL
     * Index 2: MANUSCRIPT
     * @return An array of integers representing the count of each piece type on the front corners of the card.
     */
    public int[] getActualPiece() {
        // Counter of manuscript available resources
        int[] availablePiece = new int[3];

        for (int i = 0; i < 3; i++) {
            availablePiece[i] = 0;
        }

        if (isFront()) {
            for (Corner corner : getFrontCorner()) {
                if (corner.existCorner() && (corner.getAttachedPiece() != null)) {
                    switch (corner.getAttachedPiece()) {
                        case INKWELL:
                            availablePiece[0]++;
                            break;
                        case QUILL:
                            //position 1
                            availablePiece[1]++;
                            break;
                        case MANUSCRIPT:
                            //position 2
                            availablePiece[2]++;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return availablePiece;
    }

    /**
     * Calculates the points for a given card based on its ID and the strategy associated with the ID's last digit.
     *
     * @param p    The player whose manuscript will be used to calculate the points.
     * @param card The card for which the points are being calculated.
     * @return The calculated points based on the specific strategy.
     */
    public int calculatePointStrategy(Player p, AbstractCard card){
        int points = 0;
        int id = Integer.parseInt(card.getID());
        int check = id % 10;

        if(check == 1 || check == 2|| check == 3) {
            PointStrategy strategy = new ThreePiece();
            points = strategy.calculatePoint(p.getManuscript(), card);
        }else if(check == 4 || check == 5 || check == 6){
            PointStrategy strategy = new AnglesCovered();
            points = strategy.calculatePoint(p.getManuscript(), card);
        }else{
            PointStrategy strategy = new EmptyStrategy();
            points = strategy.calculatePoint(p.getManuscript(), card);
        }
        return points;
    }

    public Corner[] getBackCorner() {
        return backCorner;
    }

    public Corner[] getFrontCorner() {
        return frontCorner;
    }
    public String toString() {
        return "AbstractCard{" +
                "ID='" + ID + '\'' +
                '}';
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractCard that = (AbstractCard) o;
        return front == that.front && point == that.point && Objects.equals(ID, that.ID) && Arrays.equals(frontCorner, that.frontCorner) && Arrays.equals(backCorner, that.backCorner) && Objects.equals(pathFrontImage, that.pathFrontImage) && Objects.equals(pathBackImage, that.pathBackImage);
    }

    public Corner getAttachedCorner(int corner) {
        if (corner < 0 || corner >= 4) {
            throw new IllegalArgumentException("Illegal argument passed");
        }

        if (front) {
            return frontCorner[corner];
        } else {
            return backCorner[corner];
        }
    }

    public void setExistenceAttachedCorner(int corner, boolean existence) {
        if (corner < 0 || corner >= 4) {
            throw new IllegalArgumentException("Illegal argument passed");
        }

        if (front) {
            frontCorner[corner].setExistence(existence);
        } else {
            backCorner[corner].setExistence(existence);
        }
    }


    public AbstractCard getAttachedCard(int corner) {
        if (corner < 0 || corner >= 4) {
            throw new IllegalArgumentException("Illegal argument passed");
        }

        if (front) {
            return frontCorner[corner].getAttachedCard();
        } else {
            return backCorner[corner].getAttachedCard();
        }
    }
}

//