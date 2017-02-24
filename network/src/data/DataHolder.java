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
}
