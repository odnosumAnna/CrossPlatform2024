package myApplication;

import javax.swing.*;
import java.awt.*;
import beans.DataSheet;
import beans.DataSheetGraph;
import beans.DataSheetTable;
import beans.DataSheetImpl;
import xml.DataSheetToXML;
import xml.SAXRead;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test extends JFrame {
    private DataSheetTable dataSheetTable = null;
    private DataSheet dataSheet = null;

    public Test() {
        setTitle("Java Beans");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600);
        setResizable(false);

        dataSheet = new DataSheetImpl();

        DataSheetGraph dataSheetGraph = new DataSheetGraph();
        dataSheetGraph.setDataSheet(dataSheet);

        dataSheetTable = new DataSheetTable();
        dataSheetTable.setDataSheet(dataSheet);

        dataSheetTable.getTable().getModel().addTableModelListener(e -> {
            dataSheetGraph.repaint();
        });

        add(dataSheetGraph, BorderLayout.CENTER);
        add(dataSheetTable, BorderLayout.WEST);

        JButton exitButton = new JButton("Exit");
        JButton clearButton = new JButton("Clear");
        JButton saveButton = new JButton("Save");
        JButton readButton = new JButton("Read");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("."));

        exitButton.addActionListener(e -> dispose());
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dataSheet = new DataSheetImpl();
                dataSheetTable.setDataSheet(dataSheet);
                dataSheetGraph.setDataSheet(dataSheet);
            }
        });
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(null)) {
                    String fileName = fileChooser.getSelectedFile().getPath();
                    DataSheetToXML dataSheetToXML = new DataSheetToXML();
                    dataSheetToXML.saveXMLData(dataSheetToXML.createDataSheetDOM(dataSheet), fileName);
                    JOptionPane.showMessageDialog(null, "File " + fileName.trim() + " saved!", "Results saved", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        readButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(null)) {
                    String fileName = fileChooser.getSelectedFile().getPath();
                    SAXRead saxRead = new SAXRead();
                    saxRead.readXMLData(fileName);
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 25, 5));
        panel.add(exitButton);
        panel.add(clearButton);
        panel.add(saveButton);
        panel.add(readButton);


        add(panel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Test frame = new Test();
            frame.setVisible(true);
        });
    }
}
