package mnwa;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class MNWA {
    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.createVertex( 0 );
        graph.createVertex( 1 );
        graph.createVertex( 2 );
        graph.createVertex( 3 );
        graph.createVertex( 4 );

        graph.createEdge( 0, 1, "" );
        graph.createEdge( 0, 2, "" );
        graph.createEdge( 0, 3, "" );
        graph.createEdge( 0, 4, "" );

        graph.createEdge( 1, 2, "" );
        graph.createEdge( 1, 3, "" );
        graph.createEdge( 1, 4, "" );

        graph.createEdge( 2, 3, "" );
        graph.createEdge( 2, 4, "" );

        graph.createEdge( 4, 3, "" );

        JFrame frame = new JFrame("Swing Paint Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize( 500, 500 );

        MyPanel myPanel = new MyPanel( graph );
        frame.add( myPanel );

        frame.setVisible(true);

        myPanel.simulate();
    }
}


class Vertex {

    public int id;
    public ArrayList<Edge> edges = new ArrayList<>();

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


class Edge {
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


class MyPanel extends JPanel {
    Graph graph;

    public MyPanel( Graph graph ) {

        this.graph = graph;
    }

    public void simulate () {// !Самая сложная и не работающая! Паша, не трогай

        long t1 = System.currentTimeMillis();
        long t0;
        float dt;

        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;

        while ( true ) {

            t0 = t1;
            t1 = System.currentTimeMillis();
            dt = ( t1 - t0 ) / 1000f;

            for ( Vertex vertex : graph.vertices ) {

                // рассчитать силы создаваемые ребрами
                float fx = 0;
                float fy = 0;

                for ( Edge edge : vertex.edges ) {

                    float [] f = edge.getForce( vertex );

                    fx += f[ 0 ];
                    fy += f[ 1 ];
                }
                for ( Vertex vertex2 : graph.vertices ) {
                    if ( vertex == vertex2 ) { continue; }

                    float dx = vertex.getX() - vertex2.getX();
                    float dy = vertex.getY() - vertex2.getY();

                    float distance = ( float ) Math.sqrt( dx * dx + dy * dy );
                    if ( distance == 0 ) { continue; }

                    float f = 1000f / distance / distance;

                    float a = ( float ) Math.atan( dy / dx );

                    float fgx = f * ( float ) Math.cos( a );
                    float fgy = f * ( float ) Math.sin( a );

                    if ( dx < 0 ) { fgx = -fgx; }
                    if ( dy < 0 ) { fgy = -fgy; }

                    fx += fgx;
                    fy += fgy;
                }
                // обновить скорость
                vertex.vx += fx;
                vertex.vy += fy;

                if ( vertex.vx < 1 ) { vertex.vx = 0; }
                if ( vertex.vy < 1 ) { vertex.vy = 0; }

                // переместить вершину
                vertex.setX( vertex.getX() + vertex.vx * dt );
                vertex.setY( vertex.getY() + vertex.vy * dt );

                // добавить трение
                vertex.vx *= 0.95f;
                vertex.vy *= 0.95f;
            }

            this.repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor( Color.white );
        g.fillRect( 0, 0, getWidth(), getHeight() );

        g.setColor( Color.blue );

        for ( Vertex vertex : graph.vertices ) {
            g.drawOval( ( int ) vertex.getX() - 7, ( int ) vertex.getY() - 14, 20, 20 );
            g.drawString( Integer.toString( vertex.id ), ( int ) vertex.getX(), ( int ) vertex.getY() );
        }

        for ( Edge edge : graph.edges ) {
            g.drawLine( ( int ) edge.one.getX(), ( int ) edge.one.getY(), ( int ) edge.two.getX(), ( int ) edge.two.getY() );
        }
        g.setColor( Color.green );
        g.fillOval( getWidth() / 2 - 2, getHeight() / 2 - 2, 5, 5 );
    }
}





