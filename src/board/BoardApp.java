package board;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static javax.swing.JOptionPane.showInputDialog;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;


import remote.IRemoteBoard;

public class BoardApp extends JFrame implements ActionListener, ChangeListener {

    private boolean isManager;


    private WhiteBoard whiteBoard;
    private JColorChooser chooser;

    public BoardApp(boolean isManager, IRemoteBoard board) {
        this.isManager = isManager;
        this.setTitle("Shared White Board");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(lineBtn);
        buttonPanel.add(circleBtn);
        buttonPanel.add(rectangleBtn);
        buttonPanel.add(textBtn);
        buttonPanel.add(triangleBtn);
        this.chooser = new JColorChooser();
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new BorderLayout());
        colorPanel.setBorder(BorderFactory.createTitledBorder(
                "Choose Text Color"));
        colorPanel.add(chooser);
        colorPanel.setPreferredSize(new Dimension(200, 200));
        chooser.setPreviewPanel(new JPanel());
        this.whiteBoard = new WhiteBoard(board);
        whiteBoard.setLayout(new BorderLayout());
        whiteBoard.setBounds(5, 50, 600, 600);
        chooser.getSelectionModel().addChangeListener(this);
        this.setLayout(new BorderLayout());
        this.add(colorPanel, BorderLayout.SOUTH);
        this.add(buttonPanel, BorderLayout.NORTH);
        this.add(whiteBoard, BorderLayout.CENTER);


        this.pack();

    }

    public void start() {
        this.setVisible(true);
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
