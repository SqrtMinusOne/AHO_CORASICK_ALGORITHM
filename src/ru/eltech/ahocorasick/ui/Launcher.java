package ru.eltech.ahocorasick.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;


public class Launcher extends JFrame {

    private final JPanel graphArea;
    private final ControlArea controlArea;
    private final Menubar menubar;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> new Launcher().setVisible(true));
    }

    private Launcher(){
        graphArea = new JPanel();
        graphArea.setBorder(new LineBorder(Color.BLACK, 5));
        graphArea.setPreferredSize(new Dimension(700, 550));
        graphArea.setBackground(Color.white); //temporary colour

        controlArea = new ControlArea();
        controlArea.setLayout(new BoxLayout(controlArea, BoxLayout.Y_AXIS)); // stretching
        controlArea.setPreferredSize(new Dimension(300, 550));


        menubar = new Menubar();
        menubar.setMaximumSize(new Dimension( 100000, 10));


        JPanel rootWithmenu = new JPanel();
        JPanel rootWindow = new JPanel();
        rootWithmenu.setLayout(new BoxLayout(rootWithmenu, BoxLayout.Y_AXIS));
        rootWithmenu.add(menubar, BorderLayout.NORTH);
        rootWithmenu.add(rootWindow, BorderLayout.SOUTH);
        //rootWindow.setVisible(true);
        rootWindow.setLayout(new BoxLayout(rootWindow, BoxLayout.X_AXIS));
        rootWindow.setPreferredSize(new Dimension(1000, 550));
        //rootWindow.add(menubar, BorderLayout.NORTH);
        rootWindow.add(graphArea, BorderLayout.WEST);
        rootWindow.add(Box.createHorizontalStrut(5), BorderLayout.CENTER);
        rootWindow.add(controlArea, BorderLayout.EAST);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(rootWithmenu);
        pack();
    }
}
