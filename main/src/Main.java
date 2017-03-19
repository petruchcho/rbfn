import data.DataHolder;
import network.rbfn.RadialBasisFunctionNetwork;

import java.util.ArrayDeque;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        DataHolder<Seed> seedHolder = new DataHolder<>(new SeedReader());
        seedHolder.normalize();
        RadialBasisFunctionNetwork network = new RadialBasisFunctionNetwork(seedHolder.getVectorSize(), seedHolder.getVectorSize());

        ArrayDeque<Seed>[] seedsByClass = new ArrayDeque[4];
        for (Seed data : seedHolder.getData()) {
            if (seedsByClass[data.getClassId()] == null) {
                seedsByClass[data.getClassId()] = new ArrayDeque<>();
            }
            seedsByClass[data.getClassId()].add(data);
        }
        for (int iter = 0; iter < 20; iter++) {
            for (int i = 1; i <= 3; i++) {
                network.initLearn(seedsByClass[i].pollFirst());
            }
        }

        Collections.shuffle(seedHolder.getData());
        for (int iter = 0; iter < 2400; iter++) {
            for (Seed data : seedHolder.getData()) {
                network.learn(data);
            }
        }

        int correct = 0;
        for (Seed data : seedHolder.getData()) {
            if (data.getClassId() == network.classify(data)) {
                correct++;
            }
            System.out.println(data.getClassId() + " " + network.calculateOutput(data.asVector()));
        }

        System.out.printf("Correctly classified = %.2f percents", correct * 100.0 / seedHolder.getData().size());
    }
}
