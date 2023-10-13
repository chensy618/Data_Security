package src;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public class PrintClient {
    public static void main(String args[]) throws RemoteException {
        try (Scanner scanner = new Scanner(System.in)) {
            PrintService printService = (PrintService) Naming.lookup("rmi://localhost:1099/PrintService");

            // Collect username and password from the user
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // Authenticate user
            if (printService.authenticate(username, password)) {

                // Use printService to invoke print server operations
                printService.print("example.txt", "printer1");
                printService.queue("printer1");
                printService.topQueue("printer1", 1);
                printService.start();
                printService.stop();
                printService.restart();
                printService.status("printer1");
                printService.readConfig("config1");
                printService.setConfig("config1", "value1");
            } else {
                System.out.println("Authentication failed. Access denied.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
