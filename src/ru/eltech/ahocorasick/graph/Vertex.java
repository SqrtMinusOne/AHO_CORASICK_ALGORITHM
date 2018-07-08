package ru.eltech.ahocorasick.graph;

import ru.eltech.ahocorasick.Settings;

import java.util.concurrent.CopyOnWriteArrayList;

public class Vertex {

    public Vertex ( int id, float x, float y ) {
        this.setId(id);
        this.state = states.NORMAL;
        this.x = x;
        this.y = y;
    }

    public CopyOnWriteArrayList<Edge> getEdges() {
        return edges;
    }

    private final CopyOnWriteArrayList<Edge> edges = new CopyOnWriteArrayList<>();

    public synchronized void setX ( float x ) { this.x = x; }
    public synchronized float getX () { return x; }

    public synchronized void setY ( float y ) { this.y = y; }
    public synchronized float getY () { return y; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public synchronized states getState() {
        return state;
    }

    public synchronized void setState(states state) {
        this.state = state;
    }

    public int getTerminal() {
        return terminal;
    }

    public void setTerminal(int terminal) {
        this.terminal = terminal;
    }

    public float getDistanceTo(Vertex v){
        return (float)Math.sqrt(Math.pow(x - v.getX(), 2) + Math.pow(y - v.getY(), 2));
    }

    public void setNewX(float newX) {
        this.newX = newX;
    }

    public void setNewY(float newY) {
        this.newY = newY;
    }

    public enum states {NORMAL, STATE, ROOT}

    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    public boolean isIn(float x, float y){
        double r = Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
        return (r <= Settings.vertexSize()/2);
    }

    public void adjust(){
        x = newX;
        y = newY;
    }

    public boolean isHovered() {
        return isHovered;
    }

    public void setHovered(boolean hovered) {
        isHovered = hovered;
    }

    public String getPopUpInfo() {
        return popUpInfo;
    }

    public void setPopUpInfo(String popUpInfo) {
        this.popUpInfo = popUpInfo;
    }

    private String popUpInfo;
    private boolean isPressed;

    private boolean isHovered;
    private states state;

    private float newX;
    private float newY;

    private float x;
    private float y;

    @Override
    public String toString() {
        return "[" + (int)x + " " + (int)y + "]";
    }

    private int terminal = -1;
    private int id;
}
