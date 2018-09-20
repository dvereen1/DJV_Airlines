package home.controllers;

import home.DB_Models.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserRegistrationController implements Initializable{

    @FXML
    private TextField fName;
    @FXML
    private TextField lName;
    @FXML
    private TextField pNumber;
    @FXML
    private TextField emailAd;
    @FXML
    private TextField cardNum;
    @FXML
    private TextField cardName;
    @FXML
    private TextField expDate;
    @FXML
    private TextField secCode;


    //Below is just an array of the variables created above this array will be used in
    //a method to cycle through the class members and pass each into the function Validate





    @FXML
    public void registerReturn(ActionEvent actionEvent) throws IOException{


        if(formValidator(actionEvent)){
            Parent register = FXMLLoader.load(getClass().getResource("../fxml/BookFlights.fxml"));
            Scene bookFlightScene = new Scene(register);
            Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(bookFlightScene);
            window.show();
            System.out.println("Register Button Clicked clicked");
        }





    }



    public boolean formValidator(ActionEvent ae){
        boolean reg = false;

        if(fName.getText().isEmpty()){

            fName.setText("Fill this in");
            //System.out.println("The First Name field is left blank");
        }else{
            Customer customer = new Customer();
            customer.setfName(fName.getText());
            //customer.toString();
            System.out.println(customer.getfName());
            reg = true;
        }
         return reg;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }



}