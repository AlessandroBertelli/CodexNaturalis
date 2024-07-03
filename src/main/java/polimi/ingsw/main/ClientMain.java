package polimi.ingsw.main;

import polimi.ingsw.SocketAndRMI.ClientController;
import polimi.ingsw.view.*;

public class ClientMain {
    public static void main(String[] args) {
        boolean cliRequested = false;

        for (String param : args) {
            if (param.equalsIgnoreCase("--cli") || param.equalsIgnoreCase("-c")) {
                cliRequested = true;
            }
        }
        if (cliRequested) {
            TUI tui = new TUI();
            ClientController clientController = new ClientController(tui);
            tui.setClientController(clientController);
            tui.startView();
        } else {
            GUI gui = new GUI();
            ClientController clientController = new ClientController(gui);
            gui.setClientController(clientController);
            gui.startView();
        }
    }
}