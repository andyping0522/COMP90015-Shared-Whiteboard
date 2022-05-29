package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteChat extends Remote {

    void append(String user, String msg) throws RemoteException;

    String getMsg() throws RemoteException;


}
