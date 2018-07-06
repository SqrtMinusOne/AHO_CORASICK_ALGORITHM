package ru.eltech.ahocorasick.graph;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphPanel extends JPanel implements Runnable {
    private final Graph graph;

    public GraphPanel(Graph graph ) {
        this.graph = graph;
    }

    @SuppressWarnings({"InfiniteLoopStatement"})
    public void run() { //TODO

        while ( true ) {

            for (Vertex vertex : graph.getVertices()){
                calculateForces(vertex);
            }

            for (Vertex vertex : graph.getVertices()){
                vertex.adjust();
            }

            this.repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(GraphPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void calculateForces(Vertex vertex) {
        float xvel = 0;
        float yvel = 0;
        final float borderCoef= 100000;

        //Calculating forces which push this item away
        for (Vertex vertex1 : graph.getVertices()){
            float dx = vertex.getX() - vertex1.getX();
            float dy = vertex.getY() - vertex1.getY();
            float l = vertex.getDistanceTo(vertex1);
            if (l > 0){
               xvel += (dx * 25) / l;
               yvel += (dy * 25) / l;
            }
        }

        xvel /= Math.pow(graph.getVertices().size(), 2);
        yvel /= Math.pow(graph.getVertices().size(), 2);

        //Calculating forces pulling item together
        float weight = (vertex.getEdges().size() + 1) * 10;
        for (Edge edge : vertex.getEdges()){
            float dx, dy;
            if (edge.getSource() == vertex){
                dx = vertex.getX() - edge.getDestX();
                dy = vertex.getY() - edge.getDestY();
            }
            else {
                dx = vertex.getX() - edge.getSourceX();
                dy = vertex.getY() - edge.getSourceY();
            }
            xvel -= dx / weight;
            yvel -= dy / weight;
        }

        //Calculating forces, pushing items away from the borders
        if (vertex.getX() < this.getWidth()/2)
            xvel += Math.pow(this.getWidth()/2 - vertex.getX(), 2)/borderCoef;
        else
            xvel -= Math.pow(this.getWidth()/2 - vertex.getX(), 2)/borderCoef;

        if (vertex.getY() < this.getHeight()/2)
            yvel += Math.pow(this.getHeight()/2 - vertex.getY(), 2)/borderCoef;
        else
            yvel -= Math.pow(this.getHeight()/2 - vertex.getY(), 2)/borderCoef;

        if ((Math.abs(xvel) < 0.1) && (Math.abs(yvel) < 0.1)){
            xvel = yvel = 0;
        }

        float newx = vertex.getX() + xvel;
        float newy = vertex.getY() + yvel;

        if ((0 < newx) && (newx < this.getWidth()) &&
                (0 < newy) && (newy < this.getHeight())){
            vertex.setNewX(newx);
            vertex.setNewY(newy);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor( Color.white );
        g2d.fillRect( 0, 0, getWidth(), getHeight() );

        if ((graph.getVertices() !=null) && (graph.getVertices().size()!=0)) {
            for (Vertex vertex : graph.getVertices()) {
                drawVertex(g2d, vertex);
            }
        }

        for ( Edge edge : graph.getEdges()) {
            drawEdge(g2d, edge);
        }
        g2d.setColor( Color.green );
        g2d.fillOval( getWidth() / 2 - 2, getHeight() / 2 - 2, 5, 5 );
    }

    private void drawVertex(Graphics2D g, Vertex vertex) {
        VertexColor color = VertexColor.get(vertex);
        Color[] colors = new Color[]{color.grad0t, color.grad1t};
        float[] fractions = new float[]{0, 1};
        g.setPaint(new RadialGradientPaint(vertex.getX(), vertex.getY(),
                (float) Vertex.size/2,  vertex.getX(), vertex.getY(),
                fractions, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE));
        g.fillOval((int) vertex.getX() - Vertex.size/2, (int) vertex.getY() - Vertex.size/2, Vertex.size, Vertex.size);

        g.setColor(color.border);
        g.drawOval((int) vertex.getX() - Vertex.size/2, (int) vertex.getY() - Vertex.size/2, Vertex.size, Vertex.size);
        if (vertex.getTerminal() >= 0) {
            g.setColor(Color.blue);
            g.drawString(Integer.toString(vertex.getTerminal()), (int) vertex.getX() - 3, (int) vertex.getY() + 4);
        }
    }

    private static class VertexColor{
        private VertexColor(){}
        static VertexColor get(Vertex v){
            VertexColor res = new VertexColor();
            res.border = Color.black;
            float h = 0f;
            switch (v.getState()){
                case NORMAL:
                    h = 0.125f;
                    break;
                case STATE:
                    h = 0.3f;
                    break;
                case ROOT:
                    h = 0f;
                    break;
            }
            float s = 1f;
            float b = 1f;
            if (v.isPressed())
                b -= 0.3f;
            res.grad0t = Color.getHSBColor(h,s,b);
            res.grad1t = Color.getHSBColor(h,s,b-0.1f);
            return res;
        }
        Color border;
        Color grad0t;
        Color grad1t;
    }

    private void drawEdge(Graphics2D g2d, Edge edge) {
        g2d.setColor(Color.black);
        g2d.drawLine( (int) edge.getSourceX(),(int) edge.getSourceY(), ( int ) edge.getDestX(), ( int ) edge.getDestY() );
        int centerX = (int) edge.getSourceX() - ((int) edge.getSourceX() - (int) edge.getDestX())/2;
        int centerY = (int) edge.getSourceY() - ((int) edge.getSourceY() - (int) edge.getDestY())/2;
        float angle = edge.getAngle() + (float) Math.PI/2;
        if (angle > 2 * Math.PI)
            angle -= (float)(2*Math.PI);

        int newCenterX = centerX + (int)(Edge.textDistance*Math.sin(angle));
        int newCenterY = centerY + (int)(Edge.textDistance*Math.cos(angle));

     //   g2d.drawLine(centerX, centerY, newCenterX, newCenterY);
        g2d.drawString(edge.getName(), newCenterX, newCenterY);
    }
}
