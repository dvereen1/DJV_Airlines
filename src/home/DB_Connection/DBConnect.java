package home.DB_Connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    /***
     * Below are variables meant to use for mySql
     */
    private static final String USERNAME  ="dbuser";
    private static final String PASSWORD ="dbpassword";
    private static final String CONN = "jdbc:mysql://localhost/login";
    //This is the path to our database
    //private static final String SQCONN = "jdbc:sqlite:airport.db";
    private static final String SQCONN = "jdbc:sqlite:/Users/darianvereen/airport.db";


    /**
     * This method connects to the database
     */
   public static Connection getConnection() throws SQLException{

       try{
           Class.forName("org.sqlite.JDBC");
           return DriverManager.getConnection(SQCONN);
       }catch(ClassNotFoundException ex){
           ex.printStackTrace();
       }

       return null;
   }
}
