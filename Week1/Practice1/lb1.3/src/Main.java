import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

class FunctionNotFoundException extends Exception {
    public FunctionNotFoundException(String message) {
        super(message);
    }
}

class TestClass {
    public double testMethod(double a) {
        return Math.exp(-Math.abs(a)) * Math.sin(a);
    }

    public double testMethod(double a, int x) {
        return Math.exp(-Math.abs(a) * x) * Math.sin(x);
    }
}

public class Main {
    public static void main(String[] args) {
        TestClass obj = new TestClass();
        String methodName = "testMethod";
        List<Object> params = Arrays.asList(1.0, 1);

        try {
            Object result = callMethod(obj, methodName, params.toArray());

            System.out.println(obj.getClass().getSimpleName() + " [a=1.0, exp(-abs(a)*x)*sin(x)]");
            System.out.println("Типи: " + Arrays.toString(getParameterTypes(params)));
            System.out.println("Значення: " + params);
            System.out.println("Результат виклику: " + result);
        } catch (FunctionNotFoundException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }

    private static Object callMethod(Object obj, String methodName, Object[] params)
            throws FunctionNotFoundException, IllegalAccessException, InvocationTargetException {
        Method[] methods = obj.getClass().getMethods();

        for (Method method : methods) {
            if (method.getName().equals(methodName) && method.getParameterCount() == params.length) {

                Class<?>[] parameterTypes = method.getParameterTypes();
                boolean typesMatch = true;
                for (int i = 0; i < params.length; i++) {

                    if (!isParameterTypeMatch(parameterTypes[i], params[i])) {
                        typesMatch = false;
                        break;
                    }
                }
                if (typesMatch) {
                    return method.invoke(obj, params);
                }
            }
        }
        throw new FunctionNotFoundException("Метод " + methodName + " з заданими параметрами не знайдено");
    }

    private static boolean isParameterTypeMatch(Class<?> parameterType, Object param) {
        if (parameterType.isPrimitive()) {
            if (parameterType == int.class && param instanceof Integer) {
                return true;
            } else if (parameterType == double.class && param instanceof Double) {
                return true;
            }
            return false;
        } else {
            return parameterType.isInstance(param);
        }
    }

    private static Object[] getParameterTypes(List<Object> params) {
        return params.stream()
                .map(param -> {
                    if (param instanceof Integer) {
                        return "int";
                    } else if (param instanceof Double) {
                        return "double";
                    } else {
                        return "Unknown";
                    }
                })
                .toArray();
    }
}
