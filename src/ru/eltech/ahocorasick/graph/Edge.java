package ru.eltech.ahocorasick.graph;

import ru.eltech.ahocorasick.Settings;

public class Edge {
    /**
     * Constructor for Edge
     * @param source First Vertex
     * @param dest Second Vertex
     * @param name Name of Edge
     */
    public Edge (Vertex source, Vertex dest, String name ) {
        this(source, dest, name, states.NORMAL);
    }

    public Edge(Vertex source, Vertex dest, String name, states state) {
        this.state = state;
        this.source = source;
        this.dest = dest;
        this.name = name;
    }

    /**
     * Returns opposite Vertex
     * @param one source Vertex
     * @return another Vertex
     */
    public Vertex getAnotherVertex ( Vertex one ) {
        if (this.source == one)
            return dest;
        else
            return one;
    }

    public float getSourceX(){
        return source.getX() - (Settings.vertexSize())/2 * (float) Math.sin(getAngle());
    }

    public float getSourceY(){
        return source.getY() - (Settings.vertexSize())/2 * (float) Math.cos(getAngle());
    }

    public float getDestX(){
        return dest.getX() + (Settings.vertexSize())/2 * (float) Math.cos(Math.PI/2 - getAngle());
    }

    public float getDestY(){
        return dest.getY() + (Settings.vertexSize())/2 * (float) Math.sin(Math.PI/2 - getAngle());
    }

    public float getAngle(){
        float dy = source.getY() - dest.getY();
        float dx = source.getX() - dest.getX();
        double res = getAngle(dx, dy);
        return (float) res;
    }

    public static double getAngle(float dx, float dy) {
        double res;
        if ((dy > 0) && (dx > 0))
            res =  Math.atan(dx/dy);
        else if ((dy > 0) && (dx < 0))
            res = Math.atan(dx/dy) + Math.PI*2;
        else if (dy < 0)
            res = Math.atan(dx/dy) + Math.PI;
        else if ((dy == 0) && (dx > 0))
            res = Math.PI/2;
        else
            res = 3*Math.PI/2;
        return res;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getDest() {
        return dest;
    }

    public String getName() {
        return name;
    }

    public enum states {NORMAL, ROUND1, ROUND2}

    public states getState() {
        return state;
    }

    private final states state;
    private final Vertex source;
    private final Vertex dest;
    private final String name;
}

