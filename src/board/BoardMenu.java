package board;

import remote.IRemoteBoard;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class BoardMenu extends JPanel{

    private JFrame f;
    private IRemoteBoard board;

    public BoardMenu(JFrame f, IRemoteBoard board){
        this.f = f;
        this.board = board;
        this.setLayout(new FlowLayout());

        JButton newBoard = new JButton("New");
        newBoard.addActionListener(e -> {
            try {
                board.clear();
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
        });
        this.add(newBoard);

        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter obj = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");
            String formatted = obj.format(date);
            String fname = "Drawing-on-" +formatted+ ".txt";
            saveToFile(fname);
            JOptionPane.showMessageDialog(f, "Saved as " + fname);
        });
        this.add(save);


        JButton open = new JButton("Open");
        open.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File", "txt");
            chooser.setFileFilter(filter);
            int a = chooser.showOpenDialog(f);
            if (a == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    FileInputStream in = new FileInputStream(file);
                    ObjectInputStream objIn = new ObjectInputStream(in);
                    ArrayList<ColorShape> shapes = new ArrayList<>();
                    //boolean flag = true;
                    while (in.available() > 0) {
                        Object obj  = objIn.readObject();

                        shapes.add((ColorShape) obj);
                    }
                    board.setShapes(shapes);
                    objIn.close();
                    in.close();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
            }
        });
        this.add(open);

        JButton saveAs = new JButton("Save As");
        saveAs.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int a = chooser.showSaveDialog(f);
            if (a == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                String fname = file.getPath();
                if (!fname.endsWith(".txt")) {
                    fname = fname + ".txt";
                }
                saveToFile(fname);
                JOptionPane.showMessageDialog(f, "Saved");
            }
        });
        this.add(saveAs);

        JButton close = new JButton("Close");
        close.addActionListener(e -> {
            f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
        });
        this.add(close);
    }

    private void saveToFile(String fname) {
        //System.out.println(fname);
        try {
            File f = new File(fname);
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            ObjectOutputStream objectOut = new ObjectOutputStream(out);
            ArrayList<ColorShape> shapes= board.getComponents();
            for (ColorShape shape:shapes) {
                objectOut.writeObject(shape);
            }
            objectOut.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
