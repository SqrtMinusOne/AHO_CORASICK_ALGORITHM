package ru.eltech.ahocorasick.graph;
import ru.eltech.ahocorasick.alg.Algorithm;
import ru.eltech.ahocorasick.alg.BohrWithGraph;

import javax.swing.*;

public class MNWA {
    public static void main(String[] args) {
        BohrWithGraph bohr = new BohrWithGraph();
        Algorithm alg = new Algorithm(bohr);

        JFrame frame = new JFrame("Swing Paint Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize( 500, 500 );

        GraphPanel graphPanel = new GraphPanel(bohr.getGraph());
        frame.add(graphPanel);

        frame.setVisible(true);

        Thread processGraph = new Thread(graphPanel);
        processGraph.start();
        sleep();
        alg.addString("A");
        sleep();
        alg.addString("AB");
        sleep();
        alg.addString("ABC");
        sleep();
        alg.addString("ABCD");

    }

    private static Graph getExampleGraph() {
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
        return graph;
    }

    private static void sleep() {
        try {
            Thread.sleep(1000);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}






