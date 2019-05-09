package main.java.home.controllers;

import main.java.home.DB_Connection.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SplashPage {

    @FXML
    public void goMain(ActionEvent actionEvent) throws IOException{

        Parent homePage = FXMLLoader.load(getClass().getResource("../fxml/Main.fxml"));
        Scene home = new Scene(homePage);

        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.close();

        window.setScene(home);
        window.show();
        System.out.println("Begin Journey Button Clicked");

    }


    @FXML
    public void goLogin(ActionEvent actionEvent) throws IOException{
        Parent loginPage = FXMLLoader.load(getClass().getResource("../fxml/Login.fxml"));
        Scene login = new Scene(loginPage);

        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.close();

        window.setScene(login);
        window.show();
        System.out.println("Login Clicked");

    }


    public void updateFlights(){
        ArrayList<String> pastFlights = new ArrayList();
        ArrayList<String> pastTicketPrices = new ArrayList<>();
        GregorianCalendar gCal = new GregorianCalendar();
        int currentMonth = gCal.get(Calendar.MONTH) + 1;
        int day = gCal.get(Calendar.DATE);
        int currentYear = gCal.get(Calendar.YEAR);
        String currentDate = Integer.toString(currentMonth).concat("/" + Integer.toString(day)
                + "/" + currentYear);
        System.out.println("The date is: " +  currentDate);

        try{
           String queryString = "SELECT flightID FROM Flights WHERE arrivalDate < ?";
           //String queryString2 = "SELECT price FROM Ticket WHERE flightID = (SELECT flightID FROM Flights WHERE arrivalDate < ?)";
            String queryString2 = "SELECT price FROM Ticket WHERE flightID IN (SELECT flightID FROM Flights WHERE arrivalDate < ?) AND owner IS NOT NULL ";

           Connection conn = DBConnect.getConnection();
            PreparedStatement pr1 = conn.prepareStatement(queryString);
            pr1.setString(1,currentDate);

            ResultSet rs = pr1.executeQuery();
            while(rs.next()){
                pastFlights.add(rs.getString(1));
            }


            rs.close();

            //Getting all past ticket prices

            PreparedStatement pr2 = conn.prepareStatement(queryString2);



            pr2.setString(1, currentDate);
            ResultSet rs1 =  pr2.executeQuery();
            while(rs1.next()){
                pastTicketPrices.add(rs1.getString(1));
            }
            conn.close();



            for(String id : pastFlights){
                System.out.println("Flights that have already landed: " + id);
            }
            for(String price : pastTicketPrices){
                System.out.println("Past ticket prices: " + price);
            }

        }catch(SQLException err){
            System.err.println("Error: " + err);
        }
    }
}
