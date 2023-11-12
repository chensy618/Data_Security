package src.accessctrl;

import src.accessctrl.accesslist.JsonFileHandler;
import src.accessctrl.register.Register;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DatabindException;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SSLRMIPrintClient
{
    public static void main(String[] args)
    {
        try
        {
            System.setProperty("javax.net.ssl.trustStore", "./keytool/client_truststore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "Data_Security_Authentication");

            Registry registry = LocateRegistry.getRegistry("localhost");

            PrintService printService = (PrintService) registry.lookup("PrintService");

            Register register = new Register();
            String ret_value = register.mainmenu();
            if (ret_value.equals("ok"))
            {
                printService.initializeUsers();
            }
            while (true)
            {
                // Collect username and password from the user
                System.out.print("Enter username: ");
                Scanner scanner = new Scanner(System.in);
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                // Authenticate user

                if (printService.authenticate(username, password))
                {
                    // Use printService to invoke print server operations
                    while (true) {
                        String user_policy = getRoleFromJson(username);
                        if (user_policy.isEmpty()) {
                            System.out.println("This user policy doesn't exist!");
                        }
                        System.out.println(user_policy);
                        System.out.println("Data Security Menu -- [1]:print,[2]:queue,[3]:topQueue,[4]:start,[5]:stop,[6]:restart,[7]:status,[8]:readConfig,[9]:setConfig,[10]:exit,[h]:help. ");
                        Scanner scaninput = new Scanner(System.in);
                        String option = scaninput.nextLine();
                        // Check if the user has access to the selected operation
                        if (hasAccess(user_policy, option)) {
                            switch (option) {
                                case "1":
                                    printService.print("example.pdf", "printer_DS");
                                    System.out.println("Printing file example.pdf on printer printer_DS");
                                    printService.log(username, "print");
                                    break;
                                case "2":
                                    printService.queue("printer_DS");
                                    System.out.println("Listing print queue for printer printer_DS");
                                    printService.log(username, "queue");
                                    break;
                                case "3":
                                    printService.topQueue("printer_DS", 1);
                                    System.out.println("Moving job 1 to the top of the queue for printer printer_DS");
                                    printService.log(username, "topQueue");
                                    break;
                                case "4":
                                    printService.start();
                                    System.out.println("Starting the print server");
                                    printService.log(username, "start");
                                    break;
                                case "5":
                                    printService.stop();
                                    System.out.println("Stopping the print server");
                                    printService.log(username, "stop");
                                    break;
                                case "6":
                                    printService.restart();
                                    System.out.println("Restarting the print server");
                                    printService.log(username, "restart");
                                    break;
                                case "7":
                                    printService.status("printer_DS");
                                    System.out.println("Printing status of printer printer_DS");
                                    printService.log(username, "status");
                                    break;
                                case "8":
                                    printService.readConfig("config_DS");
                                    System.out.println("Reading config parameter config_DS");
                                    printService.log(username, "readConfig");
                                    break;
                                case "9":
                                    printService.setConfig("config_DS", "value_DS");
                                    System.out.println("Setting config parameter config_DS to value_DS ");
                                    printService.log(username, "setConfig");
                                    break;
                                case "10":
                                    System.out.println("Exit the Data Security Authentication System.");
                                    printService.log(username, "exit");
                                    System.exit(0);
                                case "h":
                                    System.out.println("Data Security Authentication System Help Manual.");
                                    System.out.println("Input the number from 1 to 10, you could get the matched printserver function. ");
                                    break;
                                default:
                                    System.out.println("Wrong command, please input number from 1 to 10.");
                                    printService.log(username, "Wrong command");
                                    break;
                            }
                        } else {
                            System.out.println("Access denied. You do not have permission to perform this operation.");
                        }
                    }
                } else {
                    System.out.println("Authentication failed. Access denied.");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String getRoleFromJson(String username)
    {
        String userInfo = null;
        JsonFileHandler jsonFileHandler = new JsonFileHandler();
        List<Map<String, Object>> users = jsonFileHandler.readUsersFromFile(".//aclist_policy.json");
        for (Map<String, Object> user : users)
        {
            if (!user.get("username").equals(username))
            {
                continue;
            }
            userInfo = String.format("User: %s, Role: %s, Access: %s", user.get("username"), user.get("role"), user.get("access"));
            break;
        }
        return userInfo;
    }

    private static boolean hasAccess(String userInfo, String operation) {
    // Parse the user info and check if the user has access to the specific operation
    // You may need to modify this logic based on the structure of your user info

    if (userInfo == null) {
        return false;
    }

    // Convert the operation to lowercase for case-insensitive comparison
    String lowercaseOperation = operation.toLowerCase();

    // Parse the user info JSON string or use your preferred method
    // Here, assuming a simple JSON structure
    try {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode userInfoNode = objectMapper.readTree(userInfo);

        JsonNode accessList = userInfoNode.get("access");
        if (accessList != null && accessList.isArray()) {
            for (JsonNode access : accessList) {
                if (access.asText().toLowerCase().equals(lowercaseOperation)) {
                    return true;
                }
            }
        }

        return false; // Default to false if no match found
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
}


}
