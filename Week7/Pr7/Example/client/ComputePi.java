package client;

import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import compute.Compute;

public class ComputePi {
    public static void main(String args[]) {
        if (args.length < 2) {
            System.err.println("Usage: java ComputePi <hostname> <precision>");
            return;
        }

        try {
            Registry registry = LocateRegistry.getRegistry(args[0]);
            Compute compute = (Compute) registry.lookup("Compute");

            int precision = Integer.parseInt(args[1]);
            Pi task = new Pi(precision);
            BigDecimal pi = compute.executeTask(task);

            System.out.println("Pi: " + pi);
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
    }
}
