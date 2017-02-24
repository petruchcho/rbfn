package data;

public class Vector {
    private double[] v;

    public Vector(int size) {
        v = new double[size];
    }

    public Vector(double[] v) {
        this.v = v;
    }

    public double[] v() {
        return v;
    }

    public int getSize() {
        return v.length;
    }
}
