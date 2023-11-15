package src.accessctrl.register;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import src.accessctrl.FileEncryption;
import src.accessctrl.accesslist.AccessCtrlList;
import src.accessctrl.accesslist.JsonFileHandler;

import javax.crypto.SecretKey;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Register
{
    private static final String USER_FILE = "users_access.txt";
    private static ArrayList<User> registeredUsers;

//    private static List<String> manager_access = Collections.singletonList("print,queue,topQueue,start,stop,restart,status,readConfig,setConfig");
//    private static List<String> technician_access = Collections.singletonList("start,stop,restart,status,readConfig,setConfig");
//    private static List<String> power_access = Collections.singletonList("print,queue,restart,topQueue");
//    private static List<String> ordinary_access = Collections.singletonList("print,queue");


    public static void main(String[] args)
    {
        registeredUsers = readUsersFromFile();
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the Data Security Access Control Application");
        System.out.println("Please register your new user account");
        register(sc);
    }

    public String mainmenu()
    {
        registeredUsers = readUsersFromFile();
        String menu_select;
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the Data Security Access Control Application");
        System.out.println("Please choose [1]:register or [2]:login");
        menu_select = sc.next();
        if (menu_select.equals("1"))
        {
            //System.out.println("The support country：Denmark, format:Country Code(45) + Phone number ");
            //System.out.println("Please input your username: ");
            String reg = register(sc);
            if (reg.equals("true"))
            {
                return "ok";
            }
        }
        else if (menu_select.equals("2"))
        {
            return menu_select;
        }
        else
        {
            System.out.println("The support country：Denmark, format:Country Code(45) + Phone number ");
        }
        return menu_select;
    }


    public static String register(Scanner sc)
    {
        String registerUsername = "";
        String registerPassword = "";
        String registerPassword2 = "";
        String registerRole = "";

        System.out.println("please input the user name：");
        registerUsername = sc.next();
        while (true)
        {

            if (!registerUsername.isEmpty())
            {
                if (registerPassword.equals(""))
                {
                    System.out.println("please input the password：");
                    registerPassword = sc.next();
                }

                if (registerPassword.length() >= 12)
                {
                    System.out.println("please input the password again：");
                    registerPassword2 = sc.next();
                    if (registerPassword.equals(registerPassword2))
                    {
                        while (true)
                        {
                            System.out.println("please choose the register role:[manager],[technician],[power],[ordinary]");
                            registerRole = sc.next();
                            if (registerRole.equals("manager") || registerRole.equals("technician") || registerRole.equals("power") || registerRole.equals("ordinary"))
                            {
                                break;
                            }
                        }
                        break;
                    }
                    else
                    {
                        while (true)
                        {
                            System.out.println("The passwords do not match!");
                            System.out.println("Enter your password again：");
                            registerPassword2 = sc.next();
                            if (registerPassword.equals(registerPassword2))
                            {
                                break;
                            }

                        }

                        break;
                    }
                }
                else
                {
                    while (true)
                    {
                        System.out.println("please input password with 12 digits or more：");
                        registerPassword = sc.next();
                        if (registerPassword.length() >= 12)
                        {
                            break;
                        }
                    }
                }

            }
            else
            {
                while (true)
                {
                    System.out.println("Username should not be empty！");
                    System.out.println("Enter username again：");
                    registerUsername = sc.next();
                    if (!registerUsername.isEmpty())
                    {
                        break;
                    }
                }
            }
        }

        User u = new User(registerUsername, registerPassword, registerRole);
        registeredUsers.add(u);
        writeUsersToFile(registeredUsers);
        writejson(registerUsername, registerRole);
        System.out.println("registration success！");
        SecretKey readKey = FileEncryption.readKeyFromFile("access_secret.key");
        if (readKey != null)
        {
            // 在这里使用读取的密钥进行操作
            //System.out.println("Using private key to encrypted file: " + Arrays.toString(readKey.getEncoded()));
        }
        else
        {
            System.out.println("Failed to read key from file.");
        }
        try
        {
            FileEncryption.encryptFile(".\\users_access.txt", ".\\users_access_encrypted.txt", readKey);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        Path filePath = Paths.get(".\\users_access.txt");

        try
        {
            // Delete the file
            Files.delete(filePath);

        }
        catch (IOException e)
        {
            System.err.println("Failed to delete the file: " + e.getMessage());
        }
        return "true";

        //login(sc);

    }

    public static List<String> getAccess(String role)
    {
        List<String> accessRights = null;
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();

            // Read RBAC policy
            JsonNode rbacPolicyNode = objectMapper.readTree(new File(".\\rbac_policy.json"));

            // Extract role and access rights from RBAC policy
            for (JsonNode roleNode : rbacPolicyNode.get("roles"))
            {
                String role_js = roleNode.get("role").asText();
                if (role.equals(role_js))
                {
                    accessRights = objectMapper.readValue(roleNode.get("access").traverse(), new TypeReference<List<String>>()
                    {
                    });
                    //System.out.println(accessRights);
                    break;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return accessRights;
    }

    public static void writejson(String name, String role)
    {
        List<String> role_access = null;
        //set default access : ordinary
        role_access = getAccess("ordinary");
        JsonFileHandler jsonFileHandler = new JsonFileHandler();
        AccessCtrlList accessCtrlList = new AccessCtrlList(name, role_access, role);
        if (role.equals("manager"))
        {
            role_access = getAccess(role);
            accessCtrlList.setMethod(role_access);
        }
        else if (role.equals("technician"))
        {
            role_access = getAccess(role);
            accessCtrlList.setMethod(role_access);
        }
        else if (role.equals("power"))
        {
            role_access = getAccess(role);
            accessCtrlList.setMethod(role_access);
        }
        else if (role.equals("ordinary"))
        {
            role_access = getAccess(role);
            accessCtrlList.setMethod(role_access);
        }
        accessCtrlList.setName(name);
        accessCtrlList.setRole(role);
        jsonFileHandler.writeToJsonFile(".\\rbac_user.json", accessCtrlList);
    }

    public void login(Scanner sc)
    {
        User u;
        String phone = "";
        String password = "";
        System.out.println("Enter the username: ");
        phone = sc.next();

        for (int i = 0; i < registeredUsers.size(); i++)
        {
            if (phone.equals(registeredUsers.get(i).getUsername()))
            {
                u = registeredUsers.get(i);
                System.out.println("Enter password: ");
                password = sc.next();
                if (password.equals(u.getPassword()))
                {
                    System.out.println("Login Successfully...");
                }
                else
                {
                    while (true)
                    {
                        System.out.println("Wrong password, Try again: ");
                        password = sc.next();
                        if (password.equals(u.getPassword()))
                        {
                            System.out.println("Login Successfully...");
                        }
                    }
                }
            }
        }
    }


    public static boolean isMobile(String mobiles)
    {
        Pattern p = Pattern.compile("45\\d{8}");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static ArrayList<User> readUsersFromFile()
    {
        SecretKey readKey = FileEncryption.readKeyFromFile("access_secret.key");
        if (readKey != null)
        {
            // 在这里使用读取的密钥进行操作
            //System.out.println("Using private key to read user info: " + Arrays.toString(readKey.getEncoded()));
        }
        else
        {
            System.out.println("Failed to read key from file.");
        }
        try
        {
            FileEncryption.decryptFile(".\\users_access_encrypted.txt", ".\\users_access.txt", readKey);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        ArrayList<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(",");
                if (parts.length == 3)
                {
                    String phone = parts[0];
                    String password = parts[1];
                    String role = parts[2];
                    users.add(new User(phone, password, role));
                }
            }
        }
        catch (IOException e)
        {
            // Handle any file reading errors
        }
        Path filePath = Paths.get(".\\users_access.txt");
        try
        {
            // Delete the file
            Files.delete(filePath);
        }
        catch (IOException e)
        {
            System.err.println("Failed to delete the file: " + e.getMessage());
        }
        return users;
    }

    public static void writeUsersToFile(ArrayList<User> users)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE)))
        {
            for (User user : users)
            {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getRole());
                writer.newLine();
            }
        }
        catch (IOException e)
        {
            // Handle any file writing errors
        }
    }

}