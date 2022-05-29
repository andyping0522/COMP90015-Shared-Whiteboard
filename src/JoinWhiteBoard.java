import board.BoardApp;
import remote.IRemoteBoard;
import remote.IRemoteUsers;


import java.io.*;

import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class JoinWhiteBoard {

    public static void main(String args[]) {
        try {
            String ip = args[0];
            int port = Integer.parseInt(args[1]);
            String userName = args[2];
            Registry registry = LocateRegistry.getRegistry(ip);
            IRemoteBoard board = (IRemoteBoard) registry.lookup("SharedBoard");
            IRemoteUsers users = (IRemoteUsers) registry.lookup("Users");

            if (users.verifyName(userName)) {
                System.out.println("User name has been taken by others");
                System.exit(-1);
            }
            //users.newUser(userName);

            BoardApp app = new BoardApp(false, board, userName, users, null);
            Socket socket = new Socket(ip, port);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), "UTF-8")
            );
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8")
            );
            out.write(userName + "\n");
            out.flush();
            String res;
            while ((res = in.readLine()) != null) {

                switch (res) {
                    case "Approved":
                        app.start();
                        break;
                    case "Kick":
                        app.popUpKicked();
                        break;
                    case "Rejected":
                        System.out.println("You have been rejected by the board manager");
                        System.exit(0);
                }
            }


        } catch (AccessException e) {
            e.printStackTrace();

        } catch (IOException e) {
            System.out.println("Board does not exist");
            System.exit(-1);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }


}
