package polimi.ingsw.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the main game panel that includes various components such as a grid panel,
 * rectangles for actions, leaderboard, chat functionality, and popup menus.
 */

public class GamePanel extends JPanel {

    int[] MatrixClick = {-1, -1};
    private boolean isFront = true;
    private final double zoomFactor = 2.0;
    private final JButton chatButton;
    private JTabbedPane tabbedPane;
    private final JComboBox<String> comboBox;
    private final String[] columnIndexes = {"#", "Nickname", "Points"};
    private final Object[][] tableInfo = {{"#", "Nickname", "Points"}};
    private final DefaultTableModel tableModel = new DefaultTableModel(tableInfo, columnIndexes);
    private final JTable leaderboardTable;

    private int choice = 0;
    private boolean cardPlayed = false;

    private static final int CELL_WIDTH = 63;
    private static final int CELL_HEIGHT = 43;
    //private static final int GRID_SIZE = 80;

    private final ZoomableGridPanel gridPanel;
    private final GUI gui;


    /**
     * Constructor for initializing the game panel with GUI components.
     *
     * @param gui        The GUI instance associated with the game panel
     * @param numPlayers The number of players in the game
     */

    public GamePanel(GUI gui, int numPlayers) {
        this.gui = gui;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gridPanel = new ZoomableGridPanel(zoomFactor, 35, 34);
        gridPanel.setPreferredSize(new Dimension(378, 258));    //6x6
        tabbedPane = new JTabbedPane();

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.65;
        gbc.weighty = 1.0;
        add(gridPanel, gbc);

        gridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gridPanel.requestFocusInWindow();
            }
        });


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                gridPanel.requestFocusInWindow();
            }
        });

        // Three rectangles top left
        JPanel topLeftPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        for (int i = 0; i < 3; i++) {
            ImagePanel panel = new ImagePanel();
            panel.setPreferredSize(new Dimension(CELL_WIDTH, CELL_HEIGHT));
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            topLeftPanel.add(panel);
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        add(topLeftPanel, gbc);

        // Six rectangles under the three ones top left, with some space
        JPanel middleLeftPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        for (int i = 0; i < 6; i++) {
            ImagePanel panel = new ImagePanel();
            panel.setPreferredSize(new Dimension(CELL_WIDTH, CELL_HEIGHT));
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            if (i == 0) {
                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        gui.drawCard(5);
                    }
                });
            } else if (i == 1) {
                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        gui.drawCard(6);
                    }
                });
            } else if (i == 2) {
                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        gui.drawCard(3);
                    }
                });
            } else if (i == 3) {
                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        gui.drawCard(4);
                    }
                });
            } else if (i == 4) {
                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        gui.drawCard(2);
                    }
                });
            } else {
                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        gui.drawCard(1);
                    }
                });
            }
            middleLeftPanel.add(panel);
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        gbc.weightx = 0.2;
        gbc.weighty = 0.7;
        gbc.insets = new Insets(60, 0, 0, 0); // Space between panels
        add(middleLeftPanel, gbc);

        // Selection menu over the rectangles in the bottom right
        JPanel bottomRightTopPanel = new JPanel();

        gbc.gridx = 3;
        gbc.gridy = 2; // Moves menu to the row over the rectangles
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 0, 10, 0); // Adds a space between the menu and the rectangles
        add(bottomRightTopPanel, gbc);

        // Three rectangles in the bottom right
        JPanel bottomRightPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        for (int i = 0; i < 3; i++) {
            ImagePanel panel = new ImagePanel();
            panel.setPreferredSize(new Dimension(CELL_WIDTH, CELL_HEIGHT));
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            final int panelNumber = i + 1;

            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (panelNumber == 1) {
                        choice = 0;
                        gui.playCard(MatrixClick[0], MatrixClick[1], 0, isFront);
                    }
                    if (panelNumber == 2) {
                        choice = 1;
                        gui.playCard(MatrixClick[0], MatrixClick[1], 1, isFront);
                    }
                    if (panelNumber == 3) {
                        choice = 2;
                        gui.playCard(MatrixClick[0], MatrixClick[1], 2, isFront);
                    }
                    //resetBoxDefaultOption();
                }
            });
            bottomRightPanel.add(panel);
        }

        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 0, 0); // Removes any extra spaces
        add(bottomRightPanel, gbc);

        // Popup area
        JPanel popupPanel = new JPanel();
        popupPanel.setPreferredSize(new Dimension(150, 100)); // More compact dimensions
        popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.Y_AXIS));

        String[] options = {"Front", "Back"};
        comboBox = new JComboBox<>(options);
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBox.setMaximumSize(new Dimension(80, 25));
        popupPanel.add(Box.createVerticalStrut(20));

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if ("Front".equals(comboBox.getSelectedItem())) {
                        isFront = true;
                        List<String> handCards = gui.currentHandCard;
                        gui.displayHandsCardFromId(handCards.get(0), handCards.get(1), handCards.get(2), isFront);
                    } else {
                        isFront = false;
                        List<String> handCards = gui.currentHandCard;
                        gui.displayHandsCardFromId(handCards.get(0), handCards.get(1), handCards.get(2), isFront);
                    }

                } catch (NumberFormatException ex) {
                    System.out.println("NumberFormatException occurred.");
                }
            }
        });

        popupPanel.add(Box.createVerticalStrut(7)); // Space under Popup panel

        leaderboardTable = new JTable(tableModel);
        leaderboardTable.setPreferredScrollableViewportSize(new Dimension(140, 30 * numPlayers));
        leaderboardTable.setRowHeight(30);
        leaderboardTable.setFillsViewportHeight(true);
        leaderboardTable.setDefaultEditor(Object.class, null);

        TableColumnModel columnModel = leaderboardTable.getColumnModel();
        int columnIndex = 0;
        int newWidth = 20;
        TableColumn desiredColumn = columnModel.getColumn(columnIndex);
        desiredColumn.setPreferredWidth(newWidth);
        columnIndex = 1;
        newWidth = 100;
        desiredColumn = columnModel.getColumn(columnIndex);
        desiredColumn.setPreferredWidth(newWidth);
        columnIndex = 2;
        newWidth = 20;
        desiredColumn = columnModel.getColumn(columnIndex);
        desiredColumn.setPreferredWidth(newWidth);


        chatButton = new JButton("CHAT");
        chatButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        chatButton.addActionListener(e -> {
            tabbedPane = new JTabbedPane();
            for (int i = 0; i < gui.getChatPlayerNicknames().size(); i++) {
                JPanel chatPanel = new JPanel(new BorderLayout());
                JTextArea messageArea = new JTextArea(25, 45);
                messageArea.setEditable(false);
                JScrollPane messageScrollPane = new JScrollPane(messageArea);
                JTextField inputTextField = new JTextField("Type your message here!");
                inputTextField.setFont(new Font("Verdana", Font.PLAIN, 17));
                JButton sendButton = new JButton("Send");
                sendButton.setFont(new Font("Verdana", Font.BOLD, 20));
                JPanel inputPanel = new JPanel(new BorderLayout());
                chatPanel.add(messageScrollPane, BorderLayout.CENTER);
                inputPanel.add(inputTextField, BorderLayout.CENTER);
                inputPanel.add(sendButton, BorderLayout.SOUTH);
                chatPanel.add(inputPanel, BorderLayout.SOUTH);

                tabbedPane.addTab(gui.getChatPlayerNicknames().get(i), chatPanel);
                loadSingleChatTexts(gui.getChatLogFileName(), messageArea, gui.getChatPlayerNicknames().get(i));
                int const_i = i;
                sendButton.addActionListener(e1 -> {
                    String message = inputTextField.getText();
                    if (!message.trim().isEmpty()) {
                        messageArea.append(gui.getClientController().getNickname() + ": " + message + "\n");
                        inputTextField.setText("");
                        gui.updateChatMessage(gui.getClientController().getNickname(), gui.getChatPlayerNicknames().get(const_i), message);
                    }
                });
            }
            JOptionPane.showMessageDialog(null, tabbedPane, "Chat", JOptionPane.INFORMATION_MESSAGE);
        });
        popupPanel.add(Box.createVerticalStrut(20));
        //popupPanel.add(leaderboardButton);
        popupPanel.add(leaderboardTable);
        popupPanel.add(Box.createVerticalStrut(20));
        popupPanel.add(chatButton);
        popupPanel.add(Box.createVerticalStrut(30));
        popupPanel.add(comboBox);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        add(popupPanel, gbc);
    }


    /**
     * Retrieves the current choice selected by the user.
     *
     * @return The current choice selected
     */

    public int getChoice() { return choice; }


    /**
     * Retrieves the current state of whether the front or back of a card is selected.
     *
     * @return True if front is selected, false if back is selected
     */

    public boolean getIsFront() {
        return isFront;
    }


    /**
     * Retrieves the current matrix click coordinates.
     *
     * @return The matrix click coordinates
     */
    public int[] getMatrixClick() {
        return MatrixClick;
    }

    /**
     * Resets the default option of the combo box.
     */

    public void resetBoxDefaultOption() {
        comboBox.setSelectedIndex(0);
    }

    /**
     * Enables the chat functionality.
     */
    public void enableChat() {
        chatButton.setEnabled(true);
    }

    /**
     * Disables the chat functionality.
     */
    public void disableChat() {
        chatButton.setEnabled(false);
    }

    /**
     * Updates the leaderboard with the given table content.
     *
     * @param tableContent The content to update the leaderboard with
     */
    public void updateLeaderboard(List<String[]> tableContent) {
        tableModel.setRowCount(0);
        for (String[] row : tableContent) {
            tableModel.addRow(row);
        }
    }

    /**
     * Retrieves the message area associated with a specific chat tab.
     *
     * @param title The title of the chat tab
     * @return The message area of the specified chat tab, or null if not found
     */

    public JTextArea getMessageArea(String title) {
        int tabIndex = tabbedPane.indexOfTab(title);
        if (tabIndex != -1) {
            JPanel tabPanel = (JPanel) tabbedPane.getComponentAt(tabIndex);
            JScrollPane scrollPane = (JScrollPane) tabPanel.getComponent(0);
            return (JTextArea) scrollPane.getViewport().getComponent(0);
        } else {
            return null;
        }
    }


    /**
     * Retrieves the grid panel used in the game.
     *
     * @return The grid panel.
     */
    public ZoomableGridPanel getGridPanel() {
        return gridPanel;
    }

    /**
     * Loads chat messages from a file into a specified message area.
     *
     * @param fileName       The name of the file containing chat messages.
     * @param messageArea    The JTextArea where the chat messages will be displayed.
     * @param playerNickname The nickname of the player whose chat messages are to be loaded.
     */

    private void loadSingleChatTexts(String fileName, JTextArea messageArea, String playerNickname) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                String[] subStrings = line.split(" - ");
                if (playerNickname.equals(gui.getGeneralRecipientName()) && subStrings[0].equals("G")) {   // General chat texts
                    sb.append(subStrings[1]).append(": ").append(subStrings[3]).append("\n");
                } else if ((playerNickname.equals(subStrings[1]) || playerNickname.equals(subStrings[2])) && subStrings[0].equals("P")) {  // Private texts
                    sb.append(subStrings[1]).append(": ").append(subStrings[3]).append("\n");
                }
            }
            messageArea.setText(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads all chat messages from the chat log file into the respective message areas for each player.
     * The method iterates through all chat player nicknames and updates the message areas with
     * both general and private chat texts.
     */

    public void loadAllTexts() {
        String line;
        for (int i = 0; i < gui.getChatPlayerNicknames().size(); i++) {
            try (BufferedReader reader = new BufferedReader(new FileReader(gui.getChatLogFileName()))) {
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    String[] subStrings = line.split(" - ");
                    if (gui.getChatPlayerNicknames().get(i).equals(gui.getGeneralRecipientName()) && subStrings[0].equals("G")) {   // General chat texts
                        sb.append(subStrings[1]).append(": ").append(subStrings[3]).append("\n");
                    } else if (((gui.getChatPlayerNicknames().get(i).equals(subStrings[1]) || gui.getChatPlayerNicknames().get(i).equals(subStrings[2])) && subStrings[0].equals("P"))) {  // Private texts
                        sb.append(subStrings[1]).append(": ").append(subStrings[3]).append("\n");
                    }
                }
                JTextArea messageArea;
                messageArea = getMessageArea(gui.getChatPlayerNicknames().get(i));
                messageArea.setText(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if a card has been played.
     *
     * @return true if a card has been played, false otherwise.
     */
    public boolean getCardPlayed() {
        return cardPlayed;
    }

    /**
     * Sets the status of whether a card has been played.
     *
     * @param b true if a card has been played, false otherwise.
     */
    public void setCardPlayed(boolean b) {
        cardPlayed = b;
    }


    /**
     * A JPanel that supports zooming and dragging, displaying a grid with images.
     */

    public class ZoomableGridPanel extends JPanel {
        private final Map<Point, BufferedImage> images = new LinkedHashMap<>();
        private double zoomFactor;
        private int blackRow = 39;
        private int blackCol = 39;
        private final int GRID_SIZE = 80;
        private final int CELL_WIDTH = 63;
        private final int CELL_HEIGHT = 42;
        private double translateX, translateY;
        private Point dragStartScreen, dragEndScreen;
        private boolean isDragging = false;
        private BufferedImage backgroundImage;

        /**
         * Constructs a ZoomableGridPanel with a default zoom factor and starting position.
         *
         * @param defaultZoomFactor the initial zoom factor
         * @param startRow          the starting row of the grid
         * @param startColumn       the starting column of the grid
         */

        public ZoomableGridPanel(double defaultZoomFactor, int startRow, int startColumn) {
            zoomFactor = defaultZoomFactor;
            translateX = -startRow * CELL_WIDTH * defaultZoomFactor;
            translateY = -startColumn * CELL_HEIGHT * defaultZoomFactor;

            try {
                backgroundImage = ImageIO.read(new File("src/main/graphic_resources/GUI_stuff/sfondo.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        MatrixClick[0] = blackRow + 1;
                        MatrixClick[1] = blackCol + 1;
                    } else {
                        moveBlackCell(e.getKeyCode());
                    }
                }
            });

            setFocusable(true);
            requestFocusInWindow();

            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    dragStartScreen = e.getPoint();
                    isDragging = true;
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    isDragging = false;
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    dragEndScreen = e.getPoint();
                    translateX += (dragEndScreen.getX() - dragStartScreen.getX()) / zoomFactor;
                    translateY += (dragEndScreen.getY() - dragStartScreen.getY()) / zoomFactor;
                    dragStartScreen = dragEndScreen;
                    repaint();
                }

                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    double oldZoom = zoomFactor;
                    if (e.getPreciseWheelRotation() < 0) {
                        zoomFactor *= 1.1;
                    } else {
                        zoomFactor /= 1.1;
                    }
                    zoomFactor = Math.max(zoomFactor, 0.1);
                    double scaledZoom = zoomFactor / oldZoom;
                    translateX *= scaledZoom;
                    translateY *= scaledZoom;
                    repaint();
                }
            };

            this.addMouseMotionListener(mouseAdapter);
            this.addMouseListener(mouseAdapter);
            this.addMouseWheelListener(mouseAdapter);
        }

        public void addImageAt(BufferedImage image, int gridRow, int gridColumn) {
            Point point = new Point(gridColumn - 1, gridRow - 1);
            images.remove(point);
            images.put(point, image);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            if (backgroundImage != null) {
                g2d.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
            }

            AffineTransform transform = new AffineTransform();
            transform.translate(translateX, translateY);
            transform.scale(zoomFactor, zoomFactor);
            g2d.setTransform(transform);


            drawBlackCell(g2d);
            drawImages(g2d);
        }

        private void drawBlackCell(Graphics2D g2d) {
            int x = blackCol * CELL_WIDTH;
            int y = blackRow * CELL_HEIGHT;
            g2d.setColor(Color.lightGray);
            g2d.fillRect(x, y, CELL_WIDTH, CELL_HEIGHT);
        }

        public void moveBlackCell(int key) {
            switch (key) {
                case KeyEvent.VK_UP, KeyEvent.VK_W:
                    blackRow = Math.max(0, blackRow - 1);
                    break;
                case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                    blackRow = Math.min(GRID_SIZE - 1, blackRow + 1);
                    break;
                case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                    blackCol = Math.max(0, blackCol - 1);
                    break;
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                    blackCol = Math.min(GRID_SIZE - 1, blackCol + 1);
                    break;
            }
            MatrixClick[0] = blackRow + 1;
            MatrixClick[1] = blackCol + 1;
            repaint();
        }

        private void drawImages(Graphics2D g2d) {
            int overflow = 12;
            for (Map.Entry<Point, BufferedImage> entry : images.entrySet()) {
                Point gridCoord = entry.getKey();
                BufferedImage image = entry.getValue();
                int x = gridCoord.x * CELL_WIDTH;
                int y = gridCoord.y * CELL_HEIGHT;
                g2d.drawImage(image, x - overflow, y - overflow, CELL_WIDTH + 2 * overflow, CELL_HEIGHT + 2 * overflow, null);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension((int) (GRID_SIZE * CELL_WIDTH * zoomFactor), (int) (GRID_SIZE * CELL_HEIGHT * zoomFactor));
        }
    }

    /**
     * A JPanel subclass for displaying an image.
     */
    static class ImagePanel extends JPanel {
        private BufferedImage image;

        /**
         * Sets the image to be displayed in this panel and triggers a repaint of the panel.
         *
         * @param image the BufferedImage to be displayed
         */
        public void setImage(BufferedImage image) {
            this.image = image;
            repaint();
        }

        /**
         * Paints the component with the provided image, if available.
         *
         * @param g the Graphics context
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}