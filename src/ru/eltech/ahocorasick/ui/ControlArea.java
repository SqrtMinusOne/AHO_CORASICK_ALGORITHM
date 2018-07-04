package ru.eltech.ahocorasick.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;


public class ControlArea extends JPanel {

    public ControlArea(GraphicAlgorithmProcessor processor){
        super(null);
        this.processor = processor;
        initPanes();
        Box mainBox = Box.createVerticalBox();
        mainBox.setBorder(new TitledBorder("Control components"));
        Box initialBox = createInitialBox();
        Box setupBox = createSetupBox();
        Box stepsBox = createStepsBox();
        Box clearAndExitBox = createClearAndExitBox();
        mainBox.add(initialBox);
        mainBox.add(setupBox);
        mainBox.add(stepsBox);
        mainBox.add(clearAndExitBox);
        add(mainBox);
    }

    /**
     * Initialize In and Out panes
     */
    private void initPanes() {
        JScrollPane srcPane = new JScrollPane(srcArea);
        srcPane.setPreferredSize(new Dimension(100, 200));
        srcPane.setBorder(new TitledBorder("Source text"));
        JScrollPane outPane = new JScrollPane(outArea);
        outPane.setPreferredSize(new Dimension(100, 200));
        outPane.setBorder(new TitledBorder("Output text"));
        add(srcPane);
        add(outPane);
    }

    /**
     * Create Initial Box
     * @return Box
     */
    private Box createInitialBox() {
        Box initialBox = Box.createHorizontalBox();
        initialBox.setBorder((new TitledBorder("Initial actions")));
        JButton file = new JButton("Choose a file");
        JButton start = new JButton("Start algorithm");
        initialBox.add(Box.createHorizontalGlue());
        initialBox.add(file);
        initialBox.add(Box.createHorizontalGlue());
        initialBox.add(start);
        initialBox.add(Box.createHorizontalGlue());
        file.addActionListener(processor::openFileAction);
        return initialBox;
    }

    /**
     * This box contains add string button
     * @return
     */
    private Box createSetupBox(){
        Box setupBox = Box.createHorizontalBox();
        setupBox.setBorder(new TitledBorder("Algorithm setup"));
        JButton addStringButton = new JButton("Add string");
        setupBox.add(Box.createHorizontalGlue());
        setupBox.add(addStringButton);
        setupBox.add(Box.createHorizontalGlue());
        addStringButton.addActionListener(processor::addStringAction);
        return setupBox;
    }

    /**
     * Create steps control Box
     * @return Box
     */
    private Box createStepsBox() {
        Box stepsBox = Box.createHorizontalBox();
        stepsBox.setBorder((new TitledBorder("Steps control")));
        JButton prev = new JButton("<<");
        JButton stop = new JButton("Pause / Resume");
        JButton next = new JButton(">>");
        stepsBox.add(Box.createHorizontalGlue());
        stepsBox.add(prev);
        stepsBox.add(Box.createHorizontalGlue());
        stepsBox.add(stop);
        stepsBox.add(Box.createHorizontalGlue());
        stepsBox.add(next);
        stepsBox.add(Box.createHorizontalGlue());
        return stepsBox;
    }

    /**
     * Create Clear and Exit control Box
     * @return Box
     */
    private Box createClearAndExitBox() {
        Box clearAndExitBox = Box.createHorizontalBox();
        clearAndExitBox.setBorder((new TitledBorder("Clear and exit")));
        JButton clear = new JButton("Clear");
        JButton exit = new JButton("Exit");
        clearAndExitBox.add(Box.createHorizontalGlue());
        clearAndExitBox.add(clear);
        clearAndExitBox.add(Box.createHorizontalGlue());
        clearAndExitBox.add(exit);
        clearAndExitBox.add(Box.createHorizontalGlue());
        clear.addActionListener(processor::clearAction);
        exit.addActionListener(processor::exitAction);
        return clearAndExitBox;
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

    private static final JTextArea srcArea = createArea();
    private static final JTextArea outArea = createArea();
    private final GraphicAlgorithmProcessor processor;
}
