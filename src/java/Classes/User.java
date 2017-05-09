/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Scanner;
/**
 *
 * @author jkira
 */
@ManagedBean
@SessionScoped

public class User implements Serializable{
    private int accountnumber;
    private String ssn;
    private String name;
    private String loginid;
    private String pass;
    private String sec1;
    private String sec1ans;
    private String sec2;
    private String sec2ans;
    private String accountstatus;
    private String amount;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
   

    public int getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(int accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSec1() {
        
        return sec1;
    }

    public void setSec1(String sec1) {
        
        this.sec1 = sec1;
    }

    public String getSec1ans() {
        return sec1ans;
    }

    public void setSec1ans(String sec1ans) {
        this.sec1ans = sec1ans;
    }

    public String getSec2() {
        return sec2;
    }

    public void setSec2(String sec2) {
        this.sec2 = sec2;
    }

    public String getSec2ans() {
        return sec2ans;
    }

    public void setSec2ans(String sec2ans) {
        
        this.sec2ans = sec2ans;
    }

    public String getAccountstatus() {
        return accountstatus;
    }

    public void setAccountstatus(String accountstatus) {
        this.accountstatus = accountstatus;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        if(amount!=null){
           this.amount = amount; 
        }
        else{
            this.amount = "0";
        }
        
    }
    
    public String register(){
        if(amount.isEmpty()){
            amount = "0";
        }
        try{
            msg="";
             double balance = Double.parseDouble(amount);
        String r = addusertodb(ssn, name, loginid, pass, sec1, sec1ans, sec2, sec2ans,balance);
        if(r.equals("done")){
            msg = "User Registered";
            Thread.sleep(10);
            return "RegistrationDone.xhtml";
        }
        else{
            msg = "connection DB failed. Please Try Again";
            return "";
        }
        
        }
        catch(Exception e){
            msg = "Amount must be a number";
            return "";
        }
       
       
        
    }
    
    public String addusertodb(String ssn,String name, String loginid,String pass,String sec1,String sec1ans,String sec2,String sec2ans,double amount){
         accountnumber = getaccountnumber() + 1;
         String result = "";
        int accountstatus = 1;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            
        }
        catch (Exception e)
        {
           
        }
       final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
         
        try
        {
            //connect to db
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
           
            int t = stat.executeUpdate("insert into users values('"+accountnumber+"','"+ssn+"','"+name+"','"+loginid+"','"+pass+"','"+sec1+"','"+sec1ans+"','"+sec2+"','"+sec2ans+"','"+accountstatus+"','"+amount+"')");
            int f = stat.executeUpdate("update accountnumber set id = '"+accountnumber+"'");
            msg = "User Registered";
            result = "done";
        }
        catch(SQLException e)
        {
            result = "error";
            System.out.println("connection to Database failed.");
            e.printStackTrace();
        }
        finally
        {
            try{
                conn.close();
                stat.close();
                
            }
            catch(Exception e)
            {
            e.printStackTrace();
            }
            
        }
       return result;
   }
   
   public int getaccountnumber(){
        //load the Driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //return to internalError.xhtml
            
        }
       final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
         int accountnumber = 0;
        try
        {
            //connect to db
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
           
            rs = stat.executeQuery("select * from accountnumber");
           while(rs.first()){
               accountnumber = rs.getInt(1);
               break;
           }
       
        }
        catch(SQLException e)
        {
            System.out.println("connection to Database failed.");
            e.printStackTrace();
        }
        finally
        {
            try{
                conn.close();
                stat.close();
                rs.close();
            }
            catch(Exception e)
            {
            e.printStackTrace();
            }
            
        }
        return accountnumber;
   }
    
   public void retreivepassword(){
          //load the Driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //return to internalError.xhtml
            
        } 
        msg = "";
       final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        Scanner inp = new Scanner(System.in);
        try
        {
            //connect to db
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
           
            rs = stat.executeQuery("select * from users where loginid = '"+loginid+"'");
           if(rs.first()){
               
                   String ans = sec1ans;
                   if(ans.equals(rs.getString("sec1ans"))){
                       msg = "Your Password is : "+rs.getString("pass");
                       unlockaccount(loginid);
                   }
                   else{
                       msg = "Wrong answer";
                   }
                             
                   
               }
           }
       
        
        catch(SQLException e)
        {
            System.out.println("connection to Database failed.");
            e.printStackTrace();
        }
        finally
        {
            try{
                conn.close();
                stat.close();
                rs.close();
            }
            catch(Exception e)
            {
            e.printStackTrace();
            }
            
        }
    }
    
    public void unlockaccount(String login){
        
           //load the Driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //return to internalError.xhtml
            
        }
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        
        try
        {
            //connect to db
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
            int t = stat.executeUpdate("update users set accountstatus = 1 where loginid = '"+login+"'");
            System.out.println("Account Unlocked");
        }
        catch(SQLException e)
        {
            System.out.println("connection to Database failed.");
            e.printStackTrace();
        }
        finally
        {
            try{
                conn.close();
                stat.close();
                
            }
            catch(Exception e)
            {
            e.printStackTrace();
            }
            
        }
    }
    
     public boolean checkloginid(String a){
          //load the Driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //return to internalError.xhtml
            
        }
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
         boolean validlogin = false;
        try
        {
            //connect to db
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
           
            rs = stat.executeQuery("select * from users where loginid = '"+a+"'");
           if(rs.first()){
               validlogin = true;
           }
       
        }
        catch(SQLException e)
        {
            System.out.println("connection to Database failed.");
            e.printStackTrace();
        }
        finally
        {
            try{
                conn.close();
                stat.close();
                rs.close();
            }
            catch(Exception e)
            {
            e.printStackTrace();
            }
            return validlogin;
        }
   }
     
    public String gotopassretrival1(){
         boolean validloginid = checkloginid(loginid);
         if(validloginid==true){
              try
        {
            Class.forName("com.mysql.jdbc.Driver");
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //return to internalError.xhtml
            
        }
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        
        try
        {
            //connect to db
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
           
            rs = stat.executeQuery("select * from users where loginid = '"+loginid+"'");
           if(rs.first()){
               sec1 = rs.getString("sec1");
           }
       
        }
        catch(SQLException e)
        {
            System.out.println("connection to Database failed.");
            e.printStackTrace();
        }
        finally
        {
            try{
                conn.close();
                stat.close();
                rs.close();
            }
            catch(Exception e)
            {
            e.printStackTrace();
            }
            
        }
             return "Passwordretrival1.xhtml";
         }
         else{
             msg = "Invalid Login ID";
             return "";
         }
    }
   
}
