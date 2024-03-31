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
        comboBoxFunctions.addItem("Function 3: Tabular function from file");
        panel.add(comboBoxFunctions);

        JPanel panelChart = new JPanel();
        contentPane.add(panelChart, BorderLayout.CENTER);
        panelChart.setLayout(new BorderLayout(0, 0));

        // Create chart
        JFreeChart chart = createChart();
        ChartPanel chartPanel = new ChartPanel(chart);
        panelChart.add(chartPanel, BorderLayout.CENTER);
    }

    private void plotFunctionAndDerivative() {
        int selectedFunctionIndex = comboBoxFunctions.getSelectedIndex();

        seriesFunction.clear();
        seriesDerivative.clear();

        try {
            switch (selectedFunctionIndex) {
                case 0:
                    plotFunction(new Function1());
                    break;
                case 1:
                    plotFunction(new Function2());
                    break;
                case 2:
                    plotFunction(new TabularFunctionFromFile("TblFunc.dat"));
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        double a = 0.5; // Constant for function 2
        return Math.exp(-a * x * x) * Math.sin(x);
    }

    @Override
    public double computeDerivative(double x) throws IOException {
        double a = 0.5; // Constant for function 2
        return Math.exp(-a * x * x) * (a * Math.cos(x) - 2 * x * Math.sin(x));
    }
}

class TabularFunctionFromFile implements FunctionPlotter {
    private FileListInterpolation interpolator;

    public TabularFunctionFromFile(String filename) throws IOException {
        interpolator = new FileListInterpolation(filename);
    }

    @Override
    public double computeFunction(double x) throws IOException {
        return interpolator.evalf(x);
    }

    @Override
    public double computeDerivative(double x) throws IOException {
        return NumMethods.der(x, 1.0e-4, interpolator);
    }
}
