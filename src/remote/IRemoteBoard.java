package remote;



import java.awt.*;
//import java.awt.image.BufferedImage;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface IRemoteBoard extends Remote {

    void drawLine(int x1, int y1, int x2, int y2) throws RemoteException;

    void drawRectangle(int x, int y, int w, int h) throws RemoteException;

    void drawText(String text, int x, int y) throws RemoteException;

    void drawTriangle(int x1, int y1, int x2, int y2) throws RemoteException;

    void drawCircle(int x, int y, int w, int h) throws RemoteException;

    void clear() throws RemoteException;

    byte[] getBoard() throws RemoteException;

    ArrayList<Shape> getComponents() throws RemoteException;
    
}
