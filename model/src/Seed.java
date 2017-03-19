import data.Vector;

public class Seed implements data.ClassifiedData {

    /**
     * 1. area A,
     2. perimeter P,
     3. compactness C = 4*pi*A/P^2,
     4. length of kernel,
     5. width of kernel,
     6. asymmetry coefficient
     7. length of kernel groove.
     */

    private final double area;
    private final double perimeter;
    private final double compactness;
    private final double kernelLength;
    private final double kernelWidth;
    private final double asymmetryCoefficient;
    private final double kernelGrooveLength;

    private final int classId;

    private final Vector vector;

    public Seed(double area, double perimeter, double compactness, double kernelLength, double kernelWidth, double asymmetryCoefficient, double kernelGrooveLength, int classId) {
        this.area = area;
        this.perimeter = perimeter;
        this.compactness = compactness;
        this.kernelLength = kernelLength;
        this.kernelWidth = kernelWidth;
        this.asymmetryCoefficient = asymmetryCoefficient;
        this.kernelGrooveLength = kernelGrooveLength;
        this.classId = classId;

        this.vector = makeVector();
    }

    @Override
    public Vector asVector() {
        return vector;
    }

    @Override
    public double getValueAt(int index) {
        return vector.v()[index];
    }

    @Override
    public void setValueAt(int index, double value) {
        vector.v()[index] = value;
    }

    private Vector makeVector() {
        return new Vector(new double[]{
                area,
                perimeter,
                compactness,
                kernelLength,
                kernelWidth,
                asymmetryCoefficient,
                kernelGrooveLength
        });
    }

    @Override
    public int getClassId() {
        return classId;
    }
}
