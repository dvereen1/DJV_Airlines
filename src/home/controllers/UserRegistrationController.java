package home.controllers;

import home.DB_Connection.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserRegistrationController implements Initializable{

    @FXML
    private TextField fName;
    @FXML
    private TextField lName;
    @FXML
    private TextField email;
    @FXML
    private TextField cCardNum;
    @FXML
    private TextField cCardName;
    @FXML
    private TextField expDate;
    @FXML
    private TextField sCode;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    private int ticketID;
    private int flightId;
    private String source;
    private String dest;
    private String depart;
    private String arrive;
    private String price;
    private String mealType;
    private String mainMeat;
    private String mainCarb;
    private String side;


    //Below is just an array of the variables created above this array will be used in
    //a method to cycle through the class members and pass each into the function Validate





    @FXML
    public void registerReturn(ActionEvent actionEvent) throws IOException{


        if(formValidator(actionEvent)){
            /*Parent register = FXMLLoader.load(getClass().getResource("../fxml/BookFlights.fxml"));
            Scene bookFlightScene = new Scene(register);
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(bookFlightScene);
            window.show();
            System.out.println("Register Button Clicked clicked");*/


            //Popup to confirm purchase
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/AlertBox.fxml"));
            Parent root = loader.load();
            AlertBoxController ab = loader.getController();
            System.out.println("This is the ticket id: " + this.ticketID);
            ab.ticketConfirmation("Confirm purchase of $" + price + " for ticket # " + Integer.toString(this.ticketID), "from " + this.source,
                   "to "+ this.dest);
            ab.setABTicketId(ticketID);

            ab.setABUserName(username.getText(), fName.getText(), lName.getText());
            ab.mealInfo(mealType,mainMeat, mainCarb, side, flightId);
            /*Dialog dialog =  new Dialog();

            //dialog.setWidth(600);
            //dialog.setHeight(400);
            dialog.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
            dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            dialog.getDialogPane().setContent(root);
            dialog.show();*/

            Scene scene = new Scene(root);
            Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }





    }



    public boolean formValidator(ActionEvent ae) throws IOException{
        boolean reg = false;

           String[] cardName = this.cCardName.getText().split(" ",2);
        //String fNameCard = "";
       // String lNameCard = "";


           String fNameCard = cardName[0].toString();
            String lNameCard = cardName[1].toString();
           System.out.println("first name on card:" + fNameCard  + "\nLast name on card:" + lNameCard);

        if(this.fName.getText().isEmpty()){
            //If the fields are blank this if block will be executed....I wonder if there is a way to get all fields collectively and if
            //one is blank trigger this if block instead of creating a whole bunch of if statements for each textfield

            this.fName.setText("Fill this in");
            //System.out.println("The First Name field is left blank");
        }else if(!this.fName.getText().equals(fNameCard) && !this.lName.getText().equals(lNameCard)){

            // if the user's name does not match the one used for the credit card
            //We are going to launch the custom dialog.


            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/AlertBox.fxml"));
            Parent root = loader.load();
            AlertBoxController ab = loader.getController();
            ab.displayText("Name given does not match name on card.");
            Dialog dialog =  new Dialog();
           //dialog.setWidth(600);
            //dialog.setHeight(400);
            dialog.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
            dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            dialog.getDialogPane().setContent(root);
            dialog.show();



        }else{
            //Customer customer = new Customer();)
            //customer.setfName(fName.getText());
            //customer.toString();
            //System.out.println(customer.getfName());
            addPassengerToDatabase();
            reg = true;
        }
         return reg;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

            System.out.println("TESTING!!!!!");

    }


    public void addPassengerToDatabase(){
        String insertQueryStringPassenger = "INSERT INTO passenger(passengerId, fName, lName, email,userName, cardNum, cardSecNum, cardExpDate, ticketId) VALUES (?,?,?,?,?,?,?,?,?)";
        String insertQueryStringLogin = "INSERT INTO login(userName, password, division) VALUES (?, ?, ?)";
        String queryString = "SELECT MAX(passengerID) FROM passenger";
        try{
            Connection conn = DBConnect.getConnection();
            PreparedStatement stmt = conn.prepareStatement(insertQueryStringPassenger);
            PreparedStatement stmt2 = conn.prepareStatement(insertQueryStringLogin);
            //Fetching Maximum passenger id so that a new passenger gets 1 + max id for their id
            ResultSet resultSet = conn.createStatement().executeQuery(queryString);
            int passID = resultSet.getInt(1)+1;

            //Now we fill in the values in our queRY STRING that were held in place with question marks
            //Need to Create a method to randomnly generate ticket ids and passenger id
            stmt.setInt(1, passID);
            stmt.setString(2,this.fName.getText());
            stmt.setString(3,this.lName.getText());
            stmt.setString(4,this.email.getText());
            stmt.setString(5,this.username.getText());
            stmt.setString(6,this.cCardNum.getText());
            stmt.setString(7,this.sCode.getText());
            stmt.setString(8,this.expDate.getText());
            stmt.setString(9, "010101");

            //Below are the value that go into the login table
            stmt2.setString(1,this.username.getText());
            stmt2.setString(2, this.password.getText());
            stmt2.setString(3,"Passenger");




            stmt.execute();
            stmt2.execute();
            conn.close();

        }catch(SQLException err){
            err.printStackTrace();
        }




    }


   public void getTicketInfo(int id, String source, String dest, String depart, String arrive, String price){
        this.ticketID = id;
        this.source = source;
        this.dest = dest;
        this.depart = depart;
        this.arrive = arrive;
        this.price = price;
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



}