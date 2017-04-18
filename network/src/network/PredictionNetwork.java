package network;

import data.Data;

public interface PredictionNetwork extends NeuralNetworkWithTeacher {
    double predictNext(Data data);
}
