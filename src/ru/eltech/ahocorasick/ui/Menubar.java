package ru.eltech.ahocorasick.ui;

import javax.swing.*;
import java.awt.*;


public class Menubar extends JMenuBar {
    private final JMenu file;
    private final JMenu help;

    public Menubar(GraphicAlgorithmProcessor processor){
        super();
        Font font = new Font("Times New Roman", Font.PLAIN, 13);
        file = createMenu("File", font);
        JMenu openMenu = new JMenu("Open");
        openMenu.setFont(font);
        file.add(openMenu);
        JMenuItem openTextItem = new JMenuItem("Open text");
        openTextItem.setFont(font);
        openTextItem.addActionListener(processor::openFileAction);
        JMenuItem openStringsItem = new JMenuItem("Open strings");
        openStringsItem.setFont(font);
        openStringsItem.addActionListener(processor::openStringsAction);
        JMenuItem openGraphItem = new JMenuItem("Open graph");
        openGraphItem.setFont(font);
        openGraphItem.addActionListener(processor::openAlgorithmAction);
        openMenu.add(openTextItem);
        openMenu.add(openStringsItem);
        openMenu.add(openGraphItem);
        JMenuItem saveResItem = new JMenuItem("Save results");
        saveResItem.setFont(font);
        file.add(saveResItem);
        saveResItem.addActionListener(processor::saveResAction);
        JMenuItem saveGraphItem = new JMenuItem("Save graph");
        saveGraphItem.setFont(font);
        file.add(saveGraphItem);
        saveGraphItem.addActionListener(processor::saveAlgorithmAction);
        file.addSeparator();
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setFont(font);
        file.add(exitItem);
        exitItem.addActionListener(processor::exitAction);
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
