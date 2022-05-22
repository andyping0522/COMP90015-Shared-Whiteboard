package board;

import javax.swing.*;
import static javax.swing.JOptionPane.showInputDialog;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;


import remote.IRemoteBoard;

public class BoardApp extends JFrame implements ActionListener, MouseListener {

    private boolean isManager;
    private String actionType;
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    private IRemoteBoard board;

    public BoardApp(boolean isManager, IRemoteBoard board) {
        this.isManager = isManager;
        this.setTitle("Shared White Board");
        this.board = board;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.actionType = "Rectangle";
        if (this.isManager) {
            // display file menu
        }
        ButtonGroup group = new ButtonGroup();
        JRadioButton lineBtn = new JRadioButton("Line");
        JRadioButton circleBtn = new JRadioButton("Circle");
        JRadioButton triangleBtn = new JRadioButton("Triangle");
        JRadioButton rectangleBtn = new JRadioButton("Rectangle");
        JRadioButton textBtn = new JRadioButton("Text");
        group.add(lineBtn);
        group.add(circleBtn);
        group.add(triangleBtn);
        group.add(rectangleBtn);
        group.add(textBtn);
        lineBtn.addActionListener(this);
        circleBtn.addActionListener(this);
        triangleBtn.addActionListener(this);
        rectangleBtn.addActionListener(this);
        textBtn.addActionListener(this);
        rectangleBtn.setSelected(true);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(lineBtn);
        buttonPanel.add(circleBtn);
        buttonPanel.add(rectangleBtn);
        buttonPanel.add(textBtn);
        buttonPanel.add(triangleBtn);
        JPanel canvas = new JPanel();
        canvas.setLayout(new BorderLayout());
        canvas.setBounds(10, 30, 10, 10);
        canvas.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());
        this.addMouseListener(this);
        this.add(buttonPanel, BorderLayout.NORTH);
        this.add(canvas, BorderLayout.CENTER);
        this.pack();

    }

    public void start() {
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actionType = e.getActionCommand();
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

        switch (this.actionType) {
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
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }

        }

        
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

}
