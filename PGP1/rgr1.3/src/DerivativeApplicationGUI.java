import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.border.EmptyBorder;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.*;

public class DerivativeApplicationGUI extends JFrame {

    private JTextField textFieldFunction;
    private JButton btnPlot;
    private JComboBox<String> comboBoxFunctions;
    private XYSeries seriesFunction;
    private XYSeries seriesDerivative;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DerivativeApplicationGUI frame = new DerivativeApplicationGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public DerivativeApplicationGUI() {
        setResizable(false);
        setTitle("Derivative Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 450);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.NORTH);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel lblFunction = new JLabel("Function:");
        panel.add(lblFunction);

        textFieldFunction = new JTextField();
        textFieldFunction.setText("0.5");
        panel.add(textFieldFunction);
        textFieldFunction.setColumns(10);

        btnPlot = new JButton("Plot");
        btnPlot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotFunctionAndDerivative();
            }
        });
        panel.add(btnPlot);

        comboBoxFunctions = new JComboBox<String>();
        comboBoxFunctions.addItem("Function 1: f(x) = exp(-x^2) * sin(x)");
        comboBoxFunctions.addItem("Function 2: f(x) = exp(-ax^2) * sin(x), where a = 0.5");
        comboBoxFunctions.addItem("Function 3: Tabular function from CSV file");
        comboBoxFunctions.addItem("Function 4: Tabular function from TreeSet");
        comboBoxFunctions.addItem("Function 5: Tabular function from TreeMap");
        comboBoxFunctions.addItem("Function 6: Analytical function");
        panel.add(comboBoxFunctions);

        JPanel panelChart = new JPanel();
        contentPane.add(panelChart, BorderLayout.CENTER);
        panelChart.setLayout(new BorderLayout(0, 0));

        JFreeChart chart = createChart();
        ChartPanel chartPanel = new ChartPanel(chart);
        panelChart.add(chartPanel, BorderLayout.CENTER);
    }

    private void plotFunctionAndDerivative() {
        int selectedFunctionIndex = comboBoxFunctions.getSelectedIndex();

        // Ініціалізуємо серії, якщо вони ще не були створені
        if (seriesFunction == null) {
            seriesFunction = new XYSeries("Function");
        }
        if (seriesDerivative == null) {
            seriesDerivative = new XYSeries("Derivative");
        }

        seriesFunction.clear();
        seriesDerivative.clear();

        switch (selectedFunctionIndex) {
            case 0:
                plotFunction(new Function1());
                break;
            case 1:
                plotFunction(new Function2());
                break;
            case 2:
                try {
                    plotFunction(new TabularFunctionFromCSV("data.csv"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Помилка: Не вдалося прочитати файл CSV", "Помилка", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case 3:
                plotFunction(new TabularFunctionFromTreeSet());
                break;
            case 4:
                plotFunction(new TabularFunctionFromTreeMap());
                break;
            case 5:
                String analyticalFunction = textFieldFunction.getText();
                plotAnalyticalFunction(analyticalFunction);
                break;
            default:
                break;
        }

        updateChartPanel();
    }


    private void plotFunction(FunctionPlotter plotter) {
        try {
            for (double x = 1.5; x <= 6.5; x += 0.05) {
                seriesFunction.add(x, plotter.computeFunction(x));
                seriesDerivative.add(x, plotter.computeDerivative(x));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void plotAnalyticalFunction(String analyticalFunction) {
        String[] parts = analyticalFunction.split("=");
        if (parts.length != 2) {
            return;
        }
        String functionExpression = parts[1].trim();
        ExpressionFunctionPlotter plotter = new ExpressionFunctionPlotter(functionExpression);
        plotFunction(plotter);
    }

    private JFreeChart createChart() {
        seriesFunction = new XYSeries("Function");
        seriesDerivative = new XYSeries("Derivative");

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesFunction);
        dataset.addSeries(seriesDerivative);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Function and Derivative",
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        return chart;
    }

    private void updateChartPanel() {
        Container contentPane = getContentPane();
        ChartPanel chartPanel = null;
        if (contentPane instanceof JPanel) {
            JPanel panelChart = (JPanel) contentPane.getComponent(1);
            if (panelChart.getComponentCount() > 0 && panelChart.getComponent(0) instanceof ChartPanel) {
                chartPanel = (ChartPanel) panelChart.getComponent(0);
            }
        }

        if (chartPanel != null) {
            chartPanel.revalidate();
            chartPanel.repaint();
        }
    }
}

interface FunctionPlotter {
    double computeFunction(double x) throws IOException;
    double computeDerivative(double x) throws IOException;
}

class Function1 implements FunctionPlotter {
    @Override
    public double computeFunction(double x) throws IOException {
        return Math.exp(-x * x) * Math.sin(x);
    }

    @Override
    public double computeDerivative(double x) throws IOException {
        return Math.exp(-x * x) * (Math.cos(x) - 2 * x * Math.sin(x));
    }
}

class Function2 implements FunctionPlotter {
    @Override
    public double computeFunction(double x) throws IOException {
        double a = 0.5;
        return Math.exp(-a * x * x) * Math.sin(x);
    }

    @Override
    public double computeDerivative(double x) throws IOException {
        double a = 0.5;
        return Math.exp(-a * x * x) * (a * Math.cos(x) - 2 * x * Math.sin(x));
    }
}

class TabularFunctionFromCSV implements FunctionPlotter {
    private Map<Double, Double> data;

    public TabularFunctionFromCSV(String filename) throws IOException {
        data = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                data.put(x, y);
            }
        }
        reader.close();
    }

    @Override
    public double computeFunction(double x) throws IOException {
        return data.getOrDefault(x, Double.NaN);
    }

    @Override
    public double computeDerivative(double x) throws IOException {
        return Double.NaN;
    }
}

class TabularFunctionFromTreeSet implements FunctionPlotter {
    private TreeSet<Double> data;

    public TabularFunctionFromTreeSet() {
        data = new TreeSet<>();
        data.add(1.0);
        data.add(2.0);
        data.add(3.0);
    }

    @Override
    public double computeFunction(double x) throws IOException {
        if (data.contains(x)) {
            return 1.0;
        } else {
            return 0.0;
        }
    }

    @Override
    public double computeDerivative(double x) throws IOException {
        return 0.0;
    }
}

class TabularFunctionFromTreeMap implements FunctionPlotter {
    private TreeMap<Double, Double> data;

    public TabularFunctionFromTreeMap() {
        data = new TreeMap<>();
        data.put(1.0, 1.0);
        data.put(2.0, 4.0);
        data.put(3.0, 9.0);
    }

    @Override
    public double computeFunction(double x) throws IOException {
        return data.getOrDefault(x, Double.NaN);
    }

    @Override
    public double computeDerivative(double x) throws IOException {
        return Double.NaN;
    }
}

class ExpressionFunctionPlotter implements FunctionPlotter {
    private String expression;

    public ExpressionFunctionPlotter(String expression) {
        this.expression = expression;
    }

    @Override
    public double computeFunction(double x) throws IOException {
        return evaluateExpression(expression, x);
    }

    @Override
    public double computeDerivative(double x) throws IOException {
        return Double.NaN;
    }

    private double evaluateExpression(String expression, double x) {
        return 0.0;
    }
}
