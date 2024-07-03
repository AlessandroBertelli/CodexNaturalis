package polimi.ingsw.view;

import polimi.ingsw.model.AbstractCard;
import polimi.ingsw.SocketAndRMI.CardsPlayed;
import polimi.ingsw.SocketAndRMI.ServerController;
import polimi.ingsw.SocketAndRMI.message.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VirtualView implements View{

    private final ServerController serverController;

    /**
     * Constructs a VirtualView with the specified ServerController.
     *
     * @param serverController The server controller to communicate with
     */
    public VirtualView(ServerController serverController) {
        this.serverController = serverController;
    }

    /**
     * Starts the view.
     * This method is overridden from the View interface.
     */
    @Override
    public void startView() {
        // Implementation specific to starting the view
    }

    /**
     * Requests server information.
     * This method is overridden from the View interface.
     */
    @Override
    public void requestServerInformation() {
        // Implementation specific to requesting server information
    }

    /**
     * Requests a nickname from the user.
     * This method is overridden from the View interface.
     */
    @Override
    public void requestNickname() {
        // Implementation specific to requesting a nickname
    }

    /**
     * Requests a color choice from the user among available options.
     *
     * @param availableColor An array of available colors to choose from
     */
    public void requestColor(String[] availableColor) {
        // Implementation specific to requesting a color choice
    }

    /**
     * Requests the number of players.
     * Sends a PlayersNumberRequest message to the server controller.
     */
    @Override
    public void requestPlayersNumber() {
        serverController.send(new PlayersNumberRequest());
    }

    /**
     * Displays the result of a login attempt.
     *
     * @param validNick            Whether the nickname provided is valid
     * @param connectionEstablished Whether the connection to the server is established
     */
    public void displayLoginResponse(boolean validNick, boolean connectionEstablished) {
        serverController.send(new LoginResponseMessage(validNick, connectionEstablished));
    }

    /**
     * Displays the response to a color choice request.
     *
     * @param acceptedColor Whether the chosen color is accepted
     * @param availableColor An array of available colors
     */
    public void displayColorResponse(boolean acceptedColor, String[] availableColor) {
        serverController.send(new ColorResponseMessage(acceptedColor, availableColor));
    }

    /**
     * Requests setup for starting and secret target cards.
     *
     * @param startingCard         The starting card to set up
     * @param firstPossibleTargetCard The first possible target card to set up
     * @param secondPossibleTargetCard The second possible target card to set up
     */
    @Override
    public void requestToSetUpStartingAndSecret(AbstractCard startingCard, AbstractCard firstPossibleTargetCard, AbstractCard secondPossibleTargetCard) {
        serverController.send(new SecretTargetAndStartingRequest(startingCard, firstPossibleTargetCard, secondPossibleTargetCard));
    }

    /**
     * Displays a generic text message.
     *
     * @param textMessage The text message to display
     */
    @Override
    public void displayText(String textMessage) {
        serverController.send(new GenericMessage(textMessage));
    }

    /**
     * Displays the list of players.
     *
     * @param players The list of players to display
     */
    @Override
    public void displayPlayersList(List<String> players) {
        serverController.send(new PlayersListMessage(players));
    }

    /**
     * Displays the secret target card for a player.
     *
     * @param personalTargetCard The secret target card to display
     * @param nickname           The nickname of the player
     */
    @Override
    public void displaySecretTargetCard(AbstractCard personalTargetCard, String nickname) {
        serverController.send(new SecretTargetCardMessage(personalTargetCard, nickname));
    }

    /**
     * Displays the three hand cards.
     *
     * @param handCard_1 The first hand card
     * @param handCard_2 The second hand card
     * @param handCard_3 The third hand card
     */
    public void displayHandsCard(AbstractCard handCard_1, AbstractCard handCard_2, AbstractCard handCard_3) {
        serverController.send(new HandsResponseMessage(handCard_1, handCard_2, handCard_3));
    }

    /**
     * Displays the decks of cards.
     *
     * @param cards The list of cards in the decks
     */
    public void displayDecks(ArrayList<String> cards) {
        serverController.send(new ShowDecksMessage(cards));
    }

    /**
     * Updates a chat message.
     *
     * @param senderNickname   The nickname of the sender
     * @param recipientNickname The nickname of the recipient
     * @param message          The message content
     */
    @Override
    public void updateChatMessage(String senderNickname, String recipientNickname, String message) {
        serverController.send(new ChatMessage(senderNickname, recipientNickname, message));
    }

    /**
     * Displays the score board.
     *
     * @param scoreMap The map containing player nicknames and their respective scores
     */
    @Override
    public void displayScore(Map<String, Integer> scoreMap) {
        serverController.send(new ScoreBoardMessage(scoreMap));
    }

    /**
     * Signals that the view is in a waiting state.
     */
    @Override
    public void inWaiting() {
        serverController.send(new WaitingRoomRequest());
    }

    /**
     * Signals that the view is in a game state.
     */
    @Override
    public void inGame() {
        serverController.send(new GameRoomRequest());
    }

    /**
     * Displays a player's manuscript.
     *
     * @param nickname         The nickname of the player
     * @param manuscript       The manuscript of cards
     * @param stringManuscript The string representation of the manuscript
     */
    @Override
    public void displayManuscript(String nickname, AbstractCard[][] manuscript, String stringManuscript) {
        serverController.send(new PlayerInformation(nickname, manuscript, stringManuscript));
    }

    /**
     * Requests a card to place on the board.
     */
    @Override
    public void requestCardToPlace() {
        serverController.send(new PlaceCardRequest());
    }

    /**
     * Displays the common target cards.
     *
     * @param firstTargetCard  The first common target card
     * @param secondTargetCard The second common target card
     */
    @Override
    public void displayCommonTargetCard(AbstractCard firstTargetCard, AbstractCard secondTargetCard) {
        serverController.send(new CommonTargetCardMessage(firstTargetCard, secondTargetCard));
    }

    /**
     * Requests to draw a card from one of the available decks.
     */
    @Override
    public void requestToDraw() {
        serverController.send(new DrawCardRequest());
    }

    /**
     * Displays the final score board.
     *
     * @param scoreBoardMap      The map containing player nicknames and their final scores
     * @param completedObjectives The map containing player nicknames and the number of completed objectives
     */
    public void displayFinalScore(Map<String, Integer> scoreBoardMap, Map<String, Integer> completedObjectives) {
        serverController.send(new FinalScoreBoardMessage(scoreBoardMap, completedObjectives));
    }

    /**
     * Updates the manuscript of a reconnected player.
     *
     * @param nickname           The nickname of the reconnected player
     * @param cardsPlayedByPlayer The list of cards played by the player
     */
    @Override
    public void updateReconnectedPlayerManuscript(String nickname, List<CardsPlayed> cardsPlayedByPlayer) {
        serverController.send(new ReconnectedManuscriptMessage(nickname, cardsPlayedByPlayer));
    }
}
