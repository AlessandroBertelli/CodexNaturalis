package polimi.ingsw.view;

import polimi.ingsw.model.AbstractCard;
import polimi.ingsw.SocketAndRMI.CardsPlayed;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Interface for all the specific views.
 */
public interface View {

    /**
     * Starts the view.
     */
    void startView();

    /**
     * This method asks the client to type the IP address and port.
     * Check by the client controller if it is valid and then always the client controller send a notification.
     */
    void requestServerInformation();

    /**
     asks the user his nickname.
     * @throws IOException if an I/O error occurs.
     */
    void requestNickname() throws IOException;

    /**
     * Asks the user to choose a color from the available colors. The list is provided by Server. If is null represent in TUI all available from Color.values()
     * @param availableColor An array of available colors.
     * @throws IOException if an I/O error occurs.
     */
    void requestColor(String [] availableColor) throws IOException;

    /**
     * Asks the first user to set the players number.
     * easily checked from the client's side.
     */
    void requestPlayersNumber();

    /**
     * this method asks the player to set up some stuff before start the game:
     * set the starting card and chose his secret target.
     * @param startingCard need to be set front or back. (default front)
     * @param firstPossibleTargetCard possible player's choice
     * @param secondPossibleTargetCard possible player's choice
     */
    void requestToSetUpStartingAndSecret(AbstractCard startingCard, AbstractCard firstPossibleTargetCard, AbstractCard secondPossibleTargetCard);

    /**
     * this method asks the player which card he wants to place and where.
     */
    void requestCardToPlace();

    /**
     * this method asks the player to choose from what type of deck he is going to pick his next card.
     */
    void requestToDraw();

    /**
     * This method shows a login response.
     * @param acceptedNickname true if the nickname is accepted.
     * @param connectionEstablished true if a solid connection is accepted.
     */
    void displayLoginResponse(boolean acceptedNickname, boolean connectionEstablished);

    /**
     * This method shows a response about the client color's choice.
     * @param acceptedColor true if the selected color is accepted.
     * @param availableColor an array with the available colors. The player must choose their color from these.
     */
    void displayColorResponse(boolean acceptedColor, String[] availableColor);

    /**
     * this method prints a text to the client view.
     * @param textMessage contains the text that needs to be displayed.
     */
    void displayText(String textMessage);

    /**
     * Show all the players nickname
     * @param players List of nicknames.
     */
    void displayPlayersList(List<String> players);

    /**
     * Show the standings
     * @param scoreMap is a map where the key is the player's nickname while the value is his score.
     */
    void displayScore(Map<String, Integer> scoreMap);

    /**
     * Show the public player information regarding all the player status.
     * @param nickname containing the manuscript's owner.
     * @param manuscript containing the player's manuscript that need to be displayed.
     * @param stringManuscript parameter necessary for the print.
     */
    void displayManuscript(String nickname, AbstractCard[][] manuscript, String stringManuscript);


    /**
     * this method shows both common target cards passed as parameter.
     * @param firstTargetCard The first common target card to be shown.
     * @param secondTargetCard The second common target card to be shown.
     */
    void displayCommonTargetCard(AbstractCard firstTargetCard, AbstractCard secondTargetCard);

    /**
     * this method shows the player's hands.
     * @param handCard_1 the first card
     * @param handCard_2 the second card
     * @param handCard_3 the third card
     */
    void displayHandsCard(AbstractCard handCard_1, AbstractCard handCard_2, AbstractCard handCard_3);

    void displayDecks(ArrayList<String> cards);

    /**
     * Show the secret target card passed as parameter.
     * @param secretTargetCard The personal target card to be shown.
     * @param nickname the player's name that owned that card.
     */
    void displaySecretTargetCard(AbstractCard secretTargetCard, String nickname);

    /**
     * waiting room
     */
    void inWaiting();

    /**
     * playing room
     */
    void inGame();

    void updateChatMessage(String senderNickname, String recipientNickname, String text);

    void displayFinalScore(Map<String, Integer> scoreBoardMap, Map<String, Integer> completedObjectives);

    void updateReconnectedPlayerManuscript(String nickname, List<CardsPlayed> cardsPlayedByPlayer);
}
