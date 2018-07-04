package ru.eltech.ahocorasick.alg;

import ru.eltech.ahocorasick.graph.Graph;

public class BohrWithGraph extends Bohr {
    public BohrWithGraph() {
        super();
        graph = new Graph();
        graph.createVertex(root.getNodeNumber());
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

    private final Graph graph;
}
