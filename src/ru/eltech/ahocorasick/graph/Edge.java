package ru.eltech.ahocorasick.graph;

public class Edge {
    /**
     * Constructor for Edge
     * @param source First Vertex
     * @param dest Second Vertex
     * @param name Name of Edge
     */
    public Edge (Vertex source, Vertex dest, String name ) {

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
        return source.getX() - (Vertex.size)/2 * (float) Math.sin(getAngle());
    }

    public float getSourceY(){
        return source.getY() - (Vertex.size)/2 * (float) Math.cos(getAngle());
    }

    public float getDestX(){
        return dest.getX() + (Vertex.size)/2 * (float) Math.cos(Math.PI/2 - getAngle());
    }

    public float getDestY(){
        return dest.getY() + (Vertex.size)/2 * (float) Math.sin(Math.PI/2 - getAngle());
    }

    public float getAngle(){
        float dy = source.getY() - dest.getY();
        float dx = source.getX() - dest.getX();
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
        return (float) res;
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

    private final Vertex source;
    private final Vertex dest;
    private final String name;
    public static final float textDistance = 15;
}

