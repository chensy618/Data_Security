package src.accessctrl.accesslist;

public class AccessCtrlList
{
    private String username;
    private String method;
    private String role;
    public AccessCtrlList()
    {

    }
    public AccessCtrlList(String name, String method, String role)
    {
        this.username = name;
        this.method = method;
        this.role = role;
    }
    public String getName()
    {
        return username;
    }

    public void setName(String name)
    {
        this.username = name;
    }
    public String getMethod()
    {
        return method;
    }
    public void setMethod(String method)
    {
        this.method = method;
    }
    public String getRole()
    {
        return role;
    }
    public void setRole(String role)
    {
        this.role = role;
    }
    public String toString()
    {
        return "AccessCtrlList{" +
                "username='"+ username + '\'' +
                ",method=" + method +
                ",role='" + role + '\'' +
                '}';

    }

}
