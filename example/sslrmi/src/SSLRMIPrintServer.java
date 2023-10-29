import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.security.*;
import java.security.cert.CertificateException;
import javax.net.ssl.*;

public class SSLRMIPrintServer
{
    public static void main(String[] args)
    {
        try
        {
            System.setProperty("javax.net.ssl.keyStore", "server_keystore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "Data_Security_Authentication");

            // Load the keystore
            char[] keystorePassword = "Data_Security_Authentication".toCharArray();
            char[] keyPassword = "Data_Security_Authentication".toCharArray();
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("server_keystore.jks"), keystorePassword);

            // Set up the key manager factory
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, keyPassword);

            // Set up the SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            // Create SSLServerSocket
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(8899);

            // Accept client connections
            SSLSocket socket = (SSLSocket) serverSocket.accept();

            // Set up input and output streams
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Read client message
            String clientMessage = reader.readLine();
            System.out.println("Server received message: " + clientMessage);

            // Send a response back to the client
            String response = "Server : This is the message from server!";
            writer.write(response);
            writer.newLine();
            writer.flush();


            // Close resources
            socket.close();
            serverSocket.close();

            Registry registry = LocateRegistry.createRegistry(1099);

            // Use SSLServerSocketFactory to create SSL sockets
            //SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

            PrintService printserver = new PrintServant();
            registry.rebind("PrintService", printserver);

            System.out.println("Server is running...");

        }
        catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException |
               UnrecoverableKeyException | KeyManagementException e)
        {
            e.printStackTrace();
        }
    }
}