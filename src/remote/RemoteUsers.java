package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RemoteUsers extends UnicastRemoteObject implements IRemoteUsers {

    private ArrayList<String> users;

    public RemoteUsers() throws RemoteException {
        this.users = new ArrayList<>();
    }

    @Override
    public ArrayList<String> getUsers() {
        return users;
    }

    @Override
    public void newUser(String name) {
        users.add(name);
    }

    @Override
    public void removeUser(String name) {
        users.remove(name);
    }

    @Override
    public boolean verifyName(String name) {
        return users.contains(name);
    }
}
