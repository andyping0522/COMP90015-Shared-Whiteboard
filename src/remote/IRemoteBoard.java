package remote;



import board.ColorShape;

import java.awt.*;
//import java.awt.image.BufferedImage;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface IRemoteBoard extends Remote {

    void drawLine(int x1, int y1, int x2, int y2, Color c) throws RemoteException;

    void drawRectangle(int x, int y, int w, int h, Color c) throws RemoteException;

    void drawText(String text, int x, int y, Color c) throws RemoteException;

    void drawTriangle(int x1, int y1, int x2, int y2, Color c) throws RemoteException;

    void drawCircle(int x, int y, int w, int h, Color c) throws RemoteException;

    void clear() throws RemoteException;

    void setShapes(ArrayList<ColorShape> shapes) throws RemoteException;


    ArrayList<ColorShape> getComponents() throws RemoteException;
    
}
