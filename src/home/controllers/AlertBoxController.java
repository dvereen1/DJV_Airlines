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
    private int ticketId;


    @FXML
    public void closeOK(ActionEvent event) throws IOException{
        try{
            String queryString = "UPDATE Ticket SET owner = ? WHERE ticketID = ?";


            Connection conn = DBConnect.getConnection();
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
        yfc.getYFUserName(username);

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
        bfc.getBUserName(username);

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

    public void ticketConfirmation(String m1, String m2, String m3){
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

    public void setABUserName(String username){
        this.isLoggedIn = true;


        this.username = username;
        System.out.println("This si the username from Alertbox method: "+ this.username);


    }

    public void setABTicketId(int ticketId){
        this.ticketId = ticketId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
