package ru.eltech.ahocorasick.alg;

import java.util.HashSet;

/**
 * TODO Bohr JavaDoc
 */
public class Bohr {
    private HashSet<Node> nodes; //Set of all nodes
    private Node root; //Root node
    private Node state; //Current state
    private int leafNumber; //Terminal states counter

    /**
     *  Constructor of Bohr
     */
    public Bohr(){
        root = new Node();
        nodes = new HashSet<Node>();
        nodes.add(root);
        state = null;
    }

    public HashSet<Node> getNodes() {
        return nodes;
    }

    public boolean containsNode(Node node){
        return nodes.contains(node);
    }

    public Node getRoot() {
        return root;
    }

    public Node getState() {
        return state;
    }

    //----------------------------------------
    public Node addNode( Node where, char ch){
        if (!where.isSon(ch)){
            Node node = new Node();
            where.addSon(node, ch);
            nodes.add(node);
            return node;
        }
        return null;
    }

    public void addString(String str) {
        Node cur = root;
        for (char ch : str.toCharArray()){
            Node son = cur.getSon(ch);
            if (son == null){
                son = addNode(cur, ch);
            }
            cur = son;
        }
        cur.addLeaf(leafNumber++);
    }

    //----------------------------------------
    public Node getNextState(char ch){
        return null;
    }

    public static Node getUp(Node node){
        return null;
    }

    private static Node getSuffLink(Node node){
        return null;
    }

    private static Node getLink(Node node, char ch){
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bohr: {");
        for (Node node : nodes){
            if (node == root){
                sb.append("[Root]");
            }
            if (node == state){
                sb.append("[Current]");
            }
            sb.append(node.toString());
        }
        sb.append("}");
        return sb.toString();
    }
}
