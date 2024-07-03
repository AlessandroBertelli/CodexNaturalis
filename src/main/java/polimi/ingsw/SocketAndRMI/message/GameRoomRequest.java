package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.SocketAndRMI.Server;

/**
 * Represents a message sent to request the client to switch to the Game Room scene (in GUI)
 * or indicating that the game has started (in TUI).
 */
public class GameRoomRequest extends Message {

    /**
     * Constructs a new GameRoomRequest object.
     */
    public GameRoomRequest() {
        super(Server.SERVER_NAME, MessageType.GAME_ROOM_REQUEST);
    }

    @Override
    public String toString() {
        return "GameRoomRequest{" +
                "nickname='" + nickname +
                "is in game room" + '\'' +
                '}';
    }
}
