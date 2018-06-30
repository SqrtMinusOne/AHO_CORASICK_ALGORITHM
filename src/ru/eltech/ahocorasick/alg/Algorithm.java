package ru.eltech.ahocorasick.alg;

import java.util.ArrayList;

/**
 * This class implements logic of Aho-Corasick algorithm.
 * It also serves as facade for Node and Bohr, compressing methods of the latter
 * into rather compact form. <br>
 * This class is not linked to any form of visual logic, thought visual enhancements
 * can be made using more advanced Bohr class. <br>
 */
public class Algorithm { //TODO: Exceptions fix
    /**
     * Default constructor
     * @param bohr Required Bohr object
     */
    public Algorithm(Bohr bohr){
        this.bohr = bohr;
        strings = new ArrayList<>();
        results = new ArrayList<>();
        text = "";
    }

    /**
     * This constructor creates Algorithm with a default Bohr
     */
    public Algorithm(){
        this(new Bohr());
    }

    /**
     * Returns current position in text
     */
    public int getTextPosition() {
        return textPosition;
    }

    /**
     * Resets whole Algorithm
     */
    public void reset(){
        bohr.clear();
        strings.clear();
        results.clear();
        text = "";
        textPosition = 0;
    }

    /**
     * Resets of algorithm status
     */
    public void restart(){
        textPosition = 0;
        bohr.clearTransitions();
        results.clear();
    }

    /**
     * Returns set text
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * Returns ArrayList of results
     * @return ArrayList
     */
    public ArrayList<AlgorithmResult> getResults() {
        return processResults(results);
    }

    /**
     * Processes results to be more informative
     * @param results ArrayList of unprocessed results
     * @return ArrayList of processed results
     */
    private ArrayList<AlgorithmResult> processResults(ArrayList<AlgorithmResult> results) {
        ArrayList<AlgorithmResult> processed = new ArrayList<>(results.size());
        for (AlgorithmResult res : results){
            int delta = strings.get(res.getPatternNumber()).length() - 1;
            AlgorithmResult newRes = new AlgorithmResult(res.getIndex() - delta, res.getPatternNumber());
            processed.add(newRes);
        }
        return processed;
    }

    /**
     * Sets up text
     * @param text String
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Adds string to Bohr
     * @param str String
     */
    public void addString(String str){
        bohr.addString(str);
        strings.add(str);
    }

    /**
     * Does one step
     * @return true if step was successful
     */
    public boolean doStep(){
        if (textPosition >= text.length())
            return false;
        bohr.getNextState(text.charAt(textPosition++));
        for (Node cur = bohr.getState(); cur != bohr.getRoot(); cur = bohr.getUp(cur)){
            if (cur.isLeaf()){
                for (int i : cur.getLeafPatternNumber()){
                    results.add(new AlgorithmResult(textPosition-1, i));
                }
            }
        }
        return true;
    }

    /**
     * Static methods which does all algorithm logic
     * @param text text to be processed
     * @param strings iterable list of strings
     * @return ArrayList of results
     */
    public static ArrayList<AlgorithmResult> doAhoCorasick(String text, Iterable<String> strings){
        Algorithm alg = new Algorithm(new Bohr());
        for (String str : strings){
            alg.addString(str);
        }
        alg.setText(text);
        alg.finishAlgorithm();
        return alg.getResults();
    }

    /**
     * Finishes algorithm
     */
    public void finishAlgorithm() {
        boolean fin;
        do{
            fin = doStep();
        } while(fin);
    }

    @Override
    public String toString() {
        return "Algorithm {\n" +
                bohr +
                "\ntextPosition = " + textPosition +
                '}';
    }

    private Bohr bohr;
    private ArrayList<String> strings;
    private ArrayList<AlgorithmResult> results;
    private String text;
    private int textPosition;
}

