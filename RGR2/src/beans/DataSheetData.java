package beans;

public class DataSheetData {
    private String date;
    private double x;
    private double y;

    public DataSheetData() {
        this.date = "";
        this.x = 0;
        this.y = 0;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "DataSheetData{" +
                "date='" + date + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}

