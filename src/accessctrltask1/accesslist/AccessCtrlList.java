package src.accessctrltask1.accesslist;

import java.util.List;


public class AccessCtrlList
{
    public String username;
    public List<String> method;
    private AccessCtrlList access_list;

    public AccessCtrlList()
    {

    }

    public AccessCtrlList(String name, List<String> method)
    {
        this.username = name;
        this.method = method;
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


    public String toString()
    {
        return "AccessCtrlList{" + "username='" + username + '\'' + ",method=" + method + '}';

    }

    public void add(AccessCtrlList accessCtrlList)
    {
        access_list = new AccessCtrlList();
        access_list.username = accessCtrlList.username;
        access_list.method = accessCtrlList.method;
    }
}
