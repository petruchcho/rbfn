package data;

public class VectorData implements Data {
    private final double[] vector;

    public VectorData(double[] vector) {
        this.vector = vector;
    }

    @Override
    public Vector asVector() {
        return new Vector(vector);
    }

    @Override
    public double getValueAt(int index) {
        return vector[index];
    }

    @Override
    public void setValueAt(int index, double value) {
        vector[index] = value;
    }
}
