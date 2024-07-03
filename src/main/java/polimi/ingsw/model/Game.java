package polimi.ingsw.model;

import com.google.gson.*;
import polimi.ingsw.exception.*;
import polimi.ingsw.model.strategy.*;
import polimi.ingsw.model.strategy.EmptyStrategy;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.stream.Collectors;
import java.util.*;

/**
 * Represents a game instance that manages players, decks, cards, and game state.
 * It's the interface that connect model to controller.
 * Implements functionality for game initialization, player management, card handling,
 * and game flow control including turn management and card playing.
 */
public class Game implements Serializable {
    private int GAME_ID;
    private int numPlayer;
    private List<Player> players;
    private Player activePlayer;
    private String nicknameFirstPlayer;
    private int numGameTurn;
    private GameState status;
    boolean isGameStarted;
    private List<Color> availableColors = new ArrayList<>(Arrays.asList(Color.values()));
    private AbstractDeck resourceDeck;
    private AbstractDeck goldDeck;
    private AbstractDeck targetDeck;
    private AbstractDeck startingDeck;   //private List<Deck> decks;
    private AbstractCard[] resourceExposedCard = new AbstractCard[2];
    private AbstractCard[] goldExposedCard = new AbstractCard[2];
    private AbstractCard[] commonTargetCard = new AbstractCard[2];
    private static final int RESOURCECARDNUMBER = 40;
    private static final int GOLDCARDNUMBER = 40;
    private static final int STARTINGCARDNUMBER = 6;
    private static final int TARGETCARDNUMBER = 16;
    private static final int NUMCORNERS = 4;
    private AbstractCard blankCard = new StartingCard("blank", "", "");

    private static Game gameInstance;

    /**
     * Default constructor for creating a new game instance.
     * Initializes an empty list of players and calls the method to create decks.
     */
    public Game() {
        this.players = new ArrayList<>();
        createDeck();
    }

    /**
     * Constructor for creating a new game instance with specified game ID and number of players.
     * Initializes an empty list of players, sets the game ID and number of players,
     * and calls the method to create decks.
     *
     * @param GAME_ID The unique identifier of the game.
     * @param numPlayer The number of players in the game.
     */
    public Game(int GAME_ID, int numPlayer) {
        this.players = new ArrayList<>();
        this.GAME_ID = GAME_ID;
        this.numPlayer = numPlayer;
        createDeck();
    }

    /**
     * Static factory method to create a new instance of the Game class.
     *
     * @return A new instance of Game.
     */
    public static Game getNewGame() {
        return new Game();
    }

    /** @return The ID of the game.
    */
    public int getGAME_ID() {
        return GAME_ID;
    }

    /**
     * @param numPlayer The number of players to set.
     */
    public void setNumPlayer(int numPlayer) {
        this.numPlayer = numPlayer;
    }

    /**
     * @return The number of players.
     */
    public int getNumPlayer() {
        return numPlayer;
    }

    /**
     * @return A list containing all players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Retrieves the status of the game (e.g., STARTING, PLAY_CARD, DRAW_CARD).
     *
     * @return The current GameState of the game.
     */
    public GameState getStatus() {
        return status;
    }

    /**
     * Sets the active player based on the given nickname.
     *
     * @param activePlayer The nickname of the player to set as active.
     */
    public void setActivePlayerByNickname(String activePlayer) {
        this.activePlayer = getPlayerByNickname(activePlayer);
    }

    /**
     * Returns the nickname of the currently active player.
     *
     * @return The nickname of the active player.
     */
    public String getActivePlayerNickname() {
        return activePlayer.getNickname();
    }

    /**
     * Sets the resource card at the specified index in the exposed resource cards array.
     *
     * @param c The resource card to set.
     * @param index The index where the card should be set.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    private void setResourceExposedCard(AbstractCard c, int index) {
        if (index < 0 || index >= resourceExposedCard.length) {
            throw new IndexOutOfBoundsException();
        }
        resourceExposedCard[index] = c;
    }

    /**
     * Returns the ID of the top resource card in the resource deck.
     * Returns "null" if the resource deck is empty.
     *
     * @return The ID of the top resource card or "null" if empty.
     */
    public String getTopResourceCardID(){
        if (resourceDeck.isEmpty()){
            return "null";
        }
        else{
            return resourceDeck.getTopCardID();
        }
    }

