import data.ClassifiedData;
import data.DataHolder;
import iris.IrisReader;
import network.rbfn.RadialBasisFunctionNetwork;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    private static final int INTERNAL_NEURONS_COUNT = 7;
    private static final int OUTPUT_NEURONS_COUNT = 3;
    private static final int TRAINING_ITERATIONS = 100000;
    private static final int TRAINING_DATA_PERCENT = 65;

    public static void main(String[] args) {
        DataHolder<? extends ClassifiedData> dataHolder = new DataHolder<>(new IrisReader());
        dataHolder.normalizeData();
        Collections.shuffle(dataHolder.getData());
        RadialBasisFunctionNetwork network = new RadialBasisFunctionNetwork(INTERNAL_NEURONS_COUNT, dataHolder.getVectorSize(), OUTPUT_NEURONS_COUNT);

        network.trainKMeans(dataHolder.getData(), 322);

        ArrayDeque<ClassifiedData>[] seedsByClass = new ArrayDeque[OUTPUT_NEURONS_COUNT + 1];
        for (ClassifiedData data : dataHolder.getData()) {
            if (seedsByClass[data.getClassId()] == null) {
                seedsByClass[data.getClassId()] = new ArrayDeque<>();
            }
            seedsByClass[data.getClassId()].add(data);
        }

        List<ClassifiedData> trainingData = new ArrayList<>();
        while (true) {
            for (int i = 1; i <= 3; i++) {
                trainingData.add(seedsByClass[i].pollFirst());
            }
            if (trainingData.size() >= TRAINING_DATA_PERCENT / 100.0 * dataHolder.getData().size()) {
                break;
            }
        }

        List<ClassifiedData> testData = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            while (!seedsByClass[i].isEmpty()) {
                testData.add(seedsByClass[i].pollFirst());
            }
        }

        double prevError = Integer.MAX_VALUE;
        for (int iter = 0; iter < TRAINING_ITERATIONS; iter++) {
            double error = 0;
            for (ClassifiedData data : trainingData) {
                //System.out.print(Arrays.toString(network.calculateOutput(seed.asVector())) + " -> ");
                double[] output = network.calculateOutput(data.asVector());
                for (int i = 0; i < output.length; i++) {
                    int shouldBe = data.getClassId() - 1 == i ? 1 : 0;
                    error += (shouldBe - output[i]) * (shouldBe - output[i]);
                }
                network.train(data);
                //System.out.println(Arrays.toString(network.calculateOutput(seed.asVector())));
            }
            if (error > prevError) {
                break;
            }
            prevError = error;
            if (iter % 100 == 0) {
                System.err.println(iter);
                System.err.println("Error = " + error);
                int correct = 0;
                for (ClassifiedData data : testData) {
                    if (data.getClassId() == network.classify(data)) {
                        correct++;
                    }
                }

                System.err.printf("Test correctly = %.2f percents\n", correct * 100.0 / testData.size());

                correct = 0;
                for (ClassifiedData data : trainingData) {
                    if (data.getClassId() == network.classify(data)) {
                        correct++;
                    }
                }

                System.err.printf("Training correctly = %.2f percents\n\n", correct * 100.0 / trainingData.size());
            }
        }


    }
}
