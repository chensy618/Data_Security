package src;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String args[]) throws NotBoundException, MalformedURLException, RemoteException
    {
        Service service = (Service) Naming.lookup("rmi://localhost:1099/Client");
        System.out.println("---" + service.echo("Hey Server !"));
    }
}    

