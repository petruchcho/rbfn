import data.ClassifiedData;
import data.Data;
import data.DataHolder;
import iris.IrisReader;
import network.rbfn.RadialBasisFunctionNetwork;
import seed.SeedReader;

import java.util.*;

public class Main {

    private static final int INTERNAL_NEURONS_COUNT = 9;
    private static final int OUTPUT_NEURONS_COUNT = 3;
    private static final int TRAINING_ITERATIONS = 15000;
    private static final int TRAINING_DATA_PERCENT = 70;

    public static void main(String[] args) {
        DataHolder<? extends ClassifiedData> dataHolder = new DataHolder<>(new SeedReader());
        dataHolder.normalizeData();
        Collections.shuffle(dataHolder.getData());
        RadialBasisFunctionNetwork network = new RadialBasisFunctionNetwork(INTERNAL_NEURONS_COUNT, dataHolder.getVectorSize(), OUTPUT_NEURONS_COUNT);

        network.trainKMeans(dataHolder.getData(), 100);

        ArrayDeque<ClassifiedData>[] seedsByClass = new ArrayDeque[OUTPUT_NEURONS_COUNT + 1];
        for (ClassifiedData data : dataHolder.getData()) {
            if (data.getClassId() <= OUTPUT_NEURONS_COUNT) {
                if (seedsByClass[data.getClassId()] == null) {
                    seedsByClass[data.getClassId()] = new ArrayDeque<>();
                }
                seedsByClass[data.getClassId()].add(data);
            }
        }

        List<ClassifiedData> trainingData = new ArrayList<>();
        while (true) {
            for (int i = 1; i <= OUTPUT_NEURONS_COUNT; i++) {
                trainingData.add(seedsByClass[i].pollFirst());
            }
            if (trainingData.size() >= TRAINING_DATA_PERCENT / 100.0 * dataHolder.getData().size()) {
                break;
            }
        }

        List<ClassifiedData> testData = new ArrayList<>();
        for (int i = 1; i <= OUTPUT_NEURONS_COUNT; i++) {
            while (!seedsByClass[i].isEmpty()) {
                testData.add(seedsByClass[i].pollFirst());
            }
        }

        double bestTraining = 0;
        double bestTest = 0;
        double bestIteration = 0;

        for (int iter = 0; iter < TRAINING_ITERATIONS; iter++) {
            double error = 0;
            Collections.shuffle(trainingData);
            for (ClassifiedData data : trainingData) {
                //System.out.print(Arrays.toString(network.calculateOutput(seed.asVector())) + " -> ");
                double[] output = network.calculateOutput(data.asVector());
                for (int i = 0; i < output.length; i++) {
                    int shouldBe = data.getClassId() - 1 == i ? 1 : 0;
                    error += (shouldBe - output[i]) * (shouldBe - output[i]);
                }
                network.train(data);
                //System.out.println(Arrays.toString(network.calculateOutput(data.asVector())));
            }
//            if (error > prevError) {
//                break;
//            }

            if (iter % 100 == 0) {
                System.err.println(iter);
                System.err.println("Error = " + error);
                int correctTrain = 0;
                for (ClassifiedData data : trainingData) {
                    if (data.getClassId() == network.classify(data)) {
                        correctTrain++;
                    }
                }
                System.err.printf("Training correctly = %.2f percents\n", correctTrain * 100.0 / trainingData.size());

                int correctTest = 0;
                for (ClassifiedData data : testData) {
                    if (data.getClassId() == network.classify(data)) {
                        correctTest++;
                    }
                }
                System.err.printf("Test correctly = %.2f percents\n\n", correctTest * 100.0 / testData.size());

                if (correctTest * 100.0 / testData.size() > bestTest) {
                    bestTest = correctTest * 100.0 / testData.size();
                    bestTraining = correctTrain * 100.0 / trainingData.size();
                    bestIteration = iter;
                }
            }
        }

        System.err.printf("Best test = %.2f percents\n Best train = %.2f percents\n Best iteration = %s", bestTest, bestTraining, bestIteration);
    }
}
