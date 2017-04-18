import data.Data;
import data.DataHolder;
import data.SingleValueDataHolder;
import data.VectorData;
import finance.FinanceReader;
import network.PredictionNetwork;
import network.PredictionNetworkDecorator;
import network.rbfn.RadialBasisFunctionNetwork;
import sinus.SinusReader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class MainPrediction extends JComponent {

    private static final int INTERNAL_NEURONS_COUNT = 9;
    private static final double LEARNING_STEP = 1e-5;
    private static final double INITIAL_Q = 3.5;

    private static final int WINDOW_SIZE = 6;

    private static final int TRAINING_DATA_PERCENT = 90;

    private PredictionNetwork predictionNetwork;

    private DataHolder<Double> dataHolder;
    private List<PredictionData> trainingData = new ArrayList<>();
    private List<PredictionData> testData = new ArrayList<>();

    public MainPrediction() {
        this.predictionNetwork = new PredictionNetworkDecorator(
                new RadialBasisFunctionNetwork(
                        INTERNAL_NEURONS_COUNT,
                        WINDOW_SIZE,
                        1,
                        LEARNING_STEP,
                        INITIAL_Q
                )
        );
        initDataSets();
        initTrainNetwork();
        trainLoop();
    }

    private void trainLoop() {
        trainIteration();
        repaint();
        EventQueue.invokeLater(this::trainLoop);
    }

    private void trainIteration() {
        for (PredictionData predictionData : trainingData) {
            predictionNetwork.train(predictionData.vector, new double[]{predictionData.output});
        }
    }

    private void initTrainNetwork() {
        List<Data> trainDataSet = new ArrayList<>();
        for (PredictionData predictionData : trainingData) {
            trainDataSet.add(predictionData.vector);
        }
        predictionNetwork.initTrain(trainDataSet);
    }

    private void initDataSets() {
        //dataHolder = new SingleValueDataHolder(new SinusReader(1, 2500));
        dataHolder = new SingleValueDataHolder(new FinanceReader(2));
        dataHolder.normalizeData();

        List<Double> trainDataValues = new ArrayList<>();
        List<Double> testDataValues = new ArrayList<>();
        for (Double d : dataHolder.getData()) {
            if (trainDataValues.size() * 100 < dataHolder.getData().size() * TRAINING_DATA_PERCENT) {
                trainDataValues.add(d);
            } else {
                testDataValues.add(d);
            }
        }

        for (int i = 0; i + WINDOW_SIZE < trainDataValues.size() - 1; i++) {
            double[] vector = new double[WINDOW_SIZE];
            for (int j = 0; j < WINDOW_SIZE; j++) {
                vector[j] = trainDataValues.get(i + j);
            }
            trainingData.add(new PredictionData(new VectorData(vector), trainDataValues.get(i + WINDOW_SIZE)));
        }

        //Collections.shuffle(trainingData);

        for (int i = 0; i + WINDOW_SIZE < testDataValues.size() - 1; i++) {
            double[] vector = new double[WINDOW_SIZE];
            for (int j = 0; j < WINDOW_SIZE; j++) {
                vector[j] = testDataValues.get(i + j);
            }
            testData.add(new PredictionData(new VectorData(vector), testDataValues.get(i + WINDOW_SIZE)));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawData(g, Color.BLUE);
        drawNetworkOutputTest(g, Color.RED);
    }

    private void drawNetworkOutputTest(Graphics g, Color color) {
        g.setColor(Color.GREEN);
        int x = WINDOW_SIZE;
        for (PredictionData predictionData : trainingData) {
            int drawX = (int) Math.round(1.0 * x / dataHolder.getData().size() * getWidth());
            double y = predictionNetwork.predictNext(predictionData.vector) + 0.01;
            int drawY = (int) Math.round(y * getHeight() * 10);
            g.drawLine(drawX, drawY, drawX, drawY);
            x++;
        }

        g.setColor(color);
        x = trainingData.size() + WINDOW_SIZE + 1;
        for (PredictionData predictionData : testData) {
            int drawX = (int) Math.round(1.0 * x / dataHolder.getData().size() * getWidth());
            double y = predictionNetwork.predictNext(predictionData.vector) + 0.01;
            int drawY = (int) Math.round(y * getHeight() * 10);
            g.drawLine(drawX, drawY, drawX, drawY);
            x++;
        }
    }

    private void drawData(Graphics g, Color color) {
        g.setColor(color);
        for (int x = 0; x < dataHolder.getData().size(); x++) {
            int drawX = (int) Math.round(1.0 * x / dataHolder.getData().size() * getWidth());
            double y = dataHolder.getData().get(x) + 0.01;
            int drawY = (int) Math.round(y * getHeight() * 10);
            g.drawLine(drawX, drawY, drawX, drawY);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public static void main(String[] args) {
        JComponent main = new MainPrediction();

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(main);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(contentPane);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);
    }

    private static class PredictionData {
        private final Data vector;
        private final double output;

        private PredictionData(Data vector, double output) {
            this.vector = vector;
            this.output = output;
        }
    }
}
