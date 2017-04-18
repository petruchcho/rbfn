package data;

import java.io.IOException;
import java.util.List;

public class ObjectDataHolder<T extends Data> implements DataHolder<T> {

    private final DataReader<T> dataReader;
    private List<T> data;

    public ObjectDataHolder(DataReader<T> dataReader) {
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

    @Override
    public List<T> getData() {
        return data;
    }

    @Override
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

    @Override
    public int getVectorSize() {
        if (data == null || data.isEmpty()) {
            return 0;
        }
        return data.get(0).asVector().getSize();
    }
}
