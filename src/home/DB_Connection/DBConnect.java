package home.DB_Connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    /**
     * This method connects to the database
     */
    public static void connect(){
        Connection connect = null;
        try{
            //database parameters
            String url = "jdbc:sqlite:/Users/darianvereen/testDB.db";
            connect = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite database successful");

        }catch(SQLException error){
            System.out.println(error.getMessage());
        } finally {
            try{
                if(connect != null){
                    connect.close();
                }
            }catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        }
    }
}
