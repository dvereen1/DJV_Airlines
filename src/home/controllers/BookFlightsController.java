package home.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookFlightsController implements Initializable {

    @FXML
    private ChoiceBox<String> toChoiceBox;

    @FXML
    private ChoiceBox<String> fromChoiceBox;

    @FXML
    private Pane ticketPane;


    /**
     *
     * @param actionEvent
     * @throws IOException
     * The following method takes the user back to the home screen
     */
    public void goHome(javafx.event.ActionEvent actionEvent) throws IOException {

        ////////////The code below will most likely be put into its own method in the Main class because it is reused several times///////
        //we load the fxml file we want the program to pull information from
        Parent home = FXMLLoader.load(getClass().getResource("../fxml/Main.fxml"));
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

        Parent yourFlights = FXMLLoader.load(getClass().getResource("../fxml/YourFlights.fxml"));
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
        Parent ticketInfo = FXMLLoader.load(getClass().getResource("../fxml/TicketInfo.fxml"));
        Scene ticketScene = new Scene(ticketInfo);

        Stage ticketWindow = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        ticketWindow.setScene(ticketScene);
        ticketWindow.show();
        System.out.println("Label clicked");
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
        toChoiceBox.setValue("Atlanta");
        fromChoiceBox.getItems().addAll("Atlanta", "New York", "Las Vegas", "Los Angeles", "Miami");
        fromChoiceBox.setValue("New York");

    }
}
