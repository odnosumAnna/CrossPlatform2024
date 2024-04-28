package beans;

import java.util.ArrayList;

public class ArrayListDataSheet {
    private ArrayList<Data> dataList = new ArrayList<>();

    public int size() {
        return dataList.size();
    }

    public Data getDataItem(int index) {
        return dataList.get(index);
    }

    public void addDataItem(Data data) {
        dataList.add(data);
    }
}
