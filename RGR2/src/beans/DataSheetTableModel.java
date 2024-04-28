package beans;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class DataSheetTableModel extends AbstractTableModel {
    private int columnCount = 3;
    private ArrayList<DataSheetData> dataList = new ArrayList<>();
    String[] columnNames = {"Date", "X Value", "Y Value"};
    private DataSheet dataSheet; // Додали поле для DataSheet

    public void addDataItem(DataSheetData data) {
        dataList.add(data);
        fireTableRowsInserted(dataList.size() - 1, dataList.size() - 1);
    }

    public void removeDataItem(int index) {
        dataList.remove(index);
        fireTableRowsDeleted(index, index);
    }

    // Метод для встановлення DataSheet
    public void setDataSheet(DataSheet dataSheet) {
        this.dataSheet = dataSheet;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public int getRowCount() {
        return dataList.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        DataSheetData data = dataList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                data.setDate((String) value);
                break;
            case 1:
                data.setX(Double.parseDouble((String) value));
                break;
            case 2:
                data.setY(Double.parseDouble((String) value));
                break;
            default:
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
        System.out.println("Modified: " + data);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DataSheetData data = dataList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return data.getDate();
            case 1:
                return data.getX();
            case 2:
                return data.getY();
            default:
                return null;
        }
    }

    public ArrayList<DataSheetData> getDataList() {
        return dataList;
    }
}
