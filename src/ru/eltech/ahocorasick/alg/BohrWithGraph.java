package ru.eltech.ahocorasick.alg;

import ru.eltech.ahocorasick.graph.Edge;
import ru.eltech.ahocorasick.graph.Graph;
import ru.eltech.ahocorasick.graph.Vertex;

import java.util.Map;

/**
 * This class integrates Bohr with Graph logic
 */
public class BohrWithGraph extends Bohr {
    public BohrWithGraph() {
        super();
        graph = new Graph();
        graph.createVertex(root.getNodeNumber());
        updateStates();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BohrWithGraph: {Nodes: ").append(nodes.size()).append(" \n");
        for (Node node : nodes){
            sb.append(graph.getVertexByID(node.getNodeNumber()).toString()).append(" &");
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

    public static BohrWithGraph fromString(String str){
        BohrWithGraph bohr = new BohrWithGraph();
        bohr.nodes.clear();
        bohr.getGraph().getVertices().clear();
        String[] arr = str.split("\n");
        int ind = 1;
        while ((!arr[ind].startsWith("}")) && (ind < arr.length))  {
            if (!arr[ind].startsWith("[")){
                ind++;
                continue;
            }
            String[] arr2 = arr[ind].split(" ");
            int x = Integer.valueOf(arr2[0].replaceAll("\\D",""));
            int y = Integer.valueOf(arr2[1].replaceAll("\\D", ""));
            bohr.getGraph().addVertex(new Vertex(ind-1, x, y));
            arr[ind] = arr[ind].replaceAll(".*&", "");
            ind = processNodeInArr(bohr, arr, ind);
        }
        bohr.leafNumber++;
        bohr.solveDependencies();
        bohr.updateStates();
        bohr.updateEdges();
        return bohr;
    }

    @Override
    public Node addNode(Node where, char ch) {
        Node node = super.addNode(where, ch);
        graph.createVertex(node.getNodeNumber());
        graph.createEdge(where.getNodeNumber(), node.getNodeNumber(), Character.toString(ch));
        return node;
    }

    @Override
    public void addString(String str) {
        super.addString(str);
        updateStates();
    }

    @Override
    void clear() {
        super.clear();
        graph.clear();
        graph.createVertex(root.getNodeNumber());
    }

    @Override
    void clearTransitions() {
        super.clearTransitions();
        updateEdges();
    }

    public Graph getGraph() {
        return graph;
    }

    public void updateVertices(){
        graph.getVertices().clear();
        for (Node node : nodes){
            graph.createVertex(node.getNodeNumber());
        }
    }

    public void updateEdges(){
        graph.getEdges().clear();
        for (Vertex vertex : graph.getVertices()){
            vertex.getEdges().clear();
        }
        for (Node node : nodes) {
            for (Map.Entry<Character, Node> child : node.getSon().entrySet()) {
                if ((graph.getEdge(node.getNodeNumber(), child.getValue().getNodeNumber()))==null)
                    graph.createEdge(node.getNodeNumber(), child.getValue().getNodeNumber(),
                            Character.toString(child.getKey()));
            }
            if ((node.getSuffLink()!=null) && ((node.getSuffLink()) != root) &&
                    (graph.getEdge(node.getNodeNumber(), node.getSuffLink().getNodeNumber()) == null)
                    && (node.getUp()!=node.getSuffLink()))
            {
                graph.createEdge(node.getNodeNumber(), node.getSuffLink().getNodeNumber(),
                        "", Edge.states.ROUND1);
            }
            if ((node.getUp()!=null) && (node.getUp()!=root) &&
                    (graph.getEdge(node.getNodeNumber(), node.getUp().getNodeNumber()) == null))
            {
                graph.createEdge(node.getNodeNumber(), node.getUp().getNodeNumber(),
                        "", Edge.states.ROUND2);
            }
        }
    }

    public void updateStates(){
        for (Vertex vertex : graph.getVertices()){
            vertex.setState(Vertex.states.NORMAL);
        }
        for (Node node : nodes){
            if (node.isLeaf()){
                graph.getVertexByID(node.getNodeNumber()).
                        setTerminal(node.getLeafPatternNumber().get(0));
            }
        }
        graph.getVertexByID(root.getNodeNumber()).setState(Vertex.states.ROOT);
        if (state != root)
            graph.getVertexByID(state.getNodeNumber()).setState(Vertex.states.STATE);
    }

    private final Graph graph;
}
