package data;

public interface Data {
    Vector asVector();

    double getValueAt(int index);

    void setValueAt(int index, double value);
}
