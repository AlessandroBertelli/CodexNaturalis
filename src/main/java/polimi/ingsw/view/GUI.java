package polimi.ingsw.view;

import polimi.ingsw.model.*;
import polimi.ingsw.SocketAndRMI.CardsPlayed;
import polimi.ingsw.SocketAndRMI.ClientController;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.awt.Image;
import java.util.Map;
import java.util.List;
import javax.swing.ImageIcon;

public class GUI implements View {
    private ClientController clientController;
    private final JFrame frame;
    private final JPanel mainPanel;
    private final CardLayout cardLayout;

    private final List<String> chatPlayerNicknames = new ArrayList<>();
    private static String CHAT_LOG_FILE_NAME = "chat_log_file.txt";    //[P/G] - Sender - Recipient - text
    private static final String GENERAL_RECIPIENT = "General";
    private static File chatLogFile;
    private static int numPlayers;

    boolean par2 = true;
    boolean displayManuscript = true;
    boolean displayHandCard = true;

    GamePanel gamePanel = new GamePanel(this, numPlayers);

    List<String> currentHandCard = new ArrayList<>(3);
    String displayedCard[] = new String[6];

    static class ImagePanel extends JPanel {
        private BufferedImage image;

        public ImagePanel(String path) {
            try {
                image = ImageIO.read(new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public GUI() {
        frame = new JFrame("Codex Naturalis - AM23");
        ImageIcon icon = new ImageIcon("src/main/graphic_resources/GUI_stuff/mini_logo.jpg");
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /**
     * starts the view (GUI)
     */
    public void startView() {
        showWelcomeScreen();
    }

    /**
     * Displays the welcome screen with a background image, a "Start" button,
     * and handles the action to request server information upon button click.
     */
    private void showWelcomeScreen() {
        JPanel backgroundPanel = new ImagePanel("src/main/graphic_resources/GUI_stuff/background.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Verdana", Font.BOLD, 55));
        welcomeLabel.setForeground(Color.GRAY);
        welcomeLabel.setBackground(Color.BLACK);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            startButton.setEnabled(false);
            requestServerInformation();
        });

        backgroundPanel.add(welcomeLabel, BorderLayout.CENTER);
        backgroundPanel.add(startButton, BorderLayout.SOUTH);

        mainPanel.add(backgroundPanel, "welcome");
        cardLayout.show(mainPanel, "welcome");
        frame.setVisible(true);
    }

    /**
     * Requests server connection information via a dialog, allowing the user to choose
     * between Socket or RMI connection modes, specify the server IP address, and port.
     * Notifies the client controller with the selected connection details.
     */
    public void requestServerInformation() {
        JPanel connectionPanel = new JPanel();
        JLabel connection_setup = new JLabel("Connection mode:");
        connection_setup.setFont(new Font("Verdana", Font.BOLD, 19));
        String[] connectionModes = {"Socket", "RMI"};
        JComboBox<String> selectionMenu = new JComboBox<>(connectionModes);
        selectionMenu.setFont(new Font("Verdana", Font.PLAIN, 16));
        JLabel ip_address = new JLabel("Server IP address:");
        ip_address.setFont(new Font("Verdana", Font.BOLD, 19));
        JTextField server_ip_address = new JTextField("127.0.0.1", 16);
        server_ip_address.setFont(new Font("Verdana", Font.PLAIN, 16));
        JLabel port = new JLabel("Server port:");
        port.setFont(new Font("Verdana", Font.BOLD, 19));
        JTextField server_port = new JTextField("5033", 5);
        server_port.setFont(new Font("Verdana", Font.PLAIN, 16));

        connectionPanel.add(connection_setup);
        connectionPanel.add(selectionMenu);
        connectionPanel.add(ip_address);
        connectionPanel.add(server_ip_address);
        connectionPanel.add(port);
        connectionPanel.add(server_port);

        int choice = JOptionPane.showOptionDialog(null, connectionPanel, "Connection setup", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, connectionModes[0]);
        if (choice == JOptionPane.CLOSED_OPTION) {
            startView();
        } else {
            String user_selected_connection = (String) selectionMenu.getSelectedItem();
            boolean connectionBoolean = false;
            if (user_selected_connection != null && user_selected_connection.equals("RMI")) {
                connectionBoolean = true;
            }
            String user_selected_ip = server_ip_address.getText();
            int user_selected_port = Integer.parseInt(server_port.getText());
            clientController.onUpdateServerInfo(user_selected_ip, user_selected_port, connectionBoolean);    //true = rmi, false = socket
        }
    }

    /**
     * Requests the user to input their nickname via a dialog.
     * Notifies the client controller with the entered nickname.
     */
    @Override
    public void requestNickname() {
        JPanel nicknamePanel = new JPanel();
        JLabel nicknameLabel = new JLabel("Insert your nickname: ");
        nicknameLabel.setFont(new Font("Verdana", Font.BOLD, 19));
        JTextField user_nickname = new JTextField(10);
        user_nickname.setFont(new Font("Verdana", Font.PLAIN, 16));
        nicknamePanel.add(nicknameLabel);
        nicknamePanel.add(user_nickname);

        int choice = JOptionPane.showOptionDialog(null, nicknamePanel, "Nickname setup", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (choice == JOptionPane.CLOSED_OPTION) {
            clientController.disconnection();
        } else {
            clientController.onUpdatePlayerNick(user_nickname.getText());
        }
    }

    /**
     * Requests the number of players from the user via a dialog.
     * Validates the input and notifies the client controller with the number of players.
     */
    @Override
    public void requestPlayersNumber() {
        boolean isInputWrong = true;

        JPanel playerNumberPanel = new JPanel();
        JLabel playerNumberLabel = new JLabel("How many players do you want to play with (between 2 and 4)?");
        playerNumberLabel.setFont(new Font("Verdana", Font.BOLD, 19));
        JTextField user_player_number = new JTextField(3);
        user_player_number.setFont(new Font("Verdana", Font.PLAIN, 16));
        playerNumberPanel.add(playerNumberLabel);
        playerNumberPanel.add(user_player_number);

        while (isInputWrong) {
            int choice = JOptionPane.showOptionDialog(null, playerNumberPanel, "Player number setup", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (choice == JOptionPane.CLOSED_OPTION) {
                isInputWrong = false;
                displayText("Disconnecting from the server. You can safely close the program.");
                clientController.disconnection();
            } else {
                try {
                    if (Integer.parseInt(user_player_number.getText()) >= 2 && Integer.parseInt(user_player_number.getText()) <= 4) {
                        isInputWrong = false;
                        numPlayers = Integer.parseInt(user_player_number.getText());
                        clientController.onUpdateChosenNumberOfPlayer(numPlayers);
                    }
                } catch (NumberFormatException e) {
                    isInputWrong = true;
                }
            }
        }
    }


    /**
     * Requests the user to choose a color from available options via a dialog.
     * Notifies the client controller with the selected color.
     * param availableColor An array of available colors to choose from.
     */
    @Override
    public void requestColor(String[] availableColor) {
        int choice;
        JPanel colorPanel = new JPanel();
        JLabel color_setup = new JLabel("Choose your color:");
        color_setup.setFont(new Font("Verdana", Font.BOLD, 19));
        String[] colors = {"YELLOW", "RED", "GREEN", "BLUE"};
        JComboBox<String> selectionMenu;
        colorPanel.add(color_setup);

        if (availableColor != null && availableColor.length > 0) {
            selectionMenu = new JComboBox<>(availableColor);
        } else {
            selectionMenu = new JComboBox<>(colors);
        }
        selectionMenu.setFont(new Font("Verdana", Font.PLAIN, 16));
        colorPanel.add(selectionMenu);
        choice = JOptionPane.showOptionDialog(null, colorPanel, "Color setup", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (choice == JOptionPane.CLOSED_OPTION) {
            displayText("Disconnecting from the server. You can safely close the program.");
            clientController.disconnection();
        } else {
            String user_selected_color = (String) selectionMenu.getSelectedItem();
            clientController.onUpdateColor(user_selected_color);
        }
    }

    /**
     * Displays a response regarding the chosen color, either acceptance or rejection.
     * If rejected, prompts the user to choose another color.
     */
    public void displayColorResponse(boolean acceptedColor, String[] availableColor) {
        if (!acceptedColor) {
            JOptionPane.showMessageDialog(frame, "Color is not available... you have to chose another one", "colorError", JOptionPane.ERROR_MESSAGE);
            requestColor(availableColor);
        } else {
            JOptionPane.showMessageDialog(frame, "Color accepted.", "colorSuccess", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    /**
     * Displays the result of the login attempt, indicating connection status and nickname validity.
     * Prompts for nickname input again if invalid, or proceeds with color selection upon success.
     */
    @Override
    public void displayLoginResponse(boolean validNickname, boolean connectionEstablished) {
        if (!connectionEstablished) {
            JOptionPane.showMessageDialog(frame, "Unable to establish a connection with the server.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } else if (!validNickname) {
            requestNickname();
        } else {
            JOptionPane.showMessageDialog(frame, "Connection successful.", "Connected", JOptionPane.INFORMATION_MESSAGE);
            CHAT_LOG_FILE_NAME = clientController.getNickname() + "_" + CHAT_LOG_FILE_NAME;
            chatLogFile = new File(CHAT_LOG_FILE_NAME);
            createChatLog();
            deleteChatLogFileStarter();
            requestColor(null);
        }
    }

    /**
     * Displays a simple text message in a dialog box.
     */
    @Override
    public void displayText(String textMessage) {
        JOptionPane.showMessageDialog(frame, textMessage, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays the list of players currently in the game.
     * Updates the chat player nicknames list accordingly,
     * removing the nickname of the client player (can't send chat text to yourself)
     * and adding a "GENERAL_RECIPIENT" so there can be a general chat
     * if there are more than two players in a game.
     */
    @Override
    public void displayPlayersList(List<String> players) {
        displayText("Players in game: " + players);
        chatPlayerNicknames.addAll(players);
        boolean isFound = false;
        for (int i = 0; i < chatPlayerNicknames.size() && !isFound; i++) {
            if (chatPlayerNicknames.get(i).equals(clientController.getNickname())) {
                chatPlayerNicknames.remove(i);
                isFound = true;     //What a time save!
            }
        }
        if (chatPlayerNicknames.size() != 1) {
            chatPlayerNicknames.add(0, GENERAL_RECIPIENT);
        }
    }

    public List<String> getChatPlayerNicknames() {
        return chatPlayerNicknames;
    }

    /**
     * Displays the current game scores in a leaderboard format.
     */
    @Override
    public void displayScore(Map<String, Integer> scoreMap) {
        List<String[]> tableContent = new ArrayList<>();
        int position = 1;
        String[] columnIndexes = {"#", "Nickname", "Points"};
        tableContent.add(columnIndexes);
        for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
            String playerName = entry.getKey();
            int score = entry.getValue();
            String[] newRow = {String.valueOf(position) + "°", playerName, String.valueOf(score)};
            tableContent.add(newRow);
            position++;
        }
        gamePanel.updateLeaderboard(tableContent);
    }

    /**
     * Displays the final scores of players along with completed objectives in a dialog.
     */
    @Override
    public void displayFinalScore(Map<String, Integer> scoreMap, Map<String, Integer> completedObjectives) {
        displayScore(scoreMap);
        JPanel scorePanel = new JPanel(new GridLayout(scoreMap.size() + 1, 4));
        int i = 1;
        JLabel label;
        label = new JLabel("#");
        label.setFont(new Font("Verdana", Font.BOLD, 35));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        scorePanel.add(label);
        label = new JLabel("Player");
        label.setFont(new Font("Verdana", Font.BOLD, 35));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        scorePanel.add(label);
        label = new JLabel("Points");
        label.setFont(new Font("Verdana", Font.BOLD, 35));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        scorePanel.add(label);
        label = new JLabel("Objectives completed");
        label.setFont(new Font("Verdana", Font.BOLD, 25));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        scorePanel.add(label);
        for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
            label = new JLabel(i + "°");
            if (i == 1) {
                label.setFont(new Font("Verdana", Font.BOLD, 35));
            } else {
                label.setFont(new Font("Verdana", Font.PLAIN, 35));
            }
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            scorePanel.add(label);
            String playerNickname = entry.getKey();
            label = new JLabel(playerNickname);
            label.setFont(new Font("Verdana", Font.PLAIN, 35));
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            scorePanel.add(label);
            label = new JLabel(String.valueOf(entry.getValue()));
            label.setFont(new Font("Verdana", Font.PLAIN, 35));
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            scorePanel.add(label);
            label = new JLabel(String.valueOf(completedObjectives.get(playerNickname)));
            label.setFont(new Font("Verdana", Font.PLAIN, 25));
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            scorePanel.add(label);
            i++;
        }
        JOptionPane.showMessageDialog(null, scorePanel, "Final scores!", JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * Signals that the game is in a waiting state.
     * Disables the chat and displays "Waiting..." message.
     */
    @Override
    public void inWaiting() {
        gamePanel.disableChat();
        displayText("Waiting...");
    }

    /**
     * Signals that the game has started.
     * Switches the frame to display the game panel.
     * Enables the chat and displays "Let the games begin!" message.
     */
    @Override
    public void inGame() {
        frame.remove(mainPanel);
        frame.add(gamePanel);
        frame.setVisible(true);
        displayText("Let the games begin!");
        gamePanel.enableChat();
    }

    /**
     * Adds a card to the current hand based on the choice made.
     * Notifies the client controller of the update.
     *
     * @param choice The choice indicating which card to draw.
     */

    public void drawCard(int choice) {
        if (gamePanel.getCardPlayed()) {
            String id = "0";

            if (choice == 5) {
                id = displayedCard[0];
            }
            if (choice == 6) {
                id = displayedCard[1];
            }
            if (choice == 3) {
                id = displayedCard[2];
            }
            if (choice == 4) {
                id = displayedCard[3];
            }
            if (choice == 2) {
                id = displayedCard[4];
            }
            if (choice == 1) {
                id = displayedCard[5];
            }
            currentHandCard.add(id);
            clientController.onUpdateToDraw(choice);
            gamePanel.setCardPlayed(false);
        }
    }

    /**
     * Initiates playing a card on the game panel.
     * Notifies the client controller of the play card action.
     */

    public void playCard(int x, int y, int choice, boolean isFront) {
        if (!gamePanel.getCardPlayed()) {
            clientController.onUpdatePlayCard(x, y, choice, isFront);
        }
    }

    /**
     * Displays a manuscript by adding an image to the grid panel.
     */

    @Override
    public void displayManuscript(String nickname, AbstractCard[][] manuscript, String stringManuscript) {
        if (displayManuscript) {
            String c = fixNumberLength(manuscript[1][1].getID());
            GamePanel.ZoomableGridPanel gridPanel = gamePanel.getGridPanel();

            for (Component component : gamePanel.getComponents()) {
                if (component instanceof GamePanel.ZoomableGridPanel) {
                    gridPanel = (GamePanel.ZoomableGridPanel) component;
                    break;
                }
            }

            if (gridPanel != null) {
                displayManuscript = false;
                if (par2) {
                    try {
                        // Add image to matrix
                        BufferedImage matrixImage = ImageIO.read(new File("src/main/graphic_resources/cards_front/" + c + ".png"));
                        gridPanel.addImageAt(matrixImage, 40, 40);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        // Add image to matrix
                        BufferedImage matrixImage = ImageIO.read(new File("src/main/graphic_resources/cards_back/" + c + ".png"));
                        gridPanel.addImageAt(matrixImage, 40, 40);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.err.println("ZoomableGridPanel not found in GamePanel.");
            }
        }
    }

    /**
     * Requests the player to place a card.
     */
    public void requestCardToPlace() {
        gamePanel.resetBoxDefaultOption();
        displayText("Play a card");
    }

    /**
     * Displays two common target cards.
     *
     * @param firstTargetCard  The first target card.
     * @param secondTargetCard The second target card.
     */

    public void displayCommonTargetCard(AbstractCard firstTargetCard, AbstractCard secondTargetCard) {
        //GamePanel.ZoomableGridPanel gridPanel = gamePanel.getGridPanel();
        String c1 = fixNumberLength(firstTargetCard.getID());
        String c2 = fixNumberLength(secondTargetCard.getID());
        try {
            JPanel topLeftPanel = (JPanel) gamePanel.getComponent(1);
            GamePanel.ImagePanel imagePanel = (GamePanel.ImagePanel) topLeftPanel.getComponent(0);
            BufferedImage rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_front/" + c1 + ".png"));
            imagePanel.setImage(rectImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JPanel topLeftPanel = (JPanel) gamePanel.getComponent(1);
            GamePanel.ImagePanel imagePanel = (GamePanel.ImagePanel) topLeftPanel.getComponent(1);
            BufferedImage rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_front/" + c2 + ".png"));
            imagePanel.setImage(rectImage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Displays the cards in hand.
     *
     * @param handCard_1 The first hand card.
     * @param handCard_2 The second hand card.
     * @param handCard_3 The third hand card.
     */

    @Override
    public void displayHandsCard(AbstractCard handCard_1, AbstractCard handCard_2, AbstractCard handCard_3) {

        if (displayHandCard) {
            String c1 = fixNumberLength(handCard_1.getID());
            String c2 = fixNumberLength(handCard_2.getID());
            String c3 = fixNumberLength(handCard_3.getID());

            currentHandCard.add(c1);
            currentHandCard.add(c2);
            currentHandCard.add(c3);
            displayHandCard = false;
        }
        BufferedImage rectImage;
        try {
            JPanel topLeftPanel = (JPanel) gamePanel.getComponent(4);
            GamePanel.ImagePanel imagePanel = (GamePanel.ImagePanel) topLeftPanel.getComponent(0);
            rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_front/" + fixNumberLength(currentHandCard.get(0)) + ".png"));
            imagePanel.setImage(rectImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            JPanel topLeftPanel = (JPanel) gamePanel.getComponent(4);
            GamePanel.ImagePanel imagePanel = (GamePanel.ImagePanel) topLeftPanel.getComponent(1);
            rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_front/" + fixNumberLength(currentHandCard.get(1)) + ".png"));
            imagePanel.setImage(rectImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JPanel topLeftPanel = (JPanel) gamePanel.getComponent(4);
            GamePanel.ImagePanel imagePanel = (GamePanel.ImagePanel) topLeftPanel.getComponent(2);
            rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_front/" + fixNumberLength(currentHandCard.get(2)) + ".png"));
            imagePanel.setImage(rectImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays cards from their IDs.
     *
     * @param id1     The ID of the first card.
     * @param id2     The ID of the second card.
     * @param id3     The ID of the third card.
     * @param isFront True if the front of the cards should be displayed, false if the back should be displayed.
     */

    public void displayHandsCardFromId(String id1, String id2, String id3, boolean isFront) {
        BufferedImage rectImage;
        try {
            JPanel topLeftPanel = (JPanel) gamePanel.getComponent(4);
            GamePanel.ImagePanel imagePanel = (GamePanel.ImagePanel) topLeftPanel.getComponent(0);
            if (id1.equals("102")) {
                rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_back/102.png"));
            } else if (isFront) {
                rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_front/" + fixNumberLength(id1) + ".png"));
            } else {
                rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_back/" + fixNumberLength(id1) + ".png"));
            }
            imagePanel.setImage(rectImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JPanel topLeftPanel = (JPanel) gamePanel.getComponent(4);
            GamePanel.ImagePanel imagePanel = (GamePanel.ImagePanel) topLeftPanel.getComponent(1);
            if (id2.equals("102")) {
                rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_back/102.png"));
            } else if (isFront) {
                rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_front/" + fixNumberLength(id2) + ".png"));
            } else {
                rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_back/" + fixNumberLength(id2) + ".png"));
            }
            imagePanel.setImage(rectImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JPanel topLeftPanel = (JPanel) gamePanel.getComponent(4);
            GamePanel.ImagePanel imagePanel = (GamePanel.ImagePanel) topLeftPanel.getComponent(2);
            if (id3.equals("102")) {
                rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_back/102.png"));
            } else if (isFront) {
                rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_front/" + fixNumberLength(id3) + ".png"));
            } else {
                rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_back/" + fixNumberLength(id3) + ".png"));
            }
            imagePanel.setImage(rectImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Displays a deck of cards.
     *
     * @param cardsIDs The list of card IDs to be displayed.
     */

    @Override
    public void displayDecks(ArrayList<String> cardsIDs) {
        JPanel middleLeftPanel = (JPanel) gamePanel.getComponent(2);
        try {
            int i = 0;
            for (Component component : middleLeftPanel.getComponents()) {
                if (component instanceof GamePanel.ImagePanel) {
                    GamePanel.ImagePanel imagePanel = (GamePanel.ImagePanel) component;
                    BufferedImage rectImage;
                    if (cardsIDs.get(i).equals("102")) {
                        rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_back/102.png"));   //Error image, deck (almost) empty
                    } else if (i == 4 || i == 5) {
                        rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_back/" + fixNumberLength(cardsIDs.get(i)) + ".png"));
                    } else {
                        rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_front/" + fixNumberLength(cardsIDs.get(i)) + ".png"));
                    }
                    imagePanel.setImage(rectImage);
                    displayedCard[i] = cardsIDs.get(i);
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Displays the secret targetCard of cards.
     */
    @Override
    public void displaySecretTargetCard(AbstractCard secretTargetCard, String nickname) {
        String finalNumber = fixNumberLength(secretTargetCard.getID());
        try {

            JPanel topLeftPanel = (JPanel) gamePanel.getComponent(1);
            GamePanel.ImagePanel imagePanel = (GamePanel.ImagePanel) topLeftPanel.getComponent(2);
            BufferedImage rectImage = ImageIO.read(new File("src/main/graphic_resources/cards_front/" + finalNumber + ".png"));
            imagePanel.setImage(rectImage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initiates a request to draw a card, updating the game panel based on player choice.
     */

    public void requestToDraw() {
        gamePanel.setCardPlayed(true);
        int choice = gamePanel.getChoice();
        boolean isFront = gamePanel.getIsFront();
        insertPlayedCard();
        if (choice == 0) {
            displayHandsCardFromId("102", currentHandCard.get(1), currentHandCard.get(2), isFront);
        } else if (choice == 1) {
            displayHandsCardFromId(currentHandCard.get(0), "102", currentHandCard.get(2), isFront);
        } else {
            displayHandsCardFromId(currentHandCard.get(0), currentHandCard.get(1), "102", isFront);
        }
        currentHandCard.remove(choice);
        //currentHandCard.add("delete");
        displayText("Draw a card!");
    }

    /**
     * Retrieves the client controller associated with this class.
     *
     * @return The ClientController instance.
     */
    public ClientController getClientController() {
        return clientController;
    }

    /**
     * Sets the client controller to be used by this class.
     *
     * @param clientController The ClientController instance to set.
     */
    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    /**
     * Initiates the setup of the starting card and secret target selection process.
     *
     * @param startingCard             The starting card to choose from.
     * @param firstPossibleTargetCard  The first possible target card to choose from.
     * @param secondPossibleTargetCard The second possible target card to choose from.
     */

    public void requestToSetUpStartingAndSecret(AbstractCard startingCard, AbstractCard firstPossibleTargetCard, AbstractCard secondPossibleTargetCard) {
        String par1;

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());

//front and back sides//
        String[] sides = {"FRONT", "BACK"};
        JComboBox<String> selectionMenu = new JComboBox<>(sides);
        selectionMenu.setFont(new Font("Verdana", Font.PLAIN, 16));
        cardPanel.add(selectionMenu, BorderLayout.WEST);

        JLabel cardSetup = new JLabel("Choose your starting card side:");
        cardSetup.setFont(new Font("Verdana", Font.BOLD, 19));
        cardPanel.add(cardSetup, BorderLayout.NORTH);

        // add panel with two cards
        JPanel imagesPanel = new JPanel();
        imagesPanel.setLayout(new GridLayout(1, 2)); // One row, two columns

        // add front image at left
        ImageIcon frontCardImage = getFrontCardImage(startingCard);
        ImageIcon smallFrontCardImage = resizeImageIcon(frontCardImage, 400, 200);
        JLabel frontCardImageLabel = new JLabel(smallFrontCardImage);
        imagesPanel.add(frontCardImageLabel);

        // add back image at right
        ImageIcon backCardImage = getBackCardImage(startingCard);
        ImageIcon smallBackCardImage = resizeImageIcon(backCardImage, 398, 198);
        JLabel backCardImageLabel = new JLabel(smallBackCardImage);
        imagesPanel.add(backCardImageLabel);

        // add image panel at the center
        cardPanel.add(imagesPanel, BorderLayout.CENTER);

        int choice = JOptionPane.showOptionDialog(
                null,
                cardPanel,
                "Card Side Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null
        );

        if (choice == JOptionPane.CLOSED_OPTION) {
            displayText("Disconnecting from the server. You can safely close the program.");
            clientController.disconnection();
        } else {
            String selectedSide = (String) selectionMenu.getSelectedItem();
            if (selectedSide != null && selectedSide.equals("BACK")) {
                par2 = false;

            }

            par1 = chooseOwnTargetCard(firstPossibleTargetCard, secondPossibleTargetCard, par2);
            clientController.onUpdateSecretTargetAndStarting(par1, par2);

            frame.remove(mainPanel);
            frame.add(gamePanel);
            frame.setVisible(true);
        }
    }


    /**
     * Allows the player to choose their own target card from two options.
     *
     * @param TargetCardOne The first possible target card to choose from.
     * @param TargetCardTwo The second possible target card to choose from.
     * @param x             Boolean indicating whether the card is front-facing or not.
     * @return The ID of the chosen target card.
     */

    public String chooseOwnTargetCard(AbstractCard TargetCardOne, AbstractCard TargetCardTwo, boolean x) {
        String par1 = TargetCardTwo.getID();

        JPanel TargetCardPanel = new JPanel();
        TargetCardPanel.setLayout(new BorderLayout());

        //front and back sides//
        String[] sides = {"LEFT", "RIGHT"};
        JComboBox<String> selectionMenu = new JComboBox<>(sides);
        selectionMenu.setFont(new Font("Verdana", Font.PLAIN, 16));
        TargetCardPanel.add(selectionMenu, BorderLayout.WEST);

        JLabel cardSetup = new JLabel("Choose your target card:");
        cardSetup.setFont(new Font("Verdana", Font.BOLD, 19));
        TargetCardPanel.add(cardSetup, BorderLayout.NORTH);

        // add panel with two cards
        JPanel imagesPanelTarget = new JPanel();
        imagesPanelTarget.setLayout(new GridLayout(1, 2));

        // add front image at left
        ImageIcon frontCardImage = getFrontCardImage(TargetCardOne);
        ImageIcon smallFrontCardImage = resizeImageIcon(frontCardImage, 400, 200);
        JLabel frontCardImageLabel = new JLabel(smallFrontCardImage);
        imagesPanelTarget.add(frontCardImageLabel);

        // add front image at right
        ImageIcon FrontCardImage = getFrontCardImage(TargetCardTwo);
        ImageIcon smallFrontCardImage2 = resizeImageIcon(FrontCardImage, 398, 198);
        JLabel FrontCardImageLabel = new JLabel(smallFrontCardImage2);
        imagesPanelTarget.add(FrontCardImageLabel);

        // add image panel at the center
        TargetCardPanel.add(imagesPanelTarget, BorderLayout.CENTER);

        int choice = JOptionPane.showOptionDialog(
                null,
                TargetCardPanel,
                "Target card selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null
        );

        if (choice == JOptionPane.CLOSED_OPTION) {
            displayText("Disconnecting from the server. You can safely close the program.");
            clientController.disconnection();
        } else {
            String selectedTargetCard = (String) selectionMenu.getSelectedItem();
            if ("LEFT".equals(selectedTargetCard)) {
                par1 = TargetCardOne.getID();

            }

        }
        return par1;

    }


    /**
     * Retrieves an icon of the front image of the specified card.
     *
     * @param card The card to retrieve the front image icon for.
     * @return An icon of the front image of the card.
     */
    private ImageIcon getFrontCardImage(AbstractCard card) {
        String imagePath = "src/main/graphic_resources/cards_front/" + fixNumberLength(card.getID()) + ".png";
        return new ImageIcon(imagePath);
    }

    /**
     * Retrieves an icon of the back image of the specified card.
     *
     * @param card The card to retrieve the back image icon for.
     * @return An icon of the back image of the card.
     */
    private ImageIcon getBackCardImage(AbstractCard card) {
        String imagePath = "src/main/graphic_resources/cards_back/" + fixNumberLength(card.getID()) + ".png";
        return new ImageIcon(imagePath);
    }

    /**
     * Resizes an ImageIcon to the specified width and height.
     *
     * @param icon   The ImageIcon to resize.
     * @param width  The desired width of the resized image.
     * @param height The desired height of the resized image.
     * @return Resized ImageIcon.
     */

    public ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    /**
     * Fixes the length of a string by adding leading zeros if its length is less than 3.
     *
     * @param s The string to fix the length of.
     * @return String with fixed length.
     */
    private String fixNumberLength(String s) {
        if (s.length() < 3) {
            String zeros = "0".repeat(3 - s.length());
            s = zeros + s;
        }
        return s;
    }

    /**
     * Inserts the played card into the game panel's grid based on player choice.
     * Updates the displayed cards accordingly.
     */

    public void insertPlayedCard() {

        int choice = gamePanel.getChoice();
        int[] matrix = gamePanel.getMatrixClick();
        boolean isFront = gamePanel.getIsFront();

        if (choice == 0) {
            GamePanel.ZoomableGridPanel gridPanel = gamePanel.getGridPanel();
            try {
                // Add image to matrix
                BufferedImage matrixImage;
                if (isFront) {
                    matrixImage = ImageIO.read(new File("src/main/graphic_resources/cards_front/" + fixNumberLength(currentHandCard.get(0)) + ".png"));
                } else {
                    matrixImage = ImageIO.read(new File("src/main/graphic_resources/cards_back/" + fixNumberLength(currentHandCard.get(0)) + ".png"));
                }
                gridPanel.addImageAt(matrixImage, matrix[0], matrix[1]);
                // currentHandCard.get(0) = "delete";

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (choice == 1) {
            GamePanel.ZoomableGridPanel gridPanel = gamePanel.getGridPanel();
            try {

                // Add image to matrix
                BufferedImage matrixImage;
                if (isFront) {
                    matrixImage = ImageIO.read(new File("src/main/graphic_resources/cards_front/" + fixNumberLength(currentHandCard.get(1)) + ".png"));
                } else {
                    matrixImage = ImageIO.read(new File("src/main/graphic_resources/cards_back/" + fixNumberLength(currentHandCard.get(1)) + ".png"));
                }
                gridPanel.addImageAt(matrixImage, matrix[0], matrix[1]);
                // currentHandCard[1] = "delete";

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (choice == 2) {
            GamePanel.ZoomableGridPanel gridPanel = gamePanel.getGridPanel();
            try {
                // Add image to matrix
                BufferedImage matrixImage;
                if (isFront) {
                    matrixImage = ImageIO.read(new File("src/main/graphic_resources/cards_front/" + fixNumberLength(currentHandCard.get(2)) + ".png"));
                } else {
                    matrixImage = ImageIO.read(new File("src/main/graphic_resources/cards_back/" + fixNumberLength(currentHandCard.get(2)) + ".png"));
                }
                gridPanel.addImageAt(matrixImage, matrix[0], matrix[1]);
                //  currentHandCard[2] = "delete";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the chat message in the game interface and logs it to a file.
     *
     * @param senderNickname    The nickname of the sender.
     * @param recipientNickname The nickname of the recipient.
     * @param text              The text of the chat message.
     */

    @Override
    public void updateChatMessage(String senderNickname, String recipientNickname, String text) {
        char privateOrNotCheck;
        if (senderNickname.equals(clientController.getNickname())) {  // Client is the sender
            if (recipientNickname.equals(GENERAL_RECIPIENT)) {
                privateOrNotCheck = 'G';
            } else {
                privateOrNotCheck = 'P';
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter(CHAT_LOG_FILE_NAME, true))) {
                writer.print(privateOrNotCheck + " - ");
                writer.print(senderNickname + " - ");
                writer.print(recipientNickname + " - ");
                writer.println(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
            clientController.updateChatMessage(senderNickname, recipientNickname, text);
        } else {
            if (recipientNickname.equals(GENERAL_RECIPIENT)) {    // Client is the recipient of a general text
                privateOrNotCheck = 'G';
            } else {                                               // Client is the recipient of a private text
                privateOrNotCheck = 'P';
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter(CHAT_LOG_FILE_NAME, true))) {
                writer.print(privateOrNotCheck + " - ");
                writer.print(senderNickname + " - ");
                writer.print(clientController.getNickname() + " - ");
                writer.println(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JTextArea messageArea = gamePanel.getMessageArea(senderNickname);
            if (messageArea != null) {
                gamePanel.loadAllTexts();
            }
            playNotificationSound();
        }
    }

    /**
     * Retrieves the file name used for logging chat messages.
     *
     * @return The chat log file name.
     */
    public String getChatLogFileName() {
        return CHAT_LOG_FILE_NAME;
    }

    /**
     * Retrieves the nickname of the general recipient for chat messages.
     *
     * @return The general recipient's nickname.
     */
    public String getGeneralRecipientName() {
        return GENERAL_RECIPIENT;
    }

    /**
     * Creates the chat log file if it doesn't exist.
     */
    private void createChatLog() {
        if (!chatLogFile.exists()) {
            try {
                chatLogFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // Uncomment if you want to print the current directory
        // System.out.println("Current directory: " + System.getProperty("user.dir"));
    }

    /**
     * Registers a shutdown hook to delete the chat log file upon program termination.
     */
    public static void deleteChatLogFileStarter() {
        Runtime.getRuntime().addShutdownHook(new Thread(GUI::deleteChatLogFile));
    }

    /**
     * Deletes the chat log file if it exists.
     */
    public static void deleteChatLogFile() {
        if (chatLogFile.exists()) {
            chatLogFile.delete();
        }
    }

    /**
     * Plays a notification sound when a new chat message is received.
     */

    private void playNotificationSound() {
        //sound: mixkit-correct-answer-tone-2870 / https://mixkit.co/free-sound-effects/notification/
        try {
            File file = new File("src/main/graphic_resources/audio_resources/notification_sound.wav");
            AudioInputStream ais = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateReconnectedPlayerManuscript(String nickname, List<CardsPlayed> cardsPlayedByPlayer) {
        GamePanel.ZoomableGridPanel gridPanel = gamePanel.getGridPanel();
        for (CardsPlayed card : cardsPlayedByPlayer) {
            try {
                BufferedImage matrixImage;
                if (card.isFront()) {
                    matrixImage = ImageIO.read(new File("src/main/graphic_resources/cards_front/" + fixNumberLength(card.getId()) + ".png"));
                } else {
                    matrixImage = ImageIO.read(new File("src/main/graphic_resources/cards_back/" + fixNumberLength(card.getId()) + ".png"));
                }
                gridPanel.addImageAt(matrixImage, card.getCell().getRow(), card.getCell().getColumn());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}