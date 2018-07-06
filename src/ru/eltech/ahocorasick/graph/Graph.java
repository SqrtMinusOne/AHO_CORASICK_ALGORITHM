package ru.eltech.ahocorasick.graph;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Graph {

    public Graph () {
        vertices = new CopyOnWriteArrayList<>();
        edges = new CopyOnWriteArrayList<>();
        rand = new Random();
    }

    public Vertex getVertexByID ( int id ) {

        for ( Vertex vertex : getVertices()) {

            if ( vertex.getId() == id) { return vertex; }
        }
        return null;
    }

    public Vertex createVertex ( int id ) {
        Vertex newVertex = new Vertex( id, rand.nextFloat() * 700, rand.nextFloat() * 550 );
        getVertices().add( newVertex );
        return newVertex;
    }

    public Edge createEdge ( int idOne, int idTwo, String name ) {
        return createEdge( idOne, idTwo, name , Edge.states.NORMAL);
    }

    public Edge createEdge(int idOne, int idTwo, String name, Edge.states state) {
        Vertex one = getVertexByID( idOne );
        Vertex two = getVertexByID( idTwo );
        Edge edge = new Edge ( one, two, name, state );
        one.getEdges().add( edge );
        two.getEdges().add( edge );
        getEdges().add( edge );
        return edge;
    }

    public Edge getEdge(int idOne, int idTwo){
        Vertex one = getVertexByID( idOne );
        Vertex two = getVertexByID( idTwo );
        for (Edge edge : edges){
            if ((edge.getSource() == one) && (edge.getDest() == two))
                    return edge;
        }
        return null;
    }

    public synchronized CopyOnWriteArrayList<Edge> getEdges() {
        return edges;
    }

    public CopyOnWriteArrayList<Vertex> getVertices() {
        return vertices;
    }

    private final Random rand;
    private final CopyOnWriteArrayList<Vertex> vertices;
    private final CopyOnWriteArrayList<Edge> edges;

    public void clear() {
        vertices.clear();
        edges.clear();
    }


}
