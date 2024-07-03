package polimi.ingsw.SocketAndRMI.socket;
import polimi.ingsw.SocketAndRMI.message.Message;
import polimi.ingsw.SocketAndRMI.message.ErrorMessage;
import polimi.ingsw.SocketAndRMI.message.PingMessage;


import polimi.ingsw.SocketAndRMI.Client;
import polimi.ingsw.SocketAndRMI.ClientController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents a client using a socket to communicate with a server.
 */
public class SktClient extends Client {
    private final Socket socket;
    public static final int SOCKET_TO = 10000;
    private final ExecutorService readService = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService pingProgramme = Executors.newSingleThreadScheduledExecutor();
    private final ObjectInputStream inSTR;
    private final ObjectOutputStream outSTR;

    /**
     * Constructs a SocketClient instance and establishes a connection to the server.
     *
     * @param controller The client controller handling client operations.
     * @param ipAddress  IP address of the server.
     * @param port       Port number of the server.
     * @throws IOException If an I/O error occurs when creating the socket or connecting to the server.
     */
    public SktClient(ClientController controller, String ipAddress, int port) throws IOException {
        setClientController(controller);
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(ipAddress, port), SOCKET_TO);
        this.inSTR = new ObjectInputStream(socket.getInputStream());
        this.outSTR = new ObjectOutputStream(socket.getOutputStream());
        letsPing();
    }

    /**
     * Sends a message to the server.
     *
     * @param msg The message to be sent.
     */
    @Override
    public void send(Message msg) {
        try {
            this.outSTR.writeObject(msg);
            this.outSTR.reset();
        } catch (IOException e) {
            this.disconnect();
            getClientController().updateCorrespondingClient(new ErrorMessage(null, "message fail. disconnected."));
        }
    }

    /**
     * Reads messages from the server continuously and updates the client accordingly.
     * This method runs asynchronously in a separate thread managed by {@code readService}.
     * It stops reading when {@code readService} is shut down.
     */
    @Override
    public void read() {
        readService.execute(() -> {
            while (!readService.isShutdown()) {
                Message message = null;
                boolean disconnected = false;
                try {
                    message = (Message) inSTR.readObject();
                } catch (IOException e) {
                    disconnected = true;
                } catch (ClassNotFoundException e) {
                    disconnected = true;
                }

                if (disconnected) {
                    message = new ErrorMessage(null, "Disconnected from server.");
                    disconnect();
                    readService.shutdownNow();
                }

                getClientController().updateCorrespondingClient(message);
            }
        });
    }

    /**
     * Disconnects the client from the server.
     * If the socket is not closed, shuts down the read service, stops pinging the server,
     * and closes the socket connection.
     * Handles IOException by notifying the client controller with an error message.
     */
    @Override
    public void disconnect() {
        try {
            if (!socket.isClosed()) {
                readService.shutdownNow();
                stopPing();
                socket.close();
            }
        } catch (IOException ex) {
            ErrorMessage errorMessage = new ErrorMessage(null, "Impossible to disconnect from server.");
            getClientController().updateCorrespondingClient(errorMessage);
        }
    }

    /**
     * Initiates periodic ping messages to the server.
     * Pings are scheduled at a fixed rate using {@code pingProgramme}, sending a {@code PingMessage}
     * containing the client's nickname obtained from the client controller.
     */
    @Override
    public void letsPing() {
        pingProgramme.scheduleAtFixedRate(() -> {
            PingMessage pingMessage = new PingMessage(getClientController().getNickname());
            send(pingMessage);
        }, 0, SOCKET_TO / 2, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops sending periodic ping messages to the server.
     * Shuts down the {@code pingProgramme} immediately.
     */
    @Override
    public void stopPing() {
        pingProgramme.shutdownNow();
    }
}

