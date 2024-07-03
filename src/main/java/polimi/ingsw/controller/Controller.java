package polimi.ingsw.controller;


import polimi.ingsw.exception.LackResourceException;
import polimi.ingsw.exception.NotPlayerTurnException;
import polimi.ingsw.model.*;

import polimi.ingsw.model.strategy.*;
import polimi.ingsw.SocketAndRMI.CardsPlayed;
import polimi.ingsw.SocketAndRMI.Server;
import polimi.ingsw.SocketAndRMI.message.*;
import polimi.ingsw.view.VirtualView;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * This class represents the controller for the game.
 * It manages the game state, active players, virtual views, and game suspension.
 */
public class Controller implements Serializable {

    /**
     * The instance of the game model.
     */
    private Game game;

    /**
     * The state of the game.
     */
    private GameState gameState;

    /**
     * The name of the current active player.
     */
    private String activePlayer;

    /**
     * A mapping of player nicknames to their corresponding virtual views.
     */
    private transient Map<String, VirtualView> virtualViewMap;    // Map of the virtual views.

    /**
     * A list of all cards played during the game and their info.
     * Used to rebuild the manuscript after a client re-joins the game
     * after a disconnection.
     */
    private List<CardsPlayed> cardsPlayedHistory;

    /**
     * Indicates whether the game is currently suspended.
     */
    private boolean gameSuspended;

    /**
     * Indicates whether the color has been set.
     */
    private boolean colorSet = false;

    /**
     * Indicates the first player to reach 20 points. It's null at the beginning of the game.
     */
    private Player firstPlayerToReach20Points;

    /**
     * Indicates the first player to terminate the cards in a generic deck. It's null at the beginning of the game.
     */
    private Player firstPlayerToFinishDeck;
    @Serial
    private static final long serialVersionUID = 34793873212834L;


    /**
     * Constructor fot the Controller.
     *  Initialization is made by a private method.
     */
    public Controller() {
        initializeController();
    }


    /**
     * It initializes the Controller.
     */
    private void initializeController() {
            this.game = Game.getNewGame();
            this.virtualViewMap = new HashMap<>();
            this.gameSuspended = false;
            this.firstPlayerToFinishDeck = null;
            this.firstPlayerToReach20Points = null;
            this.cardsPlayedHistory = new ArrayList<>();
            setGameState(GameState.STARTING);
    }

    /**
     * It receives message and do an action based on the game state and not on the message type that remains unknown.
     * @param message The message that is sent by the client.
     */
    public void onMessageSwith(Message message) {
      if (isGameSuspended()) {
            System.out.println("Can't receive message since the game is suspended.");
            return;
        }
      System.out.println("GameState: " + gameState.toString());
      if (gameState == GameState.STARTING) {
            lobbySwitch(message);
      } else if (gameState == GameState.PLAY_CARD ||
                gameState == GameState.DRAW_CARD ||
                gameState == GameState.LAST_LAP ||
                gameState == GameState.ENDING) {
            turnController(message);
      }
    }

    /**
     * It receives message and do an action based on the type pf message.
     * It is invoked by the method OnMessageSwitch
     * @param message The message received from the client. The message must be a "lobby" type message.
     */
    private void lobbySwitch(Message message) {
        switch (message.getMessageType()) {
            case PLAYERSNUMBER_REPLY:
                PlayersNumberResponse playersNumberResponse = (PlayersNumberResponse) message;
                NumPlayersSetUp(playersNumberResponse.getPlayersNumber());
                break;
            case COLOR_REQUEST:
                ColorRequestMessage colorRequestMessage = (ColorRequestMessage) message;
                ColorSetUp(colorRequestMessage.getNickname(), colorRequestMessage.getColor());
                break;
            case SECRET_STARTING_RESPONSE:
                SecretAndStartingResponseMessage secretStartingResponseMessage = (SecretAndStartingResponseMessage) message;
                handleSecretAndStarting(secretStartingResponseMessage.getNickname(), secretStartingResponseMessage.getChoice(), secretStartingResponseMessage.getSide());
                break;
            default:
                System.out.println("ERROR MESSAGE (correct: PLAYERS_NUMBER_REPLY or COLOR_REQUEST or SECRET/STARTING, actual: " + message.getMessageType().toString() + ")");
                break;
        }
    }
    /**
     * Handles the login of a new player.
     * @param nickname    The nickname chosen by the new player.
     * @param virtualView The virtual view associated with the new player.
     */
    public void newLoginSetUp(String nickname, VirtualView virtualView) {
        // Adds the virtual view to the list of connected players.
        addVirtualView(nickname, virtualView);
        // Adds the player to the game using the provided nickname.
        game.addPlayerByNickname(nickname);
        // Starting player and set his starting card and possible target card
        game.startingPlayer(nickname);
    }

