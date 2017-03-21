package network.rbfn;

import data.ClassifiedData;
import data.Data;
import data.Vector;
import network.ClassificationNetwork;

import java.util.Random;

public class RadialBasisFunctionNetwork implements ClassificationNetwork {

    private final int N;
    private final int CLASSES_COUNT = 3;

    private RadialBasisFunctionNeuron[] neurons;
    private double[][] weights;
    private double learningStep;
    private double initLearningStep = 0.002;
    private Random random = new Random(123987);
    boolean firstInit = false;

    public RadialBasisFunctionNetwork(int neuronsCount, int inputVectorSize) {
        weights = new double[CLASSES_COUNT][neuronsCount + 1];
        neurons = new RadialBasisFunctionNeuron[neuronsCount];
        N = inputVectorSize;
        initValues(neuronsCount);
    }

    private void initValues(int neuronsCount) {
        learningStep = 0.000005;
        for (int i = 0; i < neuronsCount; i++) {
            for (int j = 0; j < CLASSES_COUNT; j++) {
                weights[j][i] = random.nextDouble();
            }
            neurons[i] = new RadialBasisFunctionNeuron(N);
            neurons[i].initValues(random);
        }
    }

    public void initLearn(Data data) {
        if (!firstInit) {
            for (RadialBasisFunctionNeuron neuron : neurons) {
                for (int i = 0; i < neuron.c.length; i++) {
                    neuron.c[i] = data.getValueAt(i) + random.nextDouble() / 10;
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
        modifyNetwork(data.getClassId() - 1, calculateOutput(x), x);
    }

    private void modifyNetwork(int y, double[] d, Vector x) {
        double[] u = calculateUVector(x);
        for (int j = 0; j < CLASSES_COUNT; j++) {
            int shouldBe = y == j ? 1 : 0;
            double[] deltaW = calculateDeltaW(shouldBe, d[j], u);
            for (int i = 0; i < getNeuronsCount(); i++) {
                neurons[i].modify(shouldBe, d[j], u[i], weights[j][i], x, learningStep);
            }
            for (int i = 0; i < weights.length; i++) {
                weights[j][i] += learningStep * deltaW[i];
            }
        }
    }

    private double[] calculateDeltaW(double y, double d, double[] u) {
        double[] deltaW = new double[getNeuronsCount() + 1];
        for (int i = 0; i < getNeuronsCount(); i++) {
            deltaW[i] = Math.exp(-0.5 * u[i]) * (y - d);
        }
        deltaW[getNeuronsCount()] = (y - d);
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
        int id = 0;
        double[] output = calculateOutput(data.asVector());
        for (int i = 1; i < CLASSES_COUNT; i++) {
            if (output[i] > output[id]) {
                id = i;
            }
        }
        return id + 1;
    }

    private int getNeuronsCount() {
        return neurons.length;
    }

    private double getW0(int classId) {
        return weights[classId][weights.length - 1];
    }

    public double[] calculateOutput(Vector v) {
        double[] output = new double[CLASSES_COUNT];
        for (int j = 0; j < CLASSES_COUNT; j++) {
            output[j] = getW0(j);
            for (int i = 0; i < neurons.length; i++) {
                output[j] += neurons[i].output(v) * weights[j][i];
            }
        }
        return output;
    }
}
