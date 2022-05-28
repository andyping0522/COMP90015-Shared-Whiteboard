package board;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;


import remote.IRemoteBoard;
import remote.IRemoteUsers;

public class BoardApp extends JFrame implements ActionListener, ChangeListener {


    private final WhiteBoard whiteBoard;
    private final JColorChooser chooser;
    private final String userName;
    private IRemoteUsers remoteUsers;
    private JTextArea listDisplay;
    //private ArrayList<String> users;

    public BoardApp(boolean isManager, IRemoteBoard board, String userName, IRemoteUsers remoteUsers) {
        this.userName = userName;
        this.remoteUsers = remoteUsers;
        //this.users = remoteUsers.getUsers();
        this.setTitle("Shared White Board");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel name = new JLabel();
        name.setText("Welcome, "+ userName + "      ");
        //name.setLayout(new BorderLayout());
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
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(name, BorderLayout.NORTH);
        buttonPanel.add(lineBtn, BorderLayout.SOUTH);
        buttonPanel.add(circleBtn, BorderLayout.SOUTH);
        buttonPanel.add(rectangleBtn, BorderLayout.SOUTH);
        buttonPanel.add(textBtn, BorderLayout.SOUTH);
        buttonPanel.add(triangleBtn, BorderLayout.SOUTH);
        this.chooser = new JColorChooser();
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new BorderLayout());
        colorPanel.setBorder(BorderFactory.createTitledBorder(
                "Choose Text Color"));
        colorPanel.add(chooser);
        colorPanel.setPreferredSize(new Dimension(200, 200));
        chooser.setPreviewPanel(new JPanel());
        listDisplay = new JTextArea();
        listDisplay.setLineWrap(true);
        listDisplay.setWrapStyleWord(true);

        JPanel userList = new JPanel();
        userList.add(listDisplay, BorderLayout.NORTH);
        this.whiteBoard = new WhiteBoard(board);
        whiteBoard.setLayout(new BorderLayout());
        //whiteBoard.setBounds(5, 50, 600, 600);
        chooser.getSelectionModel().addChangeListener(this);
        this.setLayout(new BorderLayout());
        //this.add(name, BorderLayout.EAST);
        this.add(listDisplay, BorderLayout.EAST);
        this.add(colorPanel, BorderLayout.SOUTH);
        this.add(buttonPanel, BorderLayout.NORTH);
        this.add(whiteBoard, BorderLayout.CENTER);
        Thread updateThread = new Thread(() -> {
            try {
                setList();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        updateThread.start();

        this.pack();

    }

    public void start() {
        this.setVisible(true);
    }


    private void setList() throws RemoteException {

        while (true) {
            ArrayList<String> users = this.remoteUsers.getUsers();
            String result = "";
            for (String user:users){
                result = result + user + "\n";
            }
            this.listDisplay.setText(result);
        }

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        whiteBoard.setType(e.getActionCommand());
    }


    @Override
    public void stateChanged(ChangeEvent e) {
        whiteBoard.setColor(this.chooser.getColor());
    }
}
