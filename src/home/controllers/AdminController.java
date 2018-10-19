package home.controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import home.DB_Connection.DBConnect;
import home.DB_Models.Flights;
import home.DB_Models.Passengers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;


public class AdminController implements Initializable{

    @FXML
    private TextField flightId;
    @FXML
    private TextField flightName;
    @FXML
    private TextField sourceId;
    @FXML
    private TextField destId;
    @FXML
    private TextField price;
    @FXML
    private JFXDatePicker departDate;
    @FXML
    private JFXDatePicker arrivalDate;
    @FXML
    private JFXTimePicker departTime;
    @FXML
    private JFXTimePicker arrivalTime;

    @FXML
    private TableView<Flights> flightsTableView;

    @FXML
    private TableColumn<Flights, Integer> flightIdCol;
    @FXML
    private TableColumn<Flights, String> flightNameCol;
    @FXML
    private TableColumn<Flights, Integer> flightSourceIdCol;
    @FXML
    private TableColumn<Flights, Integer> flightDestIdCol;
    @FXML
    private TableColumn<Flights, Date> flightDepartDateCol;
    @FXML
    private TableColumn<Flights, Date> flightArrivalDateCol;
    @FXML
    private TableColumn<Flights, String> flightDepartTimeCol;
    @FXML
    private TableColumn<Flights, String> flightArrivalTimeCol;


    @FXML
    private TableView<Passengers> passengerTableView;
    @FXML
    private TableColumn<Passengers, String> custId;
    @FXML
    private TableColumn<Passengers, String> custFName;
    @FXML
    private TableColumn<Passengers, String> custLName;
    @FXML
    private TableColumn<Passengers, String> custEmail;
    @FXML
    private TableColumn<Passengers, String> custTicketId;


    private DBConnect dc;
    //all the data coming from flights class
    private ObservableList<Flights> flightsData;
    private ObservableList<Passengers> passengersData;

