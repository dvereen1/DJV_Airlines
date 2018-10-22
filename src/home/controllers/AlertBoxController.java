package home.controllers;

import home.DB_Connection.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.sqlite.core.DB;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AlertBoxController implements Initializable {

    @FXML
    private  Label label;
    @FXML
    private Label label1;
    @FXML
    private Label label2;

    private boolean isLoggedIn;
    private String username;
    private String mealType;
    private String mainMeat;
    private String mainCarb;
    private String side;
    private int flightId;
    private int ticketId;
    private String fName;
    private String lName;


    @FXML
    public void closeOK(ActionEvent event) throws IOException{
        try{

            Connection conn = DBConnect.getConnection();

            String queryString = "UPDATE Ticket SET owner = ? WHERE ticketID = ?";
            String insertString = "INSERT INTO Meals(mealID, mealType, mainMeat, mainCarb, side) VALUES (?,?,?,?,?)";

            //Setting values into meal table
            String flightIDString = Integer.toString(flightId);
            String ticketIDString = Integer.toString(ticketId);
            String flightTicketID = flightIDString.concat(ticketIDString);
            int mealId = Integer.parseInt(flightTicketID);

            //Creating prepared statement to save meal options into database
            PreparedStatement pr3 = conn.prepareStatement(insertString);
            pr3.setInt(1,  mealId);
            pr3.setString(2, mealType);
            pr3.setString(3, mainMeat);
            pr3.setString(4, mainCarb);
            pr3.setString(5, side);

            pr3.execute();


            //updating ownership of tickets
            PreparedStatement pr = conn.prepareStatement(queryString);
            pr.setString(1, username);
            pr.setInt(2, ticketId);
            pr.execute();
            conn.close();

        }catch(SQLException err){
            System.err.println("Error: " + err);
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/YourFlights.fxml"));
        Parent root = loader.load();
        YourFlightsController yfc = loader.getController();
        yfc.getYFUserName(username, fName, lName);

        Scene scene = new Scene(root);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void closeNo(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/BookFlights.fxml"));
        Parent root = loader.load();
        BookFlightsController bfc = loader.getController();
        //System.out.println("This is the username from closeNo method: " + username);
        bfc.getBUserName(username, fName, lName);

        Scene scene = new Scene(root);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void displayText(String message){
         label1.setMaxWidth(200);
         label1.setWrapText(true);
            label1.setText(message);

    }

    public void ticketConfirmation(String m1, String m2, String m3 ){


        label.setMaxWidth(200);
        label.setWrapText(true);
        label.setText(m1);

        label2.setMaxWidth(200);
        label2.setWrapText(true);
        label2.setText(m3);

        label1.setMaxWidth(200);
        label1.setWrapText(true);
        label1.setText(m2);

        //Label labels = new Label(m1);
        //Label labels2 = new Label(m2);



    }

    public void mealInfo(String mealType,
                         String mainMeat, String mainCarb, String side, int flightId){

        this.mealType = mealType;
        this.mainMeat = mainMeat;
        this.mainCarb = mainCarb;
        this.side = side;
        this.flightId = flightId;

        System.out.println("This is mealType: " + mealType);
        System.out.println("This is mainMeat " + mainMeat);
        System.out.println("This is mainCarb: " + mainCarb);
        System.out.println("This is side: " + side);
        System.out.println("This is flightId: " + flightId);



    }

    public void setABUserName(String username, String fName, String lName){
        this.isLoggedIn = true;


        this.username = username;
        this.fName = fName;
        this.lName =lName;
        System.out.println("This si the username from Ticket controller: "+ this.username);
        System.out.println("This si the username from Ticket controller: "+ this.fName);
        System.out.println("This si the username from Ticket controller: "+ this.lName);



    }

    public void setABTicketId(int ticketId){
        this.ticketId = ticketId;
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
