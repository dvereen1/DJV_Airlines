package home.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookFlightsController implements Initializable {

    @FXML
    private ChoiceBox<String> toChoiceBox;

    @FXML
    private ChoiceBox<String> fromChoiceBox;


    /**
     * The following method returns User back to main page
     *
     */
    public void goHome(javafx.event.ActionEvent actionEvent) throws IOException {
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

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        toChoiceBox.getItems().addAll("Atlanta", "New York", "Las Vegas", "Los Angeles", "Denver");
        toChoiceBox.setValue("Atlanta");
        fromChoiceBox.getItems().addAll("Atlanta", "New York", "Las Vegas", "Los Angeles", "Denver");
        fromChoiceBox.setValue("New York");

    }
}
