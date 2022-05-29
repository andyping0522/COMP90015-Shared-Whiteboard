package remote;


import board.ColorShape;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;
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
    private ArrayList<ColorShape> shapes;
    public RemoteBoard() throws RemoteException {
        this.board = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
        this.shapes = new ArrayList<>();
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, Color c) throws RemoteException {

        Shape line = new Line2D.Double(x1, y1, x2, y2);
        this.shapes.add(new ColorShape(c, line));
        //System.out.println("yo");
    }

    @Override
    public void drawRectangle(int x, int y, int w, int h, Color c) throws RemoteException {
        Shape rect = new Rectangle(x, y, w, h);
        this.shapes.add(new ColorShape(c, rect));
        //System.out.println("bruh");
    }

    @Override
    public void drawText(String text, int x, int y, Color c) throws RemoteException {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        Font font = new Font("TimesRoman", Font.PLAIN, 20);

        GlyphVector vect = font.createGlyphVector(g.getFontRenderContext(), text);
        Shape t = vect.getOutline(x, y);
        this.shapes.add(new ColorShape(c, t));
    }

    @Override
    public void drawTriangle(int x1, int y1, int x2, int y2, Color c) throws RemoteException {

        // calculate side length
        int horizontal = x2 - x1;
        int x3;
        if (x2 < x1) {
            x3 = x1 - horizontal;
        } else {
            x3 = x2 - 2 * horizontal;
        }
        int y3 = y2;


        Shape tri = new Polygon(new int[] {x1, x2, x3}, new int[] {y1, y2, y3}, 3);
        this.shapes.add(new ColorShape(c, tri));
    }

    @Override
    public void drawCircle(int x, int y, int w, int h, Color c) throws RemoteException {
        Graphics g = this.board.getGraphics();


        g.drawOval(x, y, w, h);
        System.out.println("bruh");
        Shape oval = new Ellipse2D.Double(x, y, w, h);
        this.shapes.add(new ColorShape(c, oval));
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
    public ArrayList<ColorShape> getComponents() {
        return shapes;
    }
}
