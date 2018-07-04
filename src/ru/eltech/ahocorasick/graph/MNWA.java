package ru.eltech.ahocorasick.graph;
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