    /**
     * Returns the ID of the top gold card in the gold deck.
     * Returns "null" if the gold deck is empty.
     *
     * @return The ID of the top gold card or "null" if empty.
     */
    public String getTopGoldCardID(){
        if (goldDeck.isEmpty()){
            return "null";
        }
        else{
            return goldDeck.getTopCardID();
        }
    }

    /**
     * Sets the gold card at the specified index in the exposed gold cards array.
     *
     * @param c The gold card to set.
     * @param index The index where the card should be set.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    private void setGoldExposedCard(AbstractCard c, int index) {
        if (index < 0 || index >= goldExposedCard.length) {
            throw new IndexOutOfBoundsException();
        }
        goldExposedCard[index] = c;
    }

    /**
     * Returns the array of exposed resource cards.
     *
     * @return An array containing the exposed resource cards.
     */
    public AbstractCard[] getResourceExposedCard() {
        return resourceExposedCard;
    }

    /**
     * Returns the array of common target cards.
     *
     * @return An array containing the common target cards.
     */
    public AbstractCard[] getCommonTargetCard() {
        return commonTargetCard;
    }

    /**
     * Returns the array of exposed gold cards.
     *
     * @return An array containing the exposed gold cards.
     */
    public AbstractCard[] getGoldExposedCard() {
        return goldExposedCard;
    }

