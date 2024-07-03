package polimi.ingsw.view;

import polimi.ingsw.model.*;
import polimi.ingsw.SocketAndRMI.CardsPlayed;
import polimi.ingsw.SocketAndRMI.ClientController;

import java.util.*;

public class TUI implements View {

     //The client's controller (It is necessary to communicate with the server)
    private ClientController clientController;
    private final String GENERAL_RECIPIENT = "General";
    private final List<String> chatPlayersNicknames = new ArrayList<>();
    private final Scanner scanner;

    /**
     * TUI's constructor
     */
    public TUI() {
        scanner = new Scanner(System.in);
    }

    /**
     * Starts the view (TUI).
     */
    public void startView() {
        System.out.println("                       _                                                   \n" +
                "                      | |                          _                       \n" +
                "           _ _ _  ____| | ____ ___  ____   ____   | |_  ___                \n" +
                "          | | | |/ _  ) |/ ___) _ \\|    \\ / _  )  |  _)/ _ \\               \n" +
                "          | | | ( (/ /| ( (__| |_| | | | ( (/ /   | |_| |_| |              \n" +
                "           \\____|\\____)_|\\____)___/|_|_|_|\\____)   \\___)___/               \n" +
                "                                                                           \n" +
                "                _                                                 _ _      \n" +
                "               | |                           _                   | (_)     \n" +
                "  ____ ___   _ | | ____ _   _    ____   ____| |_ _   _  ____ ____| |_  ___ \n" +
                " / ___) _ \\ / || |/ _  | \\ / )  |  _ \\ / _  |  _) | | |/ ___) _  | | |/___)\n" +
                "( (__| |_| ( (_| ( (/ / ) X (   | | | ( ( | | |_| |_| | |  ( ( | | | |___ |\n" +
                " \\____)___/ \\____|\\____|_/ \\_)  |_| |_|\\_||_|\\___)____|_|   \\_||_|_|_(___/ \n" +
                "                                                                           ");
        requestServerInformation();
    }

    /**
     * Requests server connection information from the user via console input,
     * validates the input, and updates the server information via the ClientController.
     */


    public void requestServerInformation() {
        String ipAddress, defaultIpAddress = "127.0.0.1", input;      // Localhost address.
        int port, defaultPort = 5033;
        boolean validInput = false, rmiConnection = false;

        // Choose the connection type between Socket or RMI
        do {
            System.out.println("Please select the preferred connection type: ");
            System.out.println("1) Socket connection (default)");
            System.out.println("2) RMI connection");
            int choose;
            try {
                input = scanner.nextLine();
                if (input.isEmpty())
                    choose = 1;
                else
                    choose = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                choose = 1;
            }
            switch (choose) {
                case 1 -> {
                    rmiConnection = false;
                    validInput = true;
                    System.out.println("You selected socket connection.");
                }
                case 2 -> {
                    rmiConnection = true;
                    validInput = true;
                    System.out.println("You selected RMI connection.");
                }
                default -> {
                    System.out.println("Incorrect choice.");
                    validInput = false;
                }
            }
        } while (!validInput);

        if (rmiConnection)
            defaultPort = 1099;

        // Insert and verify the IP address and the port
        validInput = false;
        do {
            System.out.print("Please insert the IP address of the server (default: " + defaultIpAddress + "): ");
            ipAddress = scanner.nextLine();
            if (ipAddress.equalsIgnoreCase(""))
                ipAddress = defaultIpAddress;
            System.out.print("Please insert the port of the server (default: " + defaultPort + "): ");
            try {
                input = scanner.nextLine();
                if (input.isEmpty())
                    port = defaultPort;
                else
                    port = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                port = defaultPort;
            }

            if (ClientController.checkIPAddress(ipAddress) && ClientController.checkPort(port))
                validInput = true;
        } while (!validInput);

        clientController.onUpdateServerInfo(ipAddress, port, rmiConnection);
    }


    /**
     * Requests the user's nickname via console input and updates it via
     * the ClientController.
     */
    @Override
    public void requestNickname() {
        System.out.print("Enter your nickname: ");
        String nickname = scanner.nextLine();
        clientController.onUpdatePlayerNick(nickname);
    }

    /**
     * Requests the number of players for the game via console input,
     * validates the input, and updates the number of players via the
     * ClientController.
     */


