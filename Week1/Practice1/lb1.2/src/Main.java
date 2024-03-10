import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;

class Check {
    private double x;
    private double y;

    public Check(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double Dist() {
        return Math.sqrt(x * x + y * y);
    }

    public void setRandomData() {
        this.x = Math.random() * 10;
        this.y = Math.random() * 10;
    }

    public void setData(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "x = " + x + "\ny = " + y;
    }
}

public class Main {
    public static void main(String[] args) {
        Check obj = new Check(3.0, 4.0);

        System.out.println("Стан об'єкту:");
        printFields(obj);

        System.out.println("Виклик методу...");
        printMethods(obj);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть порядковий номер методу [1, " + obj.getClass().getDeclaredMethods().length + "]:");
        int choice = scanner.nextInt();

        if (choice >= 1 && choice <= obj.getClass().getDeclaredMethods().length) {
            try {
                Method method = obj.getClass().getDeclaredMethods()[choice - 1];
                if (method.getParameterCount() == 0) {
                    if (method.getReturnType() != void.class) {
                        Object result = method.invoke(obj);
                        System.out.println("Результат виклику методу: " + result);
                    } else {
                        method.invoke(obj);
                        System.out.println("Метод викликаний.");
                    }
                } else {
                    System.out.println("Метод має параметри. Не можливо викликати.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Некоректний вибір методу.");
        }
    }

    private static void printFields(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                System.out.println(field.getType().getName() + " " + field.getName() + " = " + field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printMethods(Object obj) {
        Method[] methods = obj.getClass().getDeclaredMethods();
        int count = 1;
        for (Method method : methods) {
            if (method.getParameterCount() == 0) {
                System.out.println(count + "). " + method.toString());
                count++;
            }
        }
    }
}
