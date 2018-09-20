package home.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class TicketInfoController {


    /**
     *
     * @param actionEvent
     * @throws IOException
     * The following method brings an unregisgterd user to registration page. There will be checks in place
     * to determine user's status within database. Otherwise after purchase is confirmed this ticket will show
     * up within list of user's tickets all which can be accessed on Your Flights Page
     */

    @FXML
    public void purchaseTicket(ActionEvent actionEvent) throws IOException{
         //String source = ((Node) actionEvent.getSource()).getId();
         //System.out.println(source);
       /// if( source == "confirmBtn"){
            Parent register = FXMLLoader.load(getClass().getResource("../fxml/UserRegistration.fxml"));
            Scene registerScene = new Scene(register);

            Stage regWindow = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
            regWindow.setScene(registerScene);
            regWindow.show();
            System.out.println("Confirm button clicked");
       // }else if(source == "returnBtn"){

        //}


    }

    @FXML
    public void returnPrior(ActionEvent actionEvent) throws IOException{
        Parent prior = FXMLLoader.load(getClass().getResource("../fxml/BookFlights.fxml"));
        Scene priorScene = new Scene(prior);

        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(priorScene);
        window.show();
        System.out.println("Return button clicked");

    }
}