    /**
     * Sets the number of players for the game, updates the game state, and notifies
     * players how many players are connected.
     * @param numPlayer the number of players to set for the game
     */
    public void NumPlayersSetUp(int numPlayer) {
        game.setNumPlayer(numPlayer);
        System.out.println("This game will have " + game.getNumPlayer() + " players.");
        if (virtualViewMap.size() == numPlayer) {
            transmissionMessage("Players connected: " + virtualViewMap.size() + "/" + game.getNumPlayer());
        }
    }

    /**
     * Handles the selection of a color by a player. Sets the chosen color for the player identified by the provided nickname.
     * Additionally, it performs various actions based on the game state and the status of color selections by other players.
     * @param nickname The nickname of the player selecting the color.
     * @param color    The color chosen by the player.
     */
    public void ColorSetUp(String nickname, String color) {
        try {
            // Sets the chosen color for the player.
            Player player = game.getPlayerByNickname(nickname);
            game.setPlayerColor(player, color);
            // Color accepted
            System.out.println("Player \"" + nickname + "\" has chosen the color: " + color);
            // Notifies the player's virtual view about the successful color selection.
            virtualViewMap.get(nickname).displayColorResponse(true, game.getAvailableColorString());


            // If only one player is connected, prompts for the number of players.
            if (virtualViewMap.size() == 1) {
                virtualViewMap.get(nickname).requestPlayersNumber();
            } else if ((virtualViewMap.size() != 1) && (game.getNumPlayer() == 0) && (getNicknamePlayers().get(0).equalsIgnoreCase(nickname)) ) {
                virtualViewMap.get(getNicknamePlayers().get(0)).requestPlayersNumber();
            }

            // If more than one player is connected, transmits the message indicating the number of players.

            AbstractCard[] possibleSecretTargetCard = player.getPossibleTargetCard();
            getVirtualViewMap().get(nickname).requestToSetUpStartingAndSecret(player.getStartingCard(), possibleSecretTargetCard[0],possibleSecretTargetCard[1]);
        } catch (IllegalArgumentException e) {
            // If an illegal argument exception is caught, shows a color response indicating failure.
            virtualViewMap.get(nickname).displayColorResponse(false, game.getAvailableColorString());
        }
    }

