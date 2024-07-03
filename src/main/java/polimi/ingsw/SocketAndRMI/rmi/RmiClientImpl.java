package polimi.ingsw.SocketAndRMI.rmi;

import polimi.ingsw.SocketAndRMI.Client;
import polimi.ingsw.SocketAndRMI.ClientController;
import polimi.ingsw.SocketAndRMI.Server;
import polimi.ingsw.SocketAndRMI.message.Message;
import polimi.ingsw.SocketAndRMI.message.MessageType;
import polimi.ingsw.SocketAndRMI.message.PingMessage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class RmiClientImpl extends Client implements RmiClient, Runnable {
    private RmiServer rmiServer;
    private final ExecutorService readProgramme = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService pingProgramme = Executors.newSingleThreadScheduledExecutor();
    public static final int RMI_TO = 10000;
    private String ipAddress;
    private int port;


    /**
     * Constructs a new RMI client implementation.
     *
     * @param controller The client controller to manage client-side operations.
     * @param ipAddress The IP address of the RMI server.
     * @param port The port number of the RMI server.
     * @throws RemoteException If a remote communication error occurs during the setup.
     */
    public RmiClientImpl(ClientController controller, String ipAddress, int port) throws RemoteException {
        setClientController(controller);
        this.ipAddress = ipAddress;
        this.port = port;

        try {
            Registry registry = LocateRegistry.getRegistry(this.ipAddress, this.port);
            this.rmiServer = (RmiServer) registry.lookup(Server.SERVER_NAME);
            letsPing();
        } catch (NotBoundException e) {
            handleNotBoundException(e);
        }
    }

    /**
     * Handles a NotBoundException by printing the stack trace.
     *
     * @param e The NotBoundException that occurred.
     */
    private void handleNotBoundException(NotBoundException e) {
        e.printStackTrace();
    }

    /**
     * Sends a message to the server. This method delegates the actual sending logic to the {@link #handleSend(Message)} method.
     * If a {@link RemoteException} occurs, it disconnects the client.
     *
     * @param message The message to be sent.
     */
    @Override
    public void send(Message message) {
        try {
            handleSend(message);
        } catch (RemoteException ex) {
            disconnect();
        }
    }
    /**
     * Handles the actual sending of a message to the server.
     * If the message type is {@link MessageType#LOGIN_REQUEST}, it adds the client to the server.
     * For other message types, it forwards the message to the server.
     *
     * @param message The message to be sent.
     * @throws RemoteException If a remote communication error occurs.
     */
    private void handleSend(Message message) throws RemoteException {
        if (message.getMessageType() == MessageType.LOGIN_REQUEST) {
            rmiServer.joinClient(message.getNickname(), this);
        } else {
            rmiServer.sendToServer(message);
        }
    }

    /**
     * Sends a message to the client by executing the handling logic in a separate thread.
     *
     * @param message The message to be sent.
     * @throws RemoteException If a remote communication error occurs.
     */
    public void sendToClient(Message message) throws RemoteException {
        readProgramme.execute(() -> handleMessage(message));
    }

    /**
     * Handles the received message.
     * If the message type is not {@link MessageType#PING_MESSAGE}, it updates the corresponding client.
     *
     * @param message The message to be handled.
     */
    private void handleMessage(Message message) {
        if (message.getMessageType() != MessageType.PING_MESSAGE) {
            clientController.updateCorrespondingClient(message);
        }
    }


    @Override
    public void read() {
        // Not implemented for RMI connection
    }

    @Override
    public void disconnect() {
        if (rmiServer != null) {
            System.out.println("Disconnecting from the server. You can safely close the program.");
            rmiServer = null;
            stopPing();
            // System.exit(0);
        }
    }

    /**
     * Initiates a periodic ping to maintain connection with the client.
     * Pings are scheduled at a fixed rate using {@link #pingProgramme}.
     *
     * @throws RemoteException If a remote communication error occurs during the ping operation.
     */
    @Override
    public void letsPing() {
        pingProgramme.scheduleAtFixedRate(
                () -> {
                    PingMessage pingMessage = new PingMessage(clientController.getNickname());
                    send(pingMessage);
                },
                0,
                RMI_TO / 2,
                TimeUnit.MILLISECONDS
        );
    }


    /**
     * Stops the periodic pinging scheduled with {@link #pingProgramme}.
     */
    @Override
    public void stopPing() {
        pingProgramme.shutdownNow();
    }

    @Override
    /**
     * Any exceptions that occur during execution are handled by the {@link #handleException(Exception)} method.
     */
    public void run() {
        try {
            //empty
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /**
     * Handles exceptions by wrapping them in a {@link RuntimeException} and throwing it.
     *
     * @param ex The exception to be handled.
     */
    private void handleException(Exception ex) {
        throw new RuntimeException(ex);
    }
}
