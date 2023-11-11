package src.accessctrl.accesslist;

import java.util.List;


public class AccessCtrlList
{
    public String username;
    public List<String> method;
    public String role;
    private AccessCtrlList access_list;

    public AccessCtrlList()
    {

    }

    public AccessCtrlList(String name, List<String> method, String role)
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

    public List<String> getMethod()
    {
        return method;
    }

    public void setMethod(List<String> method)
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
        return "AccessCtrlList{" + "username='" + username + '\'' + ",method=" + method + ",role='" + role + '\'' + '}';

    }

    public void add(AccessCtrlList accessCtrlList)
    {
        access_list = new AccessCtrlList();
        access_list.username = accessCtrlList.username;
        access_list.method = accessCtrlList.method;
        access_list.role = accessCtrlList.role;
    }
}
