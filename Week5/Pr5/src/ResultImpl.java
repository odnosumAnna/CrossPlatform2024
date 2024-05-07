import java.io.Serializable;
import java.math.BigInteger;

public class ResultImpl implements Result, Serializable {
    private static final long serialVersionUID = -1L;
    private BigInteger output;
    private double scoreTime;

    public ResultImpl(BigInteger output, double scoreTime) {
        this.output = output;
        this.scoreTime = scoreTime;
    }

    @Override
    public Object output() {
        return output;
    }

    @Override
    public double scoreTime() {
        return scoreTime;
    }
}
