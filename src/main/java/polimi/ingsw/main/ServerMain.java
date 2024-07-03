package polimi.ingsw.main;

import polimi.ingsw.SocketAndRMI.rmi.RmiServerImpl;
import polimi.ingsw.controller.Controller;
import polimi.ingsw.SocketAndRMI.Server;
import polimi.ingsw.SocketAndRMI.socket.SktServer;

import java.rmi.RemoteException;

public class ServerMain {
    public static void main(String[] args) {
        System.out.println("Starting server");
        Controller controller = new Controller();
        Server server = new Server(controller);
        SktServer sktServer = new SktServer(server, SktServer.SOCKET_SERVER_PORT);
        Thread thread = new Thread(sktServer);
        thread.start();
        System.out.println("Server started");
        try {
            RmiServerImpl remoteServer = new RmiServerImpl(server);
        } catch (RemoteException e) {
            System.out.println("Cannot start RMI server.");
        }
    }
}
