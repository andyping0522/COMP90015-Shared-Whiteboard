package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteUsers extends Remote {

    ArrayList<String> getUsers() throws RemoteException;

    void newUser(String name) throws RemoteException;

    void removeUser(String name) throws RemoteException;

    boolean verifyName(String name) throws RemoteException;
}
