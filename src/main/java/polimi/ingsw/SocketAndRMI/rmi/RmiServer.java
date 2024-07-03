package polimi.ingsw.SocketAndRMI.rmi;

import polimi.ingsw.SocketAndRMI.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface for the RMI server.
 * Defines methods to add clients and receive messages from clients.
 */
public interface RmiServer extends Remote {

    /**
     * Adds a client with the specified nickname and RMI client interface.
     *
     * @param nickname   The nickname of the client.
     * @param rmiClient  The RMI client interface of the client.
     * @throws RemoteException If a remote communication error occurs.
     */
    void joinClient(String nickname, RmiClient rmiClient) throws RemoteException;

    /**
     * Receives a message from a client and processes it on the server.
     *
     * @param message The message received from the client.
     * @throws RemoteException If a remote communication error occurs.
     */
    void sendToServer(Message message) throws RemoteException;
}
