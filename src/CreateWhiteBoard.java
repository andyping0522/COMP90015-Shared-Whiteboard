import board.BoardApp;
import connection.ConnectionManager;
import remote.IRemoteBoard;
import remote.IRemoteUsers;

import javax.net.ServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CreateWhiteBoard {
    //private ConnectionManager manager;




    public static void main(String args[]) {

        try {
            String ip = args[0];
            int port = Integer.parseInt(args[1]);
            String userName = args[2];
            Registry registry = LocateRegistry.getRegistry(ip);
            IRemoteBoard board = (IRemoteBoard) registry.lookup("SharedBoard");
            IRemoteUsers users = (IRemoteUsers) registry.lookup("Users");
            users.newUser(userName);
            ConnectionManager manager = new ConnectionManager();
            BoardApp app = new BoardApp(true, board, userName, users, manager);
            app.start();
            ServerSocketFactory factory = ServerSocketFactory.getDefault();
            ServerSocket socket = factory.createServerSocket(port);
            while (true) {
                Socket client = socket.accept();
                Thread worker = new Thread () {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(client.getInputStream(), "UTF-8")
                            );

                            String userName = in.readLine();
                            manager.add(userName, client);
                            app.popUpJoinRequest(userName);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                worker.start();

            }
        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
