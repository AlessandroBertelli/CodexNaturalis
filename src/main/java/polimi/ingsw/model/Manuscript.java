package polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Represents a manuscript in the game.
 */
public class Manuscript implements Serializable {
    private final AbstractCard startingCard;
    private final AbstractCard[][] manuscriptMatrix = new AbstractCard[81][81];
    private final List<Cell> availableCells;

    private int[] totalResources;   //plant, animal, insect, fungi : total manuscript's resources

    private int[] totalPiece;        //inkwell, quill, manuscript : total manuscript's resources
    private final AbstractCard noCard;
    private final AbstractCard yesCard;
    private int topRow;
    private int bottomRow;

    private int leftColumn;
    private int rightColumn;

    /**
     * Constructs a manuscript with the given starting card.
     *
     * @param startingCard The starting card for the manuscript.
     */
    public Manuscript(AbstractCard startingCard) {
        this.startingCard = startingCard;
        this.availableCells = new ArrayList<>();
        initializeManuscriptMatrix();
        totalResources = new int[4];
        for (int i = 0; i < totalResources.length; i++) {
            totalResources[i] = 0;
        }
        totalPiece = new int[3];
        for (int i = 0; i < totalPiece.length; i++) {
            totalPiece[i] = 0;
        }
        Corner[] noCorners = new Corner[4];
        for (int i = 0; i < 4; i++) {
            noCorners[i] = new Corner("NO_" + i, false, null, null);
        }
        noCard = new ResourceCard("NO", "", "pathBack", 0, noCorners, noCorners, null);
        yesCard = new ResourceCard("SI", "", "pathBack", 0, noCorners, noCorners, null);
        placeCard(startingCard, 40, 40);
    }

    /**
     * Initializes the manuscript matrix with cards and sets initial parameters for printing.
     * The matrix is populated with Card objects except for positions where indices sum to an odd number,
     * which are initialized with a specific "NO" card.
     * Additionally, places a "yesCard" at the starting position (row=40, column=40) and adds this position
     * to the list of available cells for further use.
     * Initializes topRow, bottomRow, leftColumn, and rightColumn to 40.
     */
    private void initializeManuscriptMatrix() {
        // initialize the matrix with noCard
        for (int i = 0; i < manuscriptMatrix.length; i++) {
            for (int j = 0; j < manuscriptMatrix[i].length; j++) {
                if ((i + j) % 2 != 0) {
                    // add ID of the NO card
                    manuscriptMatrix[i][j] = new ResourceCard("NO", "", "pathBack", 0, new Corner[4], new Corner[4], null);
                }
            }
        }

        // place yes card in starting position row=40 and column=40
        manuscriptMatrix[40][40] = yesCard;
        // add starting Cell to @availableCells list
        availableCells.add(new Cell(40, 40));

        //initialize printingVariable
        topRow = bottomRow = leftColumn = rightColumn = 40;
    }

    /**
     * Gets the starting card of the manuscript.
     *
     * @return The starting card of the manuscript.
     */
    public AbstractCard getStartingCard() {
        return this.startingCard;
    }

    /**
     * Sets the side of the starting card in the manuscript.
     *
     * @param side The side of the starting card.
     */
    public void setStartingCardSide(boolean side) {
        startingCard.setFront(side);
    }

    /**
     * Sets the specified AbstractCard object at the given row and column in the manuscript matrix.
     *
     * @param card The AbstractCard object to set at the specified position.
     * @param row The row index where the card should be placed.
     * @param column The column index where the card should be placed.
     */
    private void setCard(AbstractCard card, int row, int column) {
        manuscriptMatrix[row][column] = card;
    }

    /**
     * Retrieves the AbstractCard object located at the specified row and column in the manuscript matrix.
     *
     * @param row The row index of the card to retrieve.
     * @param column The column index of the card to retrieve.
     * @return The AbstractCard object at the specified position in the manuscript matrix.
     */
    public AbstractCard getCard(int row, int column) {
        return manuscriptMatrix[row][column];
    }

