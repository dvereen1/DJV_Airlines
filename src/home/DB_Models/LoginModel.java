package home.DB_Models;

import home.DB_Connection.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginModel {

    Connection connection;


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
        PreparedStatement pr = null;
        ResultSet resultSet = null;
        //below we are selecting which table we want information in our database
        //the question mark is the placeholder
        String sql = "SELECT * FROM login WHERE userName = ? AND password = ? AND division = ?";


        try{
           //We query the PreparedStatement Obj
            pr = this.connection.prepareStatement(sql);
           //We are checking from whether the username which is held in parameter variable user checks out with whats in
            //the database
           pr.setString(1,user);
           pr.setString(2,password);
           pr.setString(3,opt);


           //resultSet = pr.executeQuery("SELECT * FROM login where username =");
            resultSet = pr.executeQuery();

           boolean bool1;
           if(resultSet.next()){
               return true;
           }
          return false;

        }catch(SQLException ex){
            return false;
        }
        finally {
            pr.close();
            resultSet.close();
        }

    }
}
