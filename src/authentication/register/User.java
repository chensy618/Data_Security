package src.authentication.register;

public class User
{
    private String phoneNumber;
    private String password;

    public User(String phoneNumber, String password)
    {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getPhone()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    public String toString()
    {
        return "User{" + "phoneNumber='" + phoneNumber + '\'' + ", password='" + password + '\'' + '}';
    }
}
