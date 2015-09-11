/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartanfinal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;



/**
 *
 * @author noe
 */
public class dbConnect {
    private Connection con;
    private Statement st;
    public ResultSet rs;
    public ResultSet copy;
    public String [] arr;
    private String query;
    private ProcessFiles file;
    public dbConnect()
    {
         try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.0.12:3306/test", "hchapa", "cangri1989");
            st = con.createStatement();
        } catch (Exception e) {
          JOptionPane.showMessageDialog(null, e);
        }
    }
    public boolean checkAccount(String user, String pass)
    {
         query = "select * from login where username = '" + user + "' and password = '"+ pass + "'";
         try
         {
             rs = st.executeQuery(query);
             if(rs.next())
             {
                 return true;
             }else{
                 return false;
             }
             
         }catch(Exception e)
         {
              JOptionPane.showMessageDialog(null, e);
         }
         return false;
         
    }
    public boolean checkAccount(String user)
    {
        
        query = "select * from login where username = '" + user + "'";
        try{
            rs = st.executeQuery(query);
            if(rs.next())
            {
                return true;
            }
            else{
                return false;
            }
        }catch(Exception e)
        {
            
        }
        
        return false;
    }
    public boolean checkInit(String init)
    {
        query = "select * from login where initialsn = '" + init + "'";
         try{
            rs = st.executeQuery(query);
            if(rs.next())
            {
                return true;
            }
            else{
                return false;
            }
        }catch(Exception e)
        {
            
        }
        
        return false;
        
    }
    public int insertAttachments(String id, ProcessFiles files)
    {
        try{
            
        file = files;
         String query = "INSERT INTO meta_attachments (id_ticket, name , type, size)"
        + " values (?, ?, ?, ?)";
 
      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      preparedStmt.setString (1, id);
      preparedStmt.setString (2, files.getName());
      preparedStmt.setString   (3, files.getType());
      preparedStmt.setString(4, String.valueOf(files.getSize()));
      
 
      // execute the preparedstatement
      preparedStmt.executeUpdate();
      ResultSet rs = preparedStmt.getGeneratedKeys();
      
      if(rs.next())
      {
          System.out.println("Id what is " + rs.getInt(1));
          int num = rs.getInt(1);
          files.setId(String.valueOf(num));
          if(files.getSend())
          {
               query = "INSERT INTO attachments (id_meta,name, bin_data) VALUES (?,?,?)";
                 PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                 stmt.setString(1, String.valueOf(num));
                stmt.setString(2, files.getName());
                stmt.setString(3, files.getBase());
                int check = stmt.executeUpdate();
                return check;
               
          }
         
          
      }
     // System.out.println("Id number is " + num);
      con.close();
           
       }catch (Exception e)
       {
           System.out.println(e.getMessage());
       }
        return 0;
    }
    public void addNewAccount(String user, String pass, String fname, String lname, String init, String role, String region)
    {
       try{
         String query = "insert into login (username, password, role, firstn, lastn, initialsn, region)"
        + " values (?, ?, ?, ?, ?, ?, ?)";
 
      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = con.prepareStatement(query);
      preparedStmt.setString (1, user);
      preparedStmt.setString (2, pass);
      preparedStmt.setString   (3, role);
      preparedStmt.setString(4, fname);
      preparedStmt.setString (5, lname);
      preparedStmt.setString(6, init);
      preparedStmt.setString(7, region);
 
      // execute the preparedstatement
      preparedStmt.execute();
      con.close();
           
       }catch (Exception e)
       {
           
       }
        
    }
    public int deleteAttachment(String id)
    {
        query = "delete from meta_attachments, attachments " +
" using meta_attachments, test.attachments where " +
" meta_attachments.id_meta = attachments.id_meta and meta_attachments.id_meta = ?";
        try{
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, id);
            int num = preparedStmt.executeUpdate();
            con.close();
            return num;
        }catch(Exception t)
        {
             JOptionPane.showMessageDialog(null, t);
             return 0;
        }
        
    }
    public int deleteAllAttachemnts(String objectid)
    {
        query = "delete from meta_attachments, attachments " +
" using meta_attachments, attachments where " +
" meta_attachments.id_meta = attachments.id_meta and meta_attachments.id_ticket = ?";
        try{
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, objectid);
            int num = preparedStmt.executeUpdate();
            con.close();
            return num;
        }catch(Exception t)
        {
             JOptionPane.showMessageDialog(null, t);
             return 0;
        }
    }
    public ArrayList getAttachmentsInfo(String objectid)
    {
        ArrayList<String[]> list = new ArrayList();
         query = "select * from meta_attachments where id_ticket = ?";
        try{
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, objectid);
            rs = preparedStmt.executeQuery();
            while(rs.next())
            {
                String arr [] = new String[3];
                arr[0] = rs.getString(1);
                arr[1] = rs.getString(3);
                arr[2] = rs.getString(4);
               System.out.println(Arrays.toString(arr));
                list.add(arr);
            }
           
            con.close();
            return list;
        }catch(Exception t)
        {
             JOptionPane.showMessageDialog(null, t);
             return null;
        }
        
    }
    public ProcessFiles getFiles(){return file;}
    public void updateAccount(String user, String pass, String fname, String lname, String init, String role, String region, String id)
    {
        try{
         String query = "UPDATE login SET username = ?, password = ?, role = ?, firstn = ?, lastn = ?, initialsn = ?, region = ? "
        + "WHERE id = ?";
 
      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = con.prepareStatement(query);
      preparedStmt.setString (1, user);
      preparedStmt.setString (2, pass);
      preparedStmt.setString   (3, role);
      preparedStmt.setString(4, fname);
      preparedStmt.setString (5, lname);
      preparedStmt.setString(6, init);
      preparedStmt.setString(7, region);
      preparedStmt.setString(8, id);
      // execute the preparedstatement
      preparedStmt.execute();
      con.close();
           
       }catch (Exception e)
       {
           
       }
        
    }
    public String [] getAccountInfo(String user, String pass)
    {
       arr = new String[9];
       query = "select * from login where username = '" + user + "' and password = '"+ pass + "'";
       
       try
       {
            rs = st.executeQuery(query);
            if(rs.next())
            {
                arr[0] = rs.getString(1);
                arr[1] = rs.getString(2);
                arr[2] = rs.getString(3);
                arr[3] = rs.getString(4);
                arr[4] = rs.getString(5);
                arr[5] = rs.getString(6);
                arr[6] = rs.getString(7);
                arr[7] = rs.getString(8);
                arr[8] = rs.getString(9);
            }
             
       }catch(Exception e)
       {
            JOptionPane.showMessageDialog(null, e);
       }
       return arr;   
    }
    public ResultSet getResult()
    {
        return copy;
    }
    public void closeConnect()
    {
      try{
          
        con.close();
      }catch(Exception e)
      {
          JOptionPane.showMessageDialog(null, e);
      }
    }
    public ProcessFiles getBlobInfo(String id)
    {
         query = "select * from attachments where id_meta = '" + id + "'";
       
       try
       {
           ProcessFiles data = new ProcessFiles();
            rs = st.executeQuery(query);
            if(rs.next())
            {
              data.setBase64(rs.getBytes(4));
              data.setName(rs.getString(3));
              return data;
            }
             
       }catch(Exception e)
       {
            JOptionPane.showMessageDialog(null, e);
       }
        return null;
    }
}
