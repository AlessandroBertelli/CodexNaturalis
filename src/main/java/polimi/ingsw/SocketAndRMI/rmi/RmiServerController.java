package polimi.ingsw.SocketAndRMI.rmi;

import polimi.ingsw.SocketAndRMI.ServerController;
import polimi.ingsw.SocketAndRMI.Server;
import polimi.ingsw.SocketAndRMI.message.Message;
import polimi.ingsw.SocketAndRMI.message.PingMessage;

import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static polimi.ingsw.SocketAndRMI.rmi.RmiClientImpl.RMI_TO;


/**
 * Represents a controller for managing communication with a remote RMI client.
 * Handles message sending, client disconnection, and ping scheduling.
 */
public class RmiServerController implements ServerController {
    private RmiClient rmiClient;
    private final RmiServerImpl remoteServer;
    private boolean isConnected;
    private final ScheduledExecutorService pingProgramme = Executors.newSingleThreadScheduledExecutor();

    /**
     * Constructs a new RmiServerController with the given client reference and server.
     * Initializes the controller, establishes initial connection state, and starts the ping process.
     *
     * @param clientReference The RMI client interface reference.
     * @param server          The RMI server reference.
     * @throws RemoteException If there is an issue with remote method invocation.
     */
    public RmiServerController(RmiClient clientReference, RmiServerImpl server) throws RemoteException {
        this.rmiClient = clientReference;
        this.remoteServer = server;
        this.isConnected = true;
        letsPing();
    }

    /**
     * Sends a message to the remote RMI client.
     * Logs the sent message and handles RemoteException by disconnecting the client.
     *
     * @param message The message to be sent to the client.
     */
    @Override
    public void send(Message message) {
        try {
            rmiClient.sendToClient(message);
            System.out.println("Message sent: " + message.toString());
        } catch (RemoteException e) {
            System.out.println("Unable to communicate with the client: disconnecting...");
            this.disconnectThisClient();
        }
    }

    /**
     * Disconnects the client from the server.
     * Clears client reference, stops ping scheduling, and notifies the server of disconnection.
     */
    @Override
    public void disconnectThisClient() {
        if (!isClientConnectionOk()) {
            return;
        }
        if (this.rmiClient != null) {
            this.rmiClient = null;  // Removes the reference to the remote client.
        }
        this.isConnected = false;
        stopPing();
        remoteServer.onClientDisconnectionFromServer(this);
    }

    /**
     * Checks if the client connection is active.
     *
     * @return True if the client connection is active, false otherwise.
     */
    @Override
    public boolean isClientConnectionOk() {
        return isConnected;
    }

    /**
     * Initiates the ping mechanism to the client at regular intervals.
     * Sends a PingMessage to monitor client connectivity.
     */
    private void letsPing() {
        pingProgramme.scheduleAtFixedRate(
                () -> send(new PingMessage(Server.SERVER_NAME)),
                0,
                RMI_TO / 2,
                TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the ping mechanism.
     * Shuts down the ping scheduling service.
     */
    private void stopPing() {
        if (!pingProgramme.isShutdown()) {
            pingProgramme.shutdownNow();
        }
    }
}


