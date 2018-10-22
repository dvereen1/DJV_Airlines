package home.controllers;

import com.jfoenix.controls.JFXListView;
import home.DB_Connection.DBConnect;
import home.DB_Models.Flights;
import home.DB_Models.Tickets;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
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

    @FXML
    private Label ticketID;
    @FXML
    private Label source;
    @FXML
    private Label dest;

    @FXML
    private JFXListView<Tickets> ticketListView;

    private ObservableList<Tickets> ticketsData;

    private String username;
    private boolean isLoggedIn;
    private int ticketId;
    private int flightId;
    private int sourceID;
    private int destID;
    private String fName;
    private String lName;


    private String queryString = "SELECT flightID, ticketID, sourceID, destinationID FROM Ticket WHERE owner = ?";

    @FXML
    public void goHome(javafx.event.ActionEvent actionEvent) throws IOException {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Main.fxml"));
        Parent home = loader.load();
        Controller mc = loader.getController();
        mc.getPassengerInfo(username, fName, lName);

        Scene homeScene = new Scene(home);
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.show();

        //Testing to make sure button click responds
        System.out.println("Home Button Clicked");


    }


    public void getYFUserName(String username, String fName, String lName){
        this.isLoggedIn = true;


        this.username = username;
        this.fName = fName;
        this.lName =lName;
        System.out.println("This si the username from Ticket controller: "+ this.username);
        System.out.println("This si the username from Ticket controller: "+ this.fName);
        System.out.println("This si the username from Ticket controller: "+ this.lName);


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
                ticketIde.setText("Ticket # " +
                        Integer.toString(ticket.getTICKETID())+ " - Click for More Info");
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

    @FXML
    public void cancelFlight(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Cancellation.fxml"));
        Parent root = loader.load();

        CancellationController cc = loader.getController();
        cc.setCCUserName(username, fName, lName);
        cc.setCCTicketId(ticketId, flightId);


        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

         //tickets = ticketListView.getSelectionModel().getSelectedItem();
         //System.out.println(tickets.getTICKETID());

       ticketListView.setOnMouseClicked(e ->{
           ticketId = ticketListView.getSelectionModel().getSelectedItem().getTICKETID();
           System.out.println("This is the ticket id from passenger ticket list: " + ticketId);
           showTicketDetails();
       });


    }



    public void showTicketDetails(){

        try{
            Connection conn = DBConnect.getConnection();
            String query1 = "SELECT sourceID, destinationID, flightID  FROM Ticket WHERE ticketID = ?";
            String query2 = "SELECT departureTime, arrivalTime, departureDate, arrivalDate FROM Flights " +
                    "WHERE flightID = ?";
           // String query2 = "SELECT sourceID, destinationID, departureTime, arrivalTime, " +
                //    "departureDate, arrivalDate FROM Flights WHERE (SELECT flightID FROM Ticket WHERE ticketID = ?)";


            PreparedStatement pr1 = conn.prepareStatement(query1);
            pr1.setInt(1, ticketId);
            ResultSet rs1 = pr1.executeQuery();

            sourceID = rs1.getInt(1);
            destID = rs1.getInt(2);
            flightId = rs1.getInt(3);
            System.out.println("This is the flight id: " + flightId);

            ticketID.setText("Ticket # " + ticketId);


            //Getting flight details
            PreparedStatement pr2 = conn.prepareStatement(query2);
            pr2.setInt(1, flightId);
            ResultSet rs2 = pr2.executeQuery();

            source.setText(getSourceDestName(sourceID) + "\nDeparting At:\n"
                    + rs2.getString(1) + "\non:\n" + rs2.getString(3));

            dest.setText(getSourceDestName(destID) + "\nArriving At:\n"
                    + rs2.getString(2) + "\non:\n" + rs2.getString(3));






            conn.close();

        }catch (SQLException err){
            System.out.println("Error: " + err );
        }
    }



    public String getSourceDestName(int sourceOrDest){

        String city = "";

        switch(sourceOrDest){
            case  0:
                city = "Atlanta";
                break;
            case 1:
                city = "Los Angeles";
                break;
            case 2:
                city = "New York";
                break;
            case  3:
                city = "Miami";
                break;
            case 4:
                city = "Las Vegas";
                break;
        }

        System.out.println("The source or destination id is: " + city);

        return city;
    }
}
