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
public class LoginUser {
    private String loginid;
    private String pass;
    private int wrongpasscount;
    private String msg;
    private String symbol;
    private String num_of_shares;
    private String price;
    private ArrayList<String> orders;
    private ArrayList<String> activity;
    private String currentpassword;
    private String newpass;
    private String retypenewpass;
    private ArrayList<Getuserstocks> userstocks;
    private String Addbalnce;
    private ArrayList<SearchingStocks> searchstock;
    private String searchstocksymbol;
    private ArrayList<SearchingStocks> watchlist;

    public ArrayList<SearchingStocks> getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(ArrayList<SearchingStocks> watchlist) {
        this.watchlist = watchlist;
    }
    
    

    public String getSearchstocksymbol() {
        return searchstocksymbol;
    }

    public void setSearchstocksymbol(String searchstocksymbol) {
        this.searchstocksymbol = searchstocksymbol;
    }
    

    public ArrayList<SearchingStocks> getSearchstock() {
        return searchstock;
    }

    public void setSearchstock(ArrayList<SearchingStocks> searchstock) {
        this.searchstock = searchstock;
    }   
    
    public String getAddbalnce() {
        return Addbalnce;
    }

    public void setAddbalnce(String Addbalnce) {
        this.Addbalnce = Addbalnce;
    }
  
    public ArrayList<Getuserstocks> getUserstocks() {
        return userstocks;
    }

    public void setUserstocks(ArrayList<Getuserstocks> userstocks) {
        this.userstocks = userstocks;
    }   
    
    public String getNewpass() {
        return newpass;
    }

    public void setNewpass(String newpass) {
        this.newpass = newpass;
    }

    public String getRetypenewpass() {
        return retypenewpass;
    }

    public void setRetypenewpass(String retypenewpass) {
        this.retypenewpass = retypenewpass;
    }
      
    public String getCurrentpassword() {
        return currentpassword;
    }

    public void setCurrentpassword(String currentpassword) {
        this.currentpassword = currentpassword;
    }
       
    public ArrayList<String> getActivity() {
        return activity;
    }

    public void setActivity(ArrayList<String> activity) {
        this.activity = activity;
    }
   
