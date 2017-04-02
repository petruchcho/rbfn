package network;

import data.Data;

public interface PredictionNetwork {
    void train(Data data);

    double predict(Data data);
}
