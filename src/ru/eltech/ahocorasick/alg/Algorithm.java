package ru.eltech.ahocorasick.alg;

import java.util.ArrayList;
import java.util.Collections;

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
        if (bohr!=null)
            bohr.clear();
        strings = new ArrayList<>();
        results = new ArrayList<>();
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
        StringBuilder sb = new StringBuilder();
        sb.append(bohr.toString());
        sb.append("\ntextPosition = ").append(textPosition);
        sb.append("\ntext = ").append(text);
        sb.append("\n").append(resultsToString());
        return  sb.toString();

    }

    public String resultsToString(){
        if (results == null)
            return "null";
        StringBuilder sb = new StringBuilder();
        for (AlgorithmResult res : results) {
            sb.append("[").append(res.getIndex()).append(":").append(res.getPatternNumber()).append("] ");
        }
        return sb.toString();
    }

    public static ArrayList<AlgorithmResult> resultsFromString(String str){
        ArrayList<AlgorithmResult> res = new ArrayList<>();
        String[] arr = str.split("[:\\[\\]]");
        for (int i = 0; i < arr.length-2; i+=3){
            AlgorithmResult newRes = new AlgorithmResult(
                    Integer.valueOf(arr[i+1]),
                    Integer.valueOf(arr[i+2])
            );
            res.add(newRes);
        }
        return res;
    }

    public static Algorithm fromString(String str){
        Algorithm alg = new Algorithm(Bohr.fromString(str));
        String[] arr = str.split("\n");
        try {
            alg.textPosition = Integer.valueOf(arr[arr.length - 3].substring(15));
        }
        catch (Exception e){
            alg.textPosition = -1;
        }
        try {
            alg.text = arr[arr.length - 2].substring(7);
        }
        catch (StringIndexOutOfBoundsException e){
            alg.text = null;
        }
        try {
            alg.results = resultsFromString(arr[arr.length - 1]);
        }
        catch (Exception e){
            alg.results = null;
        }
        try {
            alg.strings = new ArrayList<>();
            Collections.addAll(alg.strings, alg.bohr.getStringArray());
        }
        catch (Exception e){
            alg.strings = null;
        }
        return alg;
    }

    public ArrayList<String> getStrings() {
        return strings;
    }

    public class Algorithm_Status{
        public Bohr.status getBohrStatus() {
            return bohr.getStatus();
        }
        public boolean isTextPositionOK() {
            return textPositionOK;
        }
        public boolean isTextOK() {
            return textOK;
        }
        public boolean isResultsOK() {
            return resultsOK;
        }
        public boolean isOK(){
            return ((bohr.getStatus() == Bohr.status.OK) && textPositionOK && textOK && resultsOK && stringsOK);
        }

        @Override
        public String toString() {
            if (isOK())
                return "OK";
            else {
                StringBuilder sb = new StringBuilder();
                if (!textPositionOK)
                    sb.append("TextPositionNotOK ");
                if (!textOK)
                    sb.append("TextNotOK ");
                if (!resultsOK)
                    sb.append("ResultsNotOK ");
                if (!stringsOK)
                    sb.append("StringsNotOK ");
                if (bohr.getStatus() != Bohr.status.OK)
                    sb.append("Bohr_").append(bohr.getStatus());
                return sb.toString();
            }
        }

        private boolean textPositionOK = true;
        private boolean textOK = true;
        private boolean resultsOK = true;
        private boolean stringsOK = true;
    }

    public Algorithm_Status getStatus() {
        Algorithm_Status status = new Algorithm_Status();
        if (textPosition < 0)
            status.textPositionOK = false;
        if (text == null)
            status.textOK = false;
        if (results == null)
            status.resultsOK = false;
        if (strings == null)
            status.stringsOK = false;
        return status;
    }

    private Bohr bohr;
    private ArrayList<String> strings;
    private ArrayList<AlgorithmResult> results;
    private String text;
    private int textPosition;
}

