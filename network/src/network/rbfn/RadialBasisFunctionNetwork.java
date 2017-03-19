package network.rbfn;

import data.ClassifiedData;
import data.Data;
import data.Vector;
import network.ClassificationNetwork;
import network.Neuron;

import java.util.Arrays;
import java.util.Random;

public class RadialBasisFunctionNetwork implements ClassificationNetwork {

    private final int N;

    private RadialBasisFunctionNeuron[] neurons;
    private double[] w;
    private double learningStep;
    private double initLearningStep = 0.002;
    private Random random = new Random(123987);
    boolean firstInit = false;

    public RadialBasisFunctionNetwork(int neuronsCount, int inputVectorSize) {
        w = new double[neuronsCount + 1];
        neurons = new RadialBasisFunctionNeuron[neuronsCount];
        N = inputVectorSize;
        initValues(neuronsCount);
    }

    private void initValues(int neuronsCount) {
        learningStep = 3 * 1e-7;
        for (int i = 0; i < neuronsCount; i++) {
            w[i] = random.nextDouble();
            neurons[i] = new RadialBasisFunctionNeuron(N);
            neurons[i].initValues(random);
        }
    }

    public void initLearn(Data data) {
        if (!firstInit) {
            for (RadialBasisFunctionNeuron neuron : neurons) {
                for (int i = 0; i < neuron.c.length; i++) {
                    neuron.c[i] = data.getValueAt(i) + random.nextDouble() / 100;
                }
            }
            firstInit = true;
            return;
        }
        RadialBasisFunctionNeuron closestNeuron = getClosestNeuron(data);

        for (RadialBasisFunctionNeuron neuron : neurons) {
            if (neuron == closestNeuron) {
                neuron.moveCenter(data.asVector(), initLearningStep);
            } else {
                neuron.moveCenter(data.asVector(), -initLearningStep);
            }
        }

        initLearningStep *= 0.9;
    }

    private RadialBasisFunctionNeuron getClosestNeuron(Data data) {
        int w = 0;
        for (int neuronId = 1; neuronId < getNeuronsCount(); neuronId++) {
            if (neurons[neuronId].getDistToCenter(data.asVector()) < neurons[w].getDistToCenter(data.asVector())) {
                w = neuronId;
            }
        }
        return neurons[w];
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
            w[i] += learningStep * deltaW[i];
        }
    }

    private double[] calculateDeltaW(double y, double d, double[] u) {
        double[] deltaW = new double[getNeuronsCount() + 1];
        for (int i = 0; i < getNeuronsCount(); i++) {
            deltaW[i] = Math.exp(-0.5 * u[i]) * (y - d);
        }
        deltaW[getNeuronsCount()] = -(y - d);
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