    /**
     * Updates the printing variables (topRow, bottomRow, leftColumn, rightColumn)
     * based on the provided row and column coordinates.
     * Ensures that the updated variables include the specified row and column,
     * adjusting the boundaries if necessary.
     *
     * @param row The row coordinate to include in the printing boundaries.
     * @param column The column coordinate to include in the printing boundaries.
     */
    public void updatePrintingVariables(int row, int column) {
        //update printing variable to include also the first row and column usable
        if (bottomRow > row - 1) bottomRow = row - 1;
        if (topRow < row + 1) topRow = row + 1;
        if (leftColumn > column - 1) leftColumn = column - 1;
        if (rightColumn < column + 1) rightColumn = column + 1;
    }

    /**
     * Places a card in the manuscript at the specified row and column.
     *
     * @param card   The card to place.
     * @param row    The row index where the card should be placed.
     * @param column The column index where the card should be placed.
     * @throws IllegalStateException if the cell is not available
     */
    public void placeCard(AbstractCard card, int row, int column) throws IllegalArgumentException {
        // Check if the cell is contained in availableCells
        Cell currentCell = new Cell(row, column);
        if (!availableCells.contains(currentCell)) {
            throw new IllegalArgumentException("Cell [" + row + "," + column + "] is not available!");
        }

        availableCells.remove(currentCell);
        setCard(card, row, column);

        // Checking and updating with YES or NO card
        updateCorner(row, column, card.existTopLeftCorner(), row - 1, column - 1);
        updateCorner(row, column, card.existTopRightCorner(), row - 1, column + 1);
        updateCorner(row, column, card.existBottomLeftCorner(), row + 1, column - 1);
        updateCorner(row, column, card.existBottomRightCorner(), row + 1, column + 1);

        // Checking and link covered card with new placed one
        updateAttachedCard(row - 1, column - 1, card, AbstractCard::setBottomRightCard, AbstractCard::existBottomRightCorner);
        updateAttachedCard(row - 1, column + 1, card, AbstractCard::setBottomLeftCard, AbstractCard::existBottomLeftCorner);
        updateAttachedCard(row + 1, column - 1, card, AbstractCard::setTopRightCard, AbstractCard::existTopRightCorner);
        updateAttachedCard(row + 1, column + 1, card, AbstractCard::setTopLeftCard, AbstractCard::existTopLeftCorner);

        // update printing variables for display compressed manuscript
        updatePrintingVariables(row, column);
    }

    /**
     * Updates the corner of a specific cell in the manuscript matrix based on the provided parameters.
     * Depending on the cornerExist flag and the state of the existing card at the specified checkRow and checkColumn,
     * either sets the corner to a "yesCard" and adds the cell to availableCells,
     * or sets the corner to a "noCard" and removes the cell from availableCells.
     *
     * @param row The row index of the cell being updated.
     * @param column The column index of the cell being updated.
     * @param cornerExist Indicates whether the corner should exist (true) or not (false).
     * @param checkRow The row index to check for existing card state.
     * @param checkColumn The column index to check for existing card state.
     */
    private void updateCorner(int row, int column, boolean cornerExist, int checkRow, int checkColumn) {
        AbstractCard existingCard = getCard(checkRow, checkColumn);
        if (cornerExist && (existingCard == null || yesCard.equals(existingCard))) {
            setCard(yesCard, checkRow, checkColumn);
            availableCells.add(new Cell(checkRow, checkColumn));
        } else {
            if (existingCard == null || yesCard.equals(existingCard)) {
                setCard(noCard, checkRow, checkColumn);
                availableCells.remove(new Cell(checkRow, checkColumn));
            }
        }
    }

    /**
     * Updates the attached card of a specific cell in the manuscript matrix based on the provided parameters.
     * Retrieves the current attached card at the specified row and column, and if it exists and satisfies
     * the given cornerChecker predicate, sets it using the provided BiConsumer setter function with the currentCard.
     *
     * @param row The row index of the cell whose attached card is being updated.
     * @param column The column index of the cell whose attached card is being updated.
     * @param currentCard The new current card to set as attached.
     * @param setter The BiConsumer function used to set the attached card.
     * @param cornerChecker The predicate to check if the existing attached card satisfies the condition.
     */
    private void updateAttachedCard(int row, int column, AbstractCard currentCard, BiConsumer<AbstractCard, AbstractCard> setter, Predicate<AbstractCard> cornerChecker) {
        AbstractCard attachedCard = getCard(row, column);
        if (attachedCard != null && cornerChecker.test(attachedCard)) {
            setter.accept(attachedCard, currentCard);
        }
    }

