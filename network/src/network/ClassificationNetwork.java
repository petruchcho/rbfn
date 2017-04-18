package network;

import data.Data;

public interface ClassificationNetwork extends NeuralNetworkWithTeacher {
    int classify(Data data);
}
