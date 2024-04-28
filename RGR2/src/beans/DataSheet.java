package beans;

public interface DataSheet {
    int size();
    Data getDataItem(int index);
    void addDataItem(Data data);
}

