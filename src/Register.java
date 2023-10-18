package src;


import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register
{
    public static void main(String[] args)
    {
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
            //TODO
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
        if (registerPhone.length() == 10 && isMobile(registerPhone))
        {
            System.out.println("please input the password：");
            registerPassword = sc.next();
            System.out.println("please input the password again：");
            registerPassword2 = sc.next();
            if (registerPassword.length() >= 6 && registerPassword.equals(registerPassword2))
            {
                System.out.println("registration success！");
            }
            else
            {
                System.out.println("The two password inputs are inconsistent or the password is not enough 6 digits!");
            }
        }
        else
        {
            System.out.println("wrong phone number format！");
        }

        login(sc, registerPhone, registerPassword);


    }


    public static void login(Scanner sc, String iphone, String password)
    {
        String option;
        String inputPhone;
        String inputPassword;
        System.out.println("please select the menu:[1]-login,[2]-exit：");
        option = sc.next();
        if (option.equals("1"))
        {
            System.out.println("please input the mobile phone number：");
            inputPhone = sc.next();

            System.out.println("please input the password：");
            inputPassword = sc.next();
            if (inputPhone.equals(iphone) && inputPhone.length() == 10 && isMobile(inputPhone))
            {
                if (inputPassword.equals(password))
                {
                    System.out.println("login success！");
                }
                else
                {
                    System.out.println("wrong password,exit！");
                }
            }
            else
            {
                System.out.println("wrong username,exit！");
            }
        }
        else if (option.equals("2"))
        {
            System.exit(0);
        }
        else
        {
            System.out.println("please input correct option:1 or 2！");
        }
    }


    public static boolean isMobile(String mobiles)
    {
        Pattern p = Pattern.compile("45\\d{8}");
        //System.out.println("verify the mobile phone number");
        Matcher m = p.matcher(mobiles);
        //System.out.println(m.matches());
        return m.matches();
    }
}