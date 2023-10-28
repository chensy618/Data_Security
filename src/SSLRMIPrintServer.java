package src;

import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.*;
import java.security.cert.CertificateException;
import javax.net.ssl.*;

public class SSLRMIPrintServer
{
    public static void main(String[] args)
    {
        try
        {
            System.setProperty("javax.net.ssl.keyStore", "keytool/server_keystore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "Data_Security_Authentication");

            Registry registry = LocateRegistry.createRegistry(1099);

            //Use SSLServerSocketFactory to create SSL sockets
            SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslsocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(8000);

            PrintService printserver = new PrintServant();
            registry.rebind("PrintService", printserver);
            System.out.println("SSLRMIPrintServer is running...PrintService bound and ready.");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
