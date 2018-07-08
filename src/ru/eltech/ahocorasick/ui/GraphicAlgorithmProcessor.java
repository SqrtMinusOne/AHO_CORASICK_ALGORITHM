package ru.eltech.ahocorasick.ui;

import ru.eltech.ahocorasick.Settings;
import ru.eltech.ahocorasick.alg.Algorithm;
import ru.eltech.ahocorasick.alg.BohrWithGraph;
import ru.eltech.ahocorasick.graph.Graph;
import ru.eltech.ahocorasick.graph.GraphPanel;
import ru.eltech.ahocorasick.graph.MouseProcessor;
import ru.eltech.ahocorasick.graph.Vertex;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
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
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Txt files only", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(getParentContainer());
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
        updateSample();
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
        updateSample();
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
        updateSample();
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
            updateSample();
        }
        else{
            JOptionPane.showMessageDialog(getParentContainer(), "Nothing to undo");
        }
    }

    /**
     * Save result
     */
    void saveResAction(ActionEvent e){
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Txt files only", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(getParentContainer());
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

    /**
     * Save graph
     */
    void saveAlgorithmAction(ActionEvent e){
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "ACGraph files only", "acgraph");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(getParentContainer());
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File fl = chooser.getSelectedFile();
            try {
                FileWriter fw = new FileWriter(fl);
                fw.write(algorithm.toString());
                fw.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     *
     * Load graph from file
     */
    void openAlgorithmAction(ActionEvent e){
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "ACGraph files only", "acgraph");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(getParentContainer());
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File fl = chooser.getSelectedFile();
            try {
                Scanner scanner = new Scanner(fl);
                StringBuilder flContent = new StringBuilder();
                while(scanner.hasNextLine()){
                    flContent.append(scanner.nextLine() + "\n");
                }
                algorithm = Algorithm.fromString(flContent.toString());
                bohr = (BohrWithGraph) algorithm.getBohr();
                graphPanel.setGraph(bohr.getGraph());
                ControlArea.getOutArea().setText(null);
                ControlArea.getOutArea().setText(algorithm.resultsToString());
                updateSample();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     *
     * Return to the last state
     */
    void redoAction(ActionEvent e){
        Algorithm alg = algorithm.getHistory().redo();
        if (alg!=null){
            algorithm = alg;
            bohr = (BohrWithGraph) alg.getBohr();
            graphPanel.setGraph(bohr.getGraph());
            ControlArea.getOutArea().setText(null);
            ControlArea.getOutArea().setText(algorithm.resultsToString());
            updateSample();
        }
    }


    /**
     *
     * Build bohr with text from the file
     */
    void openStringsAction(ActionEvent e){
        JFileChooser chooser = new JFileChooser("C:");
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

    public void makePopUp(Vertex v){
        v.setPopUpInfo(bohr.getNode(v.getId()).toString());
    }

    public void removePopUps(){
        for (Vertex v : getGraph().getVertices()){
            v.setPopUpInfo(null);
        }
    }

    void updateSample(){
        graphPanel.setExample(getSample(Settings.sampleSize()));
    }

    public String getSample(int size){
        size--;
        int pos = size/2;
        int start = (algorithm.getTextPosition() - pos);
        start = (start + size > algorithm.getText().length() ?
                algorithm.getText().length() - size : start);
        start = (start < 0 ? 0 : start);
        int end = Integer.max(start + size, algorithm.getTextPosition());
        end = Integer.min(end+1, algorithm.getText().length());
        StringBuilder sb = new StringBuilder();
        sb.append(algorithm.getText(), start, algorithm.getTextPosition());
        sb.append("|");
        sb.append(algorithm.getText(), algorithm.getTextPosition(), end);
        return sb.toString();
    }

    public void openSettingsAction(ActionEvent e){
        JFrame settings = new SettingsFrame();
        settings.setVisible(true);
    }

    private boolean started;

    private final GraphPanel graphPanel; //View
    private Algorithm algorithm; //Controller
    private BohrWithGraph bohr; //Model
}
