package board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoardApp extends JFrame implements ActionListener, MouseListener {

    private boolean isManager;
    private String actionType;

    public BoardApp(boolean isManager) {
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
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(lineBtn);
        buttonPanel.add(circleBtn);
        buttonPanel.add(rectangleBtn);
        buttonPanel.add(textBtn);
        buttonPanel.add(triangleBtn);
        JPanel canvas = new JPanel();

        this.addMouseListener(this);
        this.add(buttonPanel);
        this.add(canvas);
        this.pack();

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

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
