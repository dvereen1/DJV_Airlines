package main.java.home.controllers;

import main.java.home.DB_Connection.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CancellationController implements Initializable {

    @FXML
    private Label ticketID;

    private int ticketId;
    private int flightId;
    private int mealId;


    private boolean isLoggedIn;
    private String username;
    private String fName;
    private String lName;


    public void setCCUserName(String username, String fName, String lName){
        this.isLoggedIn = true;


        this.username = username;
        this.fName = fName;
        this.lName =lName;
        System.out.println("This si the username from Ticket controller: "+ this.username);
        System.out.println("This si the username from Ticket controller: "+ this.fName);
        System.out.println("This si the username from Ticket controller: "+ this.lName);



    }

    public void setCCTicketId(int ticketId, int flightId){

        String flightIDString = Integer.toString(flightId);
        String ticketIDString = Integer.toString(ticketId);
        String flightTicketID = flightIDString.concat(ticketIDString);

        this.mealId = Integer.parseInt(flightTicketID);
        System.out.println("This is the cancelled meal ID: " + mealId);
        this.ticketId = ticketId;
        this.flightId = flightId;

        ticketID.setText("Ticket # " + ticketId);
    }


    @FXML
    public void cancelFlight(ActionEvent event) throws IOException {

        try{
            String query = "UPDATE Ticket SET owner = NULL, datePurchased = NULL, pDay = NULL, pMonth=NULL, pYear=NULL, bags=NULL WHERE ticketID = ?";
            String query2 = "DELETE FROM Meals where mealID = ?";

            Connection conn = DBConnect.getConnection();
            //Setting ownership to null
            PreparedStatement pr1 = conn.prepareStatement(query);
            pr1.setInt(1, ticketId);
            pr1.execute();

            //Deleting meal associated with ticket
            PreparedStatement pr2 = conn.prepareStatement(query2);
            pr2.setInt(1,mealId);
            pr2.execute();

            conn.close();
        }catch (SQLException err){
            System.err.println("Error: " + err);
        }



        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/YourFLights.fxml"));
        Parent home = loader.load();
        YourFlightsController yfc = loader.getController();
        yfc.getYFUserName(username,fName, lName);

        Scene homeScene = new Scene(home);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.show();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
