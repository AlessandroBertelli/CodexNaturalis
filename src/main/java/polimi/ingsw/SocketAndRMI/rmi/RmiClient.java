package polimi.ingsw.SocketAndRMI.rmi;

import polimi.ingsw.SocketAndRMI.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * RMI client interface for handling messages sent from the server.
 * Extends {@link Remote} to support remote method invocation.
 */
public interface RmiClient extends Remote {

    /**
     * Sends a message to the client.
     *
     * @param message The message to be sent to the client.
     * @throws RemoteException If a communication-related exception occurs during the remote method invocation.
     */
    void sendToClient(Message message) throws RemoteException;
}