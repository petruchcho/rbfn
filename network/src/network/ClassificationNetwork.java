package network;

import data.ClassifiedData;
import data.Data;

public interface ClassificationNetwork {
    void train(ClassifiedData data);

    int classify(Data data);
}
