package network;

import data.Data;

public class ClassificationNetworkDecorator implements ClassificationNetwork {

    private final NeuralNetworkWithTeacher neuralNetwork;

    public ClassificationNetworkDecorator(NeuralNetworkWithTeacher neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    @Override
    public int classify(Data data) {
        int id = 0;
        double[] output = output(data);
        for (int i = 1; i < output.length; i++) {
            if (output[i] > output[id]) {
                id = i;
            }
        }
        return id;
    }

    @Override
    public void initTrain(Iterable<? extends Data> dataSet) {
        neuralNetwork.initTrain(dataSet);
    }

    @Override
    public void train(Data data, double output) {
        neuralNetwork.train(data, output);
    }

    @Override
    public double[] output(Data data) {
        return neuralNetwork.output(data);
    }
}
