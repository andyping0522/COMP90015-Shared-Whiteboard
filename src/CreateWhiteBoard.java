import board.BoardApp;
import remote.IRemoteBoard;

import java.awt.*;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CreateWhiteBoard {

    public static void main(String args[]) {

        try {

            Registry registry = LocateRegistry.getRegistry("localhost");
            IRemoteBoard board = (IRemoteBoard) registry.lookup("SharedBoard");
            BoardApp app = new BoardApp(true, board);
            app.start();
        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
