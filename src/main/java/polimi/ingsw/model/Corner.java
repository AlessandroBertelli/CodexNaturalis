package polimi.ingsw.model;

import java.io.Serializable;

/**
 * Represents a corner on a card, which can optionally have an attached card, piece, or resource.
 * The existence of the corner can be determined, and it can be associated with various game elements.
 */
public class Corner implements Serializable {
    /** ID of the card to which this corner belongs. */
    private String Card_ID;

    /** The card attached to this corner, if any. */
    private AbstractCard attachedCard;

    /**
     * Indicates whether the corner exists:
     * - True if the corner exists (can be free or have a resource/piece).
     * - False if the corner does not exist (is blank).
     */
    private boolean existence;

    /** The type of resource attached to this corner, represented from an ENUM. */
    private final Resources attachedResource;

    /** The type of piece attached to this corner, represented from an ENUM. */
    private final Piece attachedPiece;

    /**
     * Constructs a Corner object with specified parameters.
     *
     * @param ID The ID of the card to which this corner belongs.
     * @param existence True if the corner exists.
     * @param resource The attached resource, or null if none.
     * @param piece The attached piece, or null if none.
     */
    public Corner(String ID, boolean existence, Resources resource, Piece piece){
        this.Card_ID = ID;
        this.existence = existence;
        this.attachedPiece = piece;
        this.attachedResource = resource;
        this.attachedCard = null;
    }

    /**
     * Checks if the corner exists.
     *
     * @return True if the corner exists, false otherwise.
     */
    public boolean existCorner() {
        return existence;
    }

    /**
     * Sets the existence of the corner.
     *
     * @param existence True if the corner exists, false otherwise.
     */
    public void setExistence(boolean existence) {
        this.existence = existence;
    }

    /**
     * Retrieves the attached resource of this corner.
     *
     * @return The attached resource, or null if none.
     */
    public Resources getAttachedResource() { // method that returns the corner's attached resource
        return attachedResource;
    }

    /**
     * Retrieves the attached piece of this corner.
     *
     * @return The attached piece, or null if none.
     */
    public Piece getAttachedPiece() {  // method that returns the corner's attached piece
        return attachedPiece;
    }

    /**
     * Sets the attached card for this corner.
     *
     * @param card The card to attach to this corner.
     */
    public void setAttachedCard(AbstractCard card) {
        this.attachedCard = card;
    }

    /**
     * Retrieves the attached card of this corner.
     *
     * @return The attached card, or null if none.
     */
    public AbstractCard getAttachedCard() {
        return attachedCard;
    }

    /**
     * Checks if the corner is free (exists and has no attached piece or resource).
     *
     * @return True if the corner is free, false otherwise.
     */
    public boolean isFree () {
        return existCorner() && (attachedPiece == null) && (attachedResource == null);
    }
}

