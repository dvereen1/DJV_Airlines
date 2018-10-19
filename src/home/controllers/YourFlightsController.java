package home.controllers;

import com.jfoenix.controls.JFXListView;
import home.DB_Connection.DBConnect;
import home.DB_Models.Flights;
import home.DB_Models.Tickets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class YourFlightsController implements Initializable{

    private String username;
    private boolean isLoggedIn;

    @FXML
    private JFXListView<Tickets> ticketListView;

    private ObservableList<Tickets> ticketsData;

    private String queryString = "SELECT flightID, ticketID, sourceID, destinationID FROM Ticket WHERE owner = ?";

    @FXML
    public void goHome(javafx.event.ActionEvent actionEvent) throws IOException {


        Parent home = FXMLLoader.load(getClass().getResource("../fxml/Main.fxml"));
        Scene homeScene = new Scene(home);
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.show();

        //Testing to make sure button click responds
        System.out.println("Home Button Clicked");


    }

    public void getYFUserName(String username){
        this.isLoggedIn = true;


        this.username = username;
        System.out.println("This si the username from Your Flights method: "+ this.username);


    }


    static class Cell extends ListCell<Tickets>{

        HBox hbox = new HBox();

        Label ticketIde =  new Label();


        public Cell(){
            super();
            hbox.getChildren().add(ticketIde);
        }

        public void updateItem(Tickets ticket, boolean empty){

            super.updateItem(ticket, empty);
            setText(null);
            setGraphic(null);

            if(ticket != null || !empty){
                ticketIde.setText(Integer.toString(ticket.getTICKETID()));
                setGraphic(hbox);
            }
        }



    }
    @FXML
    public void loadTickets(){

        try {
            Connection conn = DBConnect.getConnection();
            this.ticketsData = FXCollections.observableArrayList();

            PreparedStatement pr = conn.prepareStatement(queryString);
            pr.setString(1,username);

            ResultSet resultSet = pr.executeQuery();

            while(resultSet.next()){


            this.ticketsData.add(new Tickets(resultSet.getInt(1), resultSet.getInt(2),
                    resultSet.getInt(3), resultSet.getInt(4)));
           }



           this.ticketListView.setItems(ticketsData);
           this.ticketListView.setCellFactory(param -> new Cell());
            pr.close();
            conn.close();

        }catch (SQLException err){
            System.err.println("Error: " + err);

        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
