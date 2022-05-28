package board;

import remote.IRemoteBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.EventListener;

import static javax.swing.JOptionPane.showInputDialog;

public class WhiteBoard extends JPanel implements MouseListener{

    private String type;
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    private IRemoteBoard board;


    public WhiteBoard(IRemoteBoard board) {
        type = "Rectangle";
        this.board = board;
        setBackground(Color.WHITE);
        addMouseListener(this);

    }

    public void setType(String type) {
        this.type = type;
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        this.x1 = e.getX();
        this.y1 = e.getY();

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.x2 = e.getX();
        this.y2 = e.getY();

        switch (this.type) {
            case "Line":
                try {
                    this.board.drawLine(x1, y1, x2, y2);
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
                break;


            case "Circle":
                try {
                    int width = Math.abs(x2 - x1);
                    int height = Math.abs(y2 - y1);
                    this.board.drawCircle((int)this.getCoord().getX(), (int)this.getCoord().getY(), width, height);

                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
                break;

            case "Rectangle":

                try {
                    int width = Math.abs(x2 - x1);
                    int height = Math.abs(y2 - y1);
                    this.board.drawRectangle((int)this.getCoord().getX(), (int)this.getCoord().getY(), width, height);

                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
                break;

            case "Triangle":
                try {
                    int width = Math.abs(x2 - x1);
                    int height = Math.abs(y2 - y1);
                    this.board.drawTriangle(x1, y1, x2, y2);
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();

                }
                break;

            case "Text":
                try {
                    String text = showInputDialog("Enter text: ");
                    this.board.drawText(text, (int)this.getCoord().getX(), (int)this.getCoord().getY());
                    //this.repaint();
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }

        }
        //this.repaint();


    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private Point getCoord() {
        if (x1 < x2 && y1 < y2) {
            return new Point(x1, y2);
        } else if (x1 < x2 && y1 > y2) {
            return new Point(x1, y1);
        } else if (x1 > x2 && y1 > y2) {
            return new Point(x2, y1);
        } else {
            return new Point(x2, y2);
        }
    }




    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //setIgnoreRepaint(true);
        g.drawLine(199, 100, 250, 300);
        try {


            ArrayList<Shape> shapes = this.board.getComponents();
            for (Shape shape:shapes) {
                Graphics2D g3 = (Graphics2D)g;
                g3.draw(shape);
            }
            this.repaint();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(600, 600);
    }
}
