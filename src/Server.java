import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import remote.*;

public class Server {

    public static void main(String[] args) {

        int port = Integer.parseInt(args[0]);

        try {
            IRemoteBoard board = new RemoteBoard();
            IRemoteUsers users = new RemoteUsers();
            IRemoteChat chat = new RemoteChat();
            Registry registry = LocateRegistry.getRegistry("localhost");
            System.out.println(registry.toString());
            registry.bind("SharedBoard", board);
            registry.bind("Users", users);
            registry.bind("Chat", chat);

        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
