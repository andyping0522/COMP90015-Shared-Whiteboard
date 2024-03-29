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
import remote.IRemoteChat;
import remote.IRemoteUsers;

public class BoardApp extends JFrame implements ActionListener, ChangeListener {


    private final WhiteBoard whiteBoard;
    private final JColorChooser chooser;
    private final String userName;
    private final IRemoteUsers remoteUsers;
    private final IRemoteChat chat;
    //private final ConnectionManager connectionManager;
    private final ConnectionManager connectionManager;
    private JTextArea listDisplay;
    private JTextArea chatDisplay;
    private final Thread updateThread;
    private final Thread chatThread;
    //private ArrayList<String> users;

    public BoardApp(boolean isManager, IRemoteBoard board, String userName, IRemoteUsers remoteUsers,
                    ConnectionManager connectionManager, IRemoteChat chat) {
        this.userName = userName;
        this.remoteUsers = remoteUsers;
        this.chat = chat;
        this.connectionManager = connectionManager;
        //this.users = remoteUsers.getUsers();
        this.setTitle("Shared White Board");
        this.setLayout(new BorderLayout());
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

            BoardMenu menu = new BoardMenu(this, board);


            this.add(menu, BorderLayout.NORTH);
            //this.add(bar);
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
            userList.add(kickButton);
            userList.add(kickField);
        }
        JPanel left = new JPanel();
        chatDisplay = new JTextArea();
        chatDisplay.setLineWrap(true);
        chatDisplay.setWrapStyleWord(true);
        JTextField sendField = new JTextField();
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
            String msg = sendField.getText();
            try {
                if (msg.isBlank()) {
                    JOptionPane.showMessageDialog(this, "Cannot send empty message", "Alert",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    chat.append(userName, msg);
                }

            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
        });
        left.setLayout(new BorderLayout());
        JPanel n = new JPanel();
        n.setLayout(new FlowLayout());
        n.add(sendButton);
        n.add(sendField);
        left.add(chatDisplay, BorderLayout.CENTER);
        left.add(n, BorderLayout.SOUTH);
        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        center.add(buttonPanel, BorderLayout.NORTH);
        center.add(whiteBoard, BorderLayout.CENTER);
        //this.add(name, BorderLayout.EAST);
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBounds(0, 20, 600, 600);
        main.add(left, BorderLayout.WEST);
        main.add(userList, BorderLayout.EAST);
        main.add(colorPanel, BorderLayout.SOUTH);
        main.add(center, BorderLayout.CENTER);

        this.add(main, BorderLayout.CENTER);

        updateThread = new Thread(() -> {
            try {
                setList();
                //setChat();
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

        chatThread = new Thread(() -> {
            try {
                //setList();
                setChat();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        chatThread.start();

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

    private void setChat() throws RemoteException {
        while (true) {
            String msg = chat.getMsg();
            chatDisplay.setText(msg);
        }

        //System.out.println("bb");


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
        chatThread.interrupt();
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
