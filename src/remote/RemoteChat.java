package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteChat extends UnicastRemoteObject implements IRemoteChat {

    private String allMsg;

    public RemoteChat() throws RemoteException {
        this.allMsg = "";
    }

    @Override
    public void append(String user, String msg) throws RemoteException {
        allMsg = allMsg + user + ": " + msg + "\n";
        //System.out.println(allMsg);
    }

    @Override
    public String getMsg() {
        return allMsg;
    }
}
