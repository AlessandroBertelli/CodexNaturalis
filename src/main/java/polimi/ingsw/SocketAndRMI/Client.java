package polimi.ingsw.SocketAndRMI;

import java.rmi.RemoteException;
import polimi.ingsw.SocketAndRMI.message.Message;
import java.rmi.server.UnicastRemoteObject;

/**
 * The Client class represents a client in the network.
 * This class is meant to be extended by specific implementations of clients,
 * such as GUI clients or console clients.
 */
public abstract class Client extends UnicastRemoteObject {


    public ClientController clientController;

    /**
     * Constructs a new client.
     */
    protected Client() throws RemoteException {}

    /**
     * Sends a message to the server.
     * @param messageToSent the message to be sent.
     */
    public abstract void send(Message messageToSent);

    /**
     * Reads a message from the server.
     */
    public abstract void read();

    /**
     * Disconnects the client from the server.
     */
    public abstract void disconnect();

    /**
     * Enables ping functionality for the client.
     */
    public abstract void letsPing();

    /**
     * Disables ping functionality for the client.
     */
    public abstract void stopPing();

    /**
     * Returns the client controller associated with this client.
     * @return the client controller.
     */
    public ClientController getClientController() {
        return clientController;
    }

    /**
     * Sets the client controller associated with this client.
     * @param clientController the client controller to be set.
     */
    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }
}
