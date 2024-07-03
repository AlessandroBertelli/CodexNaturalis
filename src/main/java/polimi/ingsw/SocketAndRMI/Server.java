
package polimi.ingsw.SocketAndRMI;

import polimi.ingsw.controller.Controller;
import polimi.ingsw.model.GameState;
import java.util.Collections;
import java.util.HashMap;
import polimi.ingsw.SocketAndRMI.message.Message;
import polimi.ingsw.view.VirtualView;
import java.util.Map;

/**
 * This class represents the server implementation.
 * Depending on the network technology (socket or RMI), a class will extend these functionalities.
 */
public class Server {
    private final Controller controller;
    private final Map<String, ServerController> serverControllerMap; //nickname,his server controller
    public static final String SERVER_NAME = "CodexNaturalisServer";
    private final Object lockObject = new Object();

    /**
     * Creates an instance of the server.
     * @param controller
     */
    public Server(Controller controller) {
        this.controller = controller;
        this.serverControllerMap = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Forwards a message to the controller.
     * @param message The message to be forwarded to the Controller.
     */
    public void onAcquiredMessage(Message message) {
        controller.onMessageSwith(message);
    }

    /**
     * Returns the nickname of the client associated with the specified {@code ClientHandler}.
     * @param serverController The ServerController of the client.
     * @return The nickname of the client associated with the {@code clientHandler}.
     */
    private String fromServerControllerToNickname(ServerController serverController) {
        return serverControllerMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(serverController))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a new client with the specified nickname and the associated Server controller
     * @param playernick The client's nickname.
     * @param serverController The ServerController of the client.
     */
    public void joinClient(String playernick, ServerController serverController) {
        VirtualView virtualView = new VirtualView(serverController);
        if (controller.getGameState() == GameState.STARTING) {
            NewClientConnection(playernick, serverController, virtualView);
        } else {
            ClientReconnection(playernick, serverController, virtualView);
        }
    }

    /**
     * Adds a new client connection to the server. Requires the Controller in LOBBY_STATE.
     * Checks if a player with the same nickname exists; if not, associates the ServerController with the nickname.
     * @param nickname The nickname of the new client.
     * @param serverController The ServerController of the new client.
     * @param virtualView The {@code VirtualView} of the new client.
     */
    private void NewClientConnection(String nickname, ServerController serverController, VirtualView virtualView) {
        if (controller.checkNickname(nickname, virtualView)) {
            serverControllerMap.put(nickname, serverController);
            virtualView.displayLoginResponse(true, true);
            controller.newLoginSetUp(nickname, virtualView);
        } else {
            virtualView.displayLoginResponse(false, true);
        }
    }


    private void ClientReconnection(String nickname, ServerController serverController, VirtualView virtualView) {
        if (controller.getOfflinePlayers().contains(nickname)) {
            serverControllerMap.put(nickname, serverController);
            System.out.println("Player reconnected: " + nickname);
            controller.Reconnection(nickname, virtualView);
        } else {
            virtualView.displayLoginResponse(true, false);
            serverController.disconnectThisClient();
        }
    }

    /**
     * Handles client disconnection, removing references from the {@code ClientHandler} map and the {@code VirtualView} map.
     * Depending on the game status, removes the client from the player list or marks them as "offline".
     * @param serverController The ServerController of the disconnected client.
     */
    public void onDisconnectionFromClient(ServerController serverController) {
        String nicknameOfDisconnectedClient = fromServerControllerToNickname(serverController);

        if (nicknameOfDisconnectedClient != null) {
            serverControllerMap.remove(nicknameOfDisconnectedClient);

            VirtualView virtualView = controller.getVirtualViewMap().get(nicknameOfDisconnectedClient);
            if (virtualView != null) {
                controller.removeVirtualView(nicknameOfDisconnectedClient, virtualView);
            }

            controller.transmissionMessage("Player " + nicknameOfDisconnectedClient + " disconnected.");
            System.out.println("Client " + nicknameOfDisconnectedClient + " disconnected.");

            if (controller.getGameState() == GameState.STARTING) {
                controller.removePlayer(nicknameOfDisconnectedClient);
            } else {
                controller.setPlayerOffline(nicknameOfDisconnectedClient);
            }

            if (serverControllerMap.isEmpty()) {
                System.out.println("There aren't players connected: game ends!");
                //System.exit(0);
            }
        } else {
            System.out.println("Cannot find a player: " + serverController.toString());
        }
    }

}
