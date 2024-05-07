import java.io.Serializable;
import java.math.BigInteger;

public class JobOne implements Executable, Serializable {
    private static final long serialVersionUID = -1L;
    private int number;

    public JobOne(int number) {
        this.number = number;
    }

    @Override
    public Object execute() {
        BigInteger result = factorial(BigInteger.valueOf(number));
        return new ResultImpl(result, 0); // Час виконання встановлюємо на 0
    }

    private BigInteger factorial(BigInteger n) {
        if (n.compareTo(BigInteger.ONE) <= 0) {
            return BigInteger.ONE;
        } else {
            return n.multiply(factorial(n.subtract(BigInteger.ONE)));
        }
    }
}
