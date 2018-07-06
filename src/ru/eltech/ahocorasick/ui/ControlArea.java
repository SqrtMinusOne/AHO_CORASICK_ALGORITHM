package ru.eltech.ahocorasick.ui;

import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;


public class ControlArea extends JPanel {

    private static final JTextArea srcArea = createArea();
    private static final JTextArea outArea = createArea();

    public ControlArea(){
        super(null);
        JScrollPane srcPane = new JScrollPane(srcArea);
        srcPane.setPreferredSize(new Dimension(100, 200));
        srcPane.setBorder(new TitledBorder("Source text"));
        JScrollPane outPane = new JScrollPane(outArea);
        outPane.setPreferredSize(new Dimension(100, 200));
        outPane.setBorder(new TitledBorder("Output text"));
        add(srcPane);
        add(outPane);
        Box buttons = Box.createVerticalBox();
        Box buttons1 = Box.createHorizontalBox();
        Box buttons2 = Box.createHorizontalBox();
        Box buttons3 = Box.createHorizontalBox();
        buttons.setMaximumSize(new Dimension(300, 200));
        buttons.setMinimumSize(new Dimension(300, 200));
        //buttons.setAlignmentX(LEFT_ALIGNMENT);
        //buttons1.setAlignmentX(RIGHT_ALIGNMENT);
        //buttons.setBounds(0, 330, 290, 200); //w/o stretching
        buttons.add(buttons1);
        buttons.add(buttons2);
        buttons.add(buttons3);
        JButton file = new JButton("Choose a file");
        file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Txt files only", "txt");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(ControlArea.super.getParent());
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File fl = chooser.getSelectedFile();
                    try {
                        writeToSrcArea(fl);
                    }
                    catch (IOException exception){
                        exception.printStackTrace();
                    }

                }
            }
        });
        JButton start = new JButton("Start algorithm");
        JButton prev = new JButton("<<");
        JButton stop = new JButton("Pause / Resume");
        JButton next = new JButton(">>");
        JButton clear = new JButton("Clear");
        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        //lol.setAlignmentX(CENTER_ALIGNMENT); //same
        buttons1.add(Box.createHorizontalGlue());
        buttons1.add(file);
        buttons1.add(Box.createHorizontalStrut(5));
        buttons1.add(start);
        buttons1.add(Box.createHorizontalGlue());
        buttons2.add(Box.createHorizontalGlue());
        buttons2.add(prev);
        buttons2.add(Box.createHorizontalStrut(5));
        buttons2.add(stop);
        buttons2.add(Box.createHorizontalStrut(5));
        buttons2.add(next);
        buttons2.add(Box.createHorizontalGlue());
        buttons3.add(Box.createHorizontalGlue());
        buttons3.add(clear);
        buttons3.add(Box.createHorizontalStrut(5));
        buttons3.add(exit);
        buttons3.add(Box.createHorizontalGlue());
        buttons.setBorder(new TitledBorder("Control components"));
        buttons1.setBorder((new TitledBorder("Initial actions")));
        buttons2.setBorder((new TitledBorder("Steps control")));
        buttons3.setBorder((new TitledBorder("Clear and exit")));
        add(buttons);


    }

    private static JTextArea createArea(){
        /*JLabel label = new JLabel(areaName);
        label.setBounds(x, y, 290, 20);
        add(label);*/ //labels
        JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    public static void writeToSrcArea(File file) throws IOException{
        String str = file.toString();
        FileReader fr = new FileReader(str);
        Scanner scanner = new Scanner(fr);
        String flContent = "";
        while(scanner.hasNextLine()){
            flContent += scanner.nextLine();
        }
        srcArea.append(flContent);
        fr.close();
    }

    public static void saveFromOutArea(File file) throws IOException{
        FileWriter fw = new FileWriter(file);
        fw.write("Source text:\n" + srcArea.getText() + "\n\n");
        fw.write("Results:\n" + outArea.getText());
        fw.close();
    }
}
