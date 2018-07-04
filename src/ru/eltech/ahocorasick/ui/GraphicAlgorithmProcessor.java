package ru.eltech.ahocorasick.ui;

import ru.eltech.ahocorasick.alg.Algorithm;
import ru.eltech.ahocorasick.alg.BohrWithGraph;
import ru.eltech.ahocorasick.graph.GraphPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;

public class GraphicAlgorithmProcessor {
    public GraphicAlgorithmProcessor() {
        bohr = new BohrWithGraph();
        algorithm = new Algorithm(bohr);
        graphPanel = new GraphPanel(bohr.getGraph());
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    private Container getParentContainer(){
        return graphPanel.getParent();
    }

    public void start(){
        processGraph = new Thread(graphPanel);
        processGraph.start();
    }

    public GraphPanel getGraphPanel() {
        return graphPanel;
    }

    private GraphPanel graphPanel; //View
    private Thread processGraph;
    private Algorithm algorithm; //Controller
    private BohrWithGraph bohr; //Model

    /**
     * Open file ActionListener
     */
    void openFileAction(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Txt files only", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(getParentContainer());
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File fl = chooser.getSelectedFile();
            try {
                ControlArea.writeToSrcArea(fl);
            }
            catch (FileNotFoundException exeption){
                exeption.printStackTrace();
            }
        }
    }

    /**
     * Add string ActionListener
     */
    void addStringAction(ActionEvent e){
        String string = (String)JOptionPane.showInputDialog(getParentContainer(), "Input new string",
                "Add String", JOptionPane.PLAIN_MESSAGE);
        algorithm.addString(string);
    }

    /**
     * Clear ActionListener
     */
    void clearAction(ActionEvent e){
        algorithm.reset();
    }

    void exitAction(ActionEvent e){
        System.exit(0);
    }
}
