package network;

import data.ClassifiedData;
import data.Data;

public interface ClassificationNetwork {
    void learn(ClassifiedData data);

    int classify(Data data);
}
