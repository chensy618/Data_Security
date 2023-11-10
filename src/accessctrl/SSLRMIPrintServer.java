package src.accessctrl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import javax.crypto.SecretKey;
import javax.net.ssl.*;

public class SSLRMIPrintServer
{
    public static SecretKey secret_key;

    public static void main(String[] args)
    {
        try
        {
            System.setProperty("javax.net.ssl.keyStore", "./keytool/server_keystore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "Data_Security_Authentication");

            try
            {
                SecretKey former_key = FileEncryption.readKeyFromFile("secret.key");
                if (former_key != null)
                {
                    // using private key : TO DO
                    //System.out.println("Using private key to read user info: " + Arrays.toString(former_key.getEncoded()));
                }
                else
                {
                    System.out.println("Failed to read key from file.");
                }

                FileEncryption.decryptFile(".\\encrypted.txt", ".\\users.txt", former_key);
                secret_key = FileEncryption.generateSecretKey();
                FileEncryption.saveKeyToFile(secret_key, "secret.key");
                FileEncryption.encryptFile(".\\users.txt", ".\\encrypted.txt", secret_key);
                //System.out.println("create private aes key : " + secret_key);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }

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
