package ru.eltech.ahocorasick.alg;

import java.util.HashSet;

/**
 * This class manages the automate of Aho-Corasick / the suffix bohr. <br>
 * Initially the Bohr is created with only Node - root. <br>
 * Transitions between states of the automate are programmed using the lazy recursion method. <br>
 * Public methods:
 * <ul>
 *     <li>{@link #Bohr()}</li>
 *     <li>{@link #getNodes()}  }</li>
 *     <li>{@link #containsNode(Node)}  }</li>
 *     <li>{@link #getRoot()}  }</li>
 *     <li>{@link #getState()}  }</li>
 *     <li>{@link #addNode(Node, char)}  }</li>
 *     <li>{@link #addString(String)}  }</li>
 *     <li>{@link #getNextState(char)}  }</li>
 *     <li>{@link #getUp(Node)}  }</li>
 *     <li>{@link #getSuffLink(Node)} }</li>
 *     <li>{@link #getLink(Node, char)} }</li>
 * </ul>
 *
 * Private fields: //TODO: Remove private fields JavaDoc after development is finished
 * <ul>
 *     <li><b>HashSet nodes</b> - set of all nodes</li>
 *     <li><b>Node root</b> - root node</li>
 *     <li><b>Node state</b> - current state node</li>
 *     <li><b>int leafNumber</b> - Terminal states counter</li>
 * </ul>
 * @see Node
 */
public class Bohr {
    private HashSet<Node> nodes; //Set of all nodes
    private Node root; //Root node
    private Node state; //Current state
    private int leafNumber; //Terminal states counter

    /**
     *  Default constructor of Bohr
     */
    public Bohr(){
        root = new Node();
        nodes = new HashSet<>();
        nodes.add(root);
        state = root;
    }

    /**
     * Returns HashSet with all nodes in this Bohr
     * @return HashSet
     */
    public HashSet<Node> getNodes() {
        return nodes;
    }

    /**
     * Checks if given Node is stored in this Borh
     * @param node - given Node
     *
     */
    public boolean containsNode(Node node){
        return nodes.contains(node);
    }

    /**
     * Returns root
     * @return Node
     */
    public Node getRoot() {
        return root;
    }

    /**
     * Returns current state Node of the automate
     * @return Node
     */
    public Node getState() {
        return state;
    }

    //----------------------------------------

    /**
     * Adds Node to the Bohr and configures it
     * @param where - parent Node
     * @param ch - char for transition
     * @return Node
     */
    public Node addNode( Node where, char ch){
        if (!where.isSon(ch)){
            Node node = new Node();
            where.addSon(node, ch);
            nodes.add(node);
            return node;
        }
        return null;
    }

    /**
     * Adds whole String to this Bohr
     * @param str - required String
     */
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

    /**
     * Changes state of the automate by given symbol
     * @param ch - symbol for transition
     * @return Node
     */
    public Node getNextState(char ch){
        state = getLink(state, ch);
        return state;
    }

    /**
     * Returns compressed suffix link from given Node
     * @param node - required Node
     * @return Node
     */
    public Node getUp(Node node){
        Node link = node.getUp();
        if (link == null){
            if (node.isLeaf()) {
                link = getSuffLink(node);
            }
            else if (getSuffLink(node) == root){
                link = root;
            }
            else{
                link = getUp(getSuffLink(node));
            }
            node.setUp(link);
        }
      return link;
    }

    /**
     * Returns suffix link from given Node
     * @param node - required Node
     * @return Node
     */
    public Node getSuffLink(Node node){
        Node link = node.getSuffLink();
        if (link == null){
            if ((node == root ) || (node.getParent() == root)) {
                link = root;
            }
            else{
                link = getLink(getSuffLink(node.getParent()), node.getCharToParent());
            }
            node.setSuffLink(link);
        }
        return link;
    }

    /**
     * Gets next Node for given Node by given symbol
     * @param node - required Node
     * @param ch - required Symbol
     * @return Node
     */
    public Node getLink(Node node, char ch){
        Node next = node.getTransitionBy(ch);
        if (next == null){
            if (node.isSon(ch)) {
                next = node.getSon(ch);
            }
            else if (node == root){
                next = root;
            }
            else{
                next = getLink(getSuffLink(node), ch);
            }
            node.addTransition(next, ch);
        }
        return next;
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
