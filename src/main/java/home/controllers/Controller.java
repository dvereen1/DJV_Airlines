package main.java.home.controllers;

import main.java.home.DB_Models.Passengers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
    private ImageView imgView1;
    @FXML
    private ImageView imgView2;
    @FXML
    private ImageView imgView3;
    @FXML
    private AnchorPane pane1;
    @FXML
    private Pane confirmationHolder;
    @FXML
    private Label confirmationLabel;
    @FXML
    private Label firstLastName;



    private boolean isLoggedIn;
    private String fName;
    private String lName;

    private String username;

    String text;


    Passengers currentPassenger;







    @Override
    public void initialize(URL location, ResourceBundle resources) {

            Image img1 = new Image(getClass().getResource("../../res/cockpit.jpg").toExternalForm(),true);
            Image img2 = new Image(getClass().getResource("../../res/wing2.jpg").toExternalForm(),true);
            Image img3 = new Image(getClass().getResource("../../res/wing3.jpg").toExternalForm(),true);

            imgView1.setPreserveRatio(false);
            imgView2.setPreserveRatio(false);
            imgView3.setPreserveRatio(false);
            imgView1.setImage(img1);
            imgView2.setImage(img2);
            imgView3.setImage(img3);

            if(!isLoggedIn){
                //pane1.getChildren().remove(confirmationHolder);
                //pane1.getChildren().remove(confirmationLabel);
                firstLastName.setText("Guest");
            }else{
                 //pane1.getChildren().(confirmationHolder);
                // pane1.getChildren().add(confirmationLabel);
                firstLastName.setText(fName + " " + lName);

            }








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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/BookFlights.fxml"));
        Parent bookFlights = loader.load();

        BookFlightsController bfc = loader.getController();
        if(isLoggedIn){
            bfc.getBUserName(username, fName, lName);
        }


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
        System.out.println("First name from getBookFlightScene method"+fName);

        isLog();

    }

    /**
     *
     * @param actionEvent
     * @throws IOException
     * This method brings user to list of flights
     */
    @FXML
    public void getYourFlightsScene(javafx.event.ActionEvent actionEvent) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/YourFlights.fxml"));
        Parent yourFlights = loader.load();
        YourFlightsController yfc = loader.getController();
        yfc.getYFUserName(username, fName, lName);


        Scene yourFlightsScene = new Scene(yourFlights);
        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(yourFlightsScene);
        window.show();

        //Testing to make sure button click responds
        System.out.println("Your Flight Button Clicked");
        System.out.println("This is the username from main page: " + this.username);

    }


    @FXML
    private void logOut(ActionEvent actionEvent) throws IOException{
        fName = null;
        lName = null;
        username = null;
        isLoggedIn = false;

        Parent splashPage = FXMLLoader.load(getClass().getResource("../fxml/SplashPage.fxml"));
        Scene splash = new Scene(splashPage);

        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.close();

        window.setScene(splash);


        window.show();
        System.out.println("Log Out button clicked");


    }

    public void getPassengerInfo(String username, String fName, String lName){


        this.isLoggedIn = true;
        firstLastName.setText(fName + " " + lName);

        this.fName = fName;
        this.lName = lName;
        this.username = username;
        System.out.println("This si the username from getPassengerInfo method: "+ this.username);


    }

   public void isLog(){


        if(this.isLoggedIn){
            System.out.println("Passenger logged in");
           // firstLastName.setText(fName + " " + lName);
        }
   }

   public boolean getLog(){ return isLoggedIn; }


}
