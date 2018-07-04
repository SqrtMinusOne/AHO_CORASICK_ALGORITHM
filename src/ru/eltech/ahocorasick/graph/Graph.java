package ru.eltech.ahocorasick.graph;

import java.util.ArrayList;
import java.util.Random;

class Graph {
    ArrayList<Vertex> vertices = new ArrayList<>();
    ArrayList<Edge> edges = new ArrayList<>();

    Random rand = new Random();

    // public Graph () {}

    public Vertex getVertexByID ( int id ) {

        for ( Vertex vertex : vertices ) {

            if ( vertex.id == id) { return vertex; }
        }
        return null;
    }


    public Vertex createVertex ( int id ) {

        Vertex newVertex = new Vertex( id, rand.nextFloat() * 200, rand.nextFloat() * 200 );
        vertices.add( newVertex );

        return newVertex;

    }

    public Edge createEdge ( int idOne, int idTwo, String name ) {

        Vertex one = getVertexByID( idOne );
        Vertex two = getVertexByID( idTwo );
        return createEdge( one, two, name );
    }

    public Edge createEdge ( Vertex one, Vertex two, String name ) {
        Edge edge = new Edge ( one, two, name );
        one.edges.add( edge );
        two.edges.add( edge );
        edges.add( edge );

        return edge;
    }
}
