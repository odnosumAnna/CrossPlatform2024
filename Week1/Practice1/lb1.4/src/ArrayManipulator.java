import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

public class ArrayManipulator {

    private static final Random random = new Random();

    public static Object createArray(Class<?> type, int size) {
        Object array = Array.newInstance(type, size);
        if (type.isPrimitive()) {
            for (int i = 0; i < size; i++) {
                if (type == int.class) {
                    Array.setInt(array, i, random.nextInt(100));
                } else if (type == double.class) {
                    Array.setDouble(array, i, random.nextDouble() * 100);
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                Array.set(array, i, createRandomObject(type));
            }
        }
        return array;
    }


    private static Object createRandomObject(Class<?> type) {
        if (type == String.class) {
            return "RandomString" + random.nextInt(100);
        }
        return null;
    }


    public static Object resizeArray(Object array, int newSize) {
        Class<?> type = array.getClass().getComponentType();
        Object newArray = Array.newInstance(type, newSize);
        int length = Math.min(Array.getLength(array), newSize);
        System.arraycopy(array, 0, newArray, 0, length);
        return newArray;
    }

    public static String arrayToString(Object array) {
        return Arrays.deepToString(new Object[]{array});
    }

    public static String matrixToString(Object matrix) {
        return Arrays.deepToString((Object[]) matrix);
    }

    public static void main(String[] args) {

        int[] intArray = (int[]) createArray(int.class, 2);
        System.out.println(arrayToString(intArray));

        String[] stringArray = (String[]) createArray(String.class, 3);
        System.out.println(arrayToString(stringArray));

        Double[] doubleArray = (Double[]) createArray(Double.class, 5);
        System.out.println(arrayToString(doubleArray));

        int[][] intMatrix = (int[][]) createArray(int[].class, 3);
        for (int i = 0; i < intMatrix.length; i++) {
            intMatrix[i] = (int[]) createArray(int.class, 5);
        }
        System.out.println(matrixToString(intMatrix));

        intMatrix = (int[][]) resizeArray(intMatrix, 4);
        System.out.println(matrixToString(intMatrix));

        intMatrix = (int[][]) resizeArray(intMatrix, 3);
        System.out.println(matrixToString(intMatrix));
    }
}
