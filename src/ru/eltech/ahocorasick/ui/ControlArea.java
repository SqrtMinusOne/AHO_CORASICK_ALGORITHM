package ru.eltech.ahocorasick.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
                    catch (FileNotFoundException exeption){
                        exeption.printStackTrace();
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
        //lol.setAlignmentX(CENTER_ALIGNMENT); //same
        buttons1.add(Box.createHorizontalGlue());
        buttons1.add(file);
        buttons1.add(Box.createHorizontalGlue());
        buttons1.add(start);
        buttons1.add(Box.createHorizontalGlue());
        buttons2.add(prev);
        buttons2.add(Box.createHorizontalGlue());
        buttons2.add(stop);
        buttons2.add(Box.createHorizontalGlue());
        buttons2.add(next);
        buttons3.add(Box.createHorizontalGlue());
        buttons3.add(clear);
        buttons3.add(Box.createHorizontalGlue());
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

    public static void writeToSrcArea(File file) throws FileNotFoundException {
        String str = file.toString();
        FileReader fr = new FileReader(str);
        Scanner scanner = new Scanner(fr);
        String flContent = "";
        while(scanner.hasNextLine()){
            flContent += scanner.nextLine();
        }
        srcArea.append(flContent);
    }
}
