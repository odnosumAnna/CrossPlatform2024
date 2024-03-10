import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

class Pair {
    private int x, y;

    public Pair() {
        this.x = 0;
        this.y = 0;
    }

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

class CheckNext {
    private Pair origin;
    private Pair position;

    public CheckNext() {
        this.origin = new Pair();
        this.position = new Pair();
    }

    public CheckNext(int x, int y) {
        this.origin = new Pair(x, y);
        this.position = new Pair();
    }

    public CheckNext(Pair origin) {
        this.origin = origin;
        this.position = new Pair();
    }

    public CheckNext(Pair origin, int x, int y) {
        this.origin = origin;
        this.position = new Pair(x, y);
    }

    @Override
    public String toString() {
        return "origin: " + origin + "\nposition: " + position;
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Test Object Creation");

        System.out.println("Object: CheckNext:");
        try (Scanner scanner = new Scanner(System.in)) {
            CheckNext checkNext = createObject(CheckNext.class, scanner);

            System.out.println("List of methods in CheckNext class:");
            listMethods(CheckNext.class);
        }
    }

    private static <T> T createObject(Class<T> clazz, Scanner scanner) {
        try {
            System.out.println("Let Create the same with reflection...");
            System.out.println("The beginning of creation of the " + clazz.getSimpleName() + " object");

            Constructor<?>[] constructors = clazz.getConstructors();
            for (int i = 0; i < constructors.length; i++) {
                System.out.println((i + 1) + "). " + constructors[i]);
            }

            int choice;
            do {
                System.out.print("Input the Number of Constructor [1 ," + constructors.length + "]: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Please enter a valid integer.");
                    scanner.next();
                }
                choice = scanner.nextInt();
            } while (choice < 1 || choice > constructors.length);

            Object[] parameters = createParameters(constructors[choice - 1].getParameterTypes(), scanner);

            return (T) constructors[choice - 1].newInstance(parameters);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("Error creating object: " + e.getMessage());
        }
        return null;
    }

    private static Object[] createParameters(Class<?>[] parameterTypes, Scanner scanner) {
        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            System.out.println("The beginning of creation of the " + parameterTypes[i].getSimpleName() + " object");
            if (parameterTypes[i] == int.class) {
                System.out.print("Input int value: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Please enter a valid integer.");
                    scanner.next();
                }
                parameters[i] = scanner.nextInt();
            } else {
                parameters[i] = createObject(parameterTypes[i], scanner);
            }
            System.out.println("The end of creation of the " + parameterTypes[i].getSimpleName() + " object");
        }
        return parameters;
    }

    private static void listMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }
    }
}
