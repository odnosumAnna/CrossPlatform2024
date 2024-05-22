package client;

import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import compute.Compute;
import compute.Task;

public class ComputeClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            Compute compute = (Compute) registry.lookup("Compute");

            int precision = 1000;
            Task<BigDecimal> piTask = new Pi(precision);
            BigDecimal pi = compute.executeTask(piTask);

            System.out.println("Pi: " + pi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
