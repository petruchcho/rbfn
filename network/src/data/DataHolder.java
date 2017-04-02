package data;

import java.io.IOException;
import java.util.List;

public class DataHolder<T extends Data> {

    private final DataReader<T> dataReader;
    private List<T> data;

    public DataHolder(DataReader<T> dataReader) {
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

    public List<T> getData() {
        return data;
    }

    public void normalizeData() {
        if (data == null || data.isEmpty()) {
            return;
        }
        int vectorSize = data.get(0).asVector().getSize();
        for (int i = 0; i < vectorSize; i++) {
            double sum = 0;
            for (Data data : this.data) {
                sum += data.getValueAt(i) * data.getValueAt(i);
            }
            sum = Math.sqrt(sum);
            for (Data data : this.data) {
                double value = data.getValueAt(i);
                data.setValueAt(i, value / sum);
            }
        }
    }

    public int getVectorSize() {
        if (data == null || data.isEmpty()) {
            return 0;
        }
        return data.get(0).asVector().getSize();
    }
}
