package polimi.ingsw.model;

import polimi.ingsw.model.strategy.*;

/**
 * This class represents a gold card in the game.
 * It extends Abstract Card.
 */
public class GoldCard extends AbstractCard {

    private Piece piece;
    private int[] numResourceRequested;
    private String forEach;

    /**
     * Constructor for the GoldCard class.
     *
     * @param ID             the ID of the card
     * @param pathFrontImage the path to the front image of the card
     * @param pathBackImage  the path to the back image of the card
     * @param point          the points associated with the card
     * @param frontCorner    the front corners of the card
     * @param backCorner     the back corners of the card
     * @param numResourceRequested array containing the number of resources requested
     * @param attachedResource the resource attached to the card
     * @param forEach        string of type ForEach
     */
    public GoldCard(String ID, String pathFrontImage, String pathBackImage, int point, Corner[] frontCorner, Corner[] backCorner, int[] numResourceRequested, Resources attachedResource, String forEach) {
        super(ID, pathFrontImage, pathBackImage, point, frontCorner, backCorner);
        this.numResourceRequested = numResourceRequested;
        this.attachedResource = attachedResource;
        this.forEach = forEach;
    }

    /**
     * Checks if the requested resources are available in the manuscript.
     *
     * @param manuscript the manuscript to check against
     * @return true if there are enough available resources to satisfy the card's requests, false otherwise
     */
    @Override
    public boolean checkResourceRequested(Manuscript manuscript) {
        if (!this.isFront()) {
            return true;
        } else {
            AbstractCard[][] matrix = manuscript.getCompressedManuscript();

            //Counts, for each resource, its number on the manuscript
            int[] availableResources = Manuscript.getAvailableResource(matrix);

            // Checks if there are enough resources for each resource type
            for (int i = 0; i < numResourceRequested.length; i++) {
                if (availableResources[i] < numResourceRequested[i]) {
                    return false; // Not enough resources
                }
            }
            return true; // Enough resources
        }
    }

    /**
     * Updates the available resources array based on the given card resource.
     *
     * @param cardResource      the resource attached to the card
     * @param availableResources the array containing the counts of available resources
     */
    void updateAvailableResources(Resources cardResource, int[] availableResources) {
        if (cardResource == Resources.PLANT)
            availableResources[0]++;
        else if (cardResource == Resources.ANIMAL)
            availableResources[1]++;
        else if (cardResource == Resources.INSECT)
            availableResources[2]++;
        else if (cardResource == Resources.FUNGI)
            availableResources[3]++;
        else
            return;
    }

    /**
     * Calculates the availability of pieces based on the given matrix of cards.
     *
     * @param matrix the 2D array of AbstractCard representing the manuscript matrix
     * @return an array of integers representing the counts of available pieces
     */
    int[] calculateAvailablePiece(AbstractCard matrix[][]) {
        int[] availablePiece = new int[4];

        // Initialization of array used to count pieces
        for (int i = 0; i < availablePiece.length; i++)
            availablePiece[i] = 0;

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                AbstractCard currentCard = matrix[row][col];

                if (currentCard != null)
                    if ((!(currentCard.getID().equalsIgnoreCase("NO"))) && (!(currentCard.getID().equalsIgnoreCase("SI")))) {

                        //update of available piece
                        if (currentCard.isFront())
                            for (Corner corner : currentCard.getFrontCorner()) {
                                Piece piece = corner.getAttachedPiece();
                                this.updateAvilablePiece(piece, availablePiece);
                            }
                        else
                            for (Corner corner : currentCard.getBackCorner()) {
                                Piece piece = corner.getAttachedPiece();
                                this.updateAvilablePiece(piece, availablePiece);
                            }
                    }
            }
        }
        return availablePiece;
    }

    /**
     * Updates the available pieces array based on the given piece type.
     *
     * @param cardPiece        the piece attached to a corner of a card
     * @param availablePiece   the array containing the counts of available pieces
     */
    void updateAvilablePiece(Piece cardPiece, int[] availablePiece) {
        if (cardPiece == Piece.INKWELL)
            availablePiece[0]++;
        else if (cardPiece == Piece.MANUSCRIPT)
            availablePiece[1]++;
        else if (cardPiece == Piece.QUILL)
            availablePiece[2]++;
        else
            return;
    }

    public void ResourceStrategy(Manuscript m, int resources[]) {
        AbstractCard[][] matrix = m.getCompressedManuscript();
        int[] availableResources = Manuscript.getAvailableResource(matrix);;
    }

    public void PieceStrategy(Manuscript m, int piece[]) {
        AbstractCard[][] matrix = m.getCompressedManuscript();
        int[] availablePiece = calculateAvailablePiece(matrix);
    }

    public String getForEach() {
        return forEach;
    }

    public Resources getAttachedResource() {
        return attachedResource;
    }
}