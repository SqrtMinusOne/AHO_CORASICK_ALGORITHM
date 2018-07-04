package ru.eltech.ahocorasick.graph;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphPanel extends JPanel implements Runnable {
    final Graph graph;

    public GraphPanel(Graph graph ) {

        this.graph = graph;
    }

    public void run() {

        long t1 = System.currentTimeMillis();
        long t0;
        float dt;

        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;

        while ( true ) {
            t0 = t1;
            t1 = System.currentTimeMillis();
            dt = (t1 - t0) / 1000f;
            for (Vertex vertex : graph.getVertices()) {
                float fx = 0;
                float fy = 0;

                for (Edge edge : vertex.getEdges()) {

                    float[] f = edge.getForce(vertex);

                    fx += f[0];
                    fy += f[1];
                }
                for (Vertex vertex2 : graph.getVertices()) {
                    if (vertex == vertex2) {
                        continue;
                    }

                    float dx = vertex.getX() - vertex2.getX();
                    float dy = vertex.getY() - vertex2.getY();

                    float distance = (float) Math.sqrt(dx * dx + dy * dy);
                    if (distance == 0) {
                        continue;
                    }

                    float f = 1000f / distance / distance;

                    float a = (float) Math.atan(dy / dx);

                    float fgx = f * (float) Math.cos(a);
                    float fgy = f * (float) Math.sin(a);

                    if (dx < 0) {
                        fgx = -fgx;
                    }
                    if (dy < 0) {
                        fgy = -fgy;
                    }

                    fx += fgx;
                    fy += fgy;
                }
                vertex.vx += fx;
                vertex.vy += fy;

                if (vertex.vx < 1) {
                    vertex.vx = 0;
                }
                if (vertex.vy < 1) {
                    vertex.vy = 0;
                }

                vertex.setX(vertex.getX() + vertex.vx * dt);
                vertex.setY(vertex.getY() + vertex.vy * dt);

                vertex.vx *= 0.95f;
                vertex.vy *= 0.95f;
            }

            this.repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(GraphPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor( Color.white );
        g.fillRect( 0, 0, getWidth(), getHeight() );

        g.setColor( Color.blue );
        if ((graph.getVertices() !=null) && (graph.getVertices().size()!=0)) {
            for (Vertex vertex : graph.getVertices()) {
                g.drawOval((int) vertex.getX() - 7, (int) vertex.getY() - 14, 20, 20);
                g.drawString(Integer.toString(vertex.id), (int) vertex.getX(), (int) vertex.getY());
            }
        }

        for ( Edge edge : graph.getEdges()) {
            g.drawLine( ( int ) edge.one.getX(), ( int ) edge.one.getY(), ( int ) edge.two.getX(), ( int ) edge.two.getY() );
        }
        g.setColor( Color.green );
        g.fillOval( getWidth() / 2 - 2, getHeight() / 2 - 2, 5, 5 );
    }
}

