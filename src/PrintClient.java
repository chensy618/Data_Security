package src;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public class PrintClient
{

    public static void main(String args[]) throws RemoteException
    {
        try (Scanner scanner = new Scanner(System.in))
        {
            PrintService printService = (PrintService) Naming.lookup("rmi://localhost:1099/PrintService");

            // Collect username and password from the user
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

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
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
