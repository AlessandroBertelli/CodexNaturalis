package polimi.ingsw.SocketAndRMI.message;

import polimi.ingsw.SocketAndRMI.Server;

/**
 * Represents a message used to send information to a client transitioning to the Waiting Room scene (GUI and TUI).
 */
public class WaitingRoomRequest extends Message {

    /**
     * Constructs a new WaitingRoomRequest object.
     */
    public WaitingRoomRequest() {
        super(Server.SERVER_NAME, MessageType.WAITING_ROOM_REQUEST);
    }

    @Override
    public String toString() {
        return "WaitingRoomRequest{" +
                "nickname='" + nickname +
                "is in waiting room" + '\'' +
                '}';
    }
}
