package engine;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import compute.Compute;

public class ComputeServer {
    public static void main(String[] args) {
        try {
            Compute engine = new ComputeEngineImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Compute", engine);
            System.out.println("Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}