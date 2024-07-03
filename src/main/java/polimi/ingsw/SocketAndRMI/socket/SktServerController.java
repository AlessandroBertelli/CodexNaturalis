package polimi.ingsw.SocketAndRMI.socket;

import polimi.ingsw.SocketAndRMI.message.Message;
import polimi.ingsw.SocketAndRMI.ServerController;
import polimi.ingsw.SocketAndRMI.message.MessageType;


import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.io.EOFException;
import java.net.SocketTimeoutException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class SktServerController implements ServerController, Runnable {
    private final Socket clientSocket;
    private final SktServer sktServer;
    private boolean isConnected;
    private final Object inLock;
    private final Object outLock;
    private ObjectInputStream inSTR;
    private ObjectOutputStream outSTR;

    /**
     * Constructs a SktServerController instance for managing communication with a client over a socket.
     *
     * @param server       The SktServer instance associated with this controller.
     * @param clientSocket The socket connected to the client.
     */
    public SktServerController(SktServer server, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.sktServer = server;
        this.isConnected = true;
        this.inLock = new Object();
        this.outLock = new Object();

        try {
            this.outSTR = new ObjectOutputStream(this.clientSocket.getOutputStream());
            this.inSTR = new ObjectInputStream(this.clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Handles communication with the client over the socket connection.
     * Listens for incoming messages, processes them according to their type,
     * and manages exceptions related to socket communication.
     */
    public void run() {
        try {
            System.out.println("Established new client connection with: " + clientSocket.getInetAddress().toString());

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    synchronized (inLock) {
                        Message message = null;
                        try {
                            message = (Message) inSTR.readObject();
                            System.out.println("Received message: " + message.toString());
                        } catch (SocketException | SocketTimeoutException | EOFException se) {
                            handleSocketException(se);
                        }

                        if (message != null && message.getMessageType() != MessageType.PING_MESSAGE) {
                            handleMessageType(message);
                        }
                    }
                } catch (ClassNotFoundException | ClassCastException e) {
                    System.out.println("Invalid stream.");
                    e.printStackTrace();
                }
            }

            handleDisconnection();
        } catch (IOException e) {
            System.out.println("Unable to handle Socket client handler.");
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error closing client socket.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles a socket exception by closing the client socket, marking the connection as disconnected,
     * and interrupting the current thread.
     *
     * @param ex The IOException thrown during socket communication.
     * @throws IOException If an error occurs while closing the client socket.
     */
    private void handleSocketException(IOException ex) throws IOException {
        clientSocket.close();
        this.isConnected = false;
        Thread.currentThread().interrupt();
    }

    /**
     * Handles a received message based on its type.
     * If the message is a LOGIN_REQUEST, requests the server to add the client with the specified nickname.
     * Otherwise, forwards the message to the server for further processing.
     *
     * @param message The message received from the client.
     */
    private void handleMessageType(Message message) {
        if (message.getMessageType() == MessageType.LOGIN_REQUEST) {
            sktServer.joinClient(message.getNickname(), this);
        } else {
            sktServer.onAcquiredMessage(message);
        }
    }

    /**
     * Handles the disconnection event by notifying the server that the client has disconnected.
     */
    private void handleDisconnection() {
        sktServer.onDisconnectionFromClient(this);
    }

    /**
     * Sends a message to the client over the socket connection.
     *
     * @param message The message to be sent.
     */
    public void send(Message message) {
        try {
            synchronized (outLock) {
                outSTR.writeObject(message);
                outSTR.reset();
                System.out.println("Message sent: " + message.toString());
            }
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    /**
     * Handles an IOException by logging an error message and disconnecting the client.
     *
     * @param e The IOException that occurred.
     */
    private void handleIOException(IOException e) {
        System.err.println("I/O Error.");
        disconnectThisClient();
    }


    /**
     * Disconnects the current client by closing its socket connection, interrupting the current thread,
     * and notifying the server of the disconnection.
     * If the client is already disconnected, this method returns immediately.
     */
    @Override
    public void disconnectThisClient() {
        if (!isConnected) {
            return;
        }

        try {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread.currentThread().interrupt();
        this.isConnected = false;
        sktServer.onDisconnectionFromClient(this);
    }


    /**
     * Checks if the client is currently connected to the server.
     *
     * @return {@code true} if the client is connected, {@code false} otherwise.
     */
    @Override
    public boolean isClientConnectionOk() {
        return isConnected;
    }
}
