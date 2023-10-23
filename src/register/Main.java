import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Main {
    private static final String USER_FILE = "users.txt";
    private static ArrayList<User> registeredUsers;
    public static void main(String[] args)
    {
        System.out.println(USER_FILE);
        System.out.println(System.getProperty("user.dir"));
        registeredUsers = readUsersFromFile();
        for (User user : registeredUsers) {
            System.out.println(user);
        }
        String menu_select;
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the Data Security Authentication Application");
        System.out.println("Please choose [1]:register or [2]:login");
        menu_select=sc.next();
        if(menu_select.equals("1"))
        {
            System.out.println("The support country：Denmark, format:Country Code(45) + Phone number ");
            register(sc);
        }
        else if (menu_select.equals("2"))
        {
            login(sc);
        }
        else
        {
            System.exit(0);
        }
    }


    public static void register(Scanner sc)
    {


        String registerPhone = "";
        String registerPassword = "";
        String registerPassword2 = "";

        System.out.println("please input the mobile phone number：");
        registerPhone = sc.next();
        while(true) {

            if (registerPhone.length() == 10 && isMobile(registerPhone)) {
                if(registerPassword.equals("")) {
                    System.out.println("please input the password：");
                    registerPassword = sc.next();
                }

                if (registerPassword.length() >= 6) {
                    System.out.println("please input the password again：");
                    registerPassword2 = sc.next();
                    if(registerPassword.equals(registerPassword2)){
                       break;
                    }
                    else{
                        while (true) {
                        System.out.println("The passwords do not match!");
                        System.out.println("Enter your password again：");
                        registerPassword2 = sc.next();
                        if(registerPassword.equals(registerPassword2))
                           break;

                    }
                        break;
                    }
                } else {
                    while(true) {
                        System.out.println("please input password with 6 digits or more：");
                        registerPassword = sc.next();
                        if(registerPassword.length() >= 6)
                            break;
                    }
                }

            } else {
                while(true) {
                    System.out.println("wrong phone number format！");
                    System.out.println("Enter phone number again：");
                    registerPhone = sc.next();
                    if(registerPhone.length() == 10 && isMobile(registerPhone))
                        break;
                }
            }
        }

        User u = new User(registerPhone, registerPassword);
        registeredUsers.add(u);
        writeUsersToFile(registeredUsers);
        System.out.println("registration success！");

        login(sc);


    }


    public static void login(Scanner sc)
    {
        User u;
        String phone = "";
        String password = "";
        System.out.println("Enter Phone Number: ");
        phone=sc.next();

        for(int i=0;i<registeredUsers.size();i++){
            if(phone.equals(registeredUsers.get(i).getPhone())){
                u=registeredUsers.get(i);
                System.out.println("Enter password: ");
                password=sc.next();
                if(password.equals(u.getPassword())){
                    System.out.println("Login Successfully...");
                    System.exit(0);
                }
                else{
                    while(true){
                        System.out.println("Wrong password, Try again: ");
                        password=sc.next();
                        if(password.equals(u.getPassword())){
                            System.out.println("Login Successfully...");
                            System.exit(0);
                        }
                    }
                }
            }
        }
        System.out.println("User not found");
    }


    public static boolean isMobile(String mobiles)
    {
        Pattern p = Pattern.compile("45\\d{8}");
        //System.out.println("verify the mobile phone number");
        Matcher m = p.matcher(mobiles);
        //System.out.println(m.matches());
        return m.matches();
    }
    public static ArrayList<User> readUsersFromFile() {
        ArrayList<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String phone = parts[0];
                    String password = parts[1];
                    users.add(new User(phone, password));
                }
            }
        } catch (IOException e) {
            // Handle any file reading errors
        }
        return users;
    }

    public static void writeUsersToFile(ArrayList<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User user : users) {
                writer.write(user.getPhone() + "," + user.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            // Handle any file writing errors
        }
    }
}