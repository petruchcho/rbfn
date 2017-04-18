package network;

import data.Data;

public interface NeuralNetworkWithTeacher {
    void initTrain(Iterable<? extends Data> dataSet);

    void train(Data data, double output);

    double[] output(Data data);
}
