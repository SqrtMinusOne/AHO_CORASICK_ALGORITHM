package ru.eltech.ahocorasick.alg;

import java.util.LinkedList;

/**
 * This class is used to keep track on algorithm states.
 * This class needs to be used with caution, because in greatly increases the
 * amount of time required for one operation.
 */
public class AlgorithmHistory {

    /**
     * Default constructor
     * @param max number of stored states
     */
    public AlgorithmHistory(int max) {
        states = new LinkedList<>();
        this.max = max;
        on = true;
    }

    /**
     * This constructor can be used to create useless AlgorithmHistory
     * @param max number of stored states
     * @param on if false, history is not written
     */
    public AlgorithmHistory(int max, boolean on){
        this(max);
        this.on = on;
    }

    /**
     * Saves the current state of Algorithm
     * @param alg Algorithm to be saved
     */
    public void save(Algorithm alg){
        if (!on)
            return;
        while (index < states.size())
            states.removeLast();
        if (states.size() > max) {
            states.removeFirst();
            index--;
        }
        states.addLast(alg.toString());
        index++;
    }

    /**
     * Returns the last saved state
     * @return Algorithm or null
     */
    public Algorithm undo(Algorithm algorithm){
        if (index > 0) {
            int t = index - 1;
            Algorithm res = Algorithm.fromString(states.get(t));
            if (index == states.size()) {
                this.save(algorithm);
                index--;
            }
            index--;
            return res;
        }
        else
            return null;
    }

    /**
     * Returns to some number of previously saved states
     * @param a number of saved states to be reverted
     * @return Algorithm or null
     */
    public Algorithm undo(int a, Algorithm algorithm){
        Algorithm res = null;
        if ((a > index) && (a >= 0))
            return null;
        else
            for (int i = 0; i < a; i++)
                res = undo(algorithm);
        return res;
    }

    /**
     * Return the undode state, if the latter was not overwritten
     * @return Algorithm or null
     */
    public Algorithm redo(){
        if (index < states.size() - 1)
            return Algorithm.fromString(states.get(++index));
        else
            return null;
    }

    /**
     * Does redo several times
     * @param a number of states to be forwarded
     * @return Algorithm or null
     */
    public Algorithm redo(int a){
        Algorithm res = null;
        if ((a > index - states.size()) && (a >= 0))
            return null;
        else
            for (int i = 0; i < a; i++)
                res = redo();
        return res;
    }

    /**
     * Returns the last saved state
     * @return Algorithm or null
     */
    public Algorithm last(){
        if (states.size() > 0)
            return  Algorithm.fromString(states.getLast());
        else
            return null;
    }

    /**
     * Clears history
     */
    public void clear(){
        states.clear();
        index = 0;
    }

    /**
     * Returns number of saved states
     * @return int
     */
    public int size(){
        return states.size();
    }

    private boolean on;
    private int index;
    private final int max;
    private LinkedList<String> states;
}
