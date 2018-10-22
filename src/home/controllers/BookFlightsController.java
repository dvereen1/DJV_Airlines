package home.controllers;

import com.jfoenix.controls.*;
import home.DB_Connection.DBConnect;
import home.DB_Models.Flights;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BookFlightsController implements Initializable {

    @FXML
    private JFXComboBox<String> toChoiceBox;

    @FXML
    private JFXComboBox<String> fromChoiceBox;

    @FXML
    private JFXListView<Flights> flightsListView;

    @FXML
    private JFXDatePicker dateLeave;
    @FXML
    private JFXTimePicker timeLeave;

    private ObservableList<Flights> flightsData;
    private String queryString = "SELECT flightName, departureTime, arrivalTime  FROM Flights WHERE sourceID = ? " +
            "AND destinationID = ? AND departureDate = ? AND departureTime >= ?";

    //private String queryString = "SELECT flightName, departureTime, arrivalTime  FROM Flights WHERE sourceID = ? " +
            //"AND destinationID = ? AND departureDate = ? AND departureTime = ?";

    private Controller controller = new Controller();
    private boolean isLoggedIn;
    private String fName;
    private String lName;
    private String username;
    private Flights flier;

    /**
     *
     * @param actionEvent
     * @throws IOException
     * The following method takes the user back to the home screen
     */
    public void goHome(javafx.event.ActionEvent actionEvent) throws IOException {

        ////////////The code below will most likely be put into its own method in the Main class because it is reused several times///////
        //we load the fxml file we want the program to pull information from
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Main.fxml"));
        Parent home = loader.load();
        Controller mc = loader.getController();
        if(isLoggedIn){
            mc.getPassengerInfo(username, fName, lName);
        }


        Scene homeScene = new Scene(home);

        //the following line gets the current stage
        //Here we are casting the actionevent as a Stage then Node object which allows
        //us to get the source and from the source the scene we are trying to reach.
        //from the there we can reach the window.
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.show();

        //Testing to make sure button click responds
        System.out.println("Home Button Clicked");
        ////////////////////////////////////////////////////////////////////////////////////////

    }

    /**
     *
     * @param actionEvent
     * @throws IOException
     * This method brings user to list of flights.
     */
    @FXML
    public void getYourFlightsScene(javafx.event.ActionEvent actionEvent) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/YourFlights.fxml"));
        Parent yourFlights = loader.load();
        YourFlightsController yfc = loader.getController();
        if(isLoggedIn){
            yfc.getYFUserName(username, fName, lName);

        }

        Scene yourFlightsScene = new Scene(yourFlights);
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(yourFlightsScene);
        window.show();

        //Testing to make sure button click responds
        System.out.println("Your Flight Button Clicked");

    }

    /**
     *
     * @param mouseEvent
     * @throws IOException
     * The following method takes user to the ticket info page for possible confirmation..later on there will be checks in place
     * to determine whether a user has already signed in (is registered) or has  not.
     *
     */
    @FXML
    public void getTicketInfo(MouseEvent mouseEvent) throws IOException{

        int sourceId = getSourceDestId(this.fromChoiceBox.getValue().toString());
        int destId = getSourceDestId(this.toChoiceBox.getValue().toString());





        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/TicketInfo.fxml"));
        Parent ticketInfo = loader.load();

        TicketInfoController tic = loader.getController();
        tic.displayTicketInfo(sourceId, destId, flier.getDEPARTTIME(),
         flier.getARRIVALTIME(), flier.getNAME());
        if(isLoggedIn){
            tic.getTUserName(username, fName, lName);
        }


        Scene ticketScene = new Scene(ticketInfo);

        Stage ticketWindow = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        ticketWindow.setScene(ticketScene);
        ticketWindow.show();
        System.out.println("Is a user logged in? " + isLoggedIn);
        System.out.println("The Ticket id is: " + username);
        System.out.println("Label clicked");

    }




    static class Cell extends ListCell<Flights> {

        HBox hbox = new HBox(10);

        Label flightName =  new Label();
        Label flightDepartTime =  new Label();
        Label flightArriveTime =  new Label();




        public Cell(){
            super();
            //hbox.setHgrow();
            hbox.getChildren().addAll(flightName,flightDepartTime, flightArriveTime);

        }


        public  void updateItem(Flights flight, boolean empty){

            super.updateItem(flight, empty);
            setText(null);
            setGraphic(null);
            if(flight != null && !empty){
                //setText(flight.getNAME());
                flightName.setText(flight.getNAME());
                flightDepartTime.setText(flight.getDEPARTTIME());
                flightArriveTime.setText(flight.getARRIVALTIME());
                setGraphic(hbox);
            }


        }

    }
    @FXML
    public void loadFlightData(){

        int sourceId = getSourceDestId(this.fromChoiceBox.getValue().toString());
        int destId = getSourceDestId(this.toChoiceBox.getValue().toString());
        System.out.println("Source name is : " + this.fromChoiceBox.getValue().toString());
        System.out.println("Destination name is : " + this.toChoiceBox.getValue().toString());




        String departT = timeLeave.getEditor().getText().toString();
        //Here we are retrieving the hour at which the passenger wants to leave
        String[] departTA = this.timeLeave.getEditor().getText().split(":");
        System.out.println("First index in departTA array: " + departTA[0]);
        System.out.println("departT : " + departT);

        int timeRange1; // (Integer.parseInt(departTA[0]) + 5) - 12;

        //Below we set timeRange1 equal to the the time the passenger wants to leave
        //plus 5 additional hours
        if(Integer.parseInt(departTA[0]) + 5 > 12){
            timeRange1 = (Integer.parseInt(departTA[0]) + 5) - 12;
        }else{
             timeRange1 = (Integer.parseInt(departTA[0]) + 5);
        }
        /*if(timeRange1 < 0){
            timeRange1 = Math.abs(timeRange1);
        }*/

        departTA[0] = Integer.toString(timeRange1);
        String finalT = departTA[0] + ":" + departTA[1];


       String departD = dateLeave.getEditor().getText().toString();

        System.out.println("This is the time start range to leave: " + departT);
        System.out.println("This is the time end range to leave: " + finalT);

        try{

            Connection conn = DBConnect.getConnection();
            this.flightsData = FXCollections.observableArrayList();

            PreparedStatement pr = conn.prepareStatement(queryString);
            pr.setInt(1, sourceId);
            pr.setInt(2, destId);
            pr.setString(3, departD);
            pr.setString(4, departT);
            //pr.setString(5, finalT);

            ResultSet resultSet = pr.executeQuery();

            while(resultSet.next()){
               // this.flightsData.add(resultSet.getString(1));
               this.flightsData.add(new Flights(resultSet.getString(1),
                       resultSet.getString(2), resultSet.getString(3)));
              //String flightName =  resultSet.getString(1);
            }

           // this.flightsData.add(new Flights(resultSet.getString(1),
                 //   resultSet.getString(2), resultSet.getString(3)));

            this.flightsListView.setItems(this.flightsData);

           this.flightsListView.setCellFactory(param -> new Cell());
            //System.out.println("First flight: " + flightsData.get(0).getNAME());
            //System.out.println("Second flight: " + flightsData.get(1).getNAME());

            pr.close();
            //resultSet.close();
            conn.close();

        }catch (SQLException err){
            System.err.println("Error: " + err);
        }

        flightsListView.setOnMouseClicked(event -> {

            flier = flightsListView.getSelectionModel().getSelectedItem();
            try{
                getTicketInfo(event);
            }catch(IOException err){
                System.err.print("Error: " + err);
            }

             //System.out.println(flier.getARRIVALTIME());
        });


    }





    public int getSourceDestId(String sourceOrDest){

        int id = 0;

        switch(sourceOrDest){
            case "Atlanta":
                id = 0;
                break;
            case "Los Angeles":
                id = 1;
                break;
            case "New York":
                id = 2;
                break;
            case  "Miami":
                id = 3;
                break;
            case "Las Vegas":
                id = 4;
                break;
        }

        System.out.println("The source or destination id is: " + id);

        return id;
    }
    /**
     *
     * @param location
     * @param resources
     * The initialize method give values to the different component of this scene as soon as it loads
     * This allows for dynamic changes of labels,etc.
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        toChoiceBox.getItems().addAll("Atlanta", "New York", "Las Vegas", "Los Angeles", "Miami");
        toChoiceBox.setValue("Choose");
        fromChoiceBox.getItems().addAll("Atlanta", "New York", "Las Vegas", "Los Angeles", "Miami");
        fromChoiceBox.setValue("Choose");





    }


    public void getBUserName(String username, String fName, String lName){
        this.isLoggedIn = true;


        this.username = username;
        this.fName =fName;
        this.lName =lName;
        System.out.println("This si the ticket id from getBPassengerInfo method: "+ this.username);
        System.out.println("This si the ticket id from getBPassengerInfo method: "+ this.fName);
        System.out.println("This si the ticket id from getBPassengerInfo method: "+ this.lName);


    }



}
