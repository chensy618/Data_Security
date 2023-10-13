package src;

import java.rmi.RemoteException;
import java.rmi.Remote;

public interface Service extends Remote {
    public String echo(String input) throws RemoteException;
}