    private String queryString  = "SELECT * FROM Flights";
    private String queryString1  = "SELECT passengerId, fName, lName, email, ticketId FROM passenger";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.dc = new DBConnect();
    }

    @FXML
    private void loadFlightData(ActionEvent event) throws SQLException{
        try{

            Connection conn = DBConnect.getConnection();
            this.flightsData = FXCollections.observableArrayList();

            ResultSet resultSet = conn.createStatement().executeQuery(queryString);

            //we're going to run this loop as the loop checks if there's anything in the table;
            while(resultSet.next()){
                this.flightsData.add(new Flights(resultSet.getInt(1),resultSet.getString(2), resultSet.getInt(3),resultSet.getInt(4),
                        resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),resultSet.getString(8)));
            }

            resultSet.close();
            conn.close();
        }catch (SQLException err){
            System.err.println("Error: " + err);
        }

        this.flightIdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        this.flightNameCol.setCellValueFactory(new PropertyValueFactory<>("NAME"));
        this.flightSourceIdCol.setCellValueFactory(new PropertyValueFactory<>("SOURCEID"));
        this.flightDestIdCol.setCellValueFactory(new PropertyValueFactory<>("DESTID"));
        this.flightDepartDateCol.setCellValueFactory(new PropertyValueFactory<>("DEPARTDATE"));
        this.flightDepartTimeCol.setCellValueFactory(new PropertyValueFactory<>("DEPARTTIME"));
        this.flightArrivalDateCol.setCellValueFactory(new PropertyValueFactory<>("ARRIVALDATE"));
        this.flightArrivalTimeCol.setCellValueFactory(new PropertyValueFactory<>("ARRIVALTIME"));

        this.flightsTableView.setItems(null);
        this.flightsTableView.setItems(this.flightsData);

    }

    @FXML
    private void addFlights(ActionEvent event){
        String insertQueryString = "INSERT INTO Flights(flightID, flightName, sourceID, destinationID, arrivalDate, departureDate, arrivalTime, departureTime) VALUES (?,?,?,?,?,?,?,?)";
        String insertQueryString1 = "INSERT INTO Ticket(flightID, ticketID, sourceID, destinationID, price, purchased) VALUES (?,?,?,?,?,?)";
        try{

            ///Adding flights to database
            Connection conn = DBConnect.getConnection();
            PreparedStatement stmt = conn.prepareStatement(insertQueryString);

            String[] departT = this.departTime.getEditor().getText().split("(:|\\s)");
            String[] arrivalT = this.arrivalTime.getEditor().getText().split("(:|\\s)");

            String departT1 = departT[0].toString().concat(departT[1].toString());
            String arrivalT1 = arrivalT[0].toString().concat(arrivalT[1].toString());
            stmt.setString(1, this.flightId.getText());
            stmt.setString(2, this.flightName.getText());
            stmt.setString(3, this.sourceId.getText());
            stmt.setString(4, this.destId.getText());
            stmt.setString(5, this.arrivalDate.getEditor().getText());
            stmt.setString(6, this.departDate.getEditor().getText());
            stmt.setString(7, this.arrivalTime.getEditor().getText());
            stmt.setString(8, this.departTime.getEditor().getText());


            System.out.println("Departure Time: " + departT1);

            System.out.println("Arrival Time: " + arrivalT1);

            stmt.execute();
            stmt.close();


            ///Adding tickets to the database

            PreparedStatement stmt1 = conn.prepareStatement(insertQueryString1);

            stmt1.setString(1, this.flightId.getText());
            stmt1.setString(3, this.sourceId.getText());
            stmt1.setString(4, this.destId.getText());
            stmt1.setString(5, this.price.getText());
            stmt1.setBoolean(6, false);


            for(int i = 0; i < 2; i++){
                stmt1.setString(2, this.flightId.getText() + "" + i);
                stmt1.execute();

            }
            conn.close();



        }catch (SQLException err){
            err.printStackTrace();
        }
    }

    @FXML
    private void clearFields(ActionEvent event){
        this.flightId.setText("");
        this.flightName.setText("");
        this.sourceId.setText("");
        this.destId.setText("");
        this.arrivalTime.setValue(null);
        this.departTime.setValue(null);
        this.arrivalDate.setValue(null);
        this.departDate.setValue(null);
    }

    @FXML
    private void loadPassengersData(ActionEvent event) throws SQLException{
        try{

            Connection conn = DBConnect.getConnection();
            this.passengersData = FXCollections.observableArrayList();

            ResultSet resultSet = conn.createStatement().executeQuery(queryString1);

            //we're going to run this loop as the loop checks if there's anything in the table;
            //resultSet is a tabular form of the data in the database, getNext method moves the cursor to the next row.
            //then we add whats in each resultSet row  to the observable list called passengersData;
            while(resultSet.next()){
                this.passengersData.add(new Passengers(resultSet.getString(1),resultSet.getString(2), resultSet.getString(3),resultSet.getString(4),
                        null, resultSet.getString(5)));
            }

            conn.close();
        }catch (SQLException err){
            System.err.println("Error: " + err);
        }

        this.custId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.custFName.setCellValueFactory(new PropertyValueFactory<>("fName"));
        this.custLName.setCellValueFactory(new PropertyValueFactory<>("lName"));
        this.custEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.custTicketId.setCellValueFactory(new PropertyValueFactory<>("ticketId"));


        this.passengerTableView.setItems(null);
        this.passengerTableView.setItems(this.passengersData);



    }

    @FXML
    private void deteleData(ActionEvent e){

        String deleteQuery = "DELETE FROM Flights WHERE flightID = ?";

        try{
            Connection conn = DBConnect.getConnection();

            PreparedStatement stmt = conn.prepareStatement(deleteQuery);


            stmt.setString(1, this.flightId.getText());
            stmt.execute();

            conn.close();

        }catch(SQLException err){

            err.printStackTrace();
        }

    }

    @FXML
    private void logOut(ActionEvent actionEvent) throws IOException{
        Parent splashPage = FXMLLoader.load(getClass().getResource("../fxml/SplashPage.fxml"));
        Scene splash = new Scene(splashPage);

        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.close();

        window.setScene(splash);


        window.show();
        System.out.println("Log Out button clicked");


    }


    @FXML
    private void addPassenger(ActionEvent actionEvent) throws IOException{
        Parent addPass =  FXMLLoader.load(getClass().getResource("../fxml/BookPassenger.fxml"));
        Scene bookPass = new Scene(addPass);

        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.close();
        window.setScene(bookPass);
        window.show();
        System.out.print("Add Passenger button clicked");

    }
}
