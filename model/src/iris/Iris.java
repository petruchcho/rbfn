package iris;

import data.ClassifiedData;
import data.Vector;

public class Iris implements ClassifiedData {

    enum IrisClass {
        SETOSA,
        VERSICOLOUR,
        VIRGINICA
    }

    private final double sepalLength;
    private final double sepalWidth;
    private final double petalLength;
    private final double petalWidth;
    private final IrisClass irisClass;

    private final double[] vector;

    public Iris(double sepalLength, double sepalWidth, double petalLength, double petalWidth, IrisClass irisClass) {
        this.sepalLength = sepalLength;
        this.sepalWidth = sepalWidth;
        this.petalLength = petalLength;
        this.petalWidth = petalWidth;
        this.irisClass = irisClass;

        vector = new double[]{
                sepalLength,
                sepalWidth,
                petalLength,
                petalWidth
        };
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

    @Override
    public int getClassId() {
        switch (irisClass) {
            case SETOSA:
                return 1;
            case VERSICOLOUR:
                return 2;
            case VIRGINICA:
                return 3;
            default:
                throw new RuntimeException("Unexpected class");
        }
    }
}