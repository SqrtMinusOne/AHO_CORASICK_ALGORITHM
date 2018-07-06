package ru.eltech.ahocorasick.ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Menubar extends JMenuBar {
    private final JMenu file;
    private final JMenu help;

    public Menubar(){
        super();
        Font font = new Font("Verdana", Font.PLAIN, 11);
        file = createMenu("File", font);
        JMenuItem openItem = new JMenuItem("Open");
        openItem.setFont(font);
        file.add(openItem);
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Txt files only", "txt");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(Menubar.super.getParent());
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File fl = chooser.getSelectedFile();
                    try {
                        ControlArea.writeToSrcArea(fl);
                    }
                    catch (IOException exception){
                        exception.printStackTrace();
                    }
                }
            }
        });
        JMenuItem saveResItem = new JMenuItem("Save results");
        saveResItem.setFont(font);
        file.add(saveResItem);
        saveResItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Txt files only", "txt");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(Menubar.super.getParent());
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File fl = chooser.getSelectedFile();
                    try {
                        ControlArea.saveFromOutArea(fl);
                    }
                    catch (IOException exception){
                        exception.printStackTrace();
                    }
                }
            }
        });
        JMenuItem saveGraphItem = new JMenuItem("Save graph");
        saveGraphItem.setFont(font);
        file.add(saveGraphItem);
        file.addSeparator();
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setFont(font);
        file.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        help = createMenu("Help", font);
        add(file);
        add(help); //add smth
    }

    private JMenu createMenu(String name, Font font){
        JMenu menu = new JMenu(name);
        menu.setFont(font);
        return menu;
    }

    private void createMenuItem(JMenu father, String name, Font font){
        JMenuItem menuItem = new JMenuItem(name);
        menuItem.setFont(font);
        father.add(menuItem);
    }
}
