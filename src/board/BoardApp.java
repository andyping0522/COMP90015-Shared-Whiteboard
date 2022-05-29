package board;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;


import connection.ConnectionManager;
import remote.IRemoteBoard;
import remote.IRemoteUsers;

public class BoardApp extends JFrame implements ActionListener, ChangeListener {


    private final WhiteBoard whiteBoard;
    private final JColorChooser chooser;
    private final String userName;
    private final IRemoteUsers remoteUsers;
    //private final ConnectionManager connectionManager;
    private final ConnectionManager connectionManager;
    private JTextArea listDisplay;
    private Thread updateThread;
    //private ArrayList<String> users;

    public BoardApp(boolean isManager, IRemoteBoard board, String userName, IRemoteUsers remoteUsers,
                    ConnectionManager connectionManager) {
        this.userName = userName;
        this.remoteUsers = remoteUsers;
        this.connectionManager = connectionManager;
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
        if (isManager) {
            JTextField kickField = new JTextField();
            JButton kickButton = new JButton("Kick");
            kickButton.addActionListener(e -> {
                String name1 = kickField.getText();
                try {
                    if (!remoteUsers.verifyName(name1)) {
                        popUpNotExist();

                    } else if (name1.equals(userName)) {
                        popUpKickSelf();
                    } else {
//                        System.out.println(name1);
//                        System.out.println(userName);
                        connectionManager.kick(name1);
                        remoteUsers.removeUser(name1);
                    }
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            userList.add(kickField, BorderLayout.CENTER);
            userList.add(kickButton, BorderLayout.SOUTH);

        }
        this.setLayout(new BorderLayout());
        //this.add(name, BorderLayout.EAST);
        this.add(userList, BorderLayout.EAST);
        this.add(colorPanel, BorderLayout.SOUTH);
        this.add(buttonPanel, BorderLayout.NORTH);
        this.add(whiteBoard, BorderLayout.CENTER);
        updateThread = new Thread(() -> {
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

    public void popUpJoinRequest(String userName) throws IOException {
        int a = JOptionPane.showConfirmDialog(this, "Allow " + userName + " to join?");
        if (a == JOptionPane.YES_OPTION) {
            remoteUsers.newUser(userName);
            connectionManager.approve(userName);
        } else {
            connectionManager.reject(userName);
        }
    }

    public void popUpKicked() {
        JOptionPane.showMessageDialog(this, "You have been kicked", "Alert",
                JOptionPane.WARNING_MESSAGE);

        updateThread.interrupt();
        this.setVisible(false);
        this.dispose();
    }

    private void popUpNotExist() {
        JOptionPane.showMessageDialog(this, "User does not exist", "Alert",
                JOptionPane.WARNING_MESSAGE);
    }

    private void popUpKickSelf() {
        JOptionPane.showMessageDialog(this, "Cannot kick yourself", "Alert",
                JOptionPane.WARNING_MESSAGE);
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
