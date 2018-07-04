package ru.eltech.ahocorasick.graph;

public class Edge {
    public Vertex one;
    public Vertex two;
    public String name;

    public static float normalLength = 100;

    public Edge ( Vertex one, Vertex two, String name ) {

        this.one = one;
        this.two = two;
        this.name = name;

    }
    public Vertex getAnotherVertex ( Vertex one ) {
        if (this.one == one)
            return two;
        else
            return one;
    }

    public float [] getForce ( Vertex vertex ) {
        float cx = ( one.getX() + two.getX() ) / 2;
        float cy = ( one.getY() + two.getY() ) / 2;

        float dx = vertex.getX() - cx;
        float dy = vertex.getY() - cy;

        float length = ( float ) Math.sqrt( dx * dx + dy * dy );
        if ( length == 0 ) { return new float [] { 0, 0 }; }

        float scalingFactor = normalLength / length;

        float nx = dx * scalingFactor;
        float ny = dy * scalingFactor;

        float distance = ( float ) Math.sqrt( ( dx - nx ) * ( dx - nx ) + ( dy - ny ) * ( dy - ny ) );

        float f = 1f * distance;

        float a = ( float ) Math.atan( dy / dx );

        float fx = Math.abs( f * ( float ) Math.cos( a ) );
        float fy = Math.abs( f * ( float ) Math.sin( a ) );

        if ( dx > nx ) { fx = -fx; }
        if ( dy > ny ) { fy = -fy; }


        return new float [] { fx, fy };
    }
}

