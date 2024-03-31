import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
//import org.jfree.ui.RectangleInsets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;

public class DerivativeApplication {

    public static void main(String[] args) throws IOException {
        Evaluatable functs[] = new Evaluatable[3];
        functs[0] = new FFunction(0.5);
        functs[1] = new FileListInterpolation();
        functs[2] = new FileListInterpolation("TblFunc.dat"); // Виправлено цей рядок
        try {
            ((FileListInterpolation)functs[2]).readFromFile("TblFunc.dat");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        String fileName = "";
        for (Evaluatable f: functs) {
            System.out.println("Функція: " + f.getClass().getSimpleName());
            fileName = f.getClass().getSimpleName() + ".dat";
            PrintWriter out = new PrintWriter(new FileWriter(fileName));
            for (double x = 1.5; x <= 6.5; x += 0.05) {
                System.out.println("x: " + x + "\tf: " + f.evalf(x) + "\tf': " +
                        NumMethods.der(x, 1.0e-4, f));
                out.printf("%16.6e%16.6e%16.6e\n", x, f.evalf(x),
                        NumMethods.der(x, 1.0e-4, f));
            }
            System.out.println("\n");
            out.close();
        }
    }
}

interface Evaluatable {
    double evalf(double x);
}

class FFunction implements Evaluatable {
    private double a;

    public FFunction(double a) {
        this.a = a;
    }

    public FFunction() {
        this(1.0);
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    @Override
    public double evalf(double x) {
        return Math.exp(-a * x * x) * Math.sin(x);
    }
}

class NumMethods {
    private NumMethods() {
    }

    private static double meth(double x, double h, Evaluatable f) {
        return 0.5 * (f.evalf(x + h) - f.evalf(x - h)) / h;
    }

    public static double der(double x, double tol, Evaluatable f) {
        final int MAX = 100;
        double h = 0.1;
        double one = meth(x, h, f);
        h = 0.1 * h;
        double two = meth(x, h, f);
        int i = 0;
        double tmp;
        boolean ok;
        do {
            h = 0.1 * h;
            tmp = meth(x, h, f);
            ok = (Math.abs(tmp - two) >= Math.abs(two - one)) ||
                    (Math.abs(two - one) < tol);
            if (i > MAX) {
                System.out.print("Занадто багато кроків обчислень");
                System.exit(-1);
            }
            i += 1;
            one = two;
            two = tmp;
        } while (!ok);
        return two;
    }
}

class Point {
    private double[] coords = null;

    public Point(int num) {
        this.coords = new double[num];
    }

    public void setCoord(int num, double x) {
        coords[num - 1] = x;
    }

    public double getCoord(int num) {
        return coords[num - 1];
    }

    @Override
    public String toString() {
        String res = "(";
        for (double x : coords) {
            res += x + ", ";
        }
        return res.substring(0, res.length() - 2) + ")";
    }
}

class Point2D extends Point implements Comparable<Point2D> {
    public Point2D(double x, double y) {
        super(2);
        setCoord(1, x);
        setCoord(2, y);
    }

    public Point2D() {
        this(0, 0);
    }

    public double getX() {
        return getCoord(1);
    }

    public void setX(double x) {
        setCoord(1, x);
    }

    public double getY() {
        return getCoord(2);
    }

    public void setY(double y) {
        setCoord(2, y);
    }

    @Override
    public int compareTo(Point2D pt) {
        return Double.compare(getX(), pt.getX());
    }
}

abstract class Interpolator implements Evaluatable {
    abstract public void clear();

    abstract public int numPoints();

    abstract public void addPoint(Point2D pt);

    abstract public Point2D getPoint(int i);

    abstract public void setPoint(int i, Point2D pt);

    abstract public void removeLastPoint();

    abstract public void sort();

    @Override
    public double evalf(double x) {
        double res = 0.0;
        int numData = numPoints();
        double numer, denom;
        for (int k = 0; k < numData; k++) {
            numer = 1.0;
            denom = 1.0;
            for (int j = 0; j < numData; j++) {
                if (j != k) {
                    numer = numer * (x - getPoint(j).getX());
                    denom = denom * (getPoint(k).getX() - getPoint(j).getX());
                }
            }
            res = res + getPoint(k).getY() * numer / denom;
        }
        return res;
    }
}

class ListInterpolation extends Interpolator {
    private List<Point2D> data = null;

    public ListInterpolation(List<Point2D> data) {
        super();
        this.data = data;
    }

    public ListInterpolation() {
        super();
        data = new ArrayList<Point2D>();
    }

    public ListInterpolation(Point2D[] data) {
        this();
        for (Point2D pt : data)
            this.data.add(pt);
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public int numPoints() {
        return data.size();
    }

    @Override
    public void addPoint(Point2D pt) {
        data.add(pt);
    }

    @Override
    public Point2D getPoint(int i) {
        return data.get(i);
    }

    @Override
    public void setPoint(int i, Point2D pt) {
        data.set(i, pt);
    }

    @Override
    public void removeLastPoint() {
        data.remove(data.size() - 1);
    }

    @Override
    public void sort() {
        Collections.sort(data);
    }
}

class FileListInterpolation extends ListInterpolation {
    public FileListInterpolation(String filename) throws IOException {
        super();
        readFromFile(filename);
    }

    public FileListInterpolation() {
        super();
    }

    public void readFromFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        StringTokenizer st;
        while ((line = br.readLine()) != null) {
            st = new StringTokenizer(line);
            Point2D pt = new Point2D(Double.parseDouble(st.nextToken()),
                    Double.parseDouble(st.nextToken()));
            addPoint(pt);
        }
        br.close();
    }
}






