package polimi.ingsw.SocketAndRMI;

import polimi.ingsw.SocketAndRMI.message.*;
import polimi.ingsw.SocketAndRMI.rmi.RmiClientImpl;
import java.io.IOException;
import polimi.ingsw.SocketAndRMI.socket.SktClient;
import polimi.ingsw.view.View;

public class ClientController {

    private View view;
    private Client client;
    private String nickname;

    /**
     * Constructs client manager.
     * @param view The view of the client.
     */
    public ClientController(View view) {
        this.view = view;
    }

    /**
     * Updates the server information and initializes the appropriate client connection.
     * If {@code rmiConnection} is false, initializes a SocketClient connection,
     * otherwise initializes a RmiClientImpl connection.
     *
     * @param ipAddress     The IP address of the server.
     * @param port          The port number of the server.
     * @param rmiConnection {@code true} if RMI connection should be used, {@code false} for socket connection.
     */
    public void onUpdateServerInfo(String ipAddress, int port, boolean rmiConnection) {
        if (!rmiConnection) {
            try {
                this.client = new SktClient(this, ipAddress, port);
                client.read();
                view.requestNickname();
            } catch (IOException e) {
                handleSocketConnectionError(e);
            }
        } else {
            try {
                this.client = new RmiClientImpl(this, ipAddress, port);
                view.requestNickname();
            } catch (IOException e) {
                handleRmiConnectionError(e);
            }
        }
    }

    /**
     * Handles the error when unable to connect with socket connection.
     *
     * @param e The IOException thrown when attempting to establish the socket connection.
     */
    private void handleSocketConnectionError(IOException e) {
        view.displayText("Unable to connect with socket connection.");
        view.startView();
    }

    /**
     * Handles the error when unable to connect with RMI connection.
     *
     * @param e The IOException thrown when attempting to establish the RMI connection.
     */
    private void handleRmiConnectionError(IOException e) {
        view.displayText("Unable to connect with RMI connection.");
        view.startView();
    }

    /**
     * Send a message containing the nickname.
     * @param nickname The nickname of the client.
     */
    public void onUpdatePlayerNick(String nickname) {
        this.nickname = nickname;
        client.send(new LoginRequestMessage(this.nickname));
    }

    /**
     * Game automatically disconnects client if player doesn't complete login process.
     */
    public void disconnection() {
        client.disconnect();
    }

    /**
     * Send a message to the server with the game player's number selected from the first client.
     * @param playersNumber The number of the player.
     */
    public void onUpdateChosenNumberOfPlayer(int playersNumber) {
        client.send(new PlayersNumberResponse(this.nickname, playersNumber));
    }

    /**
     * Method to handle color update.
     * Sends a color change request to the client.
     *
     * @param color The new color to set.
     */
    public void onUpdateColor(String color) {
        client.send(new ColorRequestMessage(this.nickname, color));
    }

    /**
     * Method to update secret target and starting choice.
     * Sends a message to the client with the updated choices.
     *
     * @param choice The secret target choice to update.
     * @param side The starting side choice to update.
     */
    public void onUpdateSecretTargetAndStarting(String choice, boolean side) {
        client.send(new SecretAndStartingResponseMessage(this.nickname, choice, side));
    }

    /**
     * Method to update and play a card on a specified board position.
     * Sends a message to the client with the updated card placement details.
     *
     * @param x The x-coordinate of the board position to place the card.
     * @param y The y-coordinate of the board position to place the card.
     * @param choice The card choice to play.
     * @param side The side (or orientation) to place the card (true for one side, false for the other).
     */
    public void onUpdatePlayCard(int x, int y, int choice, boolean side) {
        client.send(new PlaceCardResponse(this.nickname, choice, x, y, side));
    }

    /**
     * Method to update and request to draw a card.
     * Sends a message to the client with the card draw request.
     *
     * @param choice The choice related to the card draw request.
     */
    public void onUpdateToDraw(int choice) {
        client.send((new DrawCardResponse(this.nickname,choice)));
    }

    /**
     * Updates and sends a chat message from one user to another.
     * Sends a message to the client with the chat details.
     *
     * @param senderNickname The nickname of the sender of the chat message.
     * @param recipientNickname The nickname of the recipient of the chat message.
     * @param text The text content of the chat message.
     */
    public void updateChatMessage(String senderNickname, String recipientNickname, String text){
        client.send((new ChatMessage(senderNickname, recipientNickname, text)));
    }

