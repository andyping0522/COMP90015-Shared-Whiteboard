import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import remote.IRemoteBoard;
import remote.RemoteBoard;

public class Server {

    public static void main(String args[]) {

        int port = Integer.parseInt(args[0]);

        try {
            IRemoteBoard board = new RemoteBoard();
            Registry registry = LocateRegistry.getRegistry("localhost");
            System.out.println(registry.toString());
            registry.bind("SharedBoard", board);

        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
