package ru.eltech.ahocorasick.alg;

import java.util.ArrayList; //TODO: Custom ArrayList
import java.util.HashMap; //TODO: Custom HashMap
import java.util.Objects;

/**
 * Node is used for storing singular units of Aho-Corasick automate. <br>
 * Public methods:
 * <ul>
 *     <li>{@link #Node()} - default constructor</li>
 *     <li>{@link #addSon(Node, char)} </li>
 *     <li>{@link #getSon(char)}</li>
 *     <li>{@link #getSon()} </li>
 *     <li>{@link #isSon(char)}</li>
 *     <li>{@link #addTransition(Node, char)}</li>
 *     <li>{@link #getGo()}</li>
 *     <li>{@link #isTransitionBy(char)}</li>
 *     <li>{@link #getTransitionBy(char)}</li>
 *     <li>{@link #getParent()}</li>
 *     <li>{@link #getSuffLink()}</li>
 *     <li>{@link #setSuffLink(Node)}</li>
 *     <li>{@link #getUp()}</li>
 *     <li>{@link #setUp(Node)}</li>
 *     <li>{@link #getCharToParent()}</li>
 *     <li>{@link #isLeaf()}</li>
 *     <li>{@link #getLeafPatternNumber()}</li>
 *     <li>{@link #addLeaf(int)}</li>
 * </ul>
 *
 * Private fields: //TODO: Remove private fields JavaDOc after development is finished
 * <ul>
 *     <li><b>HashMap son</b> - HashMap of children</li>
 *     <li><b>HashMap go</b> - HashMap of already calculated transitions</li>
 *     <li><b>Node parent</b> - link to the parent Node</li>
 *     <li><b>Node suffLink</b> - saved suffix link from this Node</li>
 *     <li><b>Node up</b>- saved compressed suffix link from this Node</li>
 *     <li><b>char charToParent</b> - char for the transition from the parent to this Node</li>
 *     <li><b>boolean isLeaf</b> - determines, if there is a string's end in this Node</li>
 *     <li><b>ArrayList<Integer></b> - ArrayList with numbers of strings, which end here</li>
 * </ul>
 * @version 0.2
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
     * @param node determines which Node son will be added as son
     * @param charToParent determines char, by which son will be added
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
     * @param node determines Node, to which transition is going to be added
     * @param charToTransitive determines char, by which transition to given Node is going to be added
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
    /*
     * @return parent of this node
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
     * @param leafNumber is number of string which ends here
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
