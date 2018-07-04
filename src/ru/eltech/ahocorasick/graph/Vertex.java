package ru.eltech.ahocorasick.graph;

import java.util.concurrent.CopyOnWriteArrayList;

public class Vertex {

    public int id;

    public CopyOnWriteArrayList<Edge> getEdges() {
        return edges;
    }

    private CopyOnWriteArrayList<Edge> edges = new CopyOnWriteArrayList<>();

    private float x;
    private float y;
    public float vx = 0;
    public float vy = 0;

    public Vertex ( int id, float x, float y ) {
        this.id = id;

        this.x = x;
        this.y = y;
    }

    public synchronized void setX ( float x ) { this.x = x; }
    public synchronized float getX () { return x; }

    public synchronized void setY ( float y ) { this.y = y; }
    public synchronized float getY () { return y; }
}