    /**
     * Handles the selection of starting card side and secret target card for a player,
     * updates game state accordingly, and manages game initialization.
     * @param nickname the nickname of the player making the selections
     * @param choice the ID of the secret target card chosen by the player
     * @param side the side (front or back) chosen for the starting card of the player
     */
    private void handleSecretAndStarting(String nickname, String choice, boolean side) {
        try {
            // Sets the chosen color for the player.
            Player player = game.getPlayerByNickname(nickname);

            game.setPlayerStartingCardSide(player, side);
            cardsPlayedHistory.add(new CardsPlayed(nickname, player.getStartingCard().getID(), new Cell(40, 40), side));

            try {
                for (AbstractCard card : game.getPossibleTargetCard(player)) {
                    if (card.getID().equalsIgnoreCase(choice)) {
                        game.setPlayerSecretTargetCard(player, card);
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            System.out.println("Player \"" + nickname + "\" has chosen : " + choice + " card and " +
                    ((side) ? ("front") : ("back")) + " side of starting card.");

            if (virtualViewMap.size() != 1) {
                transmissionMessage("Players connected: " + virtualViewMap.size() +((game.getNumPlayer()==0)?(""):("/" + game.getNumPlayer())));
            }

            // Switches the player's virtual view to the  room.
            virtualViewMap.get(nickname).inWaiting();


            // If all players are connected and all have chosen their colors,it starts.
            if ((getOnlinePlayers().size() == game.getNumPlayer()) && game.areAllColorSet() && game.areAllSecretTargetSet()) {
                transmissionMessage("All players are connected.");
                start();


            } else if ((getOnlinePlayers().size() == game.getNumPlayer()) && !game.areAllColorSet()) {
                // If all players are connected but not all have chosen their colors or starting/secret card set, transmits a message indicating the situation.
                transmissionMessage("All players are connected. Waiting for other players .");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("An exception occurred.");
        }
    }
    /**
     * Starts the game by initializing player views, setting game state to starting,
     * beginning the game logic, and prompting the active player to place a card.
     * Displays initial game information and notifies players of the starting player's turn.
     */
    private void start() {
        for (VirtualView virtualView : virtualViewMap.values()) {
            virtualView.inGame();
        }

        setGameState(GameState.STARTING);
        game.startingGame();
        System.out.println("Starting game with " + game.getPlayers().size() + " players.");
        setActivePlayer(game.getActivePlayerNickname());
        game.getPlayerByNickname(getActivePlayer()).setFirstPlayer(true);

        displayPlayersNicknames();

        displayGame();
        //showScoreBoard(sortScoreMap());
        transmissionMessage("It's " + getActivePlayer() + "'s turn");

        setGameState(GameState.PLAY_CARD);
        askToPlaceCard();
    }


    /**
     * It receives message and do an action based on the type pf message.
     * It is invoked by the method OnMessageSwitch
     * @param message The message received from the client. The message must be a "gameplay" type message.
     */
    private void turnController(Message message) {
        VirtualView currentVirtualView = virtualViewMap.get(getActivePlayer());
        switch (message.getMessageType()) {
            case CHAT_MESSAGE:
                forwardMessage((ChatMessage) message);
                break;
            case PLACE_CARD_RESPONSE:
                controlPlaceCardResponse(message, currentVirtualView);
                break;
            case DRAW_CARD_RESPONSE:
                controlDrawCardResponse(message, currentVirtualView);
                break;
            default:
                System.out.println("ERROR: Wrong message type (expected: PLACE_CARD_REPLY, actual: " + message.getMessageType().toString() + ")");
        }
    }

    /**
     * Forwards a chat message to a specif player or all players in the game.
     * If the message is addressed to "General", it broadcasts the message to all players
     * except the sender.
     * @param message the chat message to be forwarded
     */
    private void forwardMessage(ChatMessage message) {
        if(!message.getRecipientNickname().equals("General")){
            VirtualView virtualView = virtualViewMap.get(message.getRecipientNickname());
            if (virtualView != null) {
                virtualView.updateChatMessage(message.getSenderNickname(), message.getRecipientNickname(), message.getText());
            }
        }
        else{
            for(Player player : game.getPlayers()){
                if(!player.getNickname().equals(message.getSenderNickname())){
                    VirtualView virtualView = virtualViewMap.get(player.getNickname());
                    if (virtualView != null) {
                        virtualView.updateChatMessage(message.getSenderNickname(), message.getRecipientNickname(), message.getText());
                    }
                }
            }
        }
    }

    /**
     * Handles the response message for placing a card in the manuscript.
     * Only if the message comes from the active player, it processes the card placement.
     * If an error occurs during placement, an appropriate message is displayed
     * and the active player is prompted to try again.
     * @param message the message containing the card placement response
     * @param currentVirtualView the virtual view associated with the current player
     * Throws: IllegalStateException if the game state does not allow card placement
     *         NotPlayerTurnException if it's not the player's turn to place a card
     *         LackResourceException if the player lacks the necessary resources to place the card
     *         IllegalArgumentException if the provided card or placement is invalid
     */
    private void controlPlaceCardResponse(Message message, VirtualView currentVirtualView) {
        if (message.getNickname().equalsIgnoreCase(getActivePlayer())) {
            PlaceCardResponse messageReceived = (PlaceCardResponse) message;
            Player activePlayer = game.getPlayerByNickname(getActivePlayer());

            try {
                placeCard(messageReceived, activePlayer);
                controlPostCardPlacement(activePlayer, currentVirtualView);
            } catch (IllegalStateException | NotPlayerTurnException | LackResourceException | IllegalArgumentException e) {
                currentVirtualView.displayText(e.getMessage());
                askToPlaceCard();
            }
        } else {
            System.out.println("ERROR: Message from the wrong client (expected: " + getActivePlayer() + ", actual: " + message.getNickname() + ")");
        }
    }

    /**
     * Places a card on the game board according to the received message and updates game state.
     * @param messageReceived the response message indicating where and which card to place
     * @param activePlayer the player who is placing the card
     * throws: IllegalArgumentException if the specified card position is invalid or out of bounds
     *         IllegalStateException if the game state does not allow card placement
     *         LackResourceException if the player lacks the necessary resources to place the card
     */
    private void placeCard(PlaceCardResponse messageReceived, Player activePlayer) {
        int row = messageReceived.getRow();
        int column = messageReceived.getColumn();
        AbstractCard cardToPlace = activePlayer.getHandCardByHandPosition(messageReceived.getCardPositionToPlace());
        cardToPlace.setFront(messageReceived.getSide());
        game.playCard(activePlayer, cardToPlace, row, column);
        cardsPlayedHistory.add(new CardsPlayed(activePlayer.getNickname(), cardToPlace.getID(), new Cell(row, column), cardToPlace.isFront()));

        Manuscript.getAvailableResource(activePlayer.getCompressedManuscript());
        game.removePlayerCardFromHand(activePlayer, cardToPlace);
    }

    /**
     * Handles the post-placement actions after a player has placed a card in the game.
     * This includes showing game information, checking conditions for game state transitions,
     * and triggering appropriate actions based on player points and deck status.
     * @param activePlayer the player who has just placed a card
     * @param currentVirtualView the virtual view associated with the current game state
     */
    private void controlPostCardPlacement(Player activePlayer, VirtualView currentVirtualView) {
        displayGame();

        if (activePlayer.getPoints() >= 20 && !game.isAnyDeckEmpty()) {
            if (firstPlayerToReach20Points == null ) {
                firstPlayerToReach20Points = activePlayer;
                transmissionMessage(firstPlayerToReach20Points + "reaches 20 points: last lap!!!");
                setGameState(GameState.DRAW_CARD);
                youMustDraw(activePlayer);
            }else if (firstPlayerToReach20Points.equals(activePlayer)) {
                endGame();
            }else {
                setGameState(GameState.DRAW_CARD);
                youMustDraw(activePlayer);
            }
        }else if (game.isAnyDeckEmpty()) {
            if (firstPlayerToFinishDeck == null && firstPlayerToReach20Points ==null ) {
                firstPlayerToFinishDeck = activePlayer;
                transmissionMessage("there is an empty deck: last lap");
                nextTurn();
            } else if (firstPlayerToFinishDeck == null && firstPlayerToReach20Points!=null ) {
                firstPlayerToFinishDeck = firstPlayerToReach20Points;
                transmissionMessage("there is an empty deck: last lap continue without drawing");
                nextTurn();
            } else if (firstPlayerToFinishDeck.equals(activePlayer)) {
                endGame();
            } else {
              nextTurn();
            }

        }else {
            setGameState(GameState.DRAW_CARD);
            youMustDraw(activePlayer);
        }
    }
    /**
     * Handles the response message for drawing a card in the game.
     * If the message comes from the active player, it attempts to draw the specified card,
     * updates game state, and starts a new turn.
     * @param message the message containing the draw card response
     * @param currentVirtualView the virtual view associated with the current game state
     */
    private void controlDrawCardResponse(Message message, VirtualView currentVirtualView) {
        if (message.getNickname().equalsIgnoreCase(getActivePlayer())) {
            try {
                DrawCardResponse messageReceived = (DrawCardResponse) message;
                Player player = game.getPlayerByNickname(getActivePlayer());
                game.drawingCard(player, messageReceived.getDrawnCard());
                displayGame();
                nextTurn();
            } catch (IllegalStateException e) {
                currentVirtualView.displayText(e.getMessage());
                youMustDraw(game.getPlayerByNickname(getActivePlayer()));
            }
        }
    }

    /**
     * Notifies the specified player that they must draw a card, sends a notification message,
     * and prompts the player to choose the deck from which to draw the card.
     * @param player the player who needs to draw a card
     */
    private void youMustDraw(Player player){
        drawnCardNotificationMessage(player.getNickname() + " has to draw a card");
        VirtualView virtualView = virtualViewMap.get(getActivePlayer());
        System.out.println(getActivePlayer() + " is required to choose the deck from where he is gonna draw his card");
        virtualView.requestToDraw();
    }
    /**
     * Evolves the turn of the player, passing on the next one and notifying the players.
     */
    private void nextTurn() {
        if (!isGameSuspended()) {
            if(firstPlayerToReach20Points != null && getNextPlayer().equalsIgnoreCase(firstPlayerToReach20Points.getNickname())){
                try {
                    endGame();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ( firstPlayerToFinishDeck != null && getNextPlayer().equalsIgnoreCase(firstPlayerToFinishDeck.getNickname())) {
                try {
                    endGame();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else{
                //showScoreBoard(sortScoreMap());
                setActivePlayer(getNextPlayer());
                transmissionMessage(getActivePlayer() + "'s turn.");

                setGameState(GameState.PLAY_CARD);
                askToPlaceCard();
            }
        }
    }
    /**
     * Ends the current game session, calculates final scores and objectives,
     * and displays the final results to all players.
     * Terminates the game session after displaying the results.
     */
    private void endGame() {
        setGameState(GameState.ENDING);
        Map<String, Integer> completedObjectives = calculateStrategies();
        transmissionMessage("Last lap completed!");
        showFinalResult(finalSortScoreMap(completedObjectives), completedObjectives);
        closeGame();
    }

    /**
     * Closes the current game session, resets the game instance, initializes the controller,
     * sends a transmission message to all players, and concludes the game session gracefully.
     */
    private void closeGame() {
        Game.resetGameInstance();
        initializeController();
        //new Persistence().delete();
        transmissionMessage("Game over! Thanks for playing with us.");
        //System.exit(0);
    }


    /**
     * Returns the final sorted map in descending order, like any leaderboard.
     * It is sorted by two criteria: points first, and if points are even then it compares the number
     * of completed objectives.
     */
    private Map<String, Integer> finalSortScoreMap(Map<String, Integer> completedObjectives){
        Map<String, Integer> finalScoreMap = sortScoreMap();
        List<Map.Entry<String, Integer>> bothList = new ArrayList<>(finalScoreMap.entrySet());

        bothList.sort((e1, e2) ->{
            int pointComparison = e2.getValue().compareTo(e1.getValue());
            if(pointComparison != 0){ return pointComparison; }
            else{ return completedObjectives.get(e2.getKey()).compareTo(completedObjectives.get(e1.getKey())); }
        });

        Map<String, Integer> finalSortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> e : bothList){
            finalSortedMap.put(e.getKey(), e.getValue());
        }

        return finalSortedMap;
    }

    /**
     * Calculates the final scores for all players.
     * Each player's score is determined based on their manuscript and target cards.
     * The scores are then sorted in descending order.
     *It returns the map contains the completed objectives by each player.
     */
    private Map<String, Integer> calculateStrategies() {
        Map<String, Integer> completedObjectivesMap = new HashMap<>();
        int completedObjectives;

        for (Player player : game.getPlayers()) {
            completedObjectives = 0;
            AbstractCard[] targetCards = getTargetCards(player);

            for (AbstractCard card : targetCards) {
                int cardId = Integer.parseInt(card.getID());
                PointStrategy strategy = getPointStrategy(cardId);
                if (strategy != null) {
                    int points = strategy.calculatePoint(player.getManuscript(), card);
                    if (points != 0) {
                        completedObjectives++;
                    }
                    player.addPoints(points);
                }
            }
            completedObjectivesMap.put(player.getNickname(), completedObjectives);
        }

        for (Player player : game.getPlayers()) {
            System.out.println(player.getPoints());
        }
        return completedObjectivesMap;
    }

    /**
     * Restores the state of the current game to match the state of a previous game.
     * This method copies the game state, active player, and game suspension status from
     * the provided previous game controller to the current controller, and restores the
     * previous game's state within the current game.
     * @param previousGame The controller of the previous game whose state is to be restored.
     */
    private void reinstateBackGame(Controller previousGame) {
        // Copying game state, active player, and game suspension status
        this.gameState = previousGame.gameState;
        this.activePlayer = previousGame.activePlayer;
        this.gameSuspended = previousGame.gameSuspended;

        // Displaying information for debugging purposes
        System.out.println("Game State: " + this.gameState + ", active player: " + this.activePlayer + ", suspended: " + this.gameSuspended);

        // Restoring the previous game's state within the current game
        this.game.restorePreviousGame(previousGame.game);
    }

    /**
     * Handles the reconnection of a client that was disconnected.
     * If there are at least two online players, the game can continue.
     * @param nickname    The nickname of the reconnected player.
     * @param virtualView The virtual view of the reconnected player.
     */
    public void Reconnection(String nickname, VirtualView virtualView) {
        addVirtualView(nickname, virtualView);
        game.getPlayerByNickname(nickname).setOnlinePlayer(true);
        transmissionMessage("Player reconnected: " + nickname);
        virtualView.inGame();
        displayPlayersNicknames();
        displayGame();
        updateReconnectedPlayerManuscript(nickname, getCardPlaysByPlayer(nickname));
        if (virtualViewMap.size() == 2) {
            gameSuspended = false;
            nextTurn();
        }
    }

    /**
     * Sends the list of cards played by a specific player
     * to rebuild (re-display) their manuscript.
     */
    private void updateReconnectedPlayerManuscript(String nickname, List<CardsPlayed> cardsPlayedByPlayer){
        VirtualView virtualView = virtualViewMap.get(nickname);
        if (virtualView != null) {
            virtualView.updateReconnectedPlayerManuscript(nickname, cardsPlayedByPlayer);
        }
    }

    /**
     * Checks if the nickname passed as parameter is a valid
     * @param nickname    The nickname that will be checked.
     * @param virtualView The virtual view of the client.
     * It returns true if "nickname" is a valid.
     */
    public boolean checkNickname(String nickname, VirtualView virtualView) {
        if (nickname == null || nickname.isEmpty() || nickname.equalsIgnoreCase(Server.SERVER_NAME) || nickname.length() >= 21) {
            if(nickname != null && nickname.length() >= 21){ virtualView.displayText("This nickname is too long!"); }
            else{ virtualView.displayText("nickname not allowed!"); }
            return false;
        }
        if (game.isNicknameTaken(nickname)) {
            virtualView.displayText("Nickname '"+nickname+"' already.");
            return false;
        }
        return true;
    }

    /**
     * Returns the next player.
     * @return The name of the next active player.
     */
    private String getNextPlayer() {
        int indexOfPlayer = game.getPlayers().indexOf(game.getPlayerByNickname(getActivePlayer()));
        do {
            indexOfPlayer++;
            if (indexOfPlayer == game.getPlayers().size())
                indexOfPlayer = 0;
        } while (!game.getPlayers().get(indexOfPlayer).isOnlinePlayer());
        return game.getPlayers().get(indexOfPlayer).getNickname();
    }

    /**
     * @return The virtual view map.
     */
    public Map<String, VirtualView> getVirtualViewMap() {
        return virtualViewMap;
    }

    /**
     * Add the nickname to the corresponding the VirtualView.
     * @param nickname    The nickname of the player.
     * @param virtualView The virtual view of the player.
     */
    public void addVirtualView(String nickname, VirtualView virtualView) {
        this.virtualViewMap.put(nickname, virtualView);
    }

    /**
     * Removes the virtual view of the client specified.
     * @param nickname    The nickname of the client that will be removed.
     * @param virtualView The virtual view that will be removed.
     */
    public void removeVirtualView(String nickname, VirtualView virtualView) {
        this.virtualViewMap.remove(nickname, virtualView);
    }

    /**
     * Displays the nicknames of all players to their corresponding VirtualView, if available.
     */
    private void displayPlayersNicknames() {
        for (Player player : game.getPlayers()) {
            VirtualView virtualView = virtualViewMap.get(player.getNickname());
            if (virtualView != null) {
                virtualView.displayPlayersList(getNicknamePlayers());
            }
        }
    }


    /**
     * Retrieves a list of nicknames of all players in the game.
     * @return A List of String containing the nicknames of all players.
     */
    private List<String> getNicknamePlayers() {
        List<String> nicknames = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            nicknames.add(player.getNickname());
        }
        return nicknames;
    }

    /**
     * Shows to each player: manuscripts, public cards, decks and score.
     */
    private void displayGame() {
        displayManuscript();
        displayCommonTargetCards();
        showPersonalGoalCards();
        displayHandsCard();
        displayDecks();
        displayScoreBoard(sortScoreMap());
    }

    /**
     * Shows to each player the updated manuscript.
     */
    private void displayManuscript() {
        for (Player player : game.getPlayers()) {
            VirtualView virtualView = virtualViewMap.get(player.getNickname());
            if (virtualView != null) {
                String nickname = player.getNickname();
                Manuscript m = player.getManuscript();
                virtualView.displayManuscript(nickname, m.getCompressedManuscript(), m.getCompressedManuscriptWithIndexToString());
            }
        }
    }

    /**
     * shows to each player their hands card.
     */
    public void displayHandsCard() {
        for (Player player : game.getPlayers()) {
            VirtualView virtualView = virtualViewMap.get(player.getNickname());
            if (virtualView != null) {
                List<AbstractCard> handCards = player.getHandCards();
                virtualView.displayHandsCard(handCards.get(0), handCards.get(1), handCards.get(2));
            }
        }
    }

    /**
     * Sorts the map of player scores in descending order based on their points.
     * It returns a LinkedHashMap sorted by player scores in descending order
     */
    private Map<String, Integer> sortScoreMap() {
        Map<String, Integer> scoreMap = new HashMap<>();
        Map<String, Integer> sortedScoreMap = new LinkedHashMap<>();
        List<Integer> scoreList = new ArrayList<>();

        for (Player player : game.getPlayers()) {
            scoreMap.put(player.getNickname(), player.getPoints());
        }
        for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
            scoreList.add(entry.getValue());
        }
        scoreList.sort(Collections.reverseOrder());
        for (int num : scoreList) {
            for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
                if (entry.getValue().equals(num)) {
                    sortedScoreMap.put(entry.getKey(), num);
                }
            }
        }
        return sortedScoreMap;
    }

    /**
     * Displays the current state of various decks (gold and resource) to each player's virtual view.
     * Updates the virtual view with the IDs of cards in the exposed decks, handling special error cases
     * where card IDs are "null" by displaying a special error card.
     */
    private void displayDecks() {
        for (Player player : game.getPlayers()) {
            VirtualView virtualView = virtualViewMap.get(player.getNickname());
            ArrayList<String> cardIDs = new ArrayList<>(6);
            AbstractCard[] cards;
            cards = game.getGoldExposedCard();
            for(int i = 0; i < 2; i++){
                if(!cards[i].getID().equals("null")){
                    cardIDs.add(cards[i].getID());
                }
                else{
                    cardIDs.add("102");        //special error card, back of card 102
                }
            }
            cards = game.getResourceExposedCard();
            for(int i = 0; i < 2; i++){
                if(!cards[i].getID().equals("null")){
                    cardIDs.add(cards[i].getID());
                }
                else{
                    cardIDs.add("102");        //special error card, back of card 102
                }
            }
            if(!game.getTopGoldCardID().equals("null")){
                cardIDs.add(game.getTopGoldCardID());
            }
            else{
                cardIDs.add("102");
            }
            if(!game.getTopResourceCardID().equals("null")){
                cardIDs.add(game.getTopResourceCardID());
            }
            else{
                cardIDs.add("102");
            }
            if (virtualView != null) {
                virtualView.displayDecks(cardIDs);
            }
        }
    }

    /**
     * Sends a message to the players containing their CommonGoalCards.
     */
    private void displayCommonTargetCards() {
        for (Player player : game.getPlayers()) {
            VirtualView virtualView = virtualViewMap.get(player.getNickname());
            if (virtualView != null) {
                AbstractCard[] CommonTargetCard = game.getCommonTargetCard();
                virtualView.displayCommonTargetCard(CommonTargetCard[0], CommonTargetCard[1]);

            }
        }
    }

    /**
     * Sends a message to the players containing their PersonalGoalCards.
     */
    private void showPersonalGoalCards() {
        for (Player player : game.getPlayers()) {
            VirtualView virtualView = virtualViewMap.get(player.getNickname());
            if (virtualView != null) {
                virtualView.displaySecretTargetCard(player.getSecretTargetCard(), player.getNickname());
            }
        }
    }


    /**
     * Sends a message to the players with the corresponding score board.
     * @param scoreBoardMap The Map containing the score board.
     */
    private void displayScoreBoard(Map<String, Integer> scoreBoardMap) {
        for (Player player : game.getPlayers()) {
            VirtualView virtualView = virtualViewMap.get(player.getNickname());
            if (virtualView != null) {
                virtualView.displayScore(scoreBoardMap);
            }
        }
    }

    /**
     * Sends a message to each player with the final score board before the game ends.
     * @param scoreBoardMap       The {@code Map} containing the score board.
     * @param completedObjectives The {@code Map} containing the player nicknames and their number of completed objectives.
     */
    private void showFinalResult(Map<String, Integer> scoreBoardMap, Map<String, Integer> completedObjectives){
        for (Player player : game.getPlayers()) {
            VirtualView virtualView = virtualViewMap.get(player.getNickname());
            if (virtualView != null) {
                virtualView.displayFinalScore(scoreBoardMap, completedObjectives);
            }
        }
    }

    /**
     * Sends a message to all virtual views.
     * @param messageString The message to be transmitted.
     */
    public void transmissionMessage(String messageString) {
        for (VirtualView virtualView : virtualViewMap.values()) {
            virtualView.displayText(messageString);
        }
    }

    /**
     * Sends a notification message to all players except the active player,
     * displaying the specified message on their respective virtual views.
     * @param messageString the message to display as a notification
     */
    public void drawnCardNotificationMessage(String messageString) {
        for (Map.Entry<String, VirtualView> vv : virtualViewMap.entrySet()) {
            String nickname = vv.getKey();
            VirtualView virtualView = vv.getValue();
            if (!nickname.equals(activePlayer)) {
                virtualView.displayText(messageString);
            }
        }
    }

    /**
     * Sends a request to the active player to choose the card and the column it wants to place his card.
     */
    private void askToPlaceCard() {
        VirtualView virtualView = virtualViewMap.get(getActivePlayer());
        System.out.println(getActivePlayer() + " is required to select column and position.");
        virtualView.requestCardToPlace();
    }

    /**
     * Set the player with the nickname specified as parameter to offline, so it can be reconnected later.
     *
     * @param nickname The nickname of the offline player.
     */
    public void setPlayerOffline(String nickname) {
        virtualViewMap.remove(nickname);
        if (game.getPlayerByNickname(nickname) != null) {
            game.getPlayerByNickname(nickname).setOnlinePlayer(false);
        }
        if (game.getOnlinePlayersNumber() == 1) {
            gameSuspended = true;
            transmissionMessage("Game suspended: there's only " + game.getOnlinePlayersNumber() + " player connected.");
        } else {
            if (nickname.equals(activePlayer) && (gameState.equals(GameState.PLAY_CARD) || gameState.equals(GameState.DRAW_CARD))) {
                // If the player disconnected is not the current player, starts a new turn anyway!
                transmissionMessage("Since " + nickname + " is disconnected, the next player is: " + getNextPlayer());
                nextTurn();
            }
        }
    }

    /**
     * @return The state of the game.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * @param gameState The new state of the game.
     */
    private void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * @return A string containing the name of the current active player.
     */
    public String getActivePlayer() {
        return activePlayer;
    }

    /**
     * Sets the name of the current active player with the value passed as parameter if it exists.
     * @param nickname The name of the current active player.
     */
    public void setActivePlayer(String nickname) {
        this.activePlayer = nickname;
        game.setActivePlayerByNickname(nickname);
    }

    /**
     * @return true if the game is actually suspended.
     */
    public boolean isGameSuspended() {
        return gameSuspended;
    }

    /**
     * @return A list of the online players' nicknames.
     */
    public List<String> getOnlinePlayers() {
        List<String> onlinePlayers = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            if (player.isOnlinePlayer()) {
                onlinePlayers.add(player.getNickname());
            }
        }
        return onlinePlayers;
    }

    /**
     * @return A list of the offline players' nicknames.
     */
    public List<String> getOfflinePlayers() {
        List<String> offlinePlayers = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            if (!player.isOnlinePlayer()) {
                offlinePlayers.add(player.getNickname());
            }
        }
        return offlinePlayers;
    }

    /**
     * @return the players' list.
     */
    public List<Player> getPlayer (){
        return game.getPlayers();
    }

    /**
     * @param nickname The nickname of the player to be removed.
     */
    public void removePlayer(String nickname) {
        Player playerToRemove = game.getPlayerByNickname(nickname);
        if (playerToRemove != null) {
            game.getPlayers().remove(playerToRemove);
        } else {
            System.out.println("Can't remove the player " + nickname + " because it doesn't exist!");
        }
    }

    /**
     * Retrieves the target cards for a given player.
     * This method combines the player's secret target card with the common target cards from the game.
     * @param player The player for whom the target cards are from.
     * @return An array of AbstractCard containing the player's secret target card and the common target cards from the game.
     */
    private AbstractCard[] getTargetCards(Player player) {
        AbstractCard[] commonTargetCards = game.getCommonTargetCard();
        return new AbstractCard[]{
                player.getSecretTargetCard(),
                commonTargetCards[0],
                commonTargetCards[1]
        };
    }

    /**
     * Returns the appropriate PointStrategy for a given card ID.
     * This method uses a map to associate card IDs with their corresponding point calculation strategies.
     * @param cardId The ID of the card for which the point strategy is being retrieved.
     * @return The PointStrategy corresponding to the specified card ID, or null if no strategy is found.
     */
    private PointStrategy getPointStrategy(int cardId) {
        Map<Integer, PointStrategy> strategyMap = new HashMap<>();
        strategyMap.put(87, new Diagonals());
        strategyMap.put(88, new Diagonals());
        strategyMap.put(89, new Diagonals());
        strategyMap.put(90, new Diagonals());
        strategyMap.put(91, new LconfigurationOne());
        strategyMap.put(92, new LconfigurationTwo());
        strategyMap.put(93, new LconfigurationThree());
        strategyMap.put(94, new LconfigurationFour());
        for (int i = 95; i <= 102; i++) {
            strategyMap.put(i, new ThreePiece());
        }
        return strategyMap.get(cardId);
    }

    /**
     * Filters out all cards played by a specific player
     * identified by their nickname.
     */
    private List<CardsPlayed> getCardPlaysByPlayer(String playerNickname){
        List<CardsPlayed> cards = new ArrayList<>();
        for (CardsPlayed cardPlayed : cardsPlayedHistory){
            if (cardPlayed.getPlayerNickname().equals(playerNickname)){
                cards.add(cardPlayed);
            }
        }
        return cards;
    }
}
