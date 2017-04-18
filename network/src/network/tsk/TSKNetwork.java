package network.tsk;

import data.Data;
import network.PredictionNetwork;
import utils.MatrixUtils;

import java.util.List;
import java.util.Random;

public class TSKNetwork {

//    private final int inputSize;
//    private final int rulesCount;
//    private final Random random;
//
//    private GaussFuzzyficationNeurons[] fuzzyficationNeurons;
//    private double[][] p;
//
//    public TSKNetwork(int inputSize, int rulesCount) {
//        this.inputSize = inputSize;
//        this.rulesCount = rulesCount;
//
//        this.random = new Random(1);
//        this.fuzzyficationNeurons = new GaussFuzzyficationNeurons[rulesCount];
//        this.p = new double[rulesCount][inputSize + 1];
//        init();
//    }
//
//    private void init() {
//
//    }
//
//    @Override
//    public void train(List<Data> fullDataSet) {
//        modifyP(fullDataSet);
//    }
//
//    private void trainInternal(double[] data) {
//
//    }
//
//    private void modifyP(List<Data> dataSet) {
//        double[][] w_ = calculateW_(dataSet);
//    }
//
//    private double[][] calculateW_(List<Data> dataSet) {
//        double[][] w_ = new double[dataSet.size()][(inputSize + 1) * rulesCount];
//        for (int t = 0; t < dataSet.size(); t++) {
//
//        }
//        return w_;
//    }
//
//
//    @Override
//    public double predict(Data data) {
//        return calculateOutput(data.asVector().v());
//    }
//
//    private double calculateOutput(double[] data) {
//        double[] w = calculateW(data);
//        double[] y = calculateY(data, w);
//        return MatrixUtils.sum(y) / MatrixUtils.sum(w);
//    }
//
//    private double[] calculateW(double[] data) {
//        double[] w = new double[rulesCount];
//        for (int rule = 0; rule < rulesCount; rule++) {
//            double[] fuzzyOutput = new double[inputSize];
//            for (int i = 0; i < inputSize; i++) {
//                fuzzyOutput[i] = fuzzyficationNeurons[i].output(data[i]);
//            }
//            w[rule] = fuzzyAggregate(fuzzyOutput);
//        }
//        return w;
//    }
//
//    private double[] calculateY(double[] data, double[] w) {
//        double[] y = new double[rulesCount];
//        for (int i = 0; i < rulesCount; i++) {
//            y[i] = p[i][0];
//            for (int j = 1; j <= inputSize; j++) {
//                y[i] += data[j - 1] * p[i][j];
//            }
//            y[i] *= w[i];
//        }
//        return y;
//    }
//
//    private double fuzzyAggregate(double[] fuzzyOutput) {
//        double res = 1;
//        for (double output : fuzzyOutput) {
//            res *= output;
//        }
//        return res;
//    }
}
