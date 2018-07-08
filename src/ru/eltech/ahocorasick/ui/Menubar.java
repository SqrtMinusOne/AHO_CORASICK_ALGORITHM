package ru.eltech.ahocorasick.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;


public class Menubar extends JMenuBar {

    public Menubar(GraphicAlgorithmProcessor processor){
        super();
        Font font = new Font("Times New Roman", Font.PLAIN, 13);
        JMenu file = makeFile(processor, font);
        JMenu help = makeHelp(font);
        add(file);
        add(help); //add smth
    }

    private JMenu makeFile(GraphicAlgorithmProcessor processor, Font font) {
        JMenu file = createMenu("File", font);
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
        JMenu fileSave = createMenu("Save", font);
        JMenuItem saveResItem = new JMenuItem("Save results");
        saveResItem.setFont(font);
        fileSave.add(saveResItem);
        saveResItem.addActionListener(processor::saveResAction);
        JMenuItem saveGraphItem = new JMenuItem("Save graph");
        saveGraphItem.setFont(font);
        fileSave.add(saveGraphItem);
        saveGraphItem.addActionListener(processor::saveAlgorithmAction);
        file.add(fileSave);
        file.addSeparator();
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setFont(font);
        file.add(exitItem);
        exitItem.addActionListener(processor::exitAction);
        return file;
    }

    private JMenu makeHelp(Font font) {
        JMenu help = createMenu("Help", font);
        JMenuItem openHelp = new JMenuItem("Open help");
        openHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                URL file = Launcher.class.getResource("html/help.htm");
                try {
                    Desktop.getDesktop().browse(((URL) file).toURI());
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });
        help.add(openHelp);
        return help;
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
