package ru.eltech.ahocorasick.ui;

import ru.eltech.ahocorasick.alg.Algorithm;
import ru.eltech.ahocorasick.alg.BohrWithGraph;
import ru.eltech.ahocorasick.graph.Graph;
import ru.eltech.ahocorasick.graph.GraphPanel;
import ru.eltech.ahocorasick.graph.MouseProcessor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class combines algorithm logic and graphics
 */
public class GraphicAlgorithmProcessor {
    /**
     * Default constructor
     */
    public GraphicAlgorithmProcessor() {
        bohr = new BohrWithGraph();
        algorithm = new Algorithm(bohr);
        graphPanel = new GraphPanel(bohr.getGraph());
        graphPanel.addMouseListener(new MouseProcessor(this));
        graphPanel.addMouseMotionListener(new MouseProcessor(this));
    }

    /**
     * Returns Algorithm
     * @return Algorithm
     */
    public Algorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * Returns parent component. This is required to do some dialogs
     * @return Container
     */
    private Container getParentContainer(){
        return graphPanel.getParent();
    }

    /**
     * Starts drawing of Graph and gravity interaction
     */
    public void start(){
        //Graph processing thread
        Thread processGraph = new Thread(graphPanel);
        processGraph.start();
    }

    /**
     * Returns GraphPanel
     */
    public GraphPanel getGraphPanel() {
        return graphPanel;
    }

    /**
     * Returns Graph
     */
    public Graph getGraph(){
        return bohr.getGraph();
    }

    /**
     * Open file ActionListener
     */
    void openFileAction(ActionEvent e) //TODO Preprocessing
    {
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
        String string = JOptionPane.showInputDialog(getParentContainer(), "Input new string",
                "Add String", JOptionPane.PLAIN_MESSAGE);
        algorithm.addString(string);
    }

    /**
     * Does one step of algorithm
     */
    void stepAction(ActionEvent e){
        prepareStep();
        boolean next = algorithm.doStep();
        bohr.updateStates();
        bohr.updateEdges();
        ControlArea.getOutArea().setText(null);
        ControlArea.getOutArea().append(algorithm.resultsToString());
        if (!next){
            JOptionPane.showMessageDialog(getParentContainer(), "Algorithm is finished");
        }
    }

    /**
     * Finishes algorithm
     */
    void finishAction(ActionEvent e){
        prepareStep();
        algorithm.finishAlgorithm();
        bohr.updateStates();
        bohr.updateEdges();
        ControlArea.getOutArea().setText(algorithm.resultsToString());
    }

    /**
     * Prepares step of Algorithm
     */
    private void prepareStep() {
        if (!started){
            started = true;
            ControlArea.getSrcArea().setEditable(false);
            algorithm.setText(ControlArea.getSrcArea().getText().replaceAll("[^a-zA-Z0-9 ]", ""));
        }
        ControlArea.getOutArea().setText(null);
    }

    /**
     * Restarts the Algorithm
     */
    public void restartAction(ActionEvent e){
        algorithm.restart();
        bohr.updateStates();
        bohr.updateEdges();
        ControlArea.getSrcArea().setEditable(true);
        ControlArea.getOutArea().setText(null);
        started = false;
    }

    /**
     * Clear ActionListener
     */
    void clearAction(ActionEvent e){
        algorithm.reset();
        bohr.updateStates();
        ControlArea.getSrcArea().setEditable(true);
        ControlArea.getOutArea().setText(null);
        started = false;
    }

    /**
     * Exit ActionListener
     */
    void exitAction(ActionEvent e){
        System.exit(0);
    }

    void undoAction(ActionEvent e){
        Algorithm alg = algorithm.getHistory().undo(algorithm);
        if (alg!=null){
            algorithm = alg;
            bohr = (BohrWithGraph) alg.getBohr();
            graphPanel.setGraph(bohr.getGraph());
            ControlArea.getOutArea().setText(null);
            ControlArea.getOutArea().setText(algorithm.resultsToString());
        }
        else{
            JOptionPane.showMessageDialog(getParentContainer(), "Nothing to undo");
        }
    }

    void redoAction(ActionEvent e){
        Algorithm alg = algorithm.getHistory().redo();
        if (alg!=null){
            algorithm = alg;
            bohr = (BohrWithGraph) alg.getBohr();
            graphPanel.setGraph(bohr.getGraph());
            ControlArea.getOutArea().setText(null);
            ControlArea.getOutArea().setText(algorithm.resultsToString());
        }
    }

    void openStringsAction(ActionEvent e){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Txt files only", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(getParentContainer());
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File fl = chooser.getSelectedFile();
            Scanner fileScanner = null;
            try {
                fileScanner = new Scanner(fl);
            } catch (FileNotFoundException e1) {
                JOptionPane.showMessageDialog(getParentContainer(), "File was not open");
            }
            while (fileScanner.hasNext()){
                String str = fileScanner.nextLine().replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();
                for (String strx : str.split(" ")){
                    algorithm.addString(strx);
                }
            }
        }
    }

    private boolean started;

    private final GraphPanel graphPanel; //View
    private Algorithm algorithm; //Controller
    private BohrWithGraph bohr; //Model
}
