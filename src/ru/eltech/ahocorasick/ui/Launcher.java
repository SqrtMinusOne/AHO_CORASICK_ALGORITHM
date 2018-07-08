package ru.eltech.ahocorasick.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;


public class Launcher extends JFrame {

    public ControlArea getControlArea() {
        return controlArea;
    }

    private final ControlArea controlArea;

    public Launcher(){
        GraphicAlgorithmProcessor processor = new GraphicAlgorithmProcessor();

        JPanel graphArea = processor.getGraphPanel();
        graphArea.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        controlArea = new ControlArea(processor);
        controlArea.setLayout(new BoxLayout(controlArea, BoxLayout.Y_AXIS)); // stretching
        controlArea.setPreferredSize(new Dimension(300, 550));

        Menubar menubar = new Menubar(processor);
        menubar.setMaximumSize(new Dimension( 100000, 10));

        JPanel graphMenuPanel = new JPanel();
        graphMenuPanel.setLayout(new BoxLayout(graphMenuPanel, BoxLayout.Y_AXIS));

        Toolbar toolbar = new Toolbar(processor);
        graphMenuPanel.add(toolbar, BorderLayout.NORTH);
        graphMenuPanel.add(graphArea, BorderLayout.SOUTH);
        graphMenuPanel.setPreferredSize(new Dimension(700, 550));

        JPanel rootWithmenu = new JPanel();
        JPanel rootWindow = new JPanel();

        rootWithmenu.setLayout(new BoxLayout(rootWithmenu, BoxLayout.Y_AXIS));
        rootWithmenu.add(menubar, BorderLayout.NORTH);
        rootWithmenu.add(rootWindow, BorderLayout.SOUTH);

        rootWindow.setLayout(new BoxLayout(rootWindow, BoxLayout.X_AXIS));
        rootWindow.setPreferredSize(new Dimension(1000, 550));
        rootWindow.add(graphMenuPanel, BorderLayout.WEST);
        rootWindow.add(controlArea, BorderLayout.EAST);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(rootWithmenu);
        pack();

        processor.start();
    }
}
