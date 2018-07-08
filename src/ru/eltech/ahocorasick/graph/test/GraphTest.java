package ru.eltech.ahocorasick.graph.test;

import org.junit.Test;
import ru.eltech.ahocorasick.graph.Graph;
import ru.eltech.ahocorasick.graph.GraphPanel;
import ru.eltech.ahocorasick.graph.Vertex;

public class GraphTest {
    @Test
    public void GraphTreadSafeTest() {
        for (int i = 0; i < 10; i++)
            doDangerousActions();
    }

    private void doDangerousActions() {
        Graph graph = new Graph();

        GraphPanel graphPanel = new GraphPanel(graph);

        Thread processGraph = new Thread(graphPanel);
        processGraph.start();
        sleep();
        graph.createVertex(0);
        sleep();
        graph.createVertex(1);
        sleep();
        graph.createVertex(2);
        sleep();
        graph.createVertex(3);
        sleep();
        graph.createVertex(4);

        sleep();
        graph.createEdge(0, 1, "");
        sleep();
        graph.createEdge(0, 2, "");
        sleep();
        graph.createEdge(0, 3, "");
        sleep();
        graph.createEdge(0, 4, "");
    }

    private static void sleep() {
        try {
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void MakeGraphTest() {
        Graph graph = new Graph();
        for (int i = 0; i < 5; i++)
            graph.createVertex(i);
        for (Vertex vertex : graph.getVertices()) {
            for (Vertex vertex1 : graph.getVertices()) {
                if (vertex == vertex1)
                    continue;
                graph.createEdge(vertex, vertex1, "");
            }
        }

    }
}