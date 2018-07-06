package ru.eltech.ahocorasick.alg;

import ru.eltech.ahocorasick.graph.Graph;
import ru.eltech.ahocorasick.graph.Vertex;

public class BohrWithGraph extends Bohr {
    public BohrWithGraph() {
        super();
        graph = new Graph();
        graph.createVertex(root.getNodeNumber());
        updateStates();
    }

    @Override
    public Node addNode(Node where, char ch) {
        Node node = super.addNode(where, ch);
        synchronized (graph){
            graph.createVertex(node.getNodeNumber());
            graph.createEdge(where.getNodeNumber(), node.getNodeNumber(), Character.toString(ch));
        }
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

    private void updateEdges(){ //TODO updateEdges

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
