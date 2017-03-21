import data.DataHolder;
import network.rbfn.RadialBasisFunctionNetwork;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataHolder<Seed> seedHolder = new DataHolder<>(new SeedReader());
        seedHolder.normalize();
        RadialBasisFunctionNetwork network = new RadialBasisFunctionNetwork(7, seedHolder.getVectorSize());

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

        seedsByClass = new ArrayDeque[4];
        for (Seed data : seedHolder.getData()) {
            if (seedsByClass[data.getClassId()] == null) {
                seedsByClass[data.getClassId()] = new ArrayDeque<>();
            }
            seedsByClass[data.getClassId()].add(data);
        }

        List<Seed> trainingData = new ArrayList<>();
        for (int count = 0; count < 20; count++) {
            for (int i = 1; i <= 3; i++) {
                trainingData.add(seedsByClass[i].pollFirst());
            }
        }
        for (int iter = 0; iter < 40000; iter++) {
            double error = 0;
            for (Seed seed : trainingData) {
                //System.out.print(Arrays.toString(network.calculateOutput(seed.asVector())) + " -> ");
                double[] output = network.calculateOutput(seed.asVector());
                for (int i = 0; i < output.length; i++) {
                    int shouldBe = seed.getClassId() - 1 == i ? 1 : 0;
                    error += (shouldBe - output[i]) * (shouldBe - output[i]);
                }
                network.learn(seed);
                //System.out.println(Arrays.toString(network.calculateOutput(seed.asVector())));
            }
            if (iter % 100 == 0) {
                System.err.println(iter);
                System.err.println("Error = " + error);
            }
        }

        List<Seed> testData = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            while (!seedsByClass[i].isEmpty()) {
                testData.add(seedsByClass[i].pollFirst());
            }
        }

        int correct = 0;
        for (Seed data : testData) {
            if (data.getClassId() == network.classify(data)) {
                correct++;
            }
            System.out.println(data.getClassId() + " " + Arrays.toString(network.calculateOutput(data.asVector())));
        }

        System.out.printf("Correctly classified = %.2f percents", correct * 100.0 / testData.size());
    }
}
