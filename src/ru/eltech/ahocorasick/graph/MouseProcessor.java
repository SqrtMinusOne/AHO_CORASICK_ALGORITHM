package ru.eltech.ahocorasick.graph;

import ru.eltech.ahocorasick.ui.GraphicAlgorithmProcessor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseProcessor implements MouseListener, MouseMotionListener {

    public MouseProcessor(GraphicAlgorithmProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        for (Vertex vertex : processor.getGraph().getVertices()){
            if (vertex.isIn(e.getX(), e.getY())){
                vertex.setPressed(true);
                relativeX = e.getX() - (int)vertex.getX();
                relativeY = e.getY() - (int)vertex.getY();
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (Vertex vertex : processor.getGraph().getVertices()){
            vertex.setPressed(false);
        }
        relativeX = 0;
        relativeY = 0;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {
        mouseReleased(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        for (Vertex vertex : processor.getGraph().getVertices()){
            if (vertex.isPressed()){
                vertex.setX(e.getX() + relativeX);
                vertex.setY(e.getY() + relativeY);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) { }

    private int relativeX;
    private int relativeY;
    private final GraphicAlgorithmProcessor processor;
}
