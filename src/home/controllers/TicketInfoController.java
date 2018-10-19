package home.controllers;

import home.DB_Connection.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TicketInfoController implements Initializable{



        @FXML
        private Label ticketId;

        @FXML
        private Label source;

        @FXML
        private Label destination;

        @FXML
        private Label price;

        @FXML
        private Label departTime;

        @FXML
        private Label arrivalTime;

        @FXML
        private ImageView sourceImg;

        @FXML
        private ImageView destImg;

        private int ticketID;
        private boolean isLoggedIn;
        private String username;
        private String tPrice;




      private String queryString = "SELECT flightID FROM Flights WHERE flightName= ? AND departureTime = ? AND arrivalTime = ?";
      private String queryString1 = "SELECT MIN(ticketID) FROM Ticket WHERE owner IS null";
      private String queryString2 =  "SELECT price FROM Ticket WHERE ticketID = ?";


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



            if(!isLoggedIn){

                FXMLLoader loader =  new FXMLLoader(getClass().getResource("../fxml/UserRegistration.fxml"));
                Parent register = loader.load();
                UserRegistrationController uc = loader.getController();
                uc.getTicketInfo(ticketID, source.getText(), destination.getText(),
                        departTime.getText(), arrivalTime.getText(), price.getText());

                Scene registerScene = new Scene(register);

                Stage regWindow = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                regWindow.setScene(registerScene);
                regWindow.show();

            }else{
                String[] priced = price.getText().split(" ");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/AlertBox.fxml"));
                Parent root = loader.load();
                AlertBoxController ab = loader.getController();
                ab.ticketConfirmation("Confirm purchase of " + priced[1] + " for ticket # " + this.ticketID, "from " +this.source.getText(),
                        "to "+ this.destination.getText());
                ab.setABUserName(username);
                ab.setABTicketId(ticketID);
                //Dialog dialog =  new Dialog();
                //dialog.setWidth(600);
                //dialog.setHeight(400);
                //dialog.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
                //dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                //dialog.getDialogPane().setContent(root);
                //dialog.show();
                Scene scene = new Scene(root);
                Stage ticketInfo = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                ticketInfo.setScene(scene);
                ticketInfo.show();

            }

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

    public void displayTicketInfo(int source, int dest, String dTime, String aTime, String flightName){


        try{
            Connection conn = DBConnect.getConnection();

            //Creating prepared Statement to get the flightID
            PreparedStatement pr = conn.prepareStatement(queryString);
            pr.setString(1, flightName);
            pr.setString(2, dTime);
            pr.setString(3, aTime);

            ResultSet resultSet = pr.executeQuery();

            //Creating prepared Statement to get ticketID
            PreparedStatement pr1 = conn.prepareStatement(queryString1);
           // pr1.setInt(1, resultSet.getInt(1));


            ResultSet resultSet1 = pr1.executeQuery();
            ticketID = resultSet1.getInt(1);

            System.out.println("This is the ticket id: " + ticketID);
             pr1.close();
            //Creating prepared statement to get price for next ticket that doesn't
            //have an owner.
            //PreparedStatement pr2 = conn.prepareStatement(queryString2);
           // pr2.setInt(1, ticketID);
            //ResultSet resultSet2 = pr2.executeQuery(queryString2);
           // tPrice = resultSet2.getString(1);


            pr.close();

            conn.close();

            resultSet.close();
        }catch(SQLException err){
            System.err.println(err);
        }

        String src =  getSourceDestName(source);
        String dst =  getSourceDestName(dest);

        this.price.setText("Price: $" + tPrice);
        this.source.setText(src);
        this.destination.setText(dst);
        this.ticketId.setText("Ticket # " + Integer.toString(ticketID));
        this.departTime.setText("Departure: " + dTime);
        this.arrivalTime.setText("Arrival: " + aTime);
        setImages(source,0);
        setImages(dest, 1);


    }


    public String getSourceDestName(int sourceOrDest){

        String name = "";

        switch(sourceOrDest){

            case 0:
                name = "Atlanta\nHartsfield-Jackson Atlanta\nInternational Airport";
                break;
            case 1:
                name = "Los Angeles\nLos Angeles\nInternational Airport";
                break;
            case 2:
                name = "New York\nLaguardia Airport";
                break;
            case  3:
                name = "Miami\nMiami International Airport";
                break;
            case 4:
                name = "Las Vegas\nMcCarran International\nAirport";
                break;
        }

        System.out.println("The source or destination name is: " + name);

        return name;
    }


    public void setImages(int sourceOrDestId, int sourceOrDest){

        switch(sourceOrDestId){


            case 0:
                 Image imgAtl = new Image(getClass().getResource("../../res/GA.jpg").toExternalForm(), true);

                 if(sourceOrDest == 0){
                     this.sourceImg.setPreserveRatio(true);
                     this.sourceImg.setImage(imgAtl);
                 }else{
                     this.destImg.setPreserveRatio(true);
                     this.destImg.setImage(imgAtl);
                 }

                break;
            case 1:
                Image imgLA = new Image(getClass().getResource("../../res/LA.jpg").toExternalForm(), true);

                if(sourceOrDest == 0){
                    this.sourceImg.setPreserveRatio(true);
                    this.sourceImg.setImage(imgLA);
                }else{
                    this.destImg.setPreserveRatio(true);
                    this.destImg.setImage(imgLA);
                }

                break;
            case 2:
                Image imgNYC = new Image(getClass().getResource("../../res/nyc.jpeg").toExternalForm(), true);

                if(sourceOrDest == 0){
                    this.sourceImg.setPreserveRatio(true);
                    this.sourceImg.setImage(imgNYC);
                }else{
                    this.destImg.setPreserveRatio(true);
                    this.destImg.setImage(imgNYC);
                }

                break;
            case  3:
                Image imgMIA = new Image(getClass().getResource("../../res/miami.jpeg").toExternalForm(), true);

                if(sourceOrDest == 0){
                    this.sourceImg.setPreserveRatio(true);
                    this.sourceImg.setImage(imgMIA);
                }else{
                    this.destImg.setPreserveRatio(true);
                    this.destImg.setImage(imgMIA);
                }

                break;
            case 4:
                Image imgLV = new Image(getClass().getResource("../../res/lasVegas.jpeg").toExternalForm(), true);

                if(sourceOrDest == 0){
                    this.sourceImg.setPreserveRatio(true);
                    this.sourceImg.setImage(imgLV);
                }else{
                    this.destImg.setPreserveRatio(true);
                    this.destImg.setImage(imgLV);
                }
                break;
        }

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    public void getTUserName(String username){
        this.isLoggedIn = true;


        this.username = username;
        System.out.println("This si the username from Ticket controller: "+ this.username);


    }


   /* public void getTicketInfo(int id, String source, String dest, String depart, String arrive, String price){
        this.ticketID = id;
        this.tSource = source;
        this.dest = dest;
        this.depart = depart;
        this.arrive = arrive;
        this.tPrice = price;
    }*/
}
