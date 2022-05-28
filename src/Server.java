import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import remote.IRemoteBoard;
import remote.IRemoteUsers;
import remote.RemoteBoard;
import remote.RemoteUsers;

public class Server {

    public static void main(String args[]) {

        int port = Integer.parseInt(args[0]);

        try {
            IRemoteBoard board = new RemoteBoard();
            IRemoteUsers users = new RemoteUsers();
            Registry registry = LocateRegistry.getRegistry("localhost");
            System.out.println(registry.toString());
            registry.bind("SharedBoard", board);
            registry.bind("Users", users);

        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
