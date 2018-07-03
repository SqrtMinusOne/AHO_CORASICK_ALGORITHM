package ru.eltech.ahocorasick.alg;

import java.util.LinkedList;

public class AlgorithmHistory {

    public AlgorithmHistory(int max) {
        states = new LinkedList<>();
        this.max = max;
    }

    public void save(Algorithm alg){
        while (index < states.size())
            states.removeLast();
        if (states.size() > max) {
            states.removeFirst();
            index--;
        }
        states.addLast(alg.toString());
        index++;
    }

    public Algorithm undo(){
        if (index > 0)
            return Algorithm.fromString(states.get(--index));
        else
            return null;
    }

    public Algorithm undo(int a){
        Algorithm res = null;
        if ((a > index) && (a >= 0))
            return null;
        else
            for (int i = 0; i < a; i++)
                res = undo();
        return res;
    }

    public Algorithm redo(){
        if (index < states.size())
            return Algorithm.fromString(states.get(index++));
        else
            return null;
    }

    public Algorithm redo(int a){
        Algorithm res = null;
        if ((a > index - states.size()) && (a >= 0))
            return null;
        else
            for (int i = 0; i < a; i++)
                res = redo();
        return res;
    }

    public Algorithm last(){
        if (states.size() > 0)
            return  Algorithm.fromString(states.getLast());
        else
            return null;
    }

    public void clear(){
        states.clear();
        index = 0;
    }

    public int size(){
        return states.size();
    }

    private int index;
    private final int max;
    private LinkedList<String> states;
}
