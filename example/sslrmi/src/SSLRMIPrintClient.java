import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMISocketFactory;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Scanner;

public class SSLRMIPrintClient
{
    public static void main(String[] args)
    {
        try
        {
            System.setProperty("javax.net.ssl.trustStore", "client_truststore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "Data_Security_Authentication");

            // Create SSL socket factory
            //SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();


            // Load the truststore
            char[] truststorePassword = "Data_Security_Authentication".toCharArray();
            KeyStore truststore = KeyStore.getInstance("JKS");
            truststore.load(new FileInputStream("client_truststore.jks"), truststorePassword);

            // Set up the trust manager factory
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(truststore);

            // Set up the SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            // Create SSLSocket
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket("localhost", 8899);

            // Set up input and output streams
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Collect username and password from the user
            System.out.print("Enter username: ");
            Scanner scanner = new Scanner(System.in);
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // Send a message to the server
            String message = username + password;
            writer.write(message);
            writer.newLine();
            writer.flush();

            // Read server response
            String serverResponse = reader.readLine();
            System.out.println("Client received response: " + serverResponse);

            // Close resources
            socket.close();

            Registry registry = LocateRegistry.getRegistry("localhost");
            PrintService printService = (PrintService) registry.lookup("PrintService");

            // Collect username and password from the user
//            System.out.print("Enter username: ");
//            Scanner scanner = new Scanner(System.in);
//            String username = scanner.nextLine();
//            System.out.print("Enter password: ");
//            String password = scanner.nextLine();

            // Authenticate user
            if (printService.authenticate(username, password))
            {

                // Use printService to invoke print server operations
                while (true)
                {
                    System.out.println("Data Security Menu -- [1]:print,[2]:queue,[3]:topQueue,[4]:start,[5]:stop,[6]:restart,[7]:status,[8]:readConfig,[9]:setConfig,[10]:exit,[h]:help. ");
                    Scanner scaninput = new Scanner(System.in);
                    String option = scaninput.nextLine();
                    switch (option)
                    {
                        case "1":
                            printService.print("example.pdf", "printer_DS");
                            System.out.println("Printing file example.pdf on printer printer_DS");
                            printService.log("print");
                            break;
                        case "2":
                            printService.queue("printer_DS");
                            System.out.println("Listing print queue for printer printer_DS");
                            printService.log("queue");
                            break;
                        case "3":
                            printService.topQueue("printer_DS", 1);
                            System.out.println("Moving job 1 to the top of the queue for printer printer_DS");
                            printService.log("topQueue");
                            break;
                        case "4":
                            printService.start();
                            System.out.println("Starting the print server");
                            printService.log("start");
                            break;
                        case "5":
                            printService.stop();
                            System.out.println("Stopping the print server");
                            printService.log("stop");
                            break;
                        case "6":
                            printService.restart();
                            System.out.println("Restarting the print server");
                            printService.log("restart");
                            break;
                        case "7":
                            printService.status("printer_DS");
                            System.out.println("Printing status of printer printer_DS");
                            printService.log("status");
                            break;
                        case "8":
                            printService.readConfig("config_DS");
                            System.out.println("Reading config parameter config_DS");
                            printService.log("readConfig");
                            break;
                        case "9":
                            printService.setConfig("config_DS", "value_DS");
                            System.out.println("Setting config parameter config_DS to value_DS ");
                            printService.log("setConfig");
                            break;
                        case "10":
                            System.out.println("Exit the Data Security Authentication System.");
                            printService.log("exit");
                            System.exit(0);
                        case "h":
                            System.out.println("Data Security Authentication System Help Manual.");
                            System.out.println("Input the number from 1 to 10, you could get the matched printserver function. ");
                            break;
                        default:
                            System.out.println("Wrong command, please input number from 1 to 10.");
                            printService.log("exit");
                            break;
                    }
                }
            }
            else
            {
                System.out.println("Authentication failed. Access denied.");
            }
        }
        catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException |
               KeyManagementException | NotBoundException e)
        {
            e.printStackTrace();
        }
    }
}