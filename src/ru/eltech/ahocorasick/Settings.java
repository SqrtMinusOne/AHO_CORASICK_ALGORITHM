package ru.eltech.ahocorasick;

public class Settings {
    public static int vertexSize() {
        return VertexSize;
    }

    public static void setVertexSize(int vertexSize) {
        VertexSize = vertexSize;
    }

    public static float textDistance() {
        return textDistance;
    }

    public static int intTextDistance() { return (int)textDistance; }

    public static void setTextDistance(float textDistance) {
        Settings.textDistance = textDistance;
    }

    public static float curveCoef() {
        return curveCoef/40;
    }

    public static int intCurveCoef() {return (int)(curveCoef()*40); }

    public static void setCurveCoef(float curveCoef) {
        Settings.curveCoef = curveCoef;
    }

    public static float arrowSize() {
        return arrowSize;
    }

    public static int intArrowSize() {return (int)arrowSize(); }

    public static void setArrowSize(float arrowSize) {
        Settings.arrowSize = arrowSize;
    }

    public static int vertexWeight() {
        return vertexWeight;
    }

    public static void setVertexWeight(int vertexWeight) {
        Settings.vertexWeight = vertexWeight;
    }

    public static float borderCoef() {
        return borderCoef;
    }

    public static int intBorderCoef() {return (int)(borderCoef()); }

    public static void setBorderCoef(float borderCoef) {
        Settings.borderCoef = borderCoef;
    }

    public static void reset(){
        borderCoef = 10;
        vertexWeight = 150;
        curveCoef = 10f;
        arrowSize = 10f;
        VertexSize = 35;
        textDistance = 15;
    }

    public static int sampleSize() {
        return sampleSize;
    }

    public static void setSampleSize(int sampleSize) {
        Settings.sampleSize = sampleSize;
    }

    private static int sampleSize = 10;

    private static float borderCoef = 10;
    private static int vertexWeight = 150;

    private static float curveCoef = 10f;
    private static float arrowSize = 10f;
    private static int VertexSize = 35;
    private static float textDistance = 15;
}
