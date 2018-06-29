package ru.eltech.ahocorasick.alg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * TODO: Node JavaDoc
 */
public class Node {
    public Node(){
        son = new HashMap<>();
        go = new HashMap<>();
        leafPatternNumber = new ArrayList<>();
    }

    private HashMap<Character, Node> son; //HashMap of children

    /**
     * Adds son to this node and sets up input node
     * @param node
     * @param charToParent
     */
    public void addSon(Node node, char charToParent){
        node.charToParent = charToParent;
        node.parent = this;
        son.put(charToParent, node);
    }

    /**
     * Returns HashMap with children of this node
     */
    public HashMap<Character, Node> getSon() {
        return son;
    }

    public boolean isSon(char ch){
        return son.containsKey(ch);
    }

    public Node getSon(char ch){ return son.get(ch); }

    //----------------------------------------
    private HashMap<Character, Node> go; //HashMap of transitions

    /**
     * Adds transition by some symbol from this node
     * This is used for lazy recursion
     * @param node
     * @param charToTransitive
     */
    public void addTransition(Node node, char charToTransitive){
        go.put(charToTransitive, node);
    }

    /**
     * Shows HashMap with transitions from this node
     */
    public HashMap<Character, Node> getGo() {
        return go;
    }

    public boolean isTransitionBy(char charToTransitive){
        return go.containsKey(charToTransitive);
    }

    public Node getTransitionBy(char charToTransitive){
        return go.get(charToTransitive);
    }

    //----------------------------------------
    private Node parent; //Link to parent
    /**
     * Returns parent of this node
     * @return
     */
    public Node getParent() {
        return parent;
    }

    //----------------------------------------
    private Node suffLink; //Lazy recursion suffix link

    /**
     * Returns calculated suffix link from this node, if it exists
     */
    public Node getSuffLink() {
        return suffLink;
    }

    /**
     * Sets calculated suffix link from this node
     */
    public void setSuffLink(Node suffLink) {
        this.suffLink = suffLink;
    }

    //----------------------------------------
    private Node up; //Lazy recursion compressed suffix link

    /**
     * Returns calculated compressed suffix link from this node, if it exists
     */
    public Node getUp() {
        return up;
    }

    /**
     * Sets compressed calculated suffix link from this node
     */
    public void setUp(Node up) {
        this.up = up;
    }

    //----------------------------------------
    private char charToParent; //Char to parent

    /**
     * Returns char to parent node
     */
    public char getCharToParent() {
        return charToParent;
    }

    //----------------------------------------
    private boolean isLeaf; //is it leaf
    private ArrayList<Integer> leafPatternNumber;

    /**
     * Determines is this state terminal
     */
    public boolean isLeaf() {
        return isLeaf;
    }

    /**
     * Returns array of strings which ends in this state
     */
    public ArrayList<Integer> getLeafPatternNumber() {
        return leafPatternNumber;
    }

    /**
     * Adds number of string which ends in this state
     * @param leafNumber
     */
    public void addLeaf(int leafNumber){
        leafPatternNumber.add(leafNumber);
        isLeaf = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return charToParent == node.charToParent &&
                Objects.equals(parent, node.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, charToParent);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Node {");
        if (parent == null){
            sb.append("null");
        }else {
            sb.append(parent.charToParent).append("->").append(charToParent);
        }
        if (isLeaf()){
            sb.append(": ");
            for (int i : leafPatternNumber){
                sb.append(i).append(" ");
            }
        }
        sb.append("} ");
        return sb.toString();
    }
}
