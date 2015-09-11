/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartanfinal;



/**
 *
 * @author noe
 */
public class Login {
     private dbConnect connect;
     private String id;
     private String username;
     private String password;
     private String first;
     private String last;
     private String initials;
     private String region;
     private String role;
     private String rights;
     private String [] arr;
    
    public Login()
    {
        connect = new dbConnect();
        username = "";
        password = "";
        first = "";
        last = "";
        initials = "";
        region = "";
        role = "";
        rights = "";
    }

    public String getUser(){return username;}
    public String getPass(){return password;}
    public String getFirst(){return first;}
    public String getLast(){return last;}
    public String getInit(){return initials;}
    public String getRegion(){return region;}
    public String getRole(){return role;}
    public String getRights(){return rights;}
    public String getId(){return id;}
    public void getAccountInformation()
    {
        arr = connect.getAccountInfo(username, password);
        id = arr[0];
        first = arr[4];
        last = arr[5];
        initials = arr[6];
        region = arr[7];
        role = arr[3];
        rights = arr[8];
    }
    public void setUser(String u){username = u;}
    public void setPass(String p){password = p;}
    public void setFirst(String f){first = f;}
    public void setLast(String l){last = l;}
    public void setInit(String i){initials = i;}
    public void setRegion(String r){region = r;}
    public void setRole(String r){role = r;}
    public void setRights(String r){rights = r;}
    
}
