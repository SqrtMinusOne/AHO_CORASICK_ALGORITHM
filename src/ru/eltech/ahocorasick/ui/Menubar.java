package ru.eltech.ahocorasick.ui;

import javax.swing.*;
import java.awt.*;


public class Menubar extends JMenuBar {
    private final JMenu file;
    private final JMenu help;

    public Menubar(GraphicAlgorithmProcessor processor){
        super();
        Font font = new Font("Verdana", Font.PLAIN, 11);
        file = createMenu("File", font);
        JMenuItem openItem = new JMenuItem("Open");
        openItem.setFont(font);
        file.add(openItem);
        openItem.addActionListener(processor::openFileAction);
        file.addSeparator();
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setFont(font);
        file.add(exitItem);
        exitItem.addActionListener(processor::exitAction);
        help = createMenu("Help", font);
        add(file);
        add(help);

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