    /**
     * Updates the client view based on the type of message received.
     *
     * @param message The message containing instructions for updating the view.
     */
    public void updateCorrespondingClient(Message message) {
        switch (message.getMessageType()) {
            case PLAYERS_NUMBER_REQUEST -> {
                view.requestPlayersNumber();
            }
            case LOGIN_REPLY -> {
                LoginResponseMessage loginReplyMessage = (LoginResponseMessage) message;
                view.displayLoginResponse(loginReplyMessage.isNicknameAccepted(), loginReplyMessage.isConnectionEstablished());
            }
            case COLOR_RESPONSE -> {
                ColorResponseMessage colorResponseMessage = (ColorResponseMessage) message;
                view.displayColorResponse(colorResponseMessage.isColorAccepted(), colorResponseMessage.getAvailableColor());
            }
            case SECRET_STARTING_REQUEST -> {
                SecretTargetAndStartingRequest targetMessage = (SecretTargetAndStartingRequest) message;
                view.requestToSetUpStartingAndSecret(targetMessage.getStartingCard(),targetMessage.getFirstPossibleTargetCard(),targetMessage.getSecondPossibleTargetCard());
            }
            case PLACE_CARD_REQUEST -> {
                view.requestCardToPlace();
            }
            case DRAW_CARD_REQUEST -> {
                DrawCardRequest drawCardRequest = (DrawCardRequest) message;
                view.requestToDraw();
            }
            case PLAYER_INFORMATION -> {
                PlayerInformation playerMessage = (PlayerInformation) message;
                view.displayManuscript(
                        playerMessage.getNickname(),
                        playerMessage.getManuscript(),
                        playerMessage.getStringManuscript()
                );
            }
            case COMMON_TARGET_CARD -> {
                CommonTargetCardMessage commonTargetCardMessage = (CommonTargetCardMessage) message;
                view.displayCommonTargetCard(commonTargetCardMessage.getFirstCommonTargetCard(), commonTargetCardMessage.getSecondCommonTargetCard());
            }
            case HANDS_RESPONSE_CARD -> {
                HandsResponseMessage HandsResponseMessage = (HandsResponseMessage) message;
                view.displayHandsCard(HandsResponseMessage.getFirstHandCard(), HandsResponseMessage.getSecondHandCard(), HandsResponseMessage.getThirdHandCard());
            }
            case DISPLAY_DECKS -> {
                ShowDecksMessage DeckMessage = (ShowDecksMessage) message;
                view.displayDecks(DeckMessage.getCards());
            }
            case SECRET_TARGET_CARD -> {
                SecretTargetCardMessage secretTargetCardMessage = (SecretTargetCardMessage) message;
                view.displaySecretTargetCard(secretTargetCardMessage.getSecretTargetCard(), secretTargetCardMessage.getCardOwner());
            }
            case GENERIC_MESSAGE -> {
                GenericMessage genericMessage = (GenericMessage) message;
                view.displayText(genericMessage.getGenericMessage());
            }
            case SCORE_BOARD -> {
                ScoreBoardMessage scoreBoardMessage = (ScoreBoardMessage) message;
                view.displayScore(scoreBoardMessage.getScoreBoardMap());
            }
            case PLAYERS_LIST -> {
                PlayersListMessage playersListMessage = (PlayersListMessage) message;
                view.displayPlayersList(playersListMessage.getPlayers());
            }
            case CHAT_MESSAGE -> {
                ChatMessage chatMessage = (ChatMessage) message;
                view.updateChatMessage(chatMessage.getSenderNickname(), chatMessage.getRecipientNickname(), chatMessage.getText());
            }
            case ERROR_MESSAGE -> {
                ErrorMessage errorMessage = (ErrorMessage) message;
                view.displayText(errorMessage.getErrorDescription());
            }
            case FINAL_SCORE_BOARD -> {
                FinalScoreBoardMessage finalScoreBoardMessage = (FinalScoreBoardMessage) message;
                view.displayFinalScore(finalScoreBoardMessage.getScoreBoardMap(), finalScoreBoardMessage.getCompletedObjectives());
            }
            case RECONNECTED_MANUSCRIPT_MESSAGE -> {
                ReconnectedManuscriptMessage reconnectedManuscriptMessage = (ReconnectedManuscriptMessage) message;
                view.updateReconnectedPlayerManuscript(reconnectedManuscriptMessage.getNickname(), reconnectedManuscriptMessage.getCardsPlayedByPlayer());
            }
            case WAITING_ROOM_REQUEST -> {
                view.inWaiting();
            }
            case GAME_ROOM_REQUEST -> {
                view.inGame();
            }
            default -> {
                System.out.println("ERROR: Message unhandled! Type of message: " + message.getMessageType());
            }
        }
    }

    /**
     * @param ipAddress
     * @return true if the ipAddress is valid.
     */
    public static boolean checkIPAddress(String ipAddress) {
        if (ipAddress == null)
            return false;
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        return ipAddress.matches(PATTERN);
    }

    /**
     * @param port
     * @return true if the por number is valid.
     */
    public static boolean checkPort(int port) {
        return (port >= 1024 && port < 65536);
    }

    public String getNickname() {
        return nickname;
    }

}
