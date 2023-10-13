package src;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

public class PrintServer {
    public static void main(String args[]) throws RemoteException {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("PrintService", new PrintServant());
            System.out.println("PrintService bound and ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
