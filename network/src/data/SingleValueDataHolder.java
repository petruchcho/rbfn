package data;

import java.io.IOException;
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
        // TODO
    }

    @Override
    public int getVectorSize() {
        return 1;
    }
}