    /**
     * Retrieves the player with the given nickname from the list of players.
     *
     * @param nickname The nickname of the player to retrieve.
     * @return The Player object with the matching nickname, or null if not found.
     */
    public Player getPlayerByNickname(String nickname) {
        for (Player player : players) {
            if (player.getNickname().equalsIgnoreCase(nickname)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Adds a new player with the specified nickname to the game.
     * Throws DuplicatePlayerException if a player with the same nickname already exists.
     *
     * @param newPlayer The nickname of the new player to add.
     * @throws DuplicatePlayerException If a player with the same nickname already exists in the game.
     */
    public void addPlayerByNickname(String newPlayer) {
        if (players.stream().anyMatch(existingPlayer -> existingPlayer.getNickname().equals(newPlayer))) {
            throw new DuplicatePlayerException("Player with nickname '" + newPlayer + "' already exists!");
        }

        players.add(new Player(newPlayer));
    }

    /**
     * Adds a new player to the game.
     * Throws DuplicatePlayerException if a player with the same nickname already exists.
     *
     * @param newPlayer The new player object to add.
     * @throws DuplicatePlayerException If a player with the same nickname already exists in the game.
     */
    public void addPlayer(Player newPlayer) {
        if (players.stream().anyMatch(existingPlayer -> existingPlayer.getNickname().equals(newPlayer.getNickname()))) {
            throw new DuplicatePlayerException("Player with nickname '" + newPlayer.getNickname() + "' already exists!");
        }
        players.add(newPlayer);
    }

    /**
     * Checks if a nickname is already taken by an existing player in the game.
     *
     * @param nickname The nickname to check.
     * @return true if the nickname is already taken, false otherwise.
     */
    public boolean isNicknameTaken(String nickname) {
        return getPlayerByNickname(nickname) != null;
    }

    /**
     * Creates and initializes the decks (resource, gold, target, and starting decks) for the game.
     * Also creates and shuffles the cards for each deck.
     */
    private void createDeck() {
        resourceDeck = new DeckResources(RESOURCECARDNUMBER);
        goldDeck = new DeckGold(GOLDCARDNUMBER);
        targetDeck = new DeckTarget(TARGETCARDNUMBER);
        startingDeck = new DeckStarting(STARTINGCARDNUMBER);
        createResourceCards();
        createGoldCards();
        createTargetCards();
        createStartingCards();
        resourceDeck.shuffle();
        goldDeck.shuffle();
        targetDeck.shuffle();
        startingDeck.shuffle();
    }

    /**
     * Initializes the game by creating and shuffling all necessary decks (resource, gold, target, starting).
     * Sets the game status to STARTING and performs initial setup such as placing initial cards and selecting the first player.
     */
    public void startingGame() {
        // * - Creates the deck of cards containing resource, gold and objects by calling private method.
        status = GameState.STARTING;

        // * - Places two initial cards, both resource and gold, on the board.
        setResourceExposedCard(resourceDeck.drawCard(), 0);
        setResourceExposedCard(resourceDeck.drawCard(), 1);
        setGoldExposedCard(goldDeck.drawCard(), 0);
        setGoldExposedCard(goldDeck.drawCard(), 1);


        // * - Places two common targets on the board.
        commonTargetCard[0] = targetDeck.drawCard();
        commonTargetCard[1] = targetDeck.drawCard();

        // * - Randomly selects the first player (and make it activePlayer) and sorts the player list accordingly.
        activePlayer = players.get(new Random().nextInt(players.size()));
        Collections.rotate(players, -players.indexOf(activePlayer));

        activePlayer.setFirstPlayer(true);
        status = GameState.PLAY_CARD;
    }

    /**
     * Allows a player to play a card on the game board.
     * Validates the player's turn, card availability, and resource requirements before placing the card.
     *
     * @param player The player who wants to play the card.
     * @param cardToPlace The AbstractCard object representing the card to be played.
     * @param row The row index on the game board where the card will be placed.
     * @param column The column index on the game board where the card will be placed.
     * @throws IllegalStateException If it's not possible to play the card due to the current game state.
     * @throws NotPlayerTurnException If it's not the turn of the specified player to play.
     * @throws LackResourceException If the player lacks the required resources to play the card.
     */
    public void playCard(Player player, AbstractCard cardToPlace, int row, int column) throws IllegalStateException, NotPlayerTurnException, LackResourceException {

        if (status != GameState.PLAY_CARD) {
            throw new IllegalStateException("It's not possible to play card " + cardToPlace + ":  is not the current status");
        }
        if (!player.equals(activePlayer)) {
            throw new NotPlayerTurnException("It's not turn of player: " + activePlayer + "!");
        }
        if (!cardToPlace.checkResourceRequested(player.getManuscript()) && cardToPlace.isFront()) {
            throw new LackResourceException("It's not possible to play card " + cardToPlace + " due to lack of resources!");
        }
        if (!player.getHandCards().contains(cardToPlace)) {
            throw new IllegalArgumentException("It's not possible to play card: " + cardToPlace + " the card is not in player hand!");
        }

        try {
            // Place the chosen card onto the chosen cell
            player.placeCard(cardToPlace, row, column);
            player.addPoints(addPointWithStrategy(player, cardToPlace));
        } catch (IllegalArgumentException e) {
            throw e;
        }

    }

    /**
     * Removes a specified card from a player's hand during the PLAY_CARD phase.
     * Replaces the removed card with a blank card, indicating the player needs to draw a replacement card.
     *
     * @param player The player who wants to remove the card from their hand.
     * @param cardToRemove The AbstractCard object representing the card to be removed from the player's hand.
     * @throws IllegalStateException If it's not possible to remove the card due to the current game state.
     * @throws NotPlayerTurnException If it's not the turn of the specified player to remove the card.
     * @throws IllegalArgumentException If the specified card is not in the player's hand.
     */
    public void removePlayerCardFromHand(Player player, AbstractCard cardToRemove) {

        if ((status != GameState.PLAY_CARD)) {
            throw new IllegalStateException("It's not possible to remove card " + cardToRemove + ":  is not the current status");
        }
        if (!player.equals(activePlayer)) {
            throw new NotPlayerTurnException("It's not turn of player: " + activePlayer + "!");
        }
        if (!player.getHandCards().contains(cardToRemove)) {
            throw new IllegalArgumentException("It's not possible to remove card: " + cardToRemove + " the card is not in player hand!");
        }

        player.removeCardFromHand(cardToRemove);
        // Add a blank card that means no card relevant... waiting that the player draw a card and replace it
        player.addCardToHand(blankCard);

        status = GameState.DRAW_CARD;

    }

    /**
     * Checks if all players are ready to play and sets the game status to "PLAY_CARD" if they are.
     * Returns true if all players are ready, false otherwise.
     *
     * @return true if all players are ready, false otherwise.
     */
    public boolean checkAllPlayersReady() {
        boolean allPlayersReady = players.stream().allMatch(Player::isPlayerReady);
        if (allPlayersReady) {
            status = GameState.PLAY_CARD;
        }
        return allPlayersReady;
    }

    /**
     * Checks if all players in the game have been assigned a color.
     *
     * @return true if all players have a non-null color assigned, false otherwise.
     */
    public boolean areAllColorSet() {
        return players.stream()
                .map(Player::getColor)
                .allMatch(Objects::nonNull);
    }


    /**
     * Draws a card from the specified deck.
     *
     * @param deckType The type of deck from which to draw the card.
     *                 1 for resourceDeck, 2 for goldDeck, 3 for resourceExposedCard[0],
     *                 4 for resourceExposedCard[1], 5 for goldExposedCard[0],
     *                 6 for goldExposedCard[1].
     * @throws IllegalStateException if the specified deck is empty.
     */
    public void drawingCard(Player player, int deckType) throws IllegalStateException {

        if (status != GameState.DRAW_CARD) {
            throw new IllegalStateException("It's not possible to draw card now !");
        }
        if (!player.equals(activePlayer)) {
            throw new IllegalStateException("It's not turn of player: " + activePlayer + "!");
        }

        player.removeCardFromHand(blankCard);

        switch (deckType) {
            case 1:
                player.addCardToHand(resourceDeck.drawCard());
                break;
            case 2:
                player.addCardToHand(goldDeck.drawCard());
                break;
            case 3:
                player.addCardToHand(resourceExposedCard[0]);
                resourceExposedCard[0] = resourceDeck.drawCard();
                break;
            case 4:
                player.addCardToHand(resourceExposedCard[1]);
                resourceExposedCard[1] = resourceDeck.drawCard();
                break;
            case 5:
                player.addCardToHand(goldExposedCard[0]);
                goldExposedCard[0] = goldDeck.drawCard();
                break;
            case 6:
                player.addCardToHand(goldExposedCard[1]);
                goldExposedCard[1] = goldDeck.drawCard();
                break;
            default:
                throw new IllegalArgumentException("Invalid deck type");
        }

        status = GameState.PLAY_CARD;

    }

    /**
     * Set game state to ENDING
     */
    public void endingGame() {
        status = GameState.ENDING;
    }

    /**
     * Retrieves a map where keys are player nicknames and values are the corresponding player points.
     *
     * @return A Map<String, Integer> where keys represent player nicknames and values represent player points.
     */
    public Map<String, Integer> showScoreGame() {
        return players.stream()
                .collect(Collectors.toMap(Player::getNickname, Player::getPoints));
    }
    /**
     * Prepares a player for the starting phase of the game.
     * Sets the player's starting card from the starting deck and determines its placement.
     * Draws three additional cards for the player (2 resource cards and 1 gold card).
     * Assigns two possible target cards to the player.
     *
     * @param player The nickname of the player to prepare for the starting phase.
     * @throws IllegalArgumentException If the specified player nickname does not exist in the game.
     */
    public void startingPlayer(String player) {
        Player p = getPlayerByNickname(player);
        // * - Sets the starting card for the player and chose if place front or back
        AbstractCard startingCard = startingDeck.drawCard();
        startingCard.setFront(true);
        p.setStartingCard(startingCard);

        // * - Draws three cards for the player (2 resource cards and 1 gold card).
        p.addCardToHand(resourceDeck.drawCard());
        p.addCardToHand(resourceDeck.drawCard());
        p.addCardToHand(goldDeck.drawCard());

        p.setPossibleTargetCard(targetDeck.drawCard(), targetDeck.drawCard());
    }

    /**
     * Sets the color for a specified player.
     *
     * @param player The player whose color is to be set.
     * @param colorString The string representation of the color to assign to the player.
     * @throws IllegalArgumentException If the specified color string does not correspond to a valid color
     *                                  or if the color is already assigned to another player.
     */
    public void setPlayerColor(Player player, String colorString) {
        Color color = getColorFromString(colorString);
        if (!availableColors.contains(color)) {
            throw new IllegalArgumentException("Color " + color + " is not a valid color!");
        }
        player.setColor(color);
        availableColors.remove(color);

    }

    /**
     * Sets the secret target card for a specified player.
     *
     * @param player The player for whom the secret target card is to be set.
     * @param secretTargetCard The abstract card representing the secret target card to assign to the player.
     */
    public void setPlayerSecretTargetCard(Player player, AbstractCard secretTargetCard) {
        player.setSecretTargetCard(secretTargetCard);
    }

    /**
     * utility method.
     */
    private Color getColorFromString(String colorString) {
        for (Color color : Color.values()) {
            if (colorString.equalsIgnoreCase(color.name())) {
                return color;
            }
        }
        return null; // If no correspondent color is found
    }

    /**
     * @return number of online players
     */
    public Integer getOnlinePlayersNumber() {
        return Math.toIntExact(players.stream().filter(Player::isOnlinePlayer).count());
    }

    public List<Color> getAvailableColors() {
        return availableColors;
    }

    public String[] getAvailableColorString() {
        return availableColors.stream()
                .map(Object::toString)
                .toArray(String[]::new);
    }

    public void setPlayerStartingCardSide(Player player, boolean side) {
        player.setStartingCardSide(side);
    }


    public AbstractCard[] getPossibleTargetCard(Player p) {
        return p.getPossibleTargetCard();
    }

    public int addPointWithStrategy(Player p, AbstractCard card) {
        int points = 0;
        int CardId = Integer.parseInt(card.getID());

        if (CardId >= 1 && CardId <= 40) {
            if (card.isFront()) {
                PointStrategy strategy = new EmptyStrategy();
                points = strategy.calculatePoint(p.getManuscript(), card);
            }
        } else {
            if (card.isFront()) {
                points = card.calculatePointStrategy(p, card);
            }
        }

        return points;
    }

    /**
     * Reads card data from a JSON file and initializes the starting deck with the parsed information.
     * Each card is constructed based on the JSON data, including its ID, image paths, attached resources,
     * points, front and back corners with associated resources or pieces.
     */
    private void createResourceCards() {
        String path_to_jsonfile = "src/main/java/polimi/ingsw/json/decks/DeckResources.json";
        //List<AbstractCard> resourceCards = new ArrayList<>();
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(path_to_jsonfile));
            JsonObject rootObject = jsonElement.getAsJsonObject();
            JsonArray deckResources = rootObject.getAsJsonArray("DeckResources");

            for (JsonElement element : deckResources) {
                JsonObject resourceObject = element.getAsJsonObject();
                String ID = resourceObject.get("ID").getAsString();
                JsonArray frontCorner = resourceObject.getAsJsonArray("frontCorner");
                JsonArray backCorner = resourceObject.getAsJsonArray("backCorner");
                String imagePathToFront = resourceObject.get("imagePathToFront").getAsString();
                String imagePathToBack = resourceObject.get("imagePathToBack").getAsString();
                String attachedResource = resourceObject.get("attachedResource").getAsString().toUpperCase();
                int point = resourceObject.get("point").getAsInt();

                Corner[] frontCorners = new Corner[NUMCORNERS];
                Corner[] backCorners = new Corner[NUMCORNERS];
                for (int i = 0; i < NUMCORNERS; i++) {
                    //Elaboration of frontCorners[i]
                    String cornerContent = frontCorner.get(i).getAsString().toUpperCase();
                    if (cornerContent.equals("NULL")) {
                        frontCorners[i] = new Corner(ID, false, null, null);
                    } else if (cornerContent.equals("FREE")) {
                        frontCorners[i] = new Corner(ID, true, Resources.FREE, Piece.FREE);
                    } else {
                        try {
                            Resources enumResourcesVal = Resources.valueOf(cornerContent);
                            frontCorners[i] = new Corner(ID, true, enumResourcesVal, null);
                        } catch (IllegalArgumentException e) {
                            Piece enumPieceVal = Piece.valueOf(cornerContent);
                            frontCorners[i] = new Corner(ID, true, null, enumPieceVal);
                        }
                    }
                    //Elaboration of backCorners[i]
                    cornerContent = backCorner.get(i).getAsString().toUpperCase();
                    if (cornerContent.equals("NULL")) {
                        backCorners[i] = new Corner(ID, false, null, null);
                    } else if (cornerContent.equals("FREE")) {
                        backCorners[i] = new Corner(ID, true, Resources.FREE, Piece.FREE);
                    } else {
                        try {
                            Resources enumResourcesVal = Resources.valueOf(cornerContent);
                            backCorners[i] = new Corner(ID, true, enumResourcesVal, null);
                        } catch (IllegalArgumentException e) {
                            Piece enumPieceVal = Piece.valueOf(cornerContent);
                            backCorners[i] = new Corner(ID, true, null, enumPieceVal);
                        }
                    }
                }
                Resources enumResourcesVal = Resources.valueOf(attachedResource);
                ResourceCard card = new ResourceCard(ID, imagePathToFront, imagePathToBack, point, frontCorners, backCorners, enumResourcesVal);
                //resourceCards.add(card);
                resourceDeck.addCard(card);
            }
            //return resourceCards;
        } catch (IOException e) {
            System.out.println("IOException.");
            //return null;
        }
    }

    /**
     * Reads card data from a JSON file and initializes the starting deck with the parsed information.
     * Each card is constructed based on the JSON data, including its ID, image paths, attached resources,
     * points, front and back corners with associated resources or pieces.
     */
    private void createGoldCards() {
        String path_to_jsonfile = "src/main/java/polimi/ingsw/json/decks/DeckGold.json";
        //List<AbstractCard> goldCards = new ArrayList<>();
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(path_to_jsonfile));
            JsonObject rootObject = jsonElement.getAsJsonObject();
            JsonArray deckGold = rootObject.getAsJsonArray("DeckGold");

            for (JsonElement element : deckGold) {
                JsonObject goldObject = element.getAsJsonObject();
                String ID = goldObject.get("ID").getAsString();
                JsonArray frontCorner = goldObject.getAsJsonArray("frontCorner");
                JsonArray backCorner = goldObject.getAsJsonArray("backCorner");
                String imagePathToFront = goldObject.get("imagePathToFront").getAsString();
                String imagePathToBack = goldObject.get("imagePathToBack").getAsString();
                String attachedResource = goldObject.get("attachedResource").getAsString().toUpperCase();
                int point = goldObject.get("point").getAsInt();
                String forEach = goldObject.get("forEach").getAsString();
                JsonArray requests = goldObject.getAsJsonArray("requests");

                Corner[] frontCorners = new Corner[NUMCORNERS];
                Corner[] backCorners = new Corner[NUMCORNERS];
                int[] resourceRequested = new int[NUMCORNERS];
                for (int i = 0; i < NUMCORNERS; i++) {
                    //Elaboration of resourceRequested[i]
                    resourceRequested[i] = requests.get(i).getAsInt();
                    //Elaboration of frontCorners[i]
                    String cornerContent = frontCorner.get(i).getAsString().toUpperCase();
                    if (cornerContent.equals("NULL")) {
                        frontCorners[i] = new Corner(ID, false, null, null);
                    } else if (cornerContent.equals("FREE")) {
                        frontCorners[i] = new Corner(ID, true, Resources.FREE, Piece.FREE);
                    } else {
                        try {
                            Resources enumResourcesVal = Resources.valueOf(cornerContent);
                            frontCorners[i] = new Corner(ID, true, enumResourcesVal, null);
                        } catch (IllegalArgumentException e) {
                            Piece enumPieceVal = Piece.valueOf(cornerContent);
                            frontCorners[i] = new Corner(ID, true, null, enumPieceVal);
                        }
                    }
                    //Elaboration of backCorners[i]
                    cornerContent = backCorner.get(i).getAsString().toUpperCase();
                    if (cornerContent.equals("NULL")) {
                        backCorners[i] = new Corner(ID, false, null, null);
                    } else if (cornerContent.equals("FREE")) {
                        backCorners[i] = new Corner(ID, true, Resources.FREE, Piece.FREE);
                    } else {
                        try {
                            Resources enumResourcesVal = Resources.valueOf(cornerContent);
                            backCorners[i] = new Corner(ID, true, enumResourcesVal, null);
                        } catch (IllegalArgumentException e) {
                            Piece enumPieceVal = Piece.valueOf(cornerContent);
                            backCorners[i] = new Corner(ID, true, null, enumPieceVal);
                        }
                    }
                }
                Resources enumResourcesVal = Resources.valueOf(attachedResource);
                GoldCard card = new GoldCard(ID, imagePathToFront, imagePathToBack, point, frontCorners, backCorners, resourceRequested, enumResourcesVal, forEach);
                goldDeck.addCard(card);
                //goldCards.add(card);
            }
            //return goldCards;
        } catch (IOException e) {
            System.out.println("IOException.");
            //return null;
        }
    }

    /**
     * Reads card data from a JSON file and initializes the starting deck with the parsed information.
     * Each card is constructed based on the JSON data, including its ID, image paths, attached resources,
     * points, front and back corners with associated resources or pieces.
     */
    private void createStartingCards() {
        String path_to_jsonfile = "src/main/java/polimi/ingsw/json/decks/DeckStarting.json";
        //List<AbstractCard> startingCards = new ArrayList<>();
        List<Resources> attachedResources = new ArrayList<>();
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(path_to_jsonfile));
            JsonObject rootObject = jsonElement.getAsJsonObject();
            JsonArray deckResources = rootObject.getAsJsonArray("DeckStarting");

            for (JsonElement element : deckResources) {
                JsonObject resourceObject = element.getAsJsonObject();
                String ID = resourceObject.get("ID").getAsString();
                JsonArray frontCorner = resourceObject.getAsJsonArray("frontCorner");
                JsonArray backCorner = resourceObject.getAsJsonArray("backCorner");
                String imagePathToFront = resourceObject.get("imagePathToFront").getAsString();
                String imagePathToBack = resourceObject.get("imagePathToBack").getAsString();
                JsonArray attachedResource = resourceObject.getAsJsonArray("attachedResource");
                int point = resourceObject.get("point").getAsInt();

                //Elaboration of attachedResources
                for (int i = 0; i < attachedResource.size(); i++) {
                    try {
                        Resources enumResourcesVal = Resources.valueOf(attachedResource.get(i).getAsString().toUpperCase());
                        attachedResources.add(enumResourcesVal);
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e);
                    }
                }

                Corner[] frontCorners = new Corner[NUMCORNERS];
                Corner[] backCorners = new Corner[NUMCORNERS];
                for (int i = 0; i < NUMCORNERS; i++) {
                    //Elaboration of frontCorners[i]
                    String cornerContent = frontCorner.get(i).getAsString().toUpperCase();
                    if (cornerContent.equals("NULL")) {
                        frontCorners[i] = new Corner(ID, false, null, null);
                    } else if (cornerContent.equals("FREE")) {
                        frontCorners[i] = new Corner(ID, true, Resources.FREE, Piece.FREE);
                    } else {
                        try {
                            Resources enumResourcesVal = Resources.valueOf(cornerContent);
                            frontCorners[i] = new Corner(ID, true, enumResourcesVal, null);
                        } catch (IllegalArgumentException e) {
                            Piece enumPieceVal = Piece.valueOf(cornerContent);
                            frontCorners[i] = new Corner(ID, true, null, enumPieceVal);
                        }
                    }
                    //Elaboration of backCorners[i]
                    cornerContent = backCorner.get(i).getAsString().toUpperCase();
                    if (cornerContent.equals("NULL")) {
                        backCorners[i] = new Corner(ID, false, null, null);
                    } else if (cornerContent.equals("FREE")) {
                        backCorners[i] = new Corner(ID, true, Resources.FREE, Piece.FREE);
                    } else {
                        try {
                            Resources enumResourcesVal = Resources.valueOf(cornerContent);
                            backCorners[i] = new Corner(ID, true, enumResourcesVal, null);
                        } catch (IllegalArgumentException e) {
                            Piece enumPieceVal = Piece.valueOf(cornerContent);
                            backCorners[i] = new Corner(ID, true, null, enumPieceVal);
                        }
                    }
                }

                StartingCard card = new StartingCard(ID, imagePathToFront, imagePathToBack, point, frontCorners, backCorners,  new ArrayList<>(attachedResources));
                startingDeck.addCard(card);
                //System.out.println("" +card.getID() + ": " + card.getFrontCorner() + "; " + card.getBackCorner());
                int i = attachedResources.size() - 1;
                while (!attachedResources.isEmpty()) {
                    attachedResources.remove(i);
                    i--;
                }
                //startingCards.add(card);
            }
            //return startingCards;
        } catch (IOException e) {
            System.out.println("IOException.");
            //return null;
        }
    }

    /**
     * Reads card data from a JSON file and initializes the starting deck with the parsed information.
     * Each card is constructed based on the JSON data, including its ID, image paths, attached resources,
     * points, front and back corners with associated resources or pieces.
     */
    private void createTargetCards() {
        String path_to_jsonfile = "src/main/java/polimi/ingsw/json/decks/DeckTarget.json";
        //List<AbstractCard> targetCards = new ArrayList<>();

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(path_to_jsonfile));
            JsonObject rootObject = jsonElement.getAsJsonObject();
            JsonArray deckResources = rootObject.getAsJsonArray("DeckTarget");

            for (JsonElement element : deckResources) {
                JsonObject resourceObject = element.getAsJsonObject();
                String ID = resourceObject.get("ID").getAsString();
                String imagePathToFront = resourceObject.get("imagePathToFront").getAsString();
                String imagePathToBack = resourceObject.get("imagePathToBack").getAsString();
                String color = resourceObject.get("color").getAsString().toUpperCase();
                int point = resourceObject.get("point").getAsInt();
                String strategy = resourceObject.get("strategy").getAsString();

                TargetCard card = new TargetCard(ID, imagePathToFront, imagePathToBack, point, null, null, color, strategy);
                targetDeck.addCard(card);
                //targetCards.add(card);
            }
            //return targetCards;
        } catch (IOException e) {
            System.out.println("IOException.");
            //return null;
        }
    }

    /**
     * Applies the singleton pattern to this class.
     *
     * @return The instance of the game. Ensures that the instance of {@code Game} class is not null.
     */
    public static Game getGameInstance() {
        if (gameInstance == null) {
            gameInstance = new Game();
        }
        return gameInstance;
    }

    /**
     * Reset the current game instance.
     * Ensures that the instance of {@code Game} class is null.
     */
    public static void resetGameInstance() {
        gameInstance = null;
    }

    /**
     * Restore a game previously saved as the current game.
     *
     * @param previousGame Instance of the previously saved game.
     */
    public void restorePreviousGame(Game previousGame) {
        this.players = previousGame.getPlayers();
        this.numPlayer = previousGame.getNumPlayer();
        this.commonTargetCard = previousGame.getCommonTargetCard();
    }

    /**
     * Checks if all players have their starting cards set.
     *
     * @return {@code true} if all players have their starting cards set, {@code false} otherwise.
     */
    public boolean areAllSecretTargetSet() {
        return players.stream().allMatch(player -> player.getSecretTargetCard() != null);
    }

    public void setStatus(GameState status){
        this.status = status;
    }

    /**
     * Checks if either the resource deck or the gold deck is empty.
     * return true if either deck is empty, false otherwise
     */
    public boolean isAnyDeckEmpty() {
        if (resourceDeck.isEmpty()) {
            return true;
        }
        if (goldDeck.isEmpty()) {
            return true;
        }
        return false;
    }
    public AbstractCard getPlayerStartingCard(Player player) {
        return player.getStartingCard();
    }
    public List<AbstractCard> getPlayerHandCards(Player p) {
        return p.getHandCards();
    }

    public AbstractCard[][] getPlayerManuscript(Player p) {
        return p.getMatrixManuscript();
    }
    public void setGAME_ID(int GAME_ID) {
        this.GAME_ID = GAME_ID;
    }
    public List<String> getPlayerNickname() {
        return players.stream()
                .map(Player::getNickname)
                .collect(Collectors.toList());
    }
    public void removePlayer(Player player) {
        players.remove(player);
    }
    public int getNumConnectedPlayer() {
        return players.size();
    }
    public int getNumGameTurn() {
        return numGameTurn;
    }
    public void setTargetCard(Player p, AbstractCard targetCard) {
        p.setSecretTargetCard(targetCard);
    }

}