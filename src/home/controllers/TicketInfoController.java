package home.controllers;

import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
        @FXML
        private JFXComboBox carbCB;
        @FXML
        private JFXComboBox mealCB;
        @FXML
        private JFXComboBox sideCB;
        @FXML
        private VBox mealVBox;


        private int ticketID;
        private boolean isLoggedIn;
        private String username;
        private String fName;
        private String lName;
        private String tPrice;
        private int flightID;

    private JFXComboBox mainDish;




      private String queryString = "SELECT flightID FROM Flights WHERE flightName= ? AND departureTime = ? AND arrivalTime = ?";
      private String queryString1 = "SELECT MIN(ticketID) FROM Ticket WHERE owner IS NULL AND flightID = ?";
      private String queryString2 =  "SELECT price FROM Ticket WHERE ticketID = ?";
      private String insertString = "INSERT INTO Meals(mealID, mealType, mainMeat, mainCarb, side) VALUES (?,?,?,?,?)";

    //WHERE owner IS null
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
                uc.mealInfo(mealCB.getValue().toString(),mainDish.getValue().toString(),
                        carbCB.getValue().toString(), sideCB.getValue().toString(), flightID);

                Scene registerScene = new Scene(register);

                Stage regWindow = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                regWindow.setScene(registerScene);
                regWindow.show();


            }else{
                //
                String[] priced = price.getText().split(" ");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/AlertBox.fxml"));
                Parent root = loader.load();
                AlertBoxController ab = loader.getController();
                ab.ticketConfirmation("Confirm purchase of " + priced[1] + " for ticket # " + this.ticketID
                        + "\n Confirmation # c" + this.ticketID,
                        "from " +this.source.getText(), "to "+ this.destination.getText());
                ab.mealInfo( mealCB.getValue().toString(), mainDish.getValue().toString(), carbCB.getValue().toString(),
                        sideCB.getValue().toString(), flightID);
/*
                 System.out.println("Below is from the tickInfocontroller: ");

                System.out.println("This is mealType: " + mealCB.getEditor().getText());
                System.out.println("This is mainMeat " + mainDish.getEditor().getText());
                System.out.println("This is mainCarb: " + carbCB.getEditor().getText());
                System.out.println("This is side: " + sideCB.getEditor().getText());
                System.out.println("This is flightId: " + flightID);*/

                ab.setABUserName(username, fName, lName);
                ab.setABTicketId(ticketID);

                //The below code is if wanted to trigger a dialog

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
         FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/BookFlights.fxml"));
        Parent prior = loader.load();
        BookFlightsController bfc = loader.getController();
        bfc.getBUserName(username, fName, lName);
        Scene priorScene = new Scene(prior);


        Stage window = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(priorScene);
        window.show();

        System.out.println("Return button clicked");

    }

    public void displayTicketInfo(int source, int dest, String dTime, String aTime, String flightName){
        System.out.println(flightName);

        try{
            Connection conn = DBConnect.getConnection();

            //Creating prepared Statement to get the flightID
            PreparedStatement pr = conn.prepareStatement(queryString);
            pr.setString(1, flightName);
            pr.setString(2, dTime);
            pr.setString(3, aTime);

            ResultSet resultSet = pr.executeQuery();
             flightID = resultSet.getInt(1);
            //Creating prepared Statement to get ticketID
            PreparedStatement pr1 = conn.prepareStatement(queryString1);
             pr1.setInt(1, flightID);


            ResultSet resultSet1 = pr1.executeQuery();
            ticketID = resultSet1.getInt(1);

            System.out.println("This is the ticket id: ");



            //pr1.close();

            //Creating prepared statement to get price for next ticket that doesn't
            //have an owner.

            PreparedStatement pr2 = conn.prepareStatement(queryString2);
            pr2.setInt(1, ticketID);
            ResultSet resultSet2 = pr2.executeQuery();
            tPrice = resultSet2.getString(1);









            //pr.close();

            conn.close();

            //resultSet.close();
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



        mealCB.getItems().addAll("Standard", "Vegetarian", "Vegan");

        carbCB.getItems().addAll("Mashed Potatoes & Gravy", "Creamy Rice", "Pasta");

        sideCB.getItems().addAll("Mixed Veggies", "Classic Fries", "Lush Salad");

        mealCB.getSelectionModel().selectedIndexProperty().addListener((ob, oldVal, newVal) ->{
            addComboBox(mealCB.getValue().toString());
            System.out.println("Combobox changed");
        });


    }

    public void getTUserName(String username, String fName, String lName){
        this.isLoggedIn = true;


        this.username = username;
        this.fName = fName;
        this.lName =lName;
        System.out.println("This si the username from Ticket controller: "+ this.username);
        System.out.println("This si the username from Ticket controller: "+ this.fName);
        System.out.println("This si the username from Ticket controller: "+ this.lName);


    }

    public void addComboBox(String meal){
        System.out.println("Entered the addComboBox method, String passed in: " + meal);


        switch(meal){
            case "Standard":

                if(mealVBox.getChildren() != null) {
                    mealVBox.getChildren().remove(mainDish);
                }
                mainDish = new JFXComboBox();
                mainDish.getItems().addAll("Beef", "Chicken", "Pork");
                mainDish.setValue("Select Dish");
                mealVBox.getChildren().add(mainDish);
                break;
            case "Vegetarian":
                if(mealVBox.getChildren() != null) {
                    mealVBox.getChildren().remove(mainDish);
                }
                mainDish = new JFXComboBox();
                mainDish.getItems().addAll("Morningstar Farms", "Eggplant", "Tofu");
                mainDish.setValue("Select Dish");
                mealVBox.getChildren().add(mainDish);
                break;
            case "Vegan":
                if(mealVBox.getChildren() != null) {
                    mealVBox.getChildren().remove(mainDish);
                }
                mainDish = new JFXComboBox();
                mainDish.getItems().addAll("Tofu", "Eggplant", "Tempeh");
                mainDish.setValue("Select Dish");
                mealVBox.getChildren().add(mainDish);
                break;
        }

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
