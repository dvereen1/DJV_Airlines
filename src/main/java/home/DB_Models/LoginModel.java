package main.java.home.DB_Models;

import main.java.home.DB_Connection.DBConnect;

import java.sql.*;
import java.util.Scanner;

import main.java.home.DB_Connection.DBConnect;
import org.mindrot.jbcrypt.BCrypt;


public class LoginModel {

    Connection connection;
    int timeout = 0;


    public LoginModel(){

        //below we are checking connection to database
        try{
            this.connection = DBConnect.getConnection();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        if(this.connection == null){
            System.exit(1);
        }
    }

    public boolean isDatabaseConnected(){
        return this.connection != null;
    }


    public boolean isLogin(String user, String password, String opt) throws Exception{

        //below we are going to write code to access data from database;
       // PreparedStatement pr = null;
        PreparedStatement pr1 = null;
        //PreparedStatement st1;
       // ResultSet resultSet = null;
       // ResultSet rs = null;
        ResultSet resultSet1 = null;

        //below we are selecting which table we want information in our database
        //the question mark is the placeholder
       // String sql = "SELECT * FROM login WHERE userName = ? AND password = ? AND division = ?";
        String verify = "SELECT password FROM login WHERE userName = ? AND division = ? ";

        /*Scanner scan = new Scanner(System.in);

        System.out.println("Enter a password for admin: ");
        String pass = scan.nextLine();
        pass = BCrypt.hashpw(pass, BCrypt.gensalt());
        System.out.println(pass);
        String sqlStr = "UPDATE login SET password = ? WHERE userName = ?";*/



        try{
           //We query the PreparedStatement Obj
           /* pr = this.connection.prepareStatement(sql);
           //We are checking from whether the username which is held in parameter variable user checks out with whats in
            //the database
           pr.setString(1,user);
           pr.setString(2,password);
           pr.setString(3,opt);*/

           pr1 = this.connection.prepareStatement(verify);


           pr1.setString(1, user);
           pr1.setString(2, opt);


           //resultSet = pr.executeQuery("SELECT * FROM login where username =");
           // resultSet = pr.executeQuery();

            resultSet1 = pr1.executeQuery();
           /* st1 = this.connection.prepareStatement(sqlStr);
            st1.setString(1,pass);
            st1.setString(2, "admin1");
            st1.execute();*/
//            System.out.println("In the isLogin Method!! Query executed");

          // boolean bool1;

            String hashedPass = resultSet1.getString(1);
           if (BCrypt.checkpw(password, hashedPass)){
               System.out.println("It matches");
               return true;

           }
            else{
               System.out.println("It does not match");
               return false;
           }



        }catch(SQLException ex){
            return false;
        }
        finally {
            System.out.println("We've reached the finally");
          //  pr.close();
            pr1.close();
           // resultSet.close();
            resultSet1.close();

        }

    }
}
