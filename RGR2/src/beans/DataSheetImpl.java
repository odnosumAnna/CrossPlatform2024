package beans;

import java.util.ArrayList;
import java.util.List;

public class DataSheetImpl implements DataSheet {
    private List<Data> dataList;

    public DataSheetImpl() {
        this.dataList = new ArrayList<>();
    }

    @Override
    public int size() {
        return dataList.size();
    }

    @Override
    public Data getDataItem(int index) {
        if (index >= 0 && index < dataList.size()) {
            return dataList.get(index);
        }
        return null;
    }

    @Override
    public void addDataItem(Data data) {
        dataList.add(data);
    }
}
