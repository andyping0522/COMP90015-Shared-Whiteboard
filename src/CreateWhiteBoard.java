import board.BoardApp;
import remote.IRemoteBoard;
import remote.IRemoteUsers;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CreateWhiteBoard {

    public static void main(String args[]) {

        try {
            String ip = args[0];
            String port = args[1];
            String userName = args[2];
            Registry registry = LocateRegistry.getRegistry(ip);
            IRemoteBoard board = (IRemoteBoard) registry.lookup("SharedBoard");
            IRemoteUsers users = (IRemoteUsers) registry.lookup("Users");
            users.newUser(userName);
            BoardApp app = new BoardApp(true, board, userName, users);
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
