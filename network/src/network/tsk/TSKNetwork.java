package network.tsk;

import data.Data;
import network.Neuron;
import network.PredictionNetwork;

import java.util.Random;

public class TSKNetwork implements PredictionNetwork {

    private final int inputSize;
    private final int rulesCount;
    private final Random random;

    private GaussFuzzyficationNeurons[] fuzzyficationNeurons;

    public TSKNetwork(int inputSize, int rulesCount) {
        this.inputSize = inputSize;
        this.rulesCount = rulesCount;

        this.random = new Random(1);
        this.fuzzyficationNeurons = new GaussFuzzyficationNeurons[rulesCount];
        init();
    }

    private void init() {

    }

    @Override
    public void train(Data data) {

    }

    @Override
    public double predict(Data data) {
        return calculateOutput(data.asVector().v());
    }

    private double calculateOutput(double[] data) {
        return 0;
    }
}
