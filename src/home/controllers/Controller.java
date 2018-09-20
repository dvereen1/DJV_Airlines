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


    /**
     *
     * @param actionEvent
     * @throws IOException
     * When This button is clicked, we move to the BookFlights Scene
     */
    @FXML
    public void getBookFlightScene(javafx.event.ActionEvent actionEvent) throws IOException{
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

    /**
     *
     * @param actionEvent
     * @throws IOException
     * This method brings user to list of flights
     */
    @FXML
    public void getYourFlightsScene(javafx.event.ActionEvent actionEvent) throws IOException{

        Parent yourFlights = FXMLLoader.load(getClass().getResource("../fxml/YourFlights.fxml"));
        Scene yourFlightsScene = new Scene(yourFlights);
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(yourFlightsScene);
        window.show();

        //Testing to make sure button click responds
        System.out.println("Your Flight Button Clicked");

    }


}
