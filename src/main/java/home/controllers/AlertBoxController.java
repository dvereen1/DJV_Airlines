package main.java.home.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import main.java.home.DB_Connection.DBConnect;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.sqlite.core.DB;



import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

public class AlertBoxController implements Initializable {

    @FXML
    private  Label label;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Pane printablePane;
    @FXML
    private Pane mainBtnPane;
    @FXML
    private Pane mainBtnPane1;
    @FXML
    private AnchorPane ABPane;
    @FXML
    private JFXTextArea printTextArea;
    @FXML
    private JFXButton cBtn;
    @FXML
    private JFXButton pBtn;

    @FXML
    private JFXButton weekBtn;
    @FXML
    private JFXButton monthBtn;
    @FXML
    private JFXDatePicker weekDP;
    @FXML
    private Label insLabel;

    @FXML
    private JFXButton denyBtn;

    private boolean doReport;
    private boolean isLoggedIn;
    private String username;
    private String mealType;
    private String mainMeat;
    private String mainCarb;
    private String side;
    private int flightId;
    private int ticketId;
    private String fName;
    private String lName;
    private String[] days = {"Sunday", "Monday", "Tuesday", "Thursday", "Friday", "Saturday"};
    private String date;
    private int baggage;
    private String price;

    /**
     * The below method handles the printing of tickets
     * @param event
     */
    @FXML public void printIt(ActionEvent event){

        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        Node  toPrint = printTextArea;
        Node mightPrint = printablePane;

        PrinterJob job = PrinterJob.createPrinterJob();

        if(job == null){
            return;
        }

        boolean proceed = job.showPageSetupDialog(window);

        if(proceed){
            boolean printed = job.printPage(mightPrint);

            if(printed){
                job.endJob();
            }
        }

    }


    @FXML
    public void closeOK(ActionEvent event) throws IOException{

        GregorianCalendar gCal = new GregorianCalendar();
        int currentMonth = gCal.get(Calendar.MONTH) + 1;
        int day = gCal.get(Calendar.DATE);
      //  String dayStr = gCal.get(Calendar.DAY_OF_WEEK);
        int currentYear = gCal.get(Calendar.YEAR);

        String currentDate = Integer.toString(currentMonth).concat("/" + Integer.toString(day)
                + "/" + currentYear);


        try{


            Connection conn = DBConnect.getConnection();

            String queryString = "UPDATE Ticket SET owner = ?, price = ?, datePurchased = ?, pDay = ?, pMonth = ?, pYear = ?, bags = ? WHERE ticketID = ?";
            String insertString = "INSERT INTO Meals(mealID, mealType, mainMeat, mainCarb, side) VALUES (?,?,?,?,?)";

            //Setting values into meal table
            String flightIDString = Integer.toString(flightId);
            String ticketIDString = Integer.toString(ticketId);
            String flightTicketID = flightIDString.concat(ticketIDString);
            int mealId = Integer.parseInt(flightTicketID);

            //Creating prepared statement to save meal options into database
            PreparedStatement pr3 = conn.prepareStatement(insertString);
            pr3.setInt(1,  mealId);
            pr3.setString(2, mealType);
            pr3.setString(3, mainMeat);
            pr3.setString(4, mainCarb);
            pr3.setString(5, side);

            pr3.execute();


            //updating ownership of tickets
            PreparedStatement pr = conn.prepareStatement(queryString);
            pr.setString(1, username);
            pr.setString(2, price);
            pr.setString(3, currentDate);
            pr.setInt(4, day);
            pr.setInt(5, currentMonth);
            pr.setInt(6, currentYear);
            pr.setInt(7, baggage);
            pr.setInt(8, ticketId);
            pr.execute();
            conn.close();

        }catch(SQLException err){
            System.err.println("Error: " + err);
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/YourFlights.fxml"));
        Parent root = loader.load();
        YourFlightsController yfc = loader.getController();
        yfc.getYFUserName(username, fName, lName);

        Scene scene = new Scene(root);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void closeNo(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/BookFlights.fxml"));
        Parent root = loader.load();
        BookFlightsController bfc = loader.getController();
        //System.out.println("This is the username from closeNo method: " + username);
        bfc.getBUserName(username, fName, lName);

        Scene scene = new Scene(root);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void displayText(String message){
         label1.setMaxWidth(200);
         label1.setWrapText(true);
         label1.setText(message);

    }

    public void reportSpecifications(String message, String date){
         doReport = true;
         weekBtn.setDisable(true);
         this.date = date;

         label.setText(message);
         ABPane.getChildren().remove(mainBtnPane);
         ABPane.getChildren().add(mainBtnPane1);
         ABPane.setBottomAnchor(mainBtnPane1,1.0);


    }

    @FXML
    public void genWeeklyRe()throws IOException{
        System.out.println("Weekly report chosen");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Admin.fxml"));
        loader.load();
        AdminController adm = loader.getController();
        adm.setMonthlyRep(false, weekDP.getEditor().getText(), weekDP.getValue());

        Stage stage = (Stage) weekBtn.getScene().getWindow();
        stage.close();

    }
    @FXML
    public void genMonthlyRe() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Admin.fxml"));
        loader.load();
        AdminController adm = loader.getController();
        adm.setMonthlyRep(true, null, null);


       // adm.getSourceDestName(1);
        System.out.println("Monthly report chosen");
        Stage stage = (Stage) monthBtn.getScene().getWindow();
        stage.close();
       // adm.genReport(date);


    }

    public void ticketConfirmation(String m1, String m2, String m3 ){


        label.setMaxWidth(200);
        label.setWrapText(true);
        label.setText(m1);

        label2.setMaxWidth(200);
        label2.setWrapText(true);
        label2.setText(m3);

        label1.setMaxWidth(200);
        label1.setWrapText(true);
        label1.setText(m2);

        //Label labels = new Label(m1);
        //Label labels2 = new Label(m2);



    }

    public void mealInfo(String mealType,
                         String mainMeat, String mainCarb, String side, int flightId){


        this.mealType = mealType;
        this.mainMeat = mainMeat;
        this.mainCarb = mainCarb;
        this.side = side;
        this.flightId = flightId;
       // this.baggage = baggage;

        System.out.println("This is mealType: " + mealType);
        System.out.println("This is mainMeat " + mainMeat);
        System.out.println("This is mainCarb: " + mainCarb);
        System.out.println("This is side: " + side);
        System.out.println("This is flightId: " + flightId);



    }

    public void setBaggage(int baggage){
        this.baggage = baggage;
    }

    public void setABUserName(String username, String fName, String lName){
        this.isLoggedIn = true;


        this.username = username;
        this.fName = fName;
        this.lName =lName;
        System.out.println("This si the username from Ticket controller: "+ this.username);
        System.out.println("This si the username from Ticket controller: "+ this.fName);
        System.out.println("This si the username from Ticket controller: "+ this.lName);



    }

    public void setABTicketId(int ticketId){
        this.ticketId = ticketId;
    }

    public void setPrice(String price){

        this.price = price.split("\\$")[1];
        System.out.println("This is the updated price that will be going into database" + this.price);
    }








    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //mainBtnPane1.getChildren().removeAll(weekBtn, monthBtn, weekDP,insLabel);
        printablePane.getChildren().remove(mainBtnPane1);
       // ABPane.getChildren().remove(mainBtnPane);

        weekDP.valueProperty().addListener((ov, oldVal, newValue)->{
            System.out.println("Value Changed!!!");
            weekBtn.setDisable(false);
        });

    }
}
