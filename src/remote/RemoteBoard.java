package remote;


import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.lang.Math;
import java.util.ArrayList;

public class RemoteBoard extends UnicastRemoteObject implements IRemoteBoard {

    private BufferedImage board;
    private ArrayList<Shape> shapes;
    public RemoteBoard() throws RemoteException {
        this.board = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
        this.shapes = new ArrayList<>();
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) throws RemoteException {
        Graphics g = this.board.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.drawLine(x1, y1, x2, y2);
        Shape line = new Line2D.Double(x1, y1, x2, y2);
        this.shapes.add(line);
    }

    @Override
    public void drawRectangle(int x, int y, int w, int h) throws RemoteException {
        Graphics g = this.board.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.drawRect(x, y, w, h);
        System.out.println("bruh");
    }

    @Override
    public void drawText(String text, int x, int y) throws RemoteException {
        Graphics g = this.board.getGraphics();
        Graphics2D g2 = (Graphics2D) g;

        g2.drawString(text, x, y);

    }

    @Override
    public void drawTriangle(int x1, int y1, int x2, int y2) throws RemoteException {
        Graphics g = this.board.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        // calculate side length
        int horizontal = Math.abs(x2 - x1);
        int x3 = x1 - horizontal;
        int y3 = y2;

        g2.drawPolygon(new int[] {x1, x2, x3}, new int[] {y1, y2, y3}, 3);

    }

    @Override
    public void drawCircle(int x, int y, int w, int h) throws RemoteException {
        Graphics g = this.board.getGraphics();


        g.drawOval(x, y, w, h);
        System.out.println("bruh");
    }

    @Override
    public void clear() throws RemoteException {
        this.board = new BufferedImage(1500, 1500, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public byte[] getBoard() throws RemoteException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] out;
        try{
            ImageIO.write(board, "jpg", os);
            System.out.println("hello");
            out = os.toByteArray();
            System.out.println(out);
        } catch (IOException e) {
            e.printStackTrace();
            out = null;
        }
        return out;

    }

    @Override
    public ArrayList<Shape> getComponents() {
        return shapes;
    }
}
