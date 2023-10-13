package example;


import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String args[]) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("Client", new Servant());
    }
}
