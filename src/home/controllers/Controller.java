package home.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    //@FXML
    //Button bookFlightBtn;

    @FXML
    Button timer;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    //When This button is clicked, we move to the BookFlights Scene
    public void getFlightScene(javafx.event.ActionEvent actionEvent) throws IOException{
        //we load the fxml file we want the program to pull information from
        Parent bookFlights = FXMLLoader.load(getClass().getResource("../fxml/BookFlights.fxml"));
        Scene bookFlightScene = new Scene(bookFlights);

        //the following line gets the current stage
        //Here we are casting the actionevent as a Stage then Node object which allows
        //us to get the source and from the source the scene we are trying to reach.
        //from the there we can reach the window.
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(bookFlightScene);
        window.show();

        //Testing to make sure button click responds
        System.out.println("Book Flight Button Clicked");

    }
}
