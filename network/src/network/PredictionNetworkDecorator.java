package network;

import data.Data;

public class PredictionNetworkDecorator implements PredictionNetwork {

    private final NeuralNetworkWithTeacher neuralNetwork;

    public PredictionNetworkDecorator(NeuralNetworkWithTeacher neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    @Override
    public void initTrain(Iterable<? extends Data> dataSet) {
        neuralNetwork.initTrain(dataSet);
    }

    @Override
    public void train(Data data, double[] output) {
        neuralNetwork.train(data, output);
    }

    @Override
    public double[] output(Data data) {
        return neuralNetwork.output(data);
    }

    @Override
    public double predictNext(Data data) {
        return output(data)[0];
    }
}