    /**
     * Gets the list of available cells in the manuscript.
     *
     * @return The list of available cells.
     */
    public List<Cell> getAvailableCells() {
        return availableCells;
    }

    /**
     * Gets a compressed version of the manuscript.
     *
     * @return The compressed version of the manuscript.
     */
    public AbstractCard[][] getCompressedManuscript() {
        // Calculate the dimension of the compressed matrix
        int compressedRows = topRow - bottomRow + 1;
        int compressedColumns = rightColumn - leftColumn + 1;

        // Create a new matrix for the compressed matrix to return
        AbstractCard[][] compressedMatrix = new AbstractCard[compressedRows][compressedColumns];

        // Copy the utilized cells from original manuscriptMatrix to compressedMatrix
        for (int i = bottomRow; i <= topRow; i++) {
            if (rightColumn + 1 - leftColumn >= 0)
                System.arraycopy(manuscriptMatrix[i], leftColumn, compressedMatrix[i - bottomRow], leftColumn - leftColumn, rightColumn + 1 - leftColumn);
        }
        return compressedMatrix;
    }


    /**
     * Return a compressed version of the manuscript while preserving the original row and column indices.
     * This method uses the topRow, bottomRow, leftColumn, and rightColumn variables to determine the
     * portion of the manuscript to print, including the original indices for rows and columns.
     *
     * @return A string representing the compressed manuscript with original indices for rows and columns.
     */
    public String getCompressedManuscriptWithIndexToString() {
        StringBuilder result = new StringBuilder();

        // Add column headings
        result.append(String.format("%-5s", "")); // Room for row numbers
        for (int col = leftColumn; col <= rightColumn; col++) {
            result.append(String.format("%-8d", col));
        }
        result.append("\n");

        // Add matrix data
        for (int row = bottomRow; row <= topRow; row++) {
            result.append(String.format("%-5d", row)); // Row number
            for (int col = leftColumn; col <= rightColumn; col++) {
                if (manuscriptMatrix[row][col] != null) {
                    if (manuscriptMatrix[row][col].getID() == ("NO")) {
                        result.append(String.format("%-8s", '×'));
                    } else if (manuscriptMatrix[row][col].getID() == ("SI")) {
                        result.append(String.format("%-8s", '✓'));
                    } else {
                        result.append(String.format("%-8s", manuscriptMatrix[row][col].getID()));
                    }

                } else {
                    result.append("        "); // 8 empty spaces to keep the same format
                }
            }
            result.append("\n");
        }

        return result.toString();
    }


    /**
     * Gets the top row index of the manuscript.
     *
     * @return The top row index of the manuscript.
     */
    public int getTopRow() {
        return topRow;
    }

    /**
     * Gets the bottom row index of the manuscript.
     *
     * @return The bottom row index of the manuscript.
     */
    public int getBottomRow() {
        return bottomRow;
    }

    /**
     * Gets the left column index of the manuscript.
     *
     * @return The left column index of the manuscript.
     */
    public int getLeftColumn() {
        return leftColumn;
    }

    /**
     * Gets the right column index of the manuscript.
     *
     * @return The right column index of the manuscript.
     */
    public int getRightColumn() {
        return rightColumn;
    }

    /**
     * Gets the matrix representing the manuscript.
     *
     * @return The matrix representing the manuscript.
     */
    public AbstractCard[][] getManuscript() {
        return manuscriptMatrix;
    }

    /**
     * Generates a formatted string representation of the manuscript matrix containing AbstractCard objects.
     * Each cell in the matrix displays the ID of the AbstractCard if it exists, or empty spaces if the cell is null.
     *
     * @param matrixManuscript The 2D array of AbstractCard objects representing the manuscript matrix.
     * @return A formatted string displaying the contents of the manuscript matrix.
     */
    public static String printManuscript(AbstractCard[][] matrixManuscript) {
        StringBuilder result = new StringBuilder();

        // Add column heading
        result.append(String.format("%-5s", "")); // Room for row numbers
        for (int col = 0; col < matrixManuscript[0].length; col++) {
            result.append(String.format("%-8d", col));
        }
        result.append("\n");

        // Add matrix data
        for (int row = 0; row < matrixManuscript.length; row++) {
            result.append(String.format("%-5d", row)); // Row number
            for (int col = 0; col < matrixManuscript[row].length; col++) {
                if (matrixManuscript[row][col] != null) {
                    result.append(String.format("%-8s", matrixManuscript[row][col].getID()));
                } else {
                    result.append("        "); // 8 empty spaces to keep the same format
                }
            }
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * Retrieves the total available resources from all AbstractCard objects in the provided matrix.
     * Calculates and returns an array where each index corresponds to a type of resource,
     * summing up the available quantity across all non-"NO" and non-"SI" cards.
     *
     * @param matrix The 2D array of AbstractCard objects from which to retrieve available resources.
     * @return An array of integers representing the total available quantity of each resource type.
     *         Index 0 corresponds to the first resource type, index 1 to the second, and so on.
     */
    public static int[] getAvailableResource(AbstractCard[][] matrix) {
        int[] availableResources = new int[4];

        for (int i = 0; i < availableResources.length; i++) {
            availableResources[i] = 0;
        }

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                AbstractCard currentCard = matrix[row][col];

                if (currentCard != null)
                    if ((!(currentCard.getID().equalsIgnoreCase("NO"))) && (!(currentCard.getID().equalsIgnoreCase("SI")))) {
                            int[] currentAvailableResource = currentCard.getActualResource();
                        for(int i = 0; i<availableResources.length; i++) {
                            availableResources[i] += currentAvailableResource[i];
                        }
                    }
            }
        }
        return availableResources;
    }

    /**
     * Retrieves the total available pieces from all AbstractCard objects in the provided matrix.
     * Calculates and returns an array where each index corresponds to a type of piece,
     * summing up the available quantity across all non-"NO" and non-"SI" cards.
     *
     * @param matrix The 2D array of AbstractCard objects from which to retrieve available pieces.
     * @return An array of integers representing the total available quantity of each piece type.
     *         Index 0 corresponds to the first piece type, index 1 to the second, and so on.
     */
    public static int[] getAvailablePiece(AbstractCard[][] matrix) {
        int[] availablePiece = new int[3];

        for (int i = 0; i < availablePiece.length; i++) {
            availablePiece[i] = 0;
        }

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                AbstractCard currentCard = matrix[row][col];

                if (currentCard != null)
                    if ((!(currentCard.getID().equalsIgnoreCase("NO"))) && (!(currentCard.getID().equalsIgnoreCase("SI")))) {
                        int[] currentAvailableResource = currentCard.getActualPiece();
                        for(int i = 0; i<availablePiece.length; i++) {
                            availablePiece[i] += currentAvailableResource[i];
                        }
                    }
            }
        }
        return availablePiece;
    }

    public int[] getTotalResources() {
        return totalResources;
    }
    public int[] getTotalPiece() {
        return totalPiece;
    }
    public AbstractCard[][] getFullManuscript() {
        return manuscriptMatrix;
    }
    public boolean placeCardCheck(AbstractCard card, int row, int column) throws IllegalStateException {
        // Check if the cell is contained in availableCells
        Cell currentCell = new Cell(row, column);
        if (!availableCells.contains(currentCell))
            return true;
        else
            return false;
    }
    public String toString() {
        StringBuilder result = new StringBuilder();

        // Add column headings
        result.append(String.format("%-5s", "")); // Room for row numbers
        for (int col = 0; col < manuscriptMatrix[0].length; col++) {
            result.append(String.format("%-8d", col));
        }
        result.append("\n");

        // Add matrix data
        for (int row = 0; row < manuscriptMatrix.length; row++) {
            result.append(String.format("%-5d", row)); // Row number
            for (int col = 0; col < manuscriptMatrix[row].length; col++) {
                if (manuscriptMatrix[row][col] != null) {
                    result.append(String.format("%-8s", manuscriptMatrix[row][col].getID()));
                } else {
                    result.append("        "); // 8 empty spaces to keep the same format
                }
            }
            result.append("\n");
        }
        return result.toString();
    }

}