    @Override
    public void requestPlayersNumber() {
        int numOfPlayers;           // Default players number
        do {
            System.out.print("How many players do you want to play with (between 2 and 4)? ");
            numOfPlayers = readIntegerNumber();
        } while (numOfPlayers < 2 || numOfPlayers > 4);
        clientController.onUpdateChosenNumberOfPlayer(numOfPlayers);
    }

    /**
     * Requests the user's preferred color choice via console input,
     * validates the input, and updates the color choice via the ClientController.
     *
     * @param availableColor Array of available colors to choose from
     */

    public void requestColor(String[] availableColor) {
        String color;
        boolean validColor = false;

        if (availableColor != null && availableColor.length > 0) {
            System.out.println("Available colors: " + Arrays.toString(availableColor));
        } else {
            System.out.println("Available colors: " + Arrays.toString(Color.values()));
        }

        do {
            System.out.print("Enter your color: ");
            color = scanner.nextLine();

            // Check if selected color is available
            for (Color okColor : Color.values()) {
                if (color.equalsIgnoreCase(okColor.toString())) {
                    validColor = true;
                    break;
                }
            }

            if (!validColor) {
                System.out.println("Invalid color. Please choose from the available colors.");
            }
        } while (!validColor);

        System.out.println("You chose the color: " + color);

        clientController.onUpdateColor(color);
    }


    /**
     * Displays the response after attempting to log in,
     * handling scenarios such as unsuccessful connection
     * or invalid nickname by requesting the nickname again.
     *
     * @param validNickname         Boolean indicating if the provided nickname is valid
     * @param connectionEstablished Boolean indicating if the connection to the server is established
     */


    @Override
    public void displayLoginResponse(boolean validNickname, boolean connectionEstablished) {
        if (!connectionEstablished) {
            System.out.println("For some reason, it's impossible to establish a connection with the server.");
            System.exit(-1);
        } else if (!validNickname) {
            requestNickname();
        } else {
            System.out.println("Connection successful.");
            requestColor(null);
        }
    }

    /**
     * Displays the response to the color selection process,
     * prompting the user to choose another color if the selected
     * one is not available.
     *
     * @param acceptedColor    Boolean indicating if the chosen color was accepted
     * @param availableColor   Array of available colors to choose from
     */


    public void displayColorResponse(boolean acceptedColor, String[] availableColor) {
        if (!acceptedColor) {
            System.out.println("Color is not available...you have to chose another one");
            requestColor(availableColor);
        } else {
            System.out.println("Color accepted");
        }
    }

    /**
     * Initiates the setup of the starting card and secret target selection process.
     *
     * @param startingCard The starting card to choose from.
     * @param firstPossibleTargetCard The first possible target card to choose from.
     * @param secondPossibleTargetCard The second possible target card to choose from.
     */

    @Override
    public void requestToSetUpStartingAndSecret(AbstractCard startingCard, AbstractCard firstPossibleTargetCard, AbstractCard secondPossibleTargetCard) {
        // Displays chosen starting card
        System.out.println("Your starting card: " + startingCard.getID());
        System.out.println("Choose your starting card side:");
        System.out.println("1. For front side");
        System.out.println("2. For back side");

        // Asks user to choose between front or back starting card side
        boolean startingChoice = (getUserChoice(1, 2) == 1) ? (true) : (false);

        // Shows target card options to choose from
        System.out.println("Choose your secret target card:");
        System.out.println("1. First secret target card ID: " + firstPossibleTargetCard.getID());
        System.out.println("2. Second secret target card ID: " + secondPossibleTargetCard.getID());

        // Asks user to select target card
        String targetChoice = (getUserChoice(1, 2) == 1) ? (firstPossibleTargetCard.getID()) : (secondPossibleTargetCard.getID());

        // Notifies controller of user's choice
        clientController.onUpdateSecretTargetAndStarting(targetChoice, startingChoice);
    }

    /**
     * Displays a text message to the console.
     *
     * @param textMessage The text message to display
     */
    public void displayText(String textMessage) {
        System.out.println(textMessage);
    }

    /**
     * Displays the list of players in the chat, excluding the current client's nickname.
     * Adds a general recipient if more than one player exists.
     *
     * @param players List of player nicknames in the chat
     */