    public ArrayList<String> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<String> orders) {
        
        this.orders = orders;
    }
   
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
 
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getNum_of_shares() {
        return num_of_shares;
    }

    public void setNum_of_shares(String num_of_shares) {
        this.num_of_shares = num_of_shares;
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

    public int getWrongpasscount() {
        return wrongpasscount;
    }

    public void setWrongpasscount(int wrongpasscount) {
        this.wrongpasscount = wrongpasscount;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    
    //Methods
     public String displayorders(){
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
        int accountnumber = getaccountnumber(loginid);
       
        orders.clear();
         ArrayList<String> last5orders = new ArrayList<>();
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        
        try
        {
            //connect to db
            String a = "";
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from useractivity where accountnumber = '"+accountnumber+"'");
           while(rs.next()){
           a += "accountnumber"+" "+rs.getString("accountnumber")+" "+rs.getString("activity");              
           last5orders.add(a);
           a = "";
           }
           int ordercount=0;
           for(int i=last5orders.size()-1;i>0;i--){
               ordercount++;
               orders.add(last5orders.get(i));
               if(ordercount==5){
                   break;
               }
           }
           if(rs.first()==false){
              orders.add("No Recent Orders");
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
       return "DisplayOrders.xhtml";
    }
    
     public String login(){
        boolean validloginid = checkloginid(loginid);
        if(validloginid==true){
            boolean validuser = isvaliduser(loginid, pass);
            boolean accountlocked = isvalidaccount(loginid);
            if(validuser==true && accountlocked==false){
               // Login Successful.
               msg ="";
               activity = new ArrayList<>();
               userstocks = new ArrayList<>();
               orders = new ArrayList<>();
               searchstock = new ArrayList<>();
               watchlist = new ArrayList<>();
               return "UserHomepage.xhtml";
            }
            else if(accountlocked==true){
                //Account is Locked.
                msg = "Your Account is locked";
                return "Accountlocked.xhtml";
            }
            else{
               msg = "Invalid password";
               wrongpasscount++;
            }
            if(wrongpasscount==2){
                lockaccount(loginid);
            }
        }
        
        else{
            msg = "Invalid login ID";
        }
        return "";
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
     
     public boolean isvalidaccount(String login){
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
           
            rs = stat.executeQuery("select * from users where loginid = '"+login+"'");
           if(rs.first()){
              if(rs.getInt("accountstatus")==0){
                  validlogin = true;
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
        return validlogin;
    }
     
     public boolean isvaliduser(String login,String pass){
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
           
            rs = stat.executeQuery("select * from users where loginid = '"+login+"' and pass = '"+pass+"'");
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
            
        }
        return validlogin;
    }
    
     public void lockaccount(String login){
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
           
            int t = stat.executeUpdate("update users set accountstatus = 0 where loginid = '"+login+"'");
            System.out.println("You have typed wrong password continously for two times.We have locked your Account.");
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
    
     public String getname(){
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
        String name = "";
        try
        {
            //connect to db
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
           
            rs = stat.executeQuery("select * from users where loginid = '"+loginid+"'");
           if(rs.first()){
               name = rs.getString("name");
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
        return name;
    }
    
     public int getaccountnumber(String login){
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
       int acc = 0;
        try
        {
            //connect to db
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
           
            rs = stat.executeQuery("select * from users where loginid = '"+login+"'");
           if(rs.first()){
               acc = rs.getInt("accountnumber");
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
            return acc;
        }
    }
    
     public boolean validstockname(String symbol){
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
        boolean has = false;
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        
        try
        {
            //connect to db
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();           
            rs = stat.executeQuery("select * from stocks where symbol = '"+symbol+"'");
           if(rs.first()){
            return true;
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
         return has;
    }
    
     public void BuyMarketOrder(){
        int accountnumber = getaccountnumber(loginid);
         boolean comp = validstockname(symbol);
         if(comp==true){
              try{
                  msg ="";
                    int numofshares = Integer.parseInt(num_of_shares);
                    checking_for_a_buy_match(0, numofshares, accountnumber, symbol,true);

                }
        catch(Exception e){
                    msg = "number of shares must be a number";
                }
         }
         else{
             msg = "Invalid Stock Name";
         }
               
    }
    
     public void BuyLimitOrder(){
        int accountnumber = getaccountnumber(loginid);
        boolean comp = validstockname(symbol);
        if(comp==true){
            try{
                msg="";
                    int numofshares = Integer.parseInt(num_of_shares);
                    double price1 = Double.parseDouble(price);
                    boolean enoughbalance = checking_user_has_enough_balance_to_buy(price1*numofshares, accountnumber);
                    if(enoughbalance==true){
                        msg="";
                          checking_for_a_buy_match(price1, numofshares, accountnumber, symbol,false);
                    }
                    else{
                        msg = "Not enough balance";
                    }
                }
                catch(Exception e){
                     msg = "number of shares and price must be a number";
                }
        }
        else{
            msg = "Invalid Stock name";        }
        
    }
    
     public boolean checking_for_a_buy_match(double price,int numofshares,int accountnumber,String symbol,boolean buypotion){
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
        
        activity.clear();
         boolean has = false;
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String place_order_type="";
        if(buypotion==true){
            place_order_type = "market";
        }
        else{
            place_order_type = "limit";
        }
        try
        {
            //connect to db
            msg="";
            String msg = "You have placed a order to buy("+place_order_type+") "+symbol;
            updateactivity(accountnumber, msg);
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
            int r=0;
            int buyershares = numofshares;
            while(r==0){
                if(buypotion==true){
                     rs = stat.executeQuery("select * from orders where accountnumber != '"+accountnumber+"' and order_type = 'sell'  and num_of_shares>0 and place_order_type='limit' and status = 'pending'");
                }
                else{
                     rs = stat.executeQuery("select * from orders where accountnumber != '"+accountnumber+"' and order_type = 'sell' and price <= '"+price+"' and num_of_shares>0 and place_order_type='limit' and status = 'pending'");
                }
               
           double p = 0; 
           int min = 0;
           if(rs.next()){
               has=true;
               p = rs.getDouble("price");
               min = rs.getInt("ID");
           }
           while(rs.next()){
               has =true;
               if(rs.getDouble("price")<p){
                   min = rs.getInt("ID");
                   p = rs.getDouble("price");
               }             
           }
           if(rs.first()==false&&buyershares!=0){
               r=1;
               msg = "";
               if(buypotion==true){
                  updatebuyer1(accountnumber, symbol, "buy","market", buyershares,price); 
                  this.msg = "Your Order is still pending and added to pending orders";
               }
               else{
                          
             boolean b =  findamatchforbuyerwithmarketorder(buyershares, accountnumber, symbol, price);
                  if(b==false){
                      this.msg = "Your Order is still pending and added to pending orders";
                  }                   
               }
           }
         
           
           if(has==true){
               msg = "";
               rs = stat.executeQuery("select * from orders where ID = '"+min+"'");
               if(rs.first()){
                   int shares = rs.getInt("num_of_shares");
                   int acc = rs.getInt("accountnumber");
                   double amt = shares * rs.getDouble("price");
                   if(buyershares>shares){
                       msg = "";
                       buyershares = buyershares-shares;                  
                       amt = shares * rs.getDouble("price");
                       updatebuyer2(accountnumber, amt, shares, symbol);
                       updateseller1(min, amt, acc,shares,accountnumber,place_order_type,"buy");
                       msg = "You have bought "+shares+" shares of "+symbol+" for a price "+rs.getDouble("price");
                       updateactivity(accountnumber, msg);
                       activity.add("You have bought "+shares+" shares of "+symbol+" for a price "+rs.getDouble("price"));
                       msg = "you have sold "+shares+" shares of "+symbol+" for a price "+rs.getDouble("price");
                       updateactivity(acc, msg);
                   }
                   else if(shares>buyershares){
                       int sellshares = shares-numofshares;
                       amt = sellshares * rs.getDouble("price");
                       updatebuyer(amt, accountnumber, symbol, "buy", buyershares, rs.getDouble("price"),place_order_type);
                      
                       updateseller(min, amt, acc, sellshares);   
                       msg = "You have bought "+buyershares+" shares of "+symbol+" for a price "+rs.getDouble("price");
                       updateactivity(accountnumber, msg);
                       activity.add("You have bought "+buyershares+" shares of "+symbol+" for a price "+rs.getDouble("price"));
                       msg = "you have sold "+shares+" shares of "+symbol+" for a price "+rs.getDouble("price");
                       updateactivity(acc, msg); 
                       this.msg = "Your Order is completed";
                       r=1;
                   }
                   else{                      
                       updatebuyer(amt, accountnumber, symbol, "buy", buyershares, rs.getDouble("price"),place_order_type);
                       updateseller1(min, amt, acc, shares,accountnumber,place_order_type,"buy");
                     msg = "You have bought "+shares+" shares of "+symbol+" for a price "+rs.getDouble("price");
                       updateactivity(accountnumber, msg);
                       activity.add("You have bought "+buyershares+" shares of "+symbol+" for a price "+rs.getDouble("price"));
                       msg = "you have sold "+shares+" shares of "+symbol+" for a price "+rs.getDouble("price");
                       updateactivity(acc, msg);
                       this.msg = "Your Order is completed";
                       r=1;
                   }
               }
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
        
        
        return has;
    }
     
     public boolean findamatchforbuyerwithmarketorder(int numofshares,int accountnumber,String symbol,double buyerprice){
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
         boolean has = false;
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
         String place_order_type="market";
        
        try
        {
            //connect to db
            String msg = "";
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
            int r=0;
            int buyershares = numofshares;
            while(r==0){
                     rs = stat.executeQuery("select * from orders where accountnumber != '"+accountnumber+"' and order_type = 'sell'  and num_of_shares>0 and place_order_type='market' and status = 'pending'");
                     double p = 0; 
           int min = 0;
           if(rs.next()){
               has=true;
               p = rs.getDouble("price");
               min = rs.getInt("ID");
           }
           while(rs.next()){
               has =true;
               if(rs.getDouble("price")<p){
                   min = rs.getInt("ID");
                   p = rs.getDouble("price");
               }             
           }
           if(rs.first()==false&&buyershares!=0){
                updatebuyer1(accountnumber, symbol, "buy", "limit", buyershares, buyerprice);
                          return false;
                         
                    }
           else if(buyershares==0){
               return true;
           }
           else{
               if(has==true){
               rs = stat.executeQuery("select * from orders where ID = '"+min+"'");
               if(rs.first()){
                   int shares = rs.getInt("num_of_shares");
                   int acc = rs.getInt("accountnumber");
                   double amt = shares * buyerprice;
                   if(buyershares>shares){                       
                       buyershares = buyershares-shares;                      
                       amt = shares * buyerprice;
                       updatebuyer2(accountnumber, amt, shares, symbol);
                       updateseller1(min, amt, acc,shares,accountnumber,place_order_type,"buy");
                       msg = "You have bought "+shares+" shares of "+symbol+" for a price "+buyerprice;
                       updateactivity(accountnumber, msg);
                       activity.add("You have bought "+shares+" shares of "+symbol+" for a price "+buyerprice);
                       msg = "you have sold "+shares+" shares of "+symbol+" for a price "+buyerprice;
                       updateactivity(acc, msg);
                   }
                   else if(shares>buyershares){
                       buyershares = shares-numofshares;
                       amt = buyershares * buyerprice;
                       updatebuyer(amt, accountnumber, symbol, "buy", numofshares, buyerprice,place_order_type);
                       updateseller(min, amt, acc, buyershares);
                       msg = "You have bought "+buyershares+" shares of "+symbol+" for a price "+buyerprice;
                       updateactivity(accountnumber, msg);
                       activity.add("You have bought "+buyershares+" shares of "+symbol+" for a price "+rs.getDouble("price"));
                       msg = "you have sold "+shares+" shares of "+symbol+" for a price "+buyerprice;
                       updateactivity(acc, msg);
                       this.msg = "Your Order is completed";
                       r=1;
                   }
                   else{                      
                       updatebuyer(amt, accountnumber, symbol, "buy", numofshares, buyerprice,"limit");
                       updateseller1(min, amt, acc, shares,accountnumber,place_order_type,"buy");
                       msg = "You have bought "+shares+" shares of "+symbol+" for a price "+buyerprice;
                       updateactivity(accountnumber, msg);
                       activity.add("You have bought "+shares+" shares of "+symbol+" for a price "+buyerprice);
                       msg = "you have sold "+shares+" shares of "+symbol+" for a price "+buyerprice;
                       updateactivity(acc, msg);
                       this.msg = "Your Order is completed";
                       r=1;
                   }
               }
              }
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
        return has;
    }
        
     public void SellMarketOrder(){
        int accountnumber = getaccountnumber(loginid);
        try{
            msg="";
                    int numofshares = Integer.parseInt(num_of_shares);
                    boolean validstocktosell = checking_selected_stock_and_value_of_a_user(symbol, numofshares, accountnumber);
                    if(validstocktosell==true){
                        updatesellerstock(accountnumber, numofshares);
                    checking_for_a_sell_match(0, numofshares, accountnumber, symbol,true);
                }
                    else{
                        msg = "Invalid stock or Invalid stock quantity";
                    }
                    

                }
                catch(Exception e){
                    msg = "number of shares must be a number";
                }
    }
    
     public void SellLimitOrder(){
        int accountnumber = getaccountnumber(loginid);
        try{
            
                    int numofshares = Integer.parseInt(num_of_shares);
                    double price1 = Double.parseDouble(price);
                   boolean validstocktosell = checking_selected_stock_and_value_of_a_user(symbol, numofshares, accountnumber);
                   
                    if(validstocktosell==true){
                          updatesellerstock(accountnumber, numofshares);
                          checking_for_a_sell_match(price1, numofshares, accountnumber, symbol,false);                    
                    }
                    else{                        
                          msg = "Invalid stock or Invalid stock quantity";
                    }
                    
                }
                catch(Exception e){
                    msg = "number of shares and price must be a number";
                }
    }
    
     //Finding match for seller
     public boolean checking_for_a_sell_match(double price,int numofshares,int accountnumber,String symbol,boolean selloption){
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
        activity.clear();
        boolean has = false;
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String place_order_type="";
        if(selloption==true){
            place_order_type = "market";
        }
        else{
            place_order_type = "limit";
        }
        try
        {
            msg="";
            //connect to db
            String msg = "You have placed a order to buy("+place_order_type+") "+symbol;
            updateactivity(accountnumber, msg);
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
            int r=0;
            int sellerhares = numofshares;
            while(r==0){
                if(selloption==true){
                     rs = stat.executeQuery("select * from orders where accountnumber != '"+accountnumber+"' and order_type = 'buy'  and num_of_shares>0 and status = 'pending'");
                }
                else{
                     rs = stat.executeQuery("select * from orders where accountnumber != '"+accountnumber+"' and order_type = 'buy' and price >= '"+price+"' and num_of_shares>0 and status = 'pending'");
                }
               
           double p = 0; 
           int min = 0;
           if(rs.next()){
               has=true;
               p = rs.getDouble("price");
               min = rs.getInt("ID");
           }
           while(rs.next()){
               has =true;
               if(rs.getDouble("price")>p){
                   min = rs.getInt("ID");
                   p = rs.getDouble("price");
               }             
           }
           if(rs.first()==false&&sellerhares!=0){
               r=1;
                 if(selloption==true){
                  updatebuyer1(accountnumber, symbol, "sell",place_order_type, sellerhares,price); 
                   this.msg = "Your Order is still pending and added to pending orders";
               }
               else{                          
             boolean b =  findmatchforsellwithmarketorder(sellerhares, accountnumber, symbol, price,place_order_type);
                  if(b==false){
                      this.msg = "Your Order is still pending and added to pending orders";
                  }
                  else{
                      this.msg = "Your Order is completed";
                  }
               }
           }
         
           
           if(has==true){
               rs = stat.executeQuery("select * from orders where ID = '"+min+"'");
               if(rs.first()){
                   int shares = rs.getInt("num_of_shares");
                   int acc = rs.getInt("accountnumber");
                   double amt = shares * rs.getDouble("price");
                   msg="";
                   if(sellerhares>shares){                       
                       sellerhares = sellerhares-shares;                                             
                       amt = shares * rs.getDouble("price");
                       updatebuyerV(min, shares, amt);
                       updatesellV1(accountnumber, amt);
                       msg = "You have sold "+shares+" shares of "+symbol+" for a price "+rs.getDouble("price");
                       updateactivity(accountnumber, msg);
                       activity.add("You have sold "+shares+" shares of "+symbol+" for a price "+rs.getDouble("price"));
                       msg = "You have bought "+shares+" shares of "+symbol+" for a price "+rs.getDouble("price");
                       updateactivity(acc, msg);
                   }
                   else if(shares>sellerhares){
                       int sellshares = shares-sellerhares;
                       amt = sellshares * rs.getDouble("price");
                       updatesellV(accountnumber, shares, amt, "sell", place_order_type, price, symbol);
                       updatebuyV1(min, sellerhares, amt);
                       msg = "You have sold "+sellerhares+" shares of "+symbol+" for a price "+rs.getDouble("price");
                       updateactivity(accountnumber, msg);
                       activity.add("You have sold "+sellerhares+" shares of "+symbol+" for a price "+rs.getDouble("price"));
                       msg = "You have bought "+shares+" shares of "+symbol+" for a price "+rs.getDouble("price");
                       updateactivity(acc, msg);
                       r=1;
                       this.msg = "Your Order is completed";
                   }
                   else{                      
                       updatebuyerV(min, shares, amt);
                      updatesellV(accountnumber, shares, amt, "sell", place_order_type, price, symbol);
                       msg = "You have sold "+shares+" shares of "+symbol+" for a price "+rs.getDouble("price");
                       activity.add("You have sold "+shares+" shares of "+symbol+" for a price "+rs.getDouble("price"));
                       updateactivity(accountnumber, msg);
                       msg = "You have bought "+shares+" shares of "+symbol+" for a price "+rs.getDouble("price");
                       updateactivity(acc, msg);
                      this.msg = "Your Order is completed";
                       r=1;
                   }
               }
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
        
        
        return has;
    }
    
     public boolean findmatchforsellwithmarketorder(int numofshares,int accountnumber,String symbol,double sellprice,String place_order_type){
          boolean has = false;
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
         
        
        try
        {
            
            //connect to db
            String msg = "";
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
            int r=0;
            int sellerhares = numofshares;
            while(r==0){
                     rs = stat.executeQuery("select * from orders where accountnumber != '"+accountnumber+"' and order_type = 'buy'  and num_of_shares>0 and place_order_type='market' and status = 'pending'");
                     double p = 0; 
           int min = 0;
           if(rs.next()){
               has=true;
               p = rs.getDouble("price");
               min = rs.getInt("ID");
           }
           while(rs.next()){
               has =true;
               if(rs.getDouble("price")<p){
                   min = rs.getInt("ID");
                   p = rs.getDouble("price");
               }             
           }
           if(rs.first()==false&&sellerhares!=0){
                updatebuyer1(accountnumber, symbol, "sell", place_order_type, sellerhares, sellprice);
                          return false;
                         
                    }
           else if(sellerhares==0){
               return true;
           }
           else{
              if(has==true){
               rs = stat.executeQuery("select * from orders where ID = '"+min+"'");
               if(rs.first()){
                   int shares = rs.getInt("num_of_shares");
                   int acc = rs.getInt("accountnumber");
                   double amt = shares * rs.getDouble("price");
                   msg="";
                   if(sellerhares>shares){                       
                       sellerhares = sellerhares-shares;                                             
                       amt = shares * rs.getDouble("price");
                       msg = "You have sold "+shares+" shares of "+symbol+" for a price "+sellprice;
                       updateactivity(accountnumber, msg);
                       activity.add("You have sold "+shares+" shares of "+symbol+" for a price "+sellprice);
                       msg = "You have bought "+shares+" shares of "+symbol+" for a price "+sellprice;
                       updateactivity(acc, msg);
                       updatebuyerV(min, shares, amt);
                       updatesellV1(accountnumber, amt);
                   }
                   else if(shares>sellerhares){
                       int sellshares = shares-sellerhares;
                       amt = sellshares * rs.getDouble("price");
                       updatesellV(accountnumber, shares, amt, "sell", place_order_type, sellprice, symbol);
                       updatebuyV1(min, sellerhares, amt);
                       msg = "You have sold "+sellerhares+" shares of "+symbol+" for a price "+sellprice;
                       updateactivity(accountnumber, msg);
                       activity.add("You have sold "+sellerhares+" shares of "+symbol+" for a price "+sellprice);
                       msg = "You have bought "+shares+" shares of "+symbol+" for a price "+sellprice;
                       updateactivity(acc, msg);
                       this.msg = "Your Order is completed";
                       r=1;
                   }
                   else{       
                       msg = "You have sold "+shares+" shares of "+symbol+" for a price "+sellprice;
                       updateactivity(accountnumber, msg);
                       activity.add("You have sold "+shares+" shares of "+symbol+" for a price "+sellprice);
                       msg = "You have bought "+shares+" shares of "+symbol+" for a price "+sellprice;
                       updateactivity(acc, msg);
                       updatebuyerV(min, shares, amt);
                       updatesellV(accountnumber, shares, amt, "sell", place_order_type, sellprice, symbol);
                       this.msg = "Your Order is completed";
                       r=1;
                   }
               }
              }
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
        return has;
      }
       
      //while placing a sell order, we should check whether the user has enough stock to sell or not
     public boolean checking_selected_stock_and_value_of_a_user(String stock,int num,int accountnumber){
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
        boolean has = false;
        try
        {
            //connect to db
            String a = "";
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from userstocks where accountnumber = '"+accountnumber+"' and symbol = '"+stock+"' and numberofshares >= '"+num+"'");
                   
           if(rs.first()){
               has = true;
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
        
        return has;
    }
    
     public boolean checking_user_has_enough_balance_to_buy(double amt,int accountnumber){
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
        boolean has = false;
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try
        {
            //connect to db
            
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from users where accountnumber = '"+accountnumber+"'");
                   
           if(rs.first()){
               if(rs.getDouble("amount")>=amt){
                  has = true; 
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
        
        return has;
    }
    
     //updating seller order status tp done for those whose order is completed
     public void updateseller1(int ID,double amt,int accountnumber,int shares,int acc,String place_order_type,String order_type){
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
            int t = stat.executeUpdate("update orders set num_of_shares='0',dealprice = '"+(amt/(double)shares)+"' , status = 'done' where ID = '"+ID+"'");
            t = stat.executeUpdate("update users set amount = amount + '"+amt+"' where accountnumber = '"+accountnumber+"'");           
                     
            
             
//            rs = stat.executeQuery("select * from userstocks where accountnumber = '"+accountnumber+"' and symbol = '"+sym+"'");
//            boolean hasstockalready = false;
//            if(rs.first()){
//                hasstockalready = true;
//            }
//            if(hasstockalready==true){
//                int i = stat.executeUpdate("update userstocks set numberofshares = numberofshares+ '"+shares+"' where accountnumber = '"+acc+"' and symbol = '"+sym+"'");
//               
//            }
//            else{
//                int i = stat.executeUpdate("insert into userstocks values('"+acc+"','"+sym+"','"+shares+"')");
//                
//            }
          
       
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
    
    
    //adding buy_users_order(completed) to the orders table
     public void updatebuyer(double amt,int accountnumber,String symbol,String order_type,int numofshares,double price,String place_order_type){
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
           int s  = stat.executeUpdate("insert into orders values(null,'"+accountnumber+"','"+symbol+"','"+order_type+"','"+place_order_type+"','"+numofshares+"','"+price+"','"+price+"','done')");
           int t = stat.executeUpdate("update users set amount = amount - '"+amt+"' where accountnumber = '"+accountnumber+"'");
             boolean hasstockalready = false;
            rs = stat.executeQuery("select * from userstocks where accountnumber = '"+accountnumber+"' and symbol = '"+symbol+"'");  
            if(rs.first()){
                hasstockalready = true;
            }
            if(hasstockalready==true){
                int i = stat.executeUpdate("update userstocks set numberofshares = numberofshares+ '"+numofshares+"' where accountnumber = '"+accountnumber+"' and symbol = '"+symbol+"'");
            }
            else{
                int i = stat.executeUpdate("insert into userstocks values('"+accountnumber+"','"+symbol+"','"+numofshares+"')");
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
      
   
    //if sellers order we have update the seller's share
     public void updateseller(int ID,double amt,int accountnumber,int rem){
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
            int t = stat.executeUpdate("update orders set num_of_shares= '"+rem+"',dealprice='"+(amt/(double)rem)+"' , status = 'pending' where ID = '"+ID+"'");
            t = stat.executeUpdate("update users set amount = amount + '"+amt+"' where accountnumber = '"+accountnumber+"'");
           
          
       
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
      
    
    //Adding buy_users order(still pending) to the table
     public void updatebuyer1(int accountnumber,String symbol,String order_type,String placeordertype,int numofshares,double price){
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
            int t  = stat.executeUpdate("insert into orders values(null,'"+accountnumber+"','"+symbol+"','"+order_type+"','"+placeordertype+"','"+numofshares+"','"+price+"','"+price+"','pending')");
                         
          
       
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
     
     public void updatebuyer2(int acc,double amt,int shares,String symbol ){
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
           int t = stat.executeUpdate("update users set amount = amount - '"+amt+"' where accountnumber = '"+acc+"'");
             boolean hasstockalready = false;
            rs = stat.executeQuery("select * from userstocks where accountnumber = '"+acc+"' and symbol = '"+symbol+"'");  
            if(rs.first()){
                hasstockalready = true;
            }
            if(hasstockalready==true){
                int i = stat.executeUpdate("update userstocks set numberofshares = numberofshares+ '"+shares+"' where accountnumber = '"+acc+"' and symbol = '"+symbol+"'");
            }
            else{
                int i = stat.executeUpdate("insert into userstocks values('"+acc+"','"+symbol+"','"+shares+"')");
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
         
     public void updatesellerstock(int accountnumber,int shares){
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
            int t = stat.executeUpdate("update userstocks set numberofshares = numberofshares - '"+shares+"' where accountnumber = '"+accountnumber+"'");
           
       
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
      
     public void updatebuyerV(int id,int shares,double amt){
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
            
            int i = stat.executeUpdate("update orders set status = 'done' where ID ='"+id+"'");
            rs = stat.executeQuery("select * from orders where ID ='"+id+"'");
            String acc ="";
            String sym = "";
            if(rs.next()){
                acc = rs.getString("accountnumber");
                sym = rs.getString("symbol");
                
            }
            boolean hasstockalready = false;
            rs = stat.executeQuery("select * from userstocks where accountnumber = '"+acc+"' and symbol = '"+sym+"'");  
            if(rs.first()){
                hasstockalready = true;
            }
            if(hasstockalready==true){
                i = stat.executeUpdate("update userstocks set numberofshares = numberofshares+ '"+shares+"' where accountnumber = '"+acc+"' and symbol = '"+sym+"'");
            }
            else{
                i = stat.executeUpdate("insert into userstocks values('"+acc+"','"+sym+"','"+shares+"')");
            }
            
            i = stat.executeUpdate("update users set amount = amount - '"+amt+"' where accountnumber = '"+acc+"'");
            
            
       
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
      
     public void updatesellV(int accountnumber,int shares,double amt,String order_type,String place_order_type,double price,String symbol){
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
            
            int i = stat.executeUpdate("update users set amount = amount + '"+amt+"' where accountnumber = '"+accountnumber+"'");
            i = stat.executeUpdate("insert into orders values(null,'"+accountnumber+"','"+symbol+"','"+order_type+"','"+place_order_type+"','"+shares+"','"+price+"','"+price+"','done')");
            
       
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
      
     public void updatesellV1(int accountnumber,double amt){
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
            
            int i = stat.executeUpdate("update users set amount = amount + '"+amt+"' where accountnumber = '"+accountnumber+"'");
           
            
       
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
      
     public void updatebuyV1(int id,int shares,double amt){
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
            
            int i = stat.executeUpdate("update orders set num_of_share = '"+shares+"' where ID ='"+id+"'");
            rs = stat.executeQuery("select * from orders where ID ='"+id+"'");
            String acc ="";
            String sym = "";
            if(rs.next()){
                acc = rs.getString("accountnumber");
                sym = rs.getString("symbol");
                
            }
            boolean hasstockalready = false;
            rs = stat.executeQuery("select * from userstocks where accountnumber = '"+acc+"' and symbol = '"+sym+"'");  
            if(rs.first()){
                hasstockalready = true;
            }
            if(hasstockalready==true){
                i = stat.executeUpdate("update userstocks set numberofshares = numberofshares+ '"+shares+"' where accountnumber = '"+acc+"' and symbol = '"+sym+"'");
            }
            else{
                i = stat.executeUpdate("insert into userstocks values('"+acc+"','"+sym+"','"+shares+"')");
            }
            
            i = stat.executeUpdate("update users set amount = amount - '"+amt+"' where accountnumber = '"+acc+"'");
            
            
       
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
      
     public void updateactivity(int account,String text){
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
            
            int i = stat.executeUpdate("insert into useractivity values('"+account+"','"+text+"') ");
            
       
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
      
     public String returntomarketbuy(){
          msg = "";
          symbol = "";
          num_of_shares = "";
          activity.clear();
          return "MarketorderBuy.xhtml";
      }
      
     public String returntolimitbuy(){
          msg= "";
          symbol = "";
          num_of_shares = "";
          activity.clear();
                  
          return "LimitorderBuy.xhtml";
      }
      
     public String returntomarketsell(){
          msg = "";
          symbol = "";
          num_of_shares = "";
          activity.clear();
          return "MarketorderSell.xhtml";
      }
      
     public String returntolimitsell(){
          msg= "";
          symbol = "";
          num_of_shares = "";
          activity.clear();
          return "LimitorderSell.xhtml";
      }
      
     public String gotochangepassword(){
          msg = "";
          return "ChangePassword.xhtml";
      }
      
     public void changepassword(){
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
            rs = stat.executeQuery("select * from users where loginid = '"+loginid+"' and pass = '"+currentpassword+"'");
            if(rs.next()){
                
                if(newpass.equals(retypenewpass)){
                    int i = stat.executeUpdate("update users set pass='"+newpass+"' where loginid='"+loginid+"'");
                    msg = "Password Changed Sucessfully";
                   
                }
                else{
                    msg = "New Password and Retype Password are not matching";
                }
            }
            else{
                msg = "Wrong Password";
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
      
     public ArrayList displayuserstocks(int acc){
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
        ArrayList<Getuserstocks> last5orders = new ArrayList<>();
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        
        try
        {
            //connect to db
            
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from userstocks where accountnumber = '"+acc+"'");
           while(rs.next()){
               String symbol = rs.getString("symbol");
               int num_of_shares1 = rs.getInt("numberofshares");
               String num_of_shares = "";
               num_of_shares+=num_of_shares1;                         
           last5orders.add(new Getuserstocks(symbol, num_of_shares));
           
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
        return last5orders;
    }
    
     public String gotodisplayuserstocks(){
          msg = "";
          int accountnumb = getaccountnumber(loginid);
          userstocks = displayuserstocks(accountnumb);
          if(userstocks.isEmpty()){
              msg = "You don't have any stocks";
          }
          return "DisplayUserStock.xhtml";
      }
      
     public String gotoviewbalance(){
          int accountnumber = getaccountnumber(loginid);
          double balance = getuserbalance(accountnumber);
          msg = "Your Blance is :"+balance;
          return "DisplayUserBalance.xhtml";
      }
      
     public double getuserbalance(int acc){
       final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        double balance = 0;
        try
        {
            //connect to db
            String a = "";
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from users where accountnumber = '"+acc+"'");
           
           if(rs.next()){
               balance = rs.getDouble("amount");
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
        return balance;
    }
       
     public String logout(){
    loginid = "";
    pass = "";
    wrongpasscount= 0;
    msg= "";
    symbol= "";
    num_of_shares= "";
    price= "";
    orders.clear();
    activity.clear();
    currentpassword = "";
    newpass= "";
    retypenewpass= "";
    userstocks.clear();
    return "index.xhtml";
       }
       
     public String gotoaddbalance(){
           msg = "";          
           
           return "Addbalance.xhtml";
       }
    
     public void addbalance(){
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
        try{
            int accountnumber = getaccountnumber(loginid);
            double b = Double.parseDouble(Addbalnce);           
            if(b>0){
               final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        
        try
        {
            //connect to db
            
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
            int i = stat.executeUpdate("update users set amount = amount + '"+b+"' where accountnumber = '"+accountnumber+"'");            
            msg = "Funds added successfully";
       
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
            else{
              msg = "amount must be greater than zero";
            }
        }
        catch(Exception e){
            msg = "Amount must be a positive number";
        }
          
    }
     
     public String gotosearchstock(){
         searchstock.clear();
         searchstocksymbol = "";
         return "SearchStocks.xhtml";
     }
     
     public void search(){
        
        String sym = searchstocksymbol;
        String comp = getcompanyname(sym);
        searchstock.clear();
        int accountnumer = getaccountnumber(loginid);
        if(!"".equals(comp)){
               msg = "";            
               String recenttrade = getrecenttrade(sym);
               String highbuy = gethighbuy(sym);
               String lowsell = getlowsell(sym);
              searchstock.add(new SearchingStocks(sym, comp, recenttrade, highbuy, lowsell));
        }
        else{
            msg = "Sorry, We didn't find any company with the given symbol";
        }
    }
     
     public String getcompanyname(String symbol){
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
            rs = stat.executeQuery("select * from stocks where symbol = '"+symbol+"'");
           if(rs.first()){
            return rs.getString("company");
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
         return "";
    }
     
     public String getrecenttrade(String symbol){
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
       String recentrade = "No recent trade";
       double p = 0;
       String order = "";
        try
        {
            //connect to db
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
           
            rs = stat.executeQuery("select * from orders where symbol = '"+symbol+"' and status = 'done'");
           while(rs.next()){   
               
               p = rs.getDouble("dealprice");
               order = rs.getString("order_type");
               recentrade = "Type -> "+order +" , Price -> "+ Double.toString(p);
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
            return recentrade;
        }
    }
    
    public String gethighbuy(String symbol){
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
       String recentrade = "No recent listings";
        try
        {
            //connect to db
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
           double  max = 0;
           int id = 0;
            rs = stat.executeQuery("select * from orders where symbol = '"+symbol+"' and order_type = 'buy' and status='pending'");
           while(rs.next()){
               if(rs.getDouble("price")>max){
                   id = rs.getInt("ID");
                   max = rs.getDouble("price");
               }
               
           }
           rs = stat.executeQuery("select * from orders where ID='"+id+"'");
           if(rs.next()){
               int shares = rs.getInt("num_of_shares");
               double p = rs.getDouble("price");
               recentrade = p +"/"+shares;
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
        return recentrade;
    }
    
    public String getlowsell(String symbol){
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
       String recentrade = "No recent Listings";
        try
        {
            //connect to db
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
           double  max = 0;
           int id = 0;
            rs = stat.executeQuery("select * from orders where symbol = '"+symbol+"' and order_type = 'sell' and status='pending' and place_order_type='limit'");
            if(rs.first()){
                max = rs.getDouble("price");
                id=rs.getInt("ID");
            }
            
           while(rs.next()){
               if(rs.getDouble("price")>max){
                   recentrade = rs.getString("price");
                   max = rs.getDouble("price");
                   id = rs.getInt("ID");
               }
               
           }
           rs = stat.executeQuery("select * from orders where symbol = '"+symbol+"' and order_type = 'sell' and status='pending'");
           
           while(rs.next()){
               if(rs.getDouble("price")<max&&rs.getDouble("price")!=0){
                   id = rs.getInt("ID");
                   max = rs.getDouble("price");
               }
           }
           rs = stat.executeQuery("select * from orders where ID='"+id+"'");
           if(rs.next()){
               int shares = rs.getInt("num_of_shares");
               double p = rs.getDouble("price");
               recentrade = p +"/"+shares;
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
        return recentrade;
    }
    
    public void removeitemfromwatchlist(){
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
        int accountnumber = getaccountnumber(loginid);
        boolean isvalidwatch = validwatch(accountnumber, searchstocksymbol);
                if(isvalidwatch==true){
                    final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
      
        try
        {
            //connect to db
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
           
            int t = stat.executeUpdate("delete from watchlist where accountnumber = '"+accountnumber+"' and symbol = '"+searchstocksymbol+"' ");
            msg = "company deleted from your watchlist";
                  
        }
        catch(SQLException e)
        {
            msg= "connection to db failed";
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
                else{
                   msg = "You don't have this stock in your watchlist";
                }
   }
    
    public boolean validwatch(int account,String symbol){
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
        boolean valid=false;
        try
        {
            //connect to db
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();           
            rs = stat.executeQuery("select * from watchlist where accountnumber = '"+account+"' and symbol = '"+symbol+"'");
           if(rs.first()){
            valid = true;
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
         return valid;
    }
        
    public void addtowatchlist(){
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
        
        int accountnumber = getaccountnumber(loginid);
        
      boolean isvalidwatch = validwatch(accountnumber, searchstocksymbol);
      if(isvalidwatch==false){
          final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
          try
        {
            //connect to db
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
           
            int t = stat.executeUpdate("insert into watchlist values('"+accountnumber+"','"+searchstocksymbol+"')");
            msg = "company added to your watchlist";
                  
        }
        catch(SQLException e)
        {
            msg = "connection to Database failed";
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
      else{
          msg = "you already have this company in your watchlist";
      }
        
}

      public void displaywatchlist(){
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/jonnadak5361";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
      int accountnumber = getaccountnumber(loginid);
        try
        {
            //connect to db
            conn = DriverManager.getConnection(DB_URL, "jonnadak5361", "1446146");
            stat = conn.createStatement();
           
            rs = stat.executeQuery("select * from watchlist where accountnumber = '"+accountnumber+"'");
           watchlist.clear();
           while(rs.next()){
               msg = "";
               String comp = getcompanyname(rs.getString("symbol"));
               String recenttrade = getrecenttrade(rs.getString("symbol"));
               String highbuy = gethighbuy(rs.getString("symbol"));
               String lowsell = getlowsell(rs.getString("symbol"));
               watchlist.add(new SearchingStocks(rs.getString("symbol"), comp, recenttrade, highbuy, lowsell));
           }
           if(rs.first()==false){
               msg = "Your Watchlist is empty";
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
      
      public String gotowatchlist(){
          msg = "";
          watchlist.clear();
          displaywatchlist();
          return "DisplayWatchlist.xhtml";
      }
      
       
    
    
    
}
