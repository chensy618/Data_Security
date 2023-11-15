package src.accessctrltask1;

import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.crypto.SecretKey;
import javax.net.ssl.*;

public class SSLRMIPrintServer
{
    public static SecretKey secret_key;

    public static void main(String[] args)
    {
        try
        {
            System.setProperty("javax.net.ssl.keyStore", ".\\keytool\\server_keystore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "Data_Security_Authentication");

            try
            {
                SecretKey former_key = FileEncryption.readKeyFromFile(".\\src\\accessctrltask1\\access_secret.key");
                if (former_key != null)
                {
                    // using private key : TO DO
                    //System.out.println("Using private key to read user info: " + Arrays.toString(former_key.getEncoded()));
                }
                else
                {
                    System.out.println("Failed to read key from file.");
                }
                FileEncryption.decryptFile(".\\src\\accessctrltask1\\access_encrypted.txt", ".\\src\\accessctrltask1\\access_task1.txt", former_key);
                secret_key = FileEncryption.generateSecretKey();
                FileEncryption.saveKeyToFile(secret_key, ".\\src\\accessctrltask1\\access_secret.key");
                FileEncryption.encryptFile(".\\src\\accessctrltask1\\access_task1.txt", ".\\src\\accessctrltask1\\access_encrypted.txt", secret_key);

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

    public static boolean checkFileExists(String filePath)
    {
        File file = new File(filePath);
        return file.exists();
    }

    public static boolean createFile(String filePath) throws IOException
    {
        // 创建File对象
        File file = new File(filePath);

        // 检查文件是否已存在
        if (file.exists())
        {
            return false; // 文件已存在，无法创建
        }

        // 创建新文件
        return file.createNewFile();
    }
}
