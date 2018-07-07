package ru.eltech.ahocorasick.alg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Node is used for storing singular units of Aho-Corasick automate. <br>
 * Node constructor can receive a number of Node and boolean, which determines, if this Node is temporary.
 * Temporary Nodes are used for resolving dependencies while reading Nodes from files. <br>
 * This class is designed to take advantage of lazy link calculation in Aho-Corasick algorithm
 * @version 0.4
 */
public class Node {
    /**
     * Default constructor for Node #0 <br>
     * Number of node does not make any sense, unless the Node is in a Bohr or it is the root Node
     */
    public Node(){
        this(0);
    }

    /**
     * Constructor for Node
     * @param number Number of constructed Node
     */
    public Node(int number){
        this.nodeNumber = number;
        this.leafPatternNumber = new ArrayList<>();
        son = new HashMap<>();
        go = new HashMap<>();
    }

    /**
     * Constructor for temporary Node
     * @param number Number of constructed temporary Node
     * @param temp State of temporariness
     */
    public Node (int number, boolean temp){
        this(number);
        this.temp = temp;
    }

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

    /**
     * Determines if there is son by given symbol
     * @param ch required symbol
     * @return boolean
     */
    public boolean isSon(char ch){
        return son.containsKey(ch);
    }

    //----------------------------------------
    /*
     * @return parent of this node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Returns char to parent node
     */
    public char getCharToParent() {
        return charToParent;
    }

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

    /**
     * Returns son by given symbol, if it exists
     * @param ch required symbol
     * @return Node or null
     */
    public Node getSon(char ch){ return son.get(ch); }

    //----------------------------------------

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
    HashMap<Character, Node> getGo() {
        return go;
    }


    /**
     * Determines, if there is calculated transition by given symbol
     * @param charToTransitive required symbol
     * @return boolean
     */
    public boolean isTransitionBy(char charToTransitive){
        return go.containsKey(charToTransitive);
    }

    /**
     * Returns transition by given symbol
     * @param charToTransitive required symbol
     * @return Node or null
     */
    public Node getTransitionBy(char charToTransitive){
        return go.get(charToTransitive);
    }

    //----------------------------------------

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
    /**
     * Clears all calculated transitions for this Node
     */
    void clearTransitions(){
        go.clear();
        suffLink = null;
        up = null;
    }

    //----------------------------------------
    /**
     * Converts string to Node. Used for saving Node to file<br>
     * This method creates temporary Nodes, which should be resolved later.
     * However, this node itself does not need any additional operations. <br>
     * The String is not checked for correctness.
     * @param str Correct string, produced by Node.toString()
     * @return Node
     */
    public static Node fromString(String str){
        str = str.substring(7);
        String[] arr = str.split(" ");
        int ind = 0;
        Node node = new Node(Integer.valueOf(arr[ind++]));
        node.temp = false;
        if (!arr[ind].startsWith("null")) {
            node.charToParent = arr[ind].charAt(0);
        }
        ind += 2;
        while (arr[ind].startsWith("(")){
            char sonChar = arr[ind].charAt(1);
            int sonInt = Integer.valueOf(arr[++ind].substring(0, arr[ind].length()-1));
            node.addSon(new Node(sonInt, true), sonChar);
            ind++;
        }
        ind++;
        if (arr[ind].startsWith("SL:")){
            node.setSuffLink(new Node(Integer.valueOf(arr[++ind]), true));
            ind+=2;
        }

        if (arr[ind].startsWith("CSL:")){
            node.setUp(new Node(Integer.valueOf(arr[++ind]), true));
            ind+=2;
        }
        if (arr[ind].startsWith("END:")){
            while (!arr[++ind].startsWith("}")){
                node.addLeaf(Integer.valueOf(arr[ind]));
            }
        }
        return node;
    }

    //----------------------------------------
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
        sb.append("Node {#").append(nodeNumber).append(" ");
        if (charToParent == 0){
            sb.append("null");
        }else {
            sb.append(charToParent);
        }
        sb.append(" ->[this]-> ");
        if (!son.isEmpty()){
            for (Map.Entry<Character, Node> ent : son.entrySet()){
                sb.append("(").append(ent.getKey().charValue()).append(": ")
                        .append(ent.getValue().nodeNumber).append(") ");
            }
        }

        sb.append("| ");
        if (suffLink!=null){
            sb.append("SL: ").append(suffLink.nodeNumber).append(" | ");
        }
        if (up!=null){
            sb.append("CSL: ").append(up.nodeNumber).append(" | ");
        }
        if (isLeaf()){
            sb.append("END: ");
            for (int i : getLeafPatternNumber()){
                sb.append(i).append(" ");
            }
        }
        if (isTemp()){
            sb.append("TEMP ");
        }
        sb.append("} ");
        return sb.toString();
    }

    /**
     * Determines, if this Node is temporary
     * @return true if temporary
     */
    public boolean isTemp() {
        return temp;
    }

    /**
     * Gets number of the Node
     * @return number
     */
    int getNodeNumber() {
        return nodeNumber;
    }

    /**
     * Used to set parent, if the parent is null
     * @param parent Node
     */
    void initParent(Node parent) {
        if (this.parent == null)
            this.parent = parent;
    }

    private boolean temp;
    private final int nodeNumber;
    private Node parent; //Link to parent
    private char charToParent; //Char to parent
    private final ArrayList<Integer> leafPatternNumber;
    private boolean isLeaf; //is it leaf
    private final HashMap<Character, Node> son; //HashMap of children
    private final HashMap<Character, Node> go; //HashMap of transitions
    private Node suffLink; //Lazy recursion suffix link
    private Node up; //Lazy recursion compressed suffix link
}
