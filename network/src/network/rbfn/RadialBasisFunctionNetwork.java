package network.rbfn;

import data.ClassifiedData;
import data.Data;
import data.Vector;
import network.ClassificationNetwork;

import java.util.Random;

public class RadialBasisFunctionNetwork implements ClassificationNetwork {

    private final int N;

    private RadialBasisFunctionNeuron[] neurons;
    private double[] w;
    private double learningStep;
    private Random random = new Random(123987);

    public RadialBasisFunctionNetwork(int neuronsCount, int inputVectorSize) {
        w = new double[neuronsCount + 1];
        neurons = new RadialBasisFunctionNeuron[neuronsCount];
        N = inputVectorSize;
        initValues(neuronsCount);
    }

    private void initValues(int neuronsCount) {
        learningStep = 0.001;
        for (int i = 0; i < neuronsCount; i++) {
            w[i] = 1;
            neurons[i] = new RadialBasisFunctionNeuron(N);
            neurons[i].initValues(random);
        }
    }

    private void initLearn(Data data) {

    }

    @Override
    public void learn(ClassifiedData data) {
        Vector x = data.asVector();
        modifyNetwork(data.getClassId(), calculateOutput(x), x);
    }

    private void modifyNetwork(int y, double d, Vector x) {
        double[] u = calculateUVector(x);
        double[] deltaW = calculateDeltaW(y, d, u);
        for (int i = 0; i < getNeuronsCount(); i++) {
            neurons[i].modify(y, d, u[i], w[i], x, learningStep);
        }
        for (int i = 0; i < w.length; i++) {
            w[i] -= learningStep * deltaW[i];
        }
    }

    private double[] calculateDeltaW(double y, double d, double[] u) {
        double[] deltaW = new double[getNeuronsCount() + 1];
        for (int i = 0; i < getNeuronsCount(); i++) {
            deltaW[i] = Math.exp(-0.5 * u[i]) * (y - d);
        }
        deltaW[getNeuronsCount()] = y - d;
        return deltaW;
    }

    private double[] calculateUVector(Vector x) {
        double[] u = new double[getNeuronsCount()];
        for (int i = 0; i < getNeuronsCount(); i++) {
            for (int j = 0; j < N; j++) {
                double z = neurons[i].getZ(j, x);
                u[i] += z * z;
            }
        }
        return u;
    }

    @Override
    public int classify(Data data) {
        return (int) Math.round(calculateOutput(data.asVector()));
    }

    private int getNeuronsCount() {
        return neurons.length;
    }

    private double getW0() {
        return w[w.length - 1];
    }

    public double calculateOutput(Vector v) {
        double output = getW0();
        for (int i = 0; i < neurons.length; i++) {
            output += neurons[i].output(v) * w[i];
        }
        return output;
    }
}
