package main.java.home.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import main.java.home.DB_Connection.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

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
    private JFXComboBox expDate;
    @FXML
    private JFXComboBox expDate1;
    @FXML
    private TextField sCode;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private JFXComboBox ccCompCB;

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
    private boolean ticketConfirm;
    boolean reg = false;
    boolean admReg;

    private static ObservableList<Integer> yearArray = FXCollections.observableArrayList();




    //Below is just an array of the variables created above this array will be used in
    //a method to cycle through the class members and pass each into the function Validate





    @FXML
    public void registerReturn(ActionEvent actionEvent) throws IOException{


        if(formValidator(actionEvent)){
            if(admReg){
                FXMLLoader loader =  new FXMLLoader(getClass().getResource("../fxml/Admin.fxml"));
                Parent root = loader.load();
                Stage loginStage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                loginStage.setScene(new Scene(root));
                loginStage.show();
            }else if(!ticketConfirm){
              //Popup to confirm purchase if the page we're coming from is ticket info page
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
          }else{
              FXMLLoader loader =  new FXMLLoader(getClass().getResource("../fxml/Login.fxml"));
              Parent root = loader.load();
              Stage loginStage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
              loginStage.setScene(new Scene(root));
              loginStage.show();
          }

        }





    }

    @FXML
    public void clearAll(){

        fName.clear();
        lName.clear();
        email.clear();
        cCardNum.clear();
        cCardName.clear();
        expDate.setValue(null);
        sCode.clear();
        username.clear();
        password.clear();

    }


    public boolean formValidator(ActionEvent ae) throws IOException{


        String[] cardName;
        String fNameCard =" ";
        String lNameCard =" ";

        if(!cCardName.getText().trim().isEmpty()){

            cardName = this.cCardName.getText().split(" ",2);



            fNameCard = cardName[0];
            try{
                if(cardName[1] != null){
                    lNameCard = cardName[1];
                }
            }catch(Exception ex){

                System.err.println("No Last Name for Credit Card present \n" +
                        "Error: "  + ex );

            }


            System.out.println("first name on card:" + fNameCard  + "\nLast name on card:" + lNameCard);

        }



        if(fName.getText() == null || fName.getText().trim().isEmpty() ||
                lName.getText() == null || lName.getText().trim().isEmpty() ||
                email.getText() == null || email.getText().trim().isEmpty() ||
                cCardName.getText() == null || cCardName.getText().trim().isEmpty() ||
                cCardNum.getText() == null || cCardNum.getText().trim().isEmpty() ||
                sCode.getText() == null || sCode.getText().trim().isEmpty() ||
                username.getText() == null || username.getText().trim().isEmpty() ||
                password.getText() == null || password.getText().trim().isEmpty() ||
                ccCompCB.getValue() == null || expDate.getValue() == null || expDate1.getValue() == null){

            Alert fieldAlert = new Alert(Alert.AlertType.WARNING);
            fieldAlert.setTitle("Empty Fields");
            fieldAlert.setHeaderText(null);
            fieldAlert.setContentText("Please fill in all fields.");
            fieldAlert.showAndWait();
        }
       /*if(validateEmail()){
            reg = true;
        }*/
        if(!this.fName.getText().equals(fNameCard) && !this.lName.getText().equals(lNameCard)){

            // if the user's name does not match the one used for the credit card
            //We are going to launch the custom dialog.

             Alert alert = new Alert(Alert.AlertType.WARNING);
             alert.setTitle("Mismatch names");
             alert.setHeaderText(null);
             alert.setContentText("Name given does not match name on card");
             alert.showAndWait();





        }
        if(!validateCreditCard()){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incorrect Credit Card Number");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid Credit Card number");
            alert.showAndWait();

        }else if(validateEmail()){
            //Customer customer = new Customer();)
            //customer.setfName(fName.getText());
            //customer.toString();
            //System.out.println(customer.getfName());
            reg = true;
            addPassengerToDatabase();


        }
         return reg;
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

            ccCompCB.getItems().addAll("Visa", "MasterCard", "Discover", "American Express", "Diners Club","JCB");
            expDate.getItems().addAll("1", "2","3", "3","4", "5","6", "7","8", "9","10","11","12");
            expDate1.setItems(yearArray);

            System.out.println("TESTING!!!!!");

    }


    public void addPassengerToDatabase(){
        String emailExists = "SELECT * FROM passenger WHERE email = ?";
        String usernameExists = "SELECT * FROM passenger WHERE userName = ?";
        String insertQueryStringPassenger = "INSERT INTO passenger(passengerId, fName, lName, email,userName, cardNum, cardSecNum, cardExpDate, ticketId) VALUES (?,?,?,?,?,?,?,?,?)";
        String insertQueryStringLogin = "INSERT INTO login(userName, password, division) VALUES (?, ?, ?)";
        String queryString = "SELECT MAX(passengerID) FROM passenger";
        String hashedPW= "";
        try{


            Connection conn = DBConnect.getConnection();
            //Creating a prepared statement to determine if email exists.
            PreparedStatement pr = conn.prepareStatement(emailExists);
            pr.setString(1, email.getText());
            ResultSet rs = pr.executeQuery();

            //Creating a prepared statement to determine if username exists
            PreparedStatement pr1 = conn.prepareStatement(usernameExists);
            pr1.setString(1, username.getText());
            ResultSet rs1 = pr1.executeQuery();

            //checking for username validity
            if(rs.next()){
                System.out.println("This is the taken email: " + rs.getString(1));
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Email Unavailable");
                alert.setHeaderText(null);
                alert.setContentText("Enter different email");
                alert.showAndWait();
                reg = false;

            }else if(rs1.next()){
                System.out.println("This is the taken username: " + rs1.getString(1));
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("User name Unavailable");
                alert.setHeaderText(null);
                alert.setContentText("Enter different user name");
                alert.showAndWait();
                reg = false;

            }else{
                System.out.println("EMAIL IS AVAILABLE");
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

                //Below we are hashing the credit card number, sec number, and exp date
                stmt.setString(6, BCrypt.hashpw(cCardNum.getText(),BCrypt.gensalt()));
                stmt.setString(7, BCrypt.hashpw(sCode.getText(),BCrypt.gensalt()));
                //below is the date the date equals exp
                String expD = expDate.getValue().toString().concat("/"+expDate1.getValue().toString());
                System.out.println(expD);
                stmt.setString(8, BCrypt.hashpw(expD,BCrypt.gensalt()));
            /*stmt.setString(6,this.cCardNum.getText());
            stmt.setString(7,this.sCode.getText());
            stmt.setString(8,this.expDate.getText());*/

                stmt.setString(9, "010101");

                //Below are the value that go into the login table
                stmt2.setString(1,this.username.getText());

                //Below we will be hashing the password and entering the hash into the database.
                hashedPW = BCrypt.hashpw(password.getText(), BCrypt.gensalt());
                stmt2.setString(2, hashedPW);
                //stmt2.setString(2, this.password.getText());
                stmt2.setString(3,"Passenger");




                stmt.execute();
                stmt2.execute();
                conn.close();
            }


        }catch(SQLException err){
            err.printStackTrace();
        }




    }

    /**
     *
     * @return
     * returns if email mathces the regex defined with  Pattern
     * [a-zA-Z0-9][a-zA-Z0-9.]*@[a-zA-Z0-9]+([.a-zA-z]+)+
     */
    public boolean validateEmail(){
        Pattern pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-z]+)+");
        Matcher matcher = pattern.matcher(email.getText());
        if(matcher.find() && matcher.group().equals(email.getText())){
           // reg = true;
            return true;
        }else{
            Alert alert =  new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Email");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid Email");
            alert.showAndWait();
           // reg = false;
            return false;
        }
    }

    public boolean validateCreditCard(){

        String cComp = ccCompCB.getValue().toString();
       boolean matches = false;
        switch(cComp){
            case "Visa":
                System.out.println("Visa was selected");
               return creditRegex("4[0-9]{12}(?:[0-9]{3})");

            case "MasterCard":
                System.out.println("MasterCard was selected");
                return creditRegex("5[1-5][0-9]{14}");

            case "Discover":
                System.out.println("Discover was selected");
                return creditRegex("6(?:011|5[0-9]{2})[0-9]{12}");

            case "American Express":
                System.out.println("American Express was selected");
                return creditRegex("3[47][0-9]{13}");


            case "Diners Club":
                System.out.println("Diners Club was selected");
                return creditRegex("3(?:0[0-5]|[68][0-9])?[0-9]{11}");

            case "JCB":
                System.out.println("JCB was selected");
                return creditRegex("(?:2131|1800|35[0-9]{3})[0-9]{11}");


        }


        return false;
    }


    public boolean creditRegex(String reg){
        String regex = reg;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cCardNum.getText());
        System.out.println("This is the credit card number: " + cCardNum.getText());
        if(matcher.find() && matcher.group().equals(cCardNum.getText())){
            System.out.println("Credit card matches");
            return true;
        }else{

            return false;
        }
    }

    public void setYearArray() {

        GregorianCalendar gCal = new GregorianCalendar();
        int currentYear = gCal.get(Calendar.YEAR);
        int endYear = currentYear + 20;

        System.out.println("This is the current year from, USER REGISTRATION: " + currentYear);
        for(int i = currentYear; i < endYear; i++){
            yearArray.add(i);
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
    public void setAdmReg(boolean admReg){
        this.admReg = admReg;
    }

    public void setTicketConfirm(boolean ticketConfirm) {
        this.ticketConfirm = ticketConfirm;
    }


}