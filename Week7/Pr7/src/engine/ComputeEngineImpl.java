package engine;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import compute.Compute;
import compute.Task;

public class ComputeEngineImpl extends UnicastRemoteObject implements Compute {
    public ComputeEngineImpl() throws RemoteException {
        super();
    }

    @Override
    public <T> T executeTask(Task<T> t) throws RemoteException {
        return t.execute();
    }

    public static void main(String[] args) {
        try {
            Compute engine = new ComputeEngineImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Compute", engine);
            System.out.println("ComputeEngine is ready to work");
        } catch (RemoteException e) {
            System.err.println("ComputeEngine exception:");
            e.printStackTrace();
        }
    }
}
