package polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a player in the game.
 */
public class Player implements Serializable {
    private final String nickname;
    private Color color;
    private Manuscript manuscript;
    private AbstractCard secretTargetCard;
    private final List<AbstractCard> handCards;
    private int points;
    private int numTurnPlayed;
    private AbstractCard[] possibleTargetCard = new AbstractCard[2];
    private boolean isFirstPlayer;
    private boolean isOnline;
    private boolean lastPlayer;


    /**
     * Constructs a player with the given nickname.
     *
     * @param nickname The nickname of the player.
     */
    public Player(String nickname) {
        this.nickname = nickname;
        this.handCards = new ArrayList<>();
        this.secretTargetCard = null;
        this.points = 0;                // Default starting points
        this.isFirstPlayer=false;
        this.isOnline = true;
    } 

    /**
     * Gets the nickname of the player.
     *
     * @return The nickname of the player.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the starting card for the player's manuscript.
     *
     * @param startingCard The starting card.
     */
    public void setStartingCard(AbstractCard startingCard) {
        manuscript = new Manuscript(startingCard);
    }

    /**
     * Gets the starting card of the player's manuscript.
     *
     * @return The starting card.
     */
    public AbstractCard getStartingCard() {
        return manuscript.getStartingCard();
    }

    /**
     * Sets the side of the starting card in the manuscript.
     *
     * @param side The side of the starting card.
     */
    public void setStartingCardSide(boolean side) {
         manuscript.setStartingCardSide(side);
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card The card to add to the hand.
     */
    public void addCardToHand(AbstractCard card) {
        handCards.add(card);
    }

    /**
     * Removes a card from the player's hand.
     *
     * @param card The card to remove from the hand.
     */
    public void removeCardFromHand(AbstractCard card) {
        handCards.remove(card);
    }

    /**
     * Gets the list of cards in the player's hand.
     *
     * @return The list of cards in the hand.
     */
    public List<AbstractCard> getHandCards() {
        return handCards;
    }


    /**
     * Sets the possible target cards for the player.
     *
     * @param firstPossibleTargetCard  The first possible target card to set.
     * @param secondPossibleTargetCard The second possible target card to set.
     */
    public void setPossibleTargetCard(AbstractCard firstPossibleTargetCard, AbstractCard secondPossibleTargetCard) {
        this.possibleTargetCard[0] = firstPossibleTargetCard;
        this.possibleTargetCard[1] = secondPossibleTargetCard;
    }


    /**
     * Gets the possible target cards for the player.
     *
     * @return The possible target cards.
     */
    public AbstractCard[] getPossibleTargetCard() {
        return possibleTargetCard;
    }


    /**
     * Sets the secret target card for the player.
     *
     * @param secretTargetCard The secret target card to set.
     * @throws IllegalArgumentException if the secret target card does not belong to the possible target cards array.
     */
    public void setSecretTargetCard(AbstractCard secretTargetCard) {
        if (Arrays.stream(possibleTargetCard).noneMatch(card -> card.equals(secretTargetCard))) {
            throw new IllegalArgumentException("The secret target card must belong to the possible target cards array!");
        }
        this.secretTargetCard = secretTargetCard;
        this.isPlayerReady();
    }

    /**
     * Gets the secret target card of the player.
     *
     * @return The secret target card.
     */
    public AbstractCard getSecretTargetCard() {
        return this.secretTargetCard;
    }

    public boolean isPlayerReady() {
        return (secretTargetCard != null) ? true : false;
    }

    /**
     * Calculates the points gained from the secret target card.
     *
     * @return The points gained from the secret target card.
     */
    public int calculateSecretTargetPoint() {
        int gainedPointsFromSecretTarget;
        gainedPointsFromSecretTarget = this.secretTargetCard.calculatePoint();
        return gainedPointsFromSecretTarget;
    }

    /**
     * Adds points to the player's total points.
     *
     * @param gainedPoints The points to add.
     */
    public void addPoints(int gainedPoints) {
        points += gainedPoints;
    }

    /**
     * Gets the total points of the player.
     *
     * @return The total points of the player.
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * Checks if the current player is the first player.
     *
     * @return true if the current player is the first player, false otherwise.
     */
    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    /**
     * Sets whether the current player is the first player.
     *
     * @param firstPlayer true if the current player is the first player, false otherwise.
     */
    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }


    /**
     * Sets the color of the player.
     *
     * @param color The color to set.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets the color of the player.
     *
     * @return The color of the player.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the manuscript of the player.
     *
     * @return The manuscript of the player.
     */
    public Manuscript getManuscript() {
        return manuscript;
    }

    /**
     * Gets the manuscript in matrix version of the player.
     *
     * @return The manuscript of the player.
     */
    public AbstractCard[][] getMatrixManuscript() {
        return manuscript.getManuscript();
    }

    /**
     * Gets the compressed manuscript of the player.
     *
     * @return The compressed manuscript of the player.
     */
    public AbstractCard[][] getCompressedManuscript() {
        return manuscript.getCompressedManuscript();
    }

    /**
     * Returns a string representation of the player's manuscript in a compressed format
     * with original row and column indices preserved. This method delegates the call
     * to the corresponding method in the player's manuscript instance.
     *
     * @return A string representing the compressed manuscript with original indices for rows and columns.
     */
    public String getCompressedManuscriptWithIndexToString() {
        return manuscript.getCompressedManuscriptWithIndexToString();
    }


    /**
     * Prints the manuscript of the player.
     */
    public void printManuscript() {
        Manuscript.printManuscript(manuscript.getCompressedManuscript());
    }

    /**
     * Gets the available cells in the manuscript of the player.
     *
     * @return The available cells in the manuscript.
     */
    public List<Cell> getAvailableCells() {
        return manuscript.getAvailableCells();
    }

    /**
     * Places a card in the manuscript of the player.
     *
     * @param card   The card to place.
     * @param row    The row index where the card should be placed.
     * @param column The column index where the card should be placed.
     */
    public void placeCard(AbstractCard card, int row, int column) {
        manuscript.placeCard(card, row, column);
    }

    /**
     * @return true if the player is online, {@code false} otherwise.
     */
    public boolean isOnlinePlayer() {
        return isOnline;
    }

    /**
     * Change the status of the player in online/offline.
     * @param isOnline The current status of the player.
     */
    public void setOnlinePlayer(boolean isOnline) {
        this.isOnline = isOnline;
    }

    /**
     * Places a card in the manuscript of the player.
     */
    @Override
    public String toString() {
        return "Player{'" + nickname + "'}";
    }

    public AbstractCard getHandCardByHandPosition(int cardPositionToPlace) {
        return handCards.get(cardPositionToPlace);
    }

    public boolean isLastPlayer() {
        return this.lastPlayer;
    }

    public void setLastPlayer() {
        this.lastPlayer = true;
    }

    public void setManuscript(Manuscript manuscript){
        this.manuscript = manuscript;
    }
}
