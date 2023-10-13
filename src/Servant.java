package src;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Servant  extends UnicastRemoteObject implements Service {

    public Servant() throws RemoteException{
        super();
    }

    public String echo(String input) throws RemoteException{
        return "From Server: " + input;
    }

}
