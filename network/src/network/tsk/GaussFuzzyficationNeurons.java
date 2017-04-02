package network.tsk;

public class GaussFuzzyficationNeurons {

    private double c;
    private double delta;
    private double b;

    public double output(double x) {
        return 1 / (1 + Math.pow((x - c) / delta, b));
    }
}