    @Override
    public void displayPlayersList(List<String> players) {
        boolean isFound = false;
        System.out.println("Player's list: ");
        chatPlayersNicknames.addAll(players);
        for (String nickname : players) {
            System.out.println(nickname);
        }
        for(int i = 0; i < chatPlayersNicknames.size() && !isFound; i++){
            if(chatPlayersNicknames.get(i).equals(clientController.getNickname())){
                chatPlayersNicknames.remove(i);
                isFound = true;     //What a time save!
            }
        }
        if(chatPlayersNicknames.size() != 1){
            chatPlayersNicknames.add(0, GENERAL_RECIPIENT);
        }
    }

    /**
            * Displays the leaderboard with player names and scores.
            *
            * @param scoreMap Map containing player nicknames as keys and scores as values
 */

    @Override
    public void displayScore(Map<String, Integer> scoreMap) {
        int progressivePosition = 1;
        System.out.println("LEADERBOARD: ");
        for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
            System.out.println(progressivePosition + ") " + entry.getKey() + ": " + entry.getValue() + " points");
            progressivePosition++;
        }
    }

    /**
     * Displays "Waiting..." message to indicate the client is waiting.
     */

    @Override
    public void inWaiting() {
        System.out.println("Waiting...");
    }

    /**
            * Displays "Let the games begin" message to indicate the game has started.
 */
    @Override
    public void inGame() {
        System.out.println("Let the games begin");
    }

    /**
     * Displays the manuscript (cards played) of a specific player.
     *
     * @param nickname         Player's nickname
     * @param manuscript       Manuscript of cards played
     * @param stringManuscript String representation of the manuscript
     */



    @Override
    public void displayManuscript(String nickname, AbstractCard[][] manuscript, String stringManuscript) {
        System.out.println("Manuscript of " + nickname);
        System.out.println(stringManuscript);
    }

    /**
     * Requests the client to choose a card to play and its placement details.
     */

    @Override
    public void requestCardToPlace() {
        int x, y, choice;

        do{
            System.out.println("Which card do you want to play?");
            System.out.println("1. First card");
            System.out.println("2. Second card");
            System.out.println("3. Third card");
            System.out.println("4. Send a chat text");
            choice = getUserChoice(1, 4) - 1;
            if(choice == 3){
                sendChatText();
            }
        } while(choice == 3);

        System.out.print("Enter the row where you want to place the card: ");
        x = scanner.nextInt();
        System.out.print("Enter the column where you want to place the card: ");
        y = scanner.nextInt();

        System.out.println("Which side do you want to place the card?");
        System.out.println("1. Front side");
        System.out.println("2. Back side");
        boolean side = (getUserChoice(1, 2)) == 1;

        clientController.onUpdatePlayCard(x, y, choice, side);
    }

    /**
     * Requests the client to choose a card to draw and
     */

    public void requestToDraw() {
        int choice;
        do{
            System.out.println("From which deck do you want to draw?");
            System.out.println("1.  resource deck");
            System.out.println("2.  gold deck");
            System.out.println("3.  resource exposed card1");
            System.out.println("4.  resource exposed card2");
            System.out.println("5.  gold exposed card1");
            System.out.println("6.  gold exposed card2");
            System.out.println("7.  Send a chat text");
            choice = getUserChoice(1, 7);
            if(choice == 7){
                sendChatText();
            }
        } while(choice == 7);
        clientController.onUpdateToDraw(choice);
    }

    private void sendChatText(){
        int choice;
        int i = 1;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select chat text recipient.");
        for(String s : chatPlayersNicknames){
            System.out.println(i + ".   " + s);
            i++;
        }
        choice = getUserChoice(1, chatPlayersNicknames.size()) - 1;
        System.out.print("Insert the text you'd like to send: ");
        String text = scanner.nextLine();
        updateChatMessage(getClientController().getNickname(), chatPlayersNicknames.get(choice), text);
    }

    /**
     * Displays the client's current hand cards.
     *
     * @param handCard_1 First hand card
     * @param handCard_2 Second hand card
     * @param handCard_3 Third hand target card
     */

    @Override
    public void displayHandsCard(AbstractCard handCard_1, AbstractCard handCard_2, AbstractCard handCard_3) {
        System.out.println("First hand card: " + handCard_1);
        System.out.println("Second hand card: " + handCard_2);
        System.out.println("Third hand target card: " + handCard_3);
    }

    /**
     * Displays the client's secret target card.
     *
     * @param secretTargetCard Secret target card
     * @param nickname         Player's nickname
     */

    @Override
    public void displaySecretTargetCard(AbstractCard secretTargetCard, String nickname) {
        System.out.println("Your secret target:" + secretTargetCard);
    }

    /**
     * Displays the common target cards.
     *
     * @param firstTargetCard  First common target card
     * @param secondTargetCard Second common target card
     */


    @Override
    public void displayCommonTargetCard(AbstractCard firstTargetCard, AbstractCard secondTargetCard) {
        System.out.println("First common target card: " + firstTargetCard);
        System.out.println("Second common target card: " + secondTargetCard);
    }




    private int readIntegerNumber() {
        int number;
        try {
            number = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException ignored) {
            number = 0;
        }
        return number;
    }

    private int getUserChoice(int min, int max) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.print("Enter your choice (" + min + "-" + max + "): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.next();
            }
            choice = scanner.nextInt();
        } while (choice < min || choice > max);
        return choice;
    }

    public ClientController getClientController() {
        return clientController;
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    /**
     * Displays the decks of cards and their current top cards.
     *
     * @param cards List of card identifiers for the decks and their top cards
     */

    @Override
    public void displayDecks(ArrayList<String> cards) {
        System.out.print("Cards displayed next to decks: [ ");
        for(int i = 0; i <= 3; i++){
            System.out.print(cards.get(i) + " ");
        }
        System.out.println("] ");
        System.out.print("Top covered resource card is: ");
        System.out.println(getResourceFromId(cards.get(5)));
        System.out.print("Top covered gold card is: ");
        System.out.println(getResourceFromId(cards.get(4)));
    }

    /**
     * Determines the resource type based on the provided ID.
     *
     * @param id The ID of the resource
     * @return The type of resource ("Fungi", "Plant", "Animal", "Insect") or "null" if ID is out of range
     */
    private String getResourceFromId(String id) {
        if ((Integer.parseInt(id) >= 1 && Integer.parseInt(id) <= 10) || (Integer.parseInt(id) >= 41 && Integer.parseInt(id) <= 50)) {
            return "Fungi";
        } else if ((Integer.parseInt(id) >= 11 && Integer.parseInt(id) <= 20) || (Integer.parseInt(id) >= 51 && Integer.parseInt(id) <= 60)) {
            return "Plant";
        } else if ((Integer.parseInt(id) >= 21 && Integer.parseInt(id) <= 30) || (Integer.parseInt(id) >= 61 && Integer.parseInt(id) <= 70)) {
            return "Animal";
        } else if ((Integer.parseInt(id) >= 31 && Integer.parseInt(id) <= 40) || (Integer.parseInt(id) >= 71 && Integer.parseInt(id) <= 80)) {
            return "Insect";
        } else {
            return "null";
        }
    }

    /**
     * Updates the chat message based on sender, recipient, and text content.
     * Redirects the message to the appropriate recipient or prints a notification message.
     *
     * @param senderNickname   The nickname of the sender
     * @param recipientNickname The nickname of the recipient
     * @param text             The text message content
     */
    @Override
    public void updateChatMessage(String senderNickname, String recipientNickname, String text) {
        if (senderNickname.equals(getClientController().getNickname())) {   // Client is sender
            clientController.updateChatMessage(senderNickname, recipientNickname, text);
        } else if (recipientNickname.equals(GENERAL_RECIPIENT)) {    // Client is the recipient of a general text
            System.out.println("GENERAL CHAT - From " + senderNickname + ": " + text);
        } else if (recipientNickname.equals(getClientController().getNickname())) { // Client is the recipient of a private text
            System.out.println("PRIVATE CHAT - From " + senderNickname + ": " + text);
        } else {
            System.out.println("Error in TUI updateChatMessage method");
        }
    }

    /**
     * Displays the final scores and completed objectives of each player.
     *
     * @param scoreMap           Map containing player nicknames as keys and scores as values
     * @param completedObjectives Map containing player nicknames as keys and completed objectives count as values
     */

    @Override
    public void displayFinalScore(Map<String, Integer> scoreMap, Map<String, Integer> completedObjectives){
        displayScore(scoreMap);
        for (String player : completedObjectives.keySet()) {
            Integer objectives = completedObjectives.get(player);
            System.out.println(player + " completed objectives: " + objectives);
        }
    }

    /**
    * Display anything for TUI, this is a Gui method
     */

    @Override
    public void updateReconnectedPlayerManuscript(String nickname, List<CardsPlayed> cardsPlayedByPlayer){}
}