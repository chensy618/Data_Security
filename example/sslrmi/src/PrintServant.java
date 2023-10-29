import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class PrintServant extends UnicastRemoteObject implements PrintService {

    private Map<String, String> userPasswords;
    private static String record_file = "./systemlog.txt";
    private static PrintWriter pw = null;

    public PrintServant() throws RemoteException
    {
        super();
        initializeUsers();
    }

    private void initializeUsers()
    {
        // In a real application, you might read this data from a secure configuration or database
        userPasswords = new HashMap<>();
        userPasswords.put("user1", hashPassword("password1"));
        userPasswords.put("user2", hashPassword("password2"));
        // Add more users as needed
    }

    private String hashPassword(String password)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : hashedBytes)
            {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean authenticate(String username, String password) throws RemoteException
    {
        String storedPassword = userPasswords.get(username);

        if (storedPassword != null)
        {
            String hashedInput = hashPassword(password);
            return hashedInput != null && hashedInput.equals(storedPassword);
        }

        return false;
    }

    @Override
    public void print(String filename, String printer) throws RemoteException
    {
        System.out.println("Printing file " + filename + " on printer " + printer);
        // Implement print functionality or log the operation
    }

    @Override
    public void queue(String printer) throws RemoteException
    {
        System.out.println("Listing print queue for printer " + printer);
        // Implement queue functionality or log the operation
    }

    @Override
    public void topQueue(String printer, int job) throws RemoteException
    {
        System.out.println("Moving job " + job + " to the top of the queue for printer " + printer);
        // Implement topQueue functionality or log the operation
    }

    @Override
    public void start() throws RemoteException
    {
        System.out.println("Starting the print server");
        // Implement start functionality or log the operation
    }

    @Override
    public void stop() throws RemoteException
    {
        System.out.println("Stopping the print server");
        // Implement stop functionality or log the operation
    }

    @Override
    public void restart() throws RemoteException
    {
        System.out.println("Restarting the print server");
        // Implement restart functionality or log the operation
    }

    @Override
    public void status(String printer) throws RemoteException
    {
        System.out.println("Printing status of printer " + printer);
        // Implement status functionality or log the operation
    }

    @Override
    public void readConfig(String parameter) throws RemoteException
    {
        System.out.println("Reading config parameter: " + parameter);
        // Implement readConfig functionality or log the operation
    }

    @Override
    public void setConfig(String parameter, String value) throws RemoteException
    {
        System.out.println("Setting config parameter " + parameter + " to value " + value);
        // Implement setConfig functionality or log the operation
    }


    public void log(String print) throws RemoteException
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(record_file, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            pw = new PrintWriter(osw);
            pw.println("system call : " + print);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            pw.close();
        }
    }

}
