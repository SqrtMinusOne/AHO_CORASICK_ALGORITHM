package ru.eltech.ahocorasick.alg;

import java.util.ArrayList;
import java.util.Map;

/**
 * This class manages the automate of Aho-Corasick / the suffix bohr. <br>
 * Initially the Bohr is created with only Node - root. <br>
 * Transitions between states of the automate are programmed using the lazy recursion method. <br>
 * @see Node
 */
public class Bohr {
    /**
     *  Default constructor of Bohr
     */
    public Bohr(){
        nodes = new ArrayList<>();
        root = new Node();
        nodes.add(root);
        state = root;
        nodesNumber = 1;
    }

    //----------------------------------------
    /**
     * Returns HashSet with all nodes in this Bohr
     * @return HashSet
     */
    ArrayList<Node> getNodes() {
        return nodes;
    }

    /**
     * Checks if given Node is stored in this Bohr
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
    public Node addNode(Node where, char ch){
        if (!where.isSon(ch)){
            Node node = new Node(nodesNumber++);
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
    public void addString(String str){
        Node cur = root;
        if (str == null)
            return;
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
     * Returns suffix link from given Node
     * @param node - required Node
     * @return Node
     */
    private Node getSuffLink(Node node){
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

    /**
     * Returns compressed suffix link from given Node
     * @param node - required Node
     * @return Node
     */
    public Node getUp(Node node){
        Node link = node.getUp();
        if (link == null){
            if (!(link = getSuffLink(node)).isLeaf()) {
                if (getSuffLink(node) == root){
                    link = root;
                }
                else{
                    link = getUp(getSuffLink(node));
                }
            }
            node.setUp(link);
        }
        return link;
    }

    //----------------------------------------
    /**
     * Clears all calculated transitions for all Nodes
     */
    void clearTransitions(){
        for (Node node : nodes){
            node.clearTransitions();
        }
        state = root;
    }

    /**
     * Clears this Bohr
     */
    void clear(){
        nodes.clear();
        root = new Node();
        state = root;
        nodes.add(root);
        leafNumber = 0;
        nodesNumber = 1;
    }

    /**
     * Gets all Strings from Bohr. This can be used, if something has happened to user's
     * String array
     * @return String array
     */
    String[] getStringArray(){
        if (corrupt_node)
            return null;
        String[] arr = new String[leafNumber];
        for (Node node : nodes){
            if (node.isLeaf()){
                String str = getStringFromRoot(node);
                for (int n : node.getLeafPatternNumber() ) {
                    arr[n] = str;
                }
            }
        }
        return arr;
    }

    /**
     * Gets shortest path from root to given Node
     * @param node required Node
     * @return String
     */
    private String getStringFromRoot(Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append(node.getCharToParent());
        for (Node node2 = node.getParent(); node2 != root; node2 = node2.getParent()){
            sb.append(node2.getCharToParent());
        }
        sb.reverse();
        return sb.toString();
    }

    /**
     * Converts Bohr from string, received from Bohr.toString()<br>
     * String is not checked for correctness
     * @param str string, received from Bohr.toString()
     * @return Bohr
     */
    public static Bohr fromString(String str){
        Bohr bohr = new Bohr();
        bohr.nodes.clear();
        String[] arr = str.split("\n");
        int ind = 1;
        boolean stateFlag;
        boolean rootFlag;
        while (!arr[ind].startsWith("}")){
            stateFlag = false;
            rootFlag = false;
            if (arr[ind].startsWith("[Root]")) {
                arr[ind] = arr[ind].substring(6);
                rootFlag = true;
            }
            if (arr[ind].startsWith("[Current]")){
               stateFlag = true;
               arr[ind] = arr[ind].substring(9);
            }
            Node node;
            try {
                node = Node.fromString(arr[ind]);
            }
            catch (Exception e){
                bohr.corrupt_node = true;
                ind++;
                continue;
            }
            if (rootFlag)
                bohr.root = node;
            if (stateFlag)
                bohr.state = node;
            for (int n : node.getLeafPatternNumber())
                if (n > bohr.leafNumber)
                    bohr.leafNumber = n;
            bohr.nodes.add(node.getNodeNumber(), node);
            ind++;
        }
        bohr.leafNumber++;
        bohr.solveDependencies();
        return bohr;
    }

    /**
     * Solves dependencies in Bohr by replacing temporary Nodes with real ones<br>
     * This method ignores exceptions, however if input was incorrect, using of Bohr can cause errors
     */
    private void solveDependencies(){
        for (Node node : nodes){
            if ((node.getSuffLink()!=null) && (node.getSuffLink().isTemp())){
                node.setSuffLink(nodes.get(node.getSuffLink().getNodeNumber()));
            }
            if ((node.getUp()!=null) && (node.getUp().isTemp())){
                node.setUp(nodes.get(node.getUp().getNodeNumber()));
            }
            for (Map.Entry<Character, Node> go : node.getGo().entrySet()){
                if (go.getValue().isTemp()){
                    node.getGo().replace(go.getKey(), go.getValue(), nodes.get(go.getValue().getNodeNumber()));
                }
            }
            for (Map.Entry<Character, Node> son : node.getSon().entrySet()){
                if (son.getValue().isTemp()){
                    try {
                        Node foundSon = nodes.get(son.getValue().getNodeNumber());
                        node.getSon().replace(son.getKey(), son.getValue(), foundSon);
                        foundSon.initParent(node);
                    }
                    catch (Exception ignored){
                    }

                }
            }
        }
    }

    public enum status {UNINITIALIZED, UNRESOLVED_DEPENDENCIES, CORRUPT_NODE, OK}

    /**
     * Returns current status of Bohr.<br>
     * <ul>
     *     <li>UNINITIALIZED - Bohr was not initialized correctly or corrupted</li>
     *     <li>UNRESOLVED_DEPENDENCIES - some dependencies were not resolved</li>
     *     <li>CORRUPT_NODE - some node has not been read correctly</li>
     *     <li>OK - everything is fine</li>
     * </ul>
     * @return enum status {UNINITIALIZED, UNRESOLVED_DEPENDENCIES, OK}
     */
    public status getStatus(){
        if ((root == null) || (nodes == null) || (nodes.isEmpty()))
            return status.UNINITIALIZED;
        if (corrupt_node){
            return status.CORRUPT_NODE;
        }
        for (Node node: nodes){
            if ((node.getParent() == null) && (node != root))
                return status.UNRESOLVED_DEPENDENCIES;
            if ((node.getSuffLink()!=null) && (node.getSuffLink().isTemp())){
                return status.UNRESOLVED_DEPENDENCIES;
            }
            if ((node.getUp()!=null) && (node.getUp().isTemp())){
                return status.UNRESOLVED_DEPENDENCIES;
            }
            for (Map.Entry<Character, Node> go : node.getGo().entrySet()){
                if (go.getValue().isTemp()){
                    return status.UNRESOLVED_DEPENDENCIES;
                }
            }
            for (Map.Entry<Character, Node> son : node.getSon().entrySet()){
                if (son.getValue().isTemp()){
                    return status.UNRESOLVED_DEPENDENCIES;
                }
            }
        }
        return status.OK;
    }

    //----------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bohr: {Nodes: ").append(nodes.size()).append(" \n");
        for (Node node : nodes){
            if (node == root){
                sb.append("[Root]");
            }
            if (node == state){
                sb.append("[Current]");
            }
            sb.append(node.toString()).append('\n');
        }
        sb.append("}");
        return sb.toString();
    }

    private boolean corrupt_node;
    private static int nodesNumber;
    final ArrayList<Node> nodes; //Set of all nodes TODO: Custom HashSet
    Node root; //Root node
    Node state; //Current state
    private int leafNumber; //Terminal states counter
}
