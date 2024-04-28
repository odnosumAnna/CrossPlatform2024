package beans;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataSheetTable extends JPanel {
    private JTable table;
    private DataSheetTableModel tableModel;
    private DataSheetGraph dataSheetGraph;

    public DataSheetTable() {
        setLayout(new BorderLayout());

        table = new JTable();
        tableModel = new DataSheetTableModel();
        table.setModel(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new FlowLayout(FlowLayout.LEFT, 25, 5));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DataSheetData newData = new DataSheetData();
                tableModel.addDataItem(newData);
                System.out.println("Added: " + newData);
                if (dataSheetGraph != null) {
                    dataSheetGraph.setCustomX(newData.getX());
                    dataSheetGraph.setCustomY(newData.getY());
                }
            }
        });
        panelButtons.add(addButton);

        JButton delButton = new JButton("Delete");
        delButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    DataSheetData deletedData = tableModel.getDataList().get(selectedRow);
                    tableModel.removeDataItem(selectedRow);
                    System.out.println("Deleted: " + deletedData);
                    if (dataSheetGraph != null) {
                        if (selectedRow == 0 && tableModel.getDataList().size() > 0) {
                            DataSheetData nextData = tableModel.getDataList().get(selectedRow);
                            dataSheetGraph.setCustomX(nextData.getX());
                            dataSheetGraph.setCustomY(nextData.getY());
                        } else {
                            dataSheetGraph.setCustomX(0);
                            dataSheetGraph.setCustomY(0);
                        }
                    }
                }
                if (tableModel.getDataList().isEmpty() && dataSheetGraph != null) {
                    dataSheetGraph.setCustomX(0);
                    dataSheetGraph.setCustomY(0);
                }
            }
        });
        panelButtons.add(delButton);

        add(panelButtons, BorderLayout.SOUTH);
    }

    public JTable getTable() {
        return table;
    }

    public void setDataSheet(DataSheet dataSheet) {
        tableModel.setDataSheet(dataSheet);
    }

    public void setDataSheetGraph(DataSheetGraph dataSheetGraph) {
        this.dataSheetGraph = dataSheetGraph;
    }

    public DataSheetGraph getDataSheetGraph() {
        return dataSheetGraph;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("DataSheetTable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new DataSheetTable());
        frame.pack();
        frame.setVisible(true);
    }
}
