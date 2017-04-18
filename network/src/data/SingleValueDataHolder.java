package data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SingleValueDataHolder implements DataHolder<Double> {

    private final DataReader<Double> dataReader;
    private List<Double> data;

    public SingleValueDataHolder(DataReader<Double> dataReader) {
        this.dataReader = dataReader;
        readData();
    }

    private void readData() {
        try {
            data = dataReader.readData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Double> getData() {
        return data;
    }

    @Override
    public void normalizeData() {
        double min = data.get(0);
        for (Double x : data) {
            min = Math.min(min, x);
        }

        List<Double> normData = new ArrayList<>();
        double sum = 0;
        for (Double x : data) {
            normData.add(x - min);
            sum += (x - min) * (x - min);
        }
        sum = Math.sqrt(sum);
        data.clear();
        for (Double x : normData) {
            data.add(x / sum); // should be max value
        }
    }

    @Override
    public int getVectorSize() {
        return 1;
    }
}
