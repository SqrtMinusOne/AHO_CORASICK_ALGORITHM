package ru.eltech.ahocorasick.graph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphPanel extends JPanel implements Runnable {
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    private Graph graph;

    public GraphPanel(Graph graph ) {
        this.graph = graph;
    }

    @SuppressWarnings({"InfiniteLoopStatement"})
    public void run() {

        while ( true ) {
            for (Vertex vertex : graph.getVertices()) {
                calculateForces(vertex);
            }

            for (Vertex vertex : graph.getVertices()) {
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

    private final static float borderCoef= 3e8f;

    private void calculateForces(Vertex vertex) {
        float xvel = 0;
        float yvel = 0;

        //Calculating forces which push this item away
        for (Vertex vertex1 : graph.getVertices()){
            float dx = vertex.getX() - vertex1.getX();
            float dy = vertex.getY() - vertex1.getY();
            float l = vertex.getDistanceTo(vertex1);
            if (l > 0){
                xvel += (dx * 150) / Math.pow(l,2);
                yvel += (dy * 150) / Math.pow(l,2);
            }
        }

        //Calculating forces pulling item together
        float weight = (float)Math.pow((vertex.getEdges().size() + 1),1.3) * 10;
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
        float newBorderCoef = borderCoef/(float)Math.pow(graph.getVertices().size(), 0.3);
        if (vertex.getX() < this.getWidth()/2)
            xvel += Math.pow(this.getWidth()/2 - vertex.getX(), 4)/newBorderCoef;
        else
            xvel -= Math.pow(this.getWidth()/2 - vertex.getX(), 4)/newBorderCoef;

        if (vertex.getY() < this.getHeight()/2)
            yvel += Math.pow(this.getHeight()/2 - vertex.getY(), 4)/newBorderCoef;
        else
            yvel -= Math.pow(this.getHeight()/2 - vertex.getY(), 4)/newBorderCoef;

        if ((Math.abs(xvel) < 0.1) && (Math.abs(yvel) < 0.1)){
            xvel = yvel = 0;
        }

        float newx = vertex.getX() + xvel;
        float newy = vertex.getY() + yvel;

        if ((0 <= newx) && (newx <= this.getWidth()) &&
                (0 <= newy) && (newy <= this.getHeight())){
            vertex.setNewX(newx);
            vertex.setNewY(newy);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(new GradientPaint(0, 0, Color.white,
                this.getWidth(), this.getHeight(), Color.lightGray));
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        for ( Edge edge : graph.getEdges()) {
            drawEdge(g2d, edge);
        }

        if ((graph.getVertices() !=null) && (graph.getVertices().size()!=0)) {
            for (Vertex vertex : graph.getVertices()) {
                drawVertex(g2d, vertex);
            }
        }

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
            g.setColor(Color.black);
            g.setFont(new Font("Calibri", Font.BOLD, (int)(Vertex.size*0.75)));
            int d = (int)(Vertex.size * Math.sin(Math.PI/4)/2.3);
            g.drawString(Integer.toString(vertex.getTerminal()), (int) (vertex.getX() - d*0.7), (int) vertex.getY() + d);
        }
        if (vertex.getPopUpInfo()!=null){
            drawPopUp(g, vertex);
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
            else if (v.isHovered()){
                b -= 0.1f;
            }
            res.grad0t = Color.getHSBColor(h,s,b);
            res.grad1t = Color.getHSBColor(h,s,b-0.1f);
            return res;
        }
        Color border;
        Color grad0t;
        Color grad1t;
    }

    /**
     *                                   bezier(X,Y)
     *                                      |
     *                                      |
     *  source(X,Y) ------------------------|-------------------|----- dest(X,Y)
     *                                   center(X,Y)         shift(X,Y)
     */
    private void drawEdge(Graphics2D g2d, Edge edge) {
        int centerX = (int) edge.getSourceX() - ((int) edge.getSourceX() - (int) edge.getDestX())/2;
        int centerY = (int) edge.getSourceY() - ((int) edge.getSourceY() - (int) edge.getDestY())/2;
        float shiftX, shiftY; //Coordinates for arrow base
        Polygon arrow = new Polygon();
        float angle = edge.getAngle() + (float) Math.PI/2; //Perpendicular for edge in center
        float arrowAngle; //Perpendicular for endge in arrow base point
        if (angle > 2 * Math.PI)
            angle -= (float)(2*Math.PI);

        if (edge.getState() == Edge.states.NORMAL) {
            //Line
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine((int) edge.getSourceX(), (int) edge.getSourceY(), (int) edge.getDestX(), (int) edge.getDestY());
            g2d.setStroke(new BasicStroke(1));
            //Arrow
            shiftX = edge.getDestX() + Edge.arrowSize * (float) Math.sin(edge.getAngle());
            shiftY = edge.getDestY() + Edge.arrowSize * (float) Math.cos(edge.getAngle());
            arrowAngle = angle;
        }
        else{
            //Line
            float dCoef = 0.5f;
            if (edge.getState() == Edge.states.ROUND1)
                g2d.setColor(Color.getHSBColor(0.34f,0.93f,0.63f));
            else if (edge.getState() == Edge.states.ROUND2){
                g2d.setColor(Color.red);
                dCoef = 1;
            }
            Path2D.Float curve = new Path2D.Float();
            curve.moveTo(edge.getSourceX(), edge.getSourceY());
            float l = edge.getDest().getDistanceTo(edge.getSource());
            float bezierX = centerX + (float)(Edge.curveCoef*dCoef*Math.sin(angle))*l;
            float bezierY = centerY + (float)(Edge.curveCoef*dCoef*Math.cos(angle))*l;
            curve.curveTo(edge.getSourceX(), edge.getSourceY(),
                    bezierX, bezierY, edge.getDestX(), edge.getDestY());
            g2d.setStroke(new BasicStroke(3));
            g2d.draw(curve);
            g2d.setStroke(new BasicStroke(1));
            //Arrow
            float delta = (l - Edge.arrowSize)/l;
            float t1x = (edge.getSourceX()*(1-delta) + bezierX*(delta));
            float t1y = (edge.getSourceY()*(1-delta) + bezierY*(delta));
            float t2x = (bezierX*(1-delta) + edge.getDestX()*(delta));
            float t2y = (bezierY*(1-delta) + edge.getDestY()*(delta));
            shiftX = (t1x*(1-delta) + t2x*(delta));
            shiftY = (t1y*(1-delta) + t2y*(delta));
            arrowAngle = (float)Edge.getAngle(t1x-shiftX, t1y-shiftY) + (float) Math.PI/2;
            if (arrowAngle > 2 * Math.PI)
                arrowAngle -= (float)(2*Math.PI);
        }

        arrow.addPoint((int) edge.getDestX(), (int) edge.getDestY());
        arrow.addPoint((int)(shiftX + Edge.arrowSize * Math.sin(arrowAngle)),
                (int)(shiftY + Edge.arrowSize * Math.cos(arrowAngle)));
        arrow.addPoint((int)(shiftX - Edge.arrowSize * Math.sin(arrowAngle)),
                (int)(shiftY - Edge.arrowSize * Math.cos(arrowAngle)));
        g2d.fillPolygon(arrow);
        g2d.setFont(new Font("Calibri", Font.PLAIN, 20));
        g2d.drawString(edge.getName(),
                centerX + (int)(Edge.textDistance*Math.sin(angle)),
                centerY + (int)(Edge.textDistance*Math.cos(angle)));
    }

    private void drawPopUp(Graphics2D g2d, Vertex v){
        int width = (int)(v.getPopUpInfo().length()*5.7);
        int height = 20;
        int x1 = (int) (v.getX() + Vertex.size/2);
        int y1 = (int) (v.getY() + Vertex.size/2);
        if (x1 + width > this.getWidth()){
            x1 -= (x1 + width - this.getWidth() + 10);
            y1 += Vertex.size*0.1;
        }
        g2d.setColor(Color.getHSBColor(0.58f, 0.19f, 0.89f));
        g2d.fillRoundRect(x1, y1, width, height, 4, 4);
        g2d.setColor(Color.black);
        g2d.drawRoundRect(x1, y1, width, height, 4, 4);
        g2d.setColor(Color.getHSBColor(0,0,0.2f));
        g2d.setFont(new Font("Arial", Font.PLAIN, 13));
        g2d.drawString(v.getPopUpInfo(), x1 + 6, y1 + 14);
    }

}
