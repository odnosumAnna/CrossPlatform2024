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
        // Get selected function index
        int selectedFunctionIndex = comboBoxFunctions.getSelectedIndex();

        // Clear previous data
        seriesFunction.clear();
        seriesDerivative.clear();

        // Plot new data based on selected function
        switch (selectedFunctionIndex) {
            case 0:
                plotFunction1();
                break;
            case 1:
                plotFunction2();
                break;
            case 2:
                plotFunction3();
                break;
            default:
                break;
        }

        updateChartPanel(); // Update chart panel after plotting new data
    }

    private void plotFunction1() {
        double a = 1.0; // Constant for function 1

        for (double x = 1.5; x <= 6.5; x += 0.05) {
            seriesFunction.add(x, f1(a, x));
            seriesDerivative.add(x, df1(a, x));
        }
    }

    private void plotFunction2() {
        double a = 0.5; // Constant for function 2

        for (double x = 1.5; x <= 6.5; x += 0.05) {
            seriesFunction.add(x, f2(a, x));
            seriesDerivative.add(x, df2(a, x));
        }
    }

    private void plotFunction3() {
        try {
            FileListInterpolation interpolator = new FileListInterpolation("TblFunc.dat");

            for (double x = 1.5; x <= 6.5; x += 0.05) {
                seriesFunction.add(x, interpolator.evalf(x));
                seriesDerivative.add(x, NumMethods.der(x, 1.0e-4, interpolator));
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }



    private double f1(double a, double x) {
        return Math.exp(-x * x) * Math.sin(x);
    }

    private double df1(double a, double x) {
        return Math.exp(-x * x) * (Math.cos(x) - 2 * x * Math.sin(x));
    }

    private double f2(double a, double x) {
        return Math.exp(-a * x * x) * Math.sin(x);
    }

    private double df2(double a, double x) {
        return Math.exp(-a * x * x) * (a * Math.cos(x) - 2 * x * Math.sin(x));
    }

    private JFreeChart createChart() {
        seriesFunction = new XYSeries("Function");
        seriesDerivative = new XYSeries("Derivative");

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesFunction);
        dataset.addSeries(seriesDerivative);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Function and Derivative", // chart title
                "X", // x axis label
                "Y", // y axis label
                dataset, // data
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
        );

        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        return chart;
    }

    private void updateChartPanel() {
        // Get chart panel
        Container contentPane = getContentPane();
        ChartPanel chartPanel = null;
        if (contentPane instanceof JPanel) {
            JPanel panelChart = (JPanel) contentPane.getComponent(1);
            if (panelChart.getComponentCount() > 0 && panelChart.getComponent(0) instanceof ChartPanel) {
                chartPanel = (ChartPanel) panelChart.getComponent(0);
            }
        }

        if (chartPanel != null) {
            // Refresh chart panel
            chartPanel.revalidate();
            chartPanel.repaint();
        }
    }
}
