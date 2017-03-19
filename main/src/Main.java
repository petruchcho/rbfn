import data.DataHolder;
import network.rbfn.RadialBasisFunctionNetwork;

import java.util.ArrayDeque;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        DataHolder<Seed> seedHolder = new DataHolder<>(new SeedReader());
        seedHolder.normalize();
        RadialBasisFunctionNetwork network = new RadialBasisFunctionNetwork(3, seedHolder.getVectorSize());

        ArrayDeque<Seed>[] seedsByClass = new ArrayDeque[4];
        for (Seed data : seedHolder.getData()) {
            if (seedsByClass[data.getClassId()] == null) {
                seedsByClass[data.getClassId()] = new ArrayDeque<>();
            }
            seedsByClass[data.getClassId()].add(data);
        }
        for (int iter = 0; iter < 10; iter++) {
            for (int i = 1; i <= 3; i++) {
                network.initLearn(seedsByClass[i].pollFirst());
            }
        }

        Collections.shuffle(seedHolder.getData());
        for (int iter = 0; iter < 50; iter++) {
            for (Seed data : seedHolder.getData()) {
                network.learn(data);
            }
        }

        for (Seed data : seedHolder.getData()) {
            System.out.println(data.getClassId() + " " + network.calculateOutput(data.asVector()));
        }
    }
}
