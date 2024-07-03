package polimi.ingsw.SocketAndRMI.socket;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;



import polimi.ingsw.SocketAndRMI.Server;
import polimi.ingsw.SocketAndRMI.message.Message;
import polimi.ingsw.SocketAndRMI.ServerController;


public class SktServer implements Runnable {
    private final int port;
    private final Server server;
    public static final int SOCKET_SERVER_PORT = 5033;
    private ServerSocket serverSocket;

    /**
     * Constructor for initializing a SktServer instance with a given server and port.
     *
     * @param server The server object associated with this SktServer instance.
     * @param port   The port number on which the server listens.
     */
    public SktServer(Server server, int port) {
        this.server = server;
        this.port = port;
    }

    /**
     * Starts the socket server and listens for incoming client connections on the specified port.
     * Creates a new thread for each client connection using {@code SktServerController}.
     * Handles IOExceptions by printing error messages to the console.
     */
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            System.out.println("Socket server running on port " + this.port);

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New connection request from: " + clientSocket.getInetAddress());
                    clientSocket.setSoTimeout(SktClient.SOCKET_TO);

                    SktServerController sktServerController = new SktServerController(this, clientSocket);
                    Thread thread = new Thread(sktServerController);
                    thread.start();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Unable to start socket server.");
        }
    }

    /**
     * Handles disconnection from a client by forwarding the event to the server.
     *
     * @param serverController The server controller associated with the disconnected client.
     */
    public void onDisconnectionFromClient(ServerController serverController) {
        server.onDisconnectionFromClient(serverController);
    }

    /**
     * Requests the server to add a client with the specified nickname, managed by the given server controller.
     *
     * @param nickname          The nickname of the client to be added.
     * @param serverController  The server controller managing operations for the client.
     */
    public void joinClient(String nickname, ServerController serverController) {
        server.joinClient(nickname, serverController);
    }

    /**
     * Forwards a received message to the server for processing.
     *
     * @param message The message received from a client.
     */
    public void onAcquiredMessage(Message message) {
        server.onAcquiredMessage(message);
    }
    public int getPort() {
        return port;
    }
    public Server getServer() {
        return server;
    }
}