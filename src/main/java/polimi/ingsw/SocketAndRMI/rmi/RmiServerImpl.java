package polimi.ingsw.SocketAndRMI.rmi;

import polimi.ingsw.SocketAndRMI.ServerController;
import polimi.ingsw.SocketAndRMI.Server;
import polimi.ingsw.SocketAndRMI.message.Message;
import polimi.ingsw.SocketAndRMI.message.MessageType;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * RMI server for handling remote client interactions.
 * Binds the server implementation to an RMI registry on a specified port.
 *
 */
public class RmiServerImpl extends UnicastRemoteObject implements RmiServer {
    private final Server server;
    public static final int DEFAULT_RMI_PORT = 1099;


    /**
     * Constructs an RMI server instance for the specified server implementation.
     * Attempts to create an RMI registry and bind the server instance to it.
     *
     * @param server The server implementation to be bound to RMI.
     * @throws RemoteException      If a remote communication error occurs during RMI setup.
     * @throws AlreadyBoundException If the RMI registry is already bound to a server.
     */
    public RmiServerImpl(Server server) throws RemoteException {
        this.server = server;
        try {
            createRegistryAndBind();
        } catch (RemoteException e) {
            handleRemoteException(e);
        } catch (AlreadyBoundException e) {
            handleAlreadyBoundException(e);
        }
    }

    /**
     * Creates an RMI registry on the default port and binds the server instance to it.
     *
     * @throws RemoteException      If a remote communication error occurs during RMI registry creation.
     * @throws AlreadyBoundException If the RMI registry is already bound to a server.
     */
    private void createRegistryAndBind() throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(DEFAULT_RMI_PORT);
        registry.bind(Server.SERVER_NAME, this);
        System.out.println("RMI server created on port " + DEFAULT_RMI_PORT + "...");
    }

    /**
     * Handles a RemoteException by printing an error message and rethrowing the exception.
     *
     * @param e The RemoteException to handle.
     * @throws RemoteException The rethrown RemoteException.
     */
    private void handleRemoteException(RemoteException e) throws RemoteException {
        System.out.println("Unable to instantiate RMI server.");
        throw e; // Rethrow RemoteException to caller
    }

    /**
     * Handles an AlreadyBoundException by printing a warning message.
     *
     * @param e The AlreadyBoundException to handle.
     */
    private void handleAlreadyBoundException(AlreadyBoundException e) {
        System.out.println("RMI server already bound: " + e.getMessage());
    }


    /**
     * Adds a new client to the server.
     * @param nickname The nickname of the client.
     * @param rmiClient The remote client object.
     * @throws RemoteException If an RMI-related communication error occurs.
     */
    @Override
    public void joinClient(String nickname, RmiClient rmiClient) throws RemoteException {
        RmiServerController clientHandler = new RmiServerController(rmiClient, this);
        this.server.joinClient(nickname, clientHandler);
    }

    /**
     * Sends a message to the server for processing.
     * Logs the received message and delegates the processing to the server.
     *
     * @param message The message to send to the server.
     * @throws RemoteException If a remote communication error occurs during message transmission.
     */
    public void sendToServer(Message message) throws RemoteException {
        System.out.println("Received message: " + message.toString());
        if (message.getMessageType() != MessageType.PING_MESSAGE) {
            server.onAcquiredMessage(message);
        }
    }


    /**
     * Notifies the server about a client disconnection.
     * Delegates the disconnection handling to the server.
     *
     * @param serverController The server controller associated with the disconnected client.
     */
    public void onClientDisconnectionFromServer(ServerController serverController) {
        server.onDisconnectionFromClient(serverController);
    }
}
