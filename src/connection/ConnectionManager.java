package connection;

import javax.imageio.IIOException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
//import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionManager {

    private HashMap<String, Socket> clientSockets;

    public ConnectionManager() {
        clientSockets = new HashMap<>();
    }

    public void add(String user, Socket socket) {
        clientSockets.put(user, socket);
    }



    public void kick(String user) throws IOException {
        Socket socket = clientSockets.get(user);
        BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), "UTF-8")
        );

        out.write("Kick\n");
        out.flush();
        socket.close();
        clientSockets.remove(user);
    }

    public void approve(String user) throws IOException {
        Socket socket = clientSockets.get(user);
        BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), "UTF-8")
        );

        out.write("Approved\n");
        out.flush();
    }

    public void reject(String user) throws IOException {
        Socket socket = clientSockets.get(user);
        BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), "UTF-8")
        );

        out.write("Rejected\n");
        out.flush();
        clientSockets.remove(user);
    }




}
