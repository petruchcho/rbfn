package data;

import java.util.List;

public interface DataHolder<T> {
    List<T> getData();

    void normalizeData();

    int getVectorSize();
}
