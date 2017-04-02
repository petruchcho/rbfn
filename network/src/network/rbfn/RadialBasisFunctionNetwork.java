package network.rbfn;

import data.ClassifiedData;
import data.Data;
import data.Vector;
import network.ClassificationNetwork;

import java.util.*;

public class RadialBasisFunctionNetwork implements ClassificationNetwork {

    private final int argumentsCount;
    private final int outputVectorSize;

    private RadialBasisFunctionNeuron[] neurons;
    private double[][] weights;
    private double learningStep;
    private Random random = new Random();

    public RadialBasisFunctionNetwork(int neuronsCount, int inputVectorSize, int outputVectorSize) {
        this.argumentsCount = inputVectorSize;
        this.outputVectorSize = outputVectorSize;
        weights = new double[outputVectorSize][neuronsCount + 1];
        neurons = new RadialBasisFunctionNeuron[neuronsCount];
        initValues(neuronsCount);
    }

    private void initValues(int neuronsCount) {
        learningStep = 0.000005;
        for (int i = 0; i < neuronsCount; i++) {
            for (int j = 0; j < outputVectorSize; j++) {
                weights[j][i] = random.nextDouble() - 0.5;
            }
            neurons[i] = new RadialBasisFunctionNeuron(argumentsCount);
            neurons[i].initValues(random);
        }
    }

    @Override
    public void train(ClassifiedData data) {
        Vector x = data.asVector();
        modifyNetwork(data.getClassId() - 1, calculateOutput(x), x);
    }

    private void modifyNetwork(int y, double[] d, Vector x) {
        double[] u = calculateUVector(x);
        for (int j = 0; j < outputVectorSize; j++) {
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
            for (int j = 0; j < argumentsCount; j++) {
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
        for (int i = 1; i < outputVectorSize; i++) {
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
        double[] output = new double[outputVectorSize];
        for (int j = 0; j < outputVectorSize; j++) {
            output[j] = getW0(j);
            for (int i = 0; i < neurons.length; i++) {
                output[j] += neurons[i].output(v.v()) * weights[j][i];
            }
        }
        return output;
    }

    public void trainKMeans(List<? extends Data> dataSet, int iterations) {
        initKMeans(dataSet);
        for (int iter = 0; iter < iterations; iter++) {
            List<Data>[] clasters = new List[getNeuronsCount()];
            for (int i = 0; i < getNeuronsCount(); i++) {
                clasters[i] = new ArrayList<>();
            }
            for (Data data : dataSet) {
                clasters[getClosestNeuronIndex(data)].add(data);
            }
            for (int i = 0; i < getNeuronsCount(); i++) {
                if (clasters[i].size() > 0) {
                    neurons[i].setCenter(calculateMeanPoint(clasters[i]));
                }
            }
        }
    }

    private Vector calculateMeanPoint(List<? extends Data> dataSet) {
        double[] meanPoint = new double[argumentsCount];
        for (Data data : dataSet) {
            for (int i = 0; i < argumentsCount; i++) {
                meanPoint[i] += data.getValueAt(i);
            }
        }
        for (int i = 0; i < argumentsCount; i++) {
            meanPoint[i] /= dataSet.size();
        }
        return new Vector(meanPoint);
    }

    private void initKMeans(List<? extends Data> dataSet) {
        Vector meanPoint = calculateMeanPoint(dataSet);
        for (RadialBasisFunctionNeuron neuron : neurons) {
            double[] curCenter = Arrays.copyOf(meanPoint.v(), meanPoint.v().length);
            for (int i = 0; i < argumentsCount; i++) {
                curCenter[i] += (random.nextDouble() - 0.5) * 1e-12;
            }
            neuron.setCenter(new Vector(curCenter));
        }
    }

    private void initKMeans2(List<? extends Data> dataSet) {
        Collections.shuffle(dataSet);
        for (RadialBasisFunctionNeuron neuron : neurons) {
            neuron.setCenter(dataSet.get(random.nextInt(dataSet.size())).asVector());
        }
    }

    private int getClosestNeuronIndex(Data data) {
        int w = 0;
        for (int neuronId = 1; neuronId < getNeuronsCount(); neuronId++) {
            if (neurons[neuronId].getDistToCenter(data.asVector()) < neurons[w].getDistToCenter(data.asVector())) {
                w = neuronId;
            }
        }
        return w;
    }
}
