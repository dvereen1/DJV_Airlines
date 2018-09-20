package home.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class YourFlightsController {

    @FXML
    public void goHome(javafx.event.ActionEvent actionEvent) throws IOException {


        Parent home = FXMLLoader.load(getClass().getResource("../fxml/Main.fxml"));
        Scene homeScene = new Scene(home);
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.show();

        //Testing to make sure button click responds
        System.out.println("Home Button Clicked");


    }



}
