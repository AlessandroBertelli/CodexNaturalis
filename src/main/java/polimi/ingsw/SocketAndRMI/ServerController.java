package polimi.ingsw.SocketAndRMI;

import polimi.ingsw.SocketAndRMI.message.Message;

import java.rmi.Remote;

/**
 * This interface is responsible for managing the connection with a SPECIFIC client.
 * It contains methods for forwarding messages to a client and handles connection/disconnection.
 */
public interface ServerController extends Remote {

    /**
     * Sends a message to the corresponding Client
     * @param messageToSent The message to send to the client.
     */
    void send(Message messageToSent);

    /**
     * Disconnects the client from the server.
     */
    void disconnectThisClient();

    /**
     * return true if the client is connected, false otherwise.
     */
    boolean isClientConnectionOk();
}
