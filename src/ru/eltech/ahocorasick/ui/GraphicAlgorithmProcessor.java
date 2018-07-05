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

public class GraphicAlgorithmProcessor {
    public GraphicAlgorithmProcessor() {
        bohr = new BohrWithGraph();
        algorithm = new Algorithm(bohr);
        graphPanel = new GraphPanel(bohr.getGraph());
        graphPanel.addMouseListener(new MouseProcessor(this));
        graphPanel.addMouseMotionListener(new MouseProcessor(this));
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    private Container getParentContainer(){
        return graphPanel.getParent();
    }

    public void start(){
        //Graph processing thread
        Thread processGraph = new Thread(graphPanel);
        processGraph.start();
    }

    public GraphPanel getGraphPanel() {
        return graphPanel;
    }

    public Graph getGraph(){
        return bohr.getGraph();
    }

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
        String string = JOptionPane.showInputDialog(getParentContainer(), "Input new string",
                "Add String", JOptionPane.PLAIN_MESSAGE);
        algorithm.addString(string);
    }

    /**
     * Clear ActionListener
     */
    void clearAction(ActionEvent e){
        algorithm.reset();
        bohr.updateStates();
    }

    /**
     * Exit ActionListener
     */
    void exitAction(ActionEvent e){
        System.exit(0);
    }

    /**
     * Does one step of algorithm
     */
    void stepAction(ActionEvent e){
        prepareStep();
        boolean next = algorithm.doStep();
        bohr.updateStates();
        ControlArea.getOutArea().setText(algorithm.resultsToString());
        if (!next){
            JOptionPane.showMessageDialog(getParentContainer(), "Algorithm is finished");
        }
    }

    /**
     * Finishes algorithm
     * @param e
     */
    void finishAction(ActionEvent e){
        prepareStep();
        algorithm.finishAlgorithm();
        ControlArea.getOutArea().setText(algorithm.resultsToString());
    }

    private void prepareStep() {
        if (!started){
            started = true;
            ControlArea.getSrcArea().setEditable(false);
            algorithm.setText(ControlArea.getSrcArea().getText());
        }
        ControlArea.getOutArea().setText(null);
    }

    public void restartAction(ActionEvent e){
        algorithm.restart();
        bohr.updateStates();
        ControlArea.getSrcArea().setEditable(true);
        ControlArea.getOutArea().setText(null);
    }

    private boolean started;

    private final GraphPanel graphPanel; //View
    private final Algorithm algorithm; //Controller
    private final BohrWithGraph bohr; //Model
}
