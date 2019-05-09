package main.java.home.controllers;

import com.jfoenix.controls.*;
import main.java.home.DB_Connection.DBConnect;
import main.java.home.DB_Models.Flights;
import main.java.home.DB_Models.Passengers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
//import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
//import main.java.home.DB_Connection.DBConnect;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;


public class AdminController implements Initializable{

    @FXML
    private TextField flightId;
    @FXML
    private TextField flightName;
    @FXML
    private JFXComboBox sourceId;
    @FXML
    private JFXComboBox destId;
    @FXML
    private TextField price;
    @FXML
    private JFXDatePicker departDate;
    @FXML
    private JFXDatePicker arrivalDate;
    @FXML
    private JFXTimePicker departTime;
    @FXML
    private JFXTimePicker arrivalTime;

    @FXML
    private TableView<Flights> flightsTableView;

    @FXML
    private TableColumn<Flights, Integer> flightIdCol;
    @FXML
    private TableColumn<Flights, String> flightNameCol;
    @FXML
    private TableColumn<Flights, Integer> flightSourceIdCol;
    @FXML
    private TableColumn<Flights, Integer> flightDestIdCol;
    @FXML
    private TableColumn<Flights, Date> flightDepartDateCol;
    @FXML
    private TableColumn<Flights, Date> flightArrivalDateCol;
    @FXML
    private TableColumn<Flights, String> flightDepartTimeCol;
    @FXML
    private TableColumn<Flights, String> flightArrivalTimeCol;


    @FXML
    private TableView<Passengers> passengerTableView;
    @FXML
    private TableColumn<Passengers, String> custId;
    @FXML
    private TableColumn<Passengers, String> custFName;
    @FXML
    private TableColumn<Passengers, String> custLName;
    @FXML
    private TableColumn<Passengers, String> custEmail;
    @FXML
    private TableColumn<Passengers, String> custTicketId;

    @FXML
    private JFXDatePicker reportDP;
    @FXML
    private JFXButton reportBtn;
    @FXML
    private Label monthLbl;
    @FXML
    private JFXTextArea reportTA;
    @FXML
    private LineChart<String, Number> reportLC;



    private DBConnect dc;
    //all the data coming from flights class
    private ObservableList<Flights> flightsData;
    private ObservableList<Passengers> passengersData;

    private int completedFlights;
    private int ticketsPurchased;
    private int moneyEarned;
    private static boolean monthlyRep;
    private String[] month;
    private static String weekReport;
    private static LocalDate localDate;

    private String queryString  = "SELECT * FROM Flights";
    private String queryString1  = "SELECT passengerId, fName, lName, email, ticketId FROM passenger";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.dc = new DBConnect();

        sourceId.getItems().addAll("0","1","2","3","4");
        destId.getItems().addAll("0","1","2","3", "4");
        reportBtn.setDisable(true);

        reportDP.valueProperty().addListener((ov, oldVal, newValue)->{
            System.out.println("Value Changed!!!");
            reportBtn.setDisable(false);
        });

        /*monthCB.getItems().addAll("January","February", "March", "April", "May","June",
                "July", "August","September","October","November","December");*/
    }

   /* @FXML
    public void enableReportButton(){
        if(reportDP.getValue() != null){
            System.out.println("Date picker has a value");
            reportBtn.setDisable(false);
        }
        System.out.println("THE DATE PICKER HAS BEEN CLICKED!!");
    }
*/

    @FXML
    public void getReport(){


        /*
        * Checking if a date has been chosen, until a date is chosen the generate report button
        * is disabled
        *
        * */

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/AlertBox.fxml"));
            Parent popup = loader.load();
            AlertBoxController abc = loader.getController();
            abc.reportSpecifications("What type of report\n would you like?", reportDP.getEditor().getText());
            Dialog dialog =  new Dialog();
            dialog.setWidth(600);
            dialog.setHeight(400);
            dialog.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
            dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            dialog.getDialogPane().setContent(popup);
            dialog.showAndWait();

            if(monthlyRep){
                genReport(weekReport);
                monthlyRep = false;
            }else{
                genReport(weekReport);
                System.out.println("WEEKLY WAS SELECTED");
            }
        }catch (IOException err){
            System.err.println("Error: " + err);
        }


   //genReport();

    }

    public void genReport(String weeksReport)
    {

        ArrayList<String> pastFlights = new ArrayList();
        ArrayList<Integer> pastDestinations = new ArrayList<>();
        ArrayList<String> pastTicketPrices = new ArrayList<>();
        ArrayList<String> pastTicketIDs = new ArrayList<>();
        ArrayList<String> pastTicketPrices3 = new ArrayList<>();
        GregorianCalendar gCal = new GregorianCalendar();
        int currentMonth = gCal.get(Calendar.MONTH) + 1;
        int day = gCal.get(Calendar.DATE);
        int currentYear = gCal.get(Calendar.YEAR);

        int atlFlights = 0;
        int laFlights = 0;
        int nyFlights = 0;
        int miaFlights= 0;
        int lvFlights= 0;
        String[] month = null;
        String endMonth = "";
        String reportDate1 = "";
        String reportDate2 = "";

        //System.out.println("This is the date: " + reportDP.getEditor().getText());
       // month = date.split("/");
        if(weeksReport == null) {
            month = reportDP.getEditor().getText().split("/");

               // SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy").parse(reportDP.getEditor().getText());
        //  System.out.println("We're");


            month[1] = "1";
            endMonth = String.valueOf(Integer.parseInt(month[0])+1);
            System.out.println("E   ND MONTH IS: " +endMonth );

            reportDate1 = month[0].concat("/"+month[1]+"/"+month[2]);
            reportDate2 = endMonth.concat("/"+month[1]+"/"+month[2]);

            //weeksReport = null;
        }else{
            month = weeksReport.split("/");
            System.out.println("tHIS IS THE START DAY" + month[1]);


            Date d = java.sql.Date.valueOf(localDate);
            gCal.setTime(d);
            int dayOfWeek = gCal.get(Calendar.DAY_OF_WEEK);

            endMonth =  month[0];

            int selectedDay = Integer.parseInt(month[1]);
            int sunday = selectedDay - (dayOfWeek-(dayOfWeek-1));
            int saturday = sunday + 6;


            reportDate1 = month[0].concat("/"+ String.valueOf(sunday)+"/"+month[2]);
            reportDate2 = month[0].concat("/"+String.valueOf(saturday)+"/"+month[2]);
            System.out.println("The day of the week is: " + dayOfWeek);



        }





        //month[0] = endMonth;
        System.out.println("E   ND MONTH1  IS: " + endMonth );

        String currentDate = Integer.toString(currentMonth).concat("/" + Integer.toString(day)
                + "/" + currentYear);
        System.out.println("The date is: " +  currentDate);
        System.out.println("The report starting date is: " +  reportDate1);
        System.out.println("The report ending date is: " +  reportDate2);






        try{
            //Query string to get flights within the month we want a report of.
            String queryString = "SELECT flightID FROM Flights WHERE arrivalDate BETWEEN ? AND ?";

            //Query string to get prices and number of tickets within the month we want the report of.
            String queryString2 = "SELECT price, ticketID FROM Ticket WHERE ticketID IN (SELECT ticketID FROM Ticket WHERE datePurchased BETWEEN ? AND ?) AND owner IS NOT NULL ";
            String queryDays = "SELECT price, ticketID FROM Ticket WHERE ticketID IN (SELECT ticketID FROM Ticket WHERE pDay BETWEEN ? AND ?) AND owner IS NOT NULL ";
            String queryString3 = "SELECT count(flightID) FROM Flights WHERE destinationID = ? AND  arrivalDate BETWEEN ? AND ?";

            //Query String to get prices of tickets from previous 2 months
            // String queryString4 = "SELECT price FROM Ticket WHERE flightID IN (SELECT flightID FROM Flights WHERE arrivalDate BETWEEN ? AND ?) AND owner IS NOT NULL";

            Connection conn = DBConnect.getConnection();
            PreparedStatement pr1 = conn.prepareStatement(queryString);
            pr1.setString(1,reportDate1);
            pr1.setString(2,reportDate2);

            ResultSet rs = pr1.executeQuery();
            while(rs.next()){
                pastFlights.add(rs.getString(1));


            }


            rs.close();

            //Getting all past ticket prices
           if(monthlyRep){
               PreparedStatement pr2 = conn.prepareStatement(queryString2);



                pr2.setString(1, reportDate1);
                pr2.setString(2, reportDate2);

               ResultSet rs1 =  pr2.executeQuery();
               while(rs1.next()){
                   pastTicketPrices.add(rs1.getString(1));
                   pastTicketIDs.add(rs1.getString(2));
               }

               rs1.close();
           }else{
               PreparedStatement pr2 = conn.prepareStatement(queryDays);
               String[] startDay =  reportDate1.split("/");
               String[] endDay = reportDate2.split("/");


               pr2.setInt(1, Integer.parseInt(startDay[1]));
               pr2.setInt(2, Integer.parseInt(endDay[1]));

               ResultSet rs1 =  pr2.executeQuery();
               while(rs1.next()){
                   pastTicketPrices.add(rs1.getString(1));
                   pastTicketIDs.add(rs1.getString(2));
               }

               rs1.close();
           }


            //Counting flights to destinations
            PreparedStatement pr3 = conn.prepareStatement(queryString3);
            ResultSet rs2;


            for(int i = 0; i < 5; i++){
                pr3.setInt(1,i);
                pr3.setString(2,reportDate1);
                pr3.setString(3, reportDate2);
                rs2 = pr3.executeQuery();

                switch(i){
                    case 0:
                        atlFlights = rs2.getInt(1);

                        System.out.println("The number of flights to Atlanta: " + atlFlights);
                        break;
                    case 1:
                        laFlights = rs2.getInt(1);

                        System.out.println("The number of flights to Los Angeles: " + laFlights);
                        break;
                    case 2:
                        nyFlights = rs2.getInt(1);

                        System.out.println("The number of flights to New York: " + nyFlights);
                        break;
                    case 3:
                        miaFlights = rs2.getInt(1);

                        System.out.println("The number of flights to Miami: " + miaFlights);
                        break;
                    case 4:
                        lvFlights = rs2.getInt(1);

                        System.out.println("The number of flights to Las Vegas: " + lvFlights);
                        break;
                }

            }

            /*PreparedStatement pr4 = conn.prepareStatement(queryString4);
            ResultSet rs3;
            for(int j  = 1; j < 3; j++){
                switch(j){
                    case 1:
                        if(month[0] == "1")
                            month[0] = "12";
                        pr4.setString(1,"12/");
                        pr4.setString(2,);
                        rs3 = pr4.executeQuery();
                        break;
                    case 2:
                        break;
                }



            }*/




            conn.close();




            for(String id : pastFlights){
                System.out.println("Flights that have already landed: " + id);
            }
            for(int i = 0; i < pastTicketPrices.size(); i++){
                System.out.println("Past ticket prices: " + pastTicketPrices.get(i)+ " with ticketID: " + pastTicketIDs.get(i));
            }

        }catch(SQLException err){
            System.err.println("Error: " + err);
        }


        completedFlights = pastFlights.size();
        ticketsPurchased = pastTicketPrices.size();
        int earnedSum = 0;
        for(String price : pastTicketPrices){
            earnedSum = earnedSum + Integer.parseInt(price);
        }
        moneyEarned = earnedSum;



        int reportMonth = Integer.parseInt(month[0]);
        System.out.println("This is the selected month for report: " + reportMonth + " day: " + month[1] + " year: " + month[2]);
        monthLbl.setText(getMonth(reportMonth));

        reportTA.clear();
        reportTA.appendText("\t\t Earnings and Sales \n"+

                "=====================================\n\n" +

                "Number of tickets sold: \t\t\t" + ticketsPurchased + "\n\n"+
                "Completed flights: \t\t\t\t\t"  + completedFlights + "\n\n"+
                "Total Earnings: \t\t\t\t" + "$ " + moneyEarned + "\n\n\n" +

                "\t\t Flights Per Destination \n"+

                "=====================================\n\n" +

                "Atlanta\nHartsfield-Jackson Atlanta\nInternational Airport:\t\t\t\t" + atlFlights + "\n\n"+
                "Los Angeles\nLos Angeles\nInternational Airport:\t\t\t\t" +laFlights + "\n\n" +
                "New York\nLaguardia Airport:\t\t\t\t\t" +nyFlights + "\n\n" +
                "Miami\nMiami International Airport:\t\t\t" +miaFlights + "\n\n" +
                "Las Vegas\nMcCarran International\nAirport:\t\t\t\t\t\t\t" +lvFlights + "\n\n"





        );


        //Setting up our series for the line chart
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.getData().add(new XYChart.Data<>(getMonth(reportMonth), moneyEarned));

        reportLC.getData().add(series1);

        moneyEarned = 0;
        reportDP.setValue(null);
        reportBtn.setDisable(true);


    }

    @FXML
    private void loadFlightData(ActionEvent event) throws SQLException{
        try{

            Connection conn = DBConnect.getConnection();
            this.flightsData = FXCollections.observableArrayList();

            ResultSet resultSet = conn.createStatement().executeQuery(queryString);

            //we're going to run this loop as the loop checks if there's anything in the table;
            while(resultSet.next()){
                this.flightsData.add(new Flights(resultSet.getInt(1),resultSet.getString(2), resultSet.getInt(3),resultSet.getInt(4),
                        resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),resultSet.getString(8)));
            }

            resultSet.close();
            conn.close();
        }catch (SQLException err){
            System.err.println("Error: " + err);
        }

        this.flightIdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        this.flightNameCol.setCellValueFactory(new PropertyValueFactory<>("NAME"));
        this.flightSourceIdCol.setCellValueFactory(new PropertyValueFactory<>("SOURCEID"));
        this.flightDestIdCol.setCellValueFactory(new PropertyValueFactory<>("DESTID"));
        this.flightDepartDateCol.setCellValueFactory(new PropertyValueFactory<>("DEPARTDATE"));
        this.flightDepartTimeCol.setCellValueFactory(new PropertyValueFactory<>("DEPARTTIME"));
        this.flightArrivalDateCol.setCellValueFactory(new PropertyValueFactory<>("ARRIVALDATE"));
        this.flightArrivalTimeCol.setCellValueFactory(new PropertyValueFactory<>("ARRIVALTIME"));

        this.flightsTableView.setItems(null);
        this.flightsTableView.setItems(this.flightsData);

    }

    @FXML
    private void addFlights(ActionEvent event){
        String insertQueryString = "INSERT INTO Flights(flightID, flightName, sourceID, destinationID, arrivalDate, departureDate, arrivalTime, departureTime) VALUES (?,?,?,?,?,?,?,?)";
        String insertQueryString1 = "INSERT INTO Ticket(flightID, ticketID, sourceID, destinationID, price) VALUES (?,?,?,?,?)";

        if(formValidator()) {
            try {

                ///Adding flights to database
                Connection conn = DBConnect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(insertQueryString);

                String[] departT = this.departTime.getEditor().getText().split("(:|\\s)");
                String[] arrivalT = this.arrivalTime.getEditor().getText().split("(:|\\s)");

                String departT1 = departT[0].toString().concat(departT[1].toString());
                String arrivalT1 = arrivalT[0].toString().concat(arrivalT[1].toString());
                stmt.setString(1, this.flightId.getText());
                stmt.setString(2, this.flightName.getText());
                stmt.setString(3, this.sourceId.getValue().toString());
                stmt.setString(4, this.destId.getValue().toString());
                stmt.setString(5, this.arrivalDate.getEditor().getText());
                stmt.setString(6, this.departDate.getEditor().getText());
                stmt.setString(7, this.arrivalTime.getEditor().getText());
                stmt.setString(8, this.departTime.getEditor().getText());


                System.out.println("Departure Time: " + departT1);

                System.out.println("Arrival Time: " + arrivalT1);

                stmt.execute();
                stmt.close();


                ///Adding tickets to the database

                PreparedStatement stmt1 = conn.prepareStatement(insertQueryString1);

                stmt1.setString(1, this.flightId.getText());
                stmt1.setString(3, this.sourceId.getValue().toString());
                stmt1.setString(4, this.destId.getValue().toString());
                stmt1.setString(5, this.price.getText());


                for (int i = 0; i < 5; i++) {
                    stmt1.setString(2, this.flightId.getText() + "" + i);
                    stmt1.execute();

                }
                conn.close();


            } catch (SQLException err) {
                err.printStackTrace();
            }
        }
    }

    @FXML
    private void clearFields(ActionEvent event){
        this.flightId.setText("");
        this.flightName.setText("");
        this.sourceId.setValue(null);
        this.destId.setValue(null);
        this.arrivalTime.setValue(null);
        this.departTime.setValue(null);
        this.arrivalDate.setValue(null);
        this.departDate.setValue(null);
    }

    @FXML
    private void loadPassengersData(ActionEvent event) throws SQLException{
        try{

            Connection conn = DBConnect.getConnection();
            this.passengersData = FXCollections.observableArrayList();

            ResultSet resultSet = conn.createStatement().executeQuery(queryString1);

            //we're going to run this loop as the loop checks if there's anything in the table;
            //resultSet is a tabular form of the data in the database, getNext method moves the cursor to the next row.
            //then we add whats in each resultSet row  to the observable list called passengersData;
            while(resultSet.next()){
                this.passengersData.add(new Passengers(resultSet.getString(1),resultSet.getString(2), resultSet.getString(3),resultSet.getString(4),
                        null, resultSet.getString(5)));
            }

            conn.close();
        }catch (SQLException err){
            System.err.println("Error: " + err);
        }

        this.custId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.custFName.setCellValueFactory(new PropertyValueFactory<>("fName"));
        this.custLName.setCellValueFactory(new PropertyValueFactory<>("lName"));
        this.custEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        //this.custTicketId.setCellValueFactory(new PropertyValueFactory<>("ticketId"));


        this.passengerTableView.setItems(null);
        this.passengerTableView.setItems(this.passengersData);



    }

    @FXML
    private void deteleData(ActionEvent e){

        String deleteQuery = "DELETE FROM Flights WHERE flightID = ?";

        try{
            Connection conn = DBConnect.getConnection();

            PreparedStatement stmt = conn.prepareStatement(deleteQuery);


            stmt.setString(1, this.flightId.getText());
            stmt.execute();

            conn.close();

        }catch(SQLException err){

            err.printStackTrace();
        }

    }

    @FXML
    private void logOut(ActionEvent actionEvent) throws IOException{
        Parent splashPage = FXMLLoader.load(getClass().getResource("../fxml/SplashPage.fxml"));
        Scene splash = new Scene(splashPage);

        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.close();

        window.setScene(splash);


        window.show();
        System.out.println("Log Out button clicked");


    }


    @FXML
    private void addPassenger(ActionEvent actionEvent) throws IOException{
        Parent addPass =  FXMLLoader.load(getClass().getResource("../fxml/BookPassenger.fxml"));
        Scene bookPass = new Scene(addPass);

        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.close();
        window.setScene(bookPass);
        window.show();
        System.out.print("Add Passenger button clicked");

    }

    public boolean formValidator() {


        String[] cardName;
        String fNameCard =" ";
        String lNameCard =" ";
        String dpDate[] = departDate.getEditor().getText().split("/");
        String arDate[] = arrivalDate.getEditor().getText().split("/");
        String dpTime[] = departTime.getEditor().getText().split("[:\\s]");
        String arTime[] = arrivalTime.getEditor().getText().split("[:\\s]");

        int dDay =0;
        int dMonth=0;
        int dYear = 0;
        int aDay=0;
        int aMonth=0;
        int aYear=0;
        int dHour = 0;
        int dMinute = 0;
        int aHour = 0;
        int aMinute = 0;
        String dAMPM = "";
        String aAMPM ="";

        if(dpTime[0] != null){
            dHour = Integer.parseInt(dpDate[0]);
            System.out.println("Depart Hour: " + dHour);
        }
        if(dpTime[1] != null){
            dMinute = Integer.parseInt(dpDate[1]);
            System.out.println("Depart Minute: " + dMinute);
        }
        if(dpTime[2] != null){
            dAMPM = dpTime[2];
            System.out.println("Depart Morning or Night: " + dAMPM);
        }

        if(arTime[0] != null){
            aHour = Integer.parseInt(dpDate[0]);
            System.out.println("Arrive Hour: " + aHour);
        }
        if(arTime[1] != null){
            aMinute = Integer.parseInt(dpDate[1]);
            System.out.println("Arrive Minute: " + aMinute);
        }
        if(arTime[2] != null){
            dDay = Integer.parseInt(dpDate[1]);
            System.out.println("Arrive morning or night: " + aAMPM);
        }



        if(dpDate[0] != null){
            dMonth = Integer.parseInt(dpDate[0]);
            System.out.println("Depart Month: " + dMonth);
        }
        if(dpDate[1] != null){
            dDay = Integer.parseInt(dpDate[1]);
            System.out.println("Depart Day: " + dDay);
        }
        if(dpDate[2] != null){
            dYear= Integer.parseInt(dpDate[2]);
            System.out.println("Depart year: " + dYear);
        }
        if(arDate[0] != null){
            aMonth = Integer.parseInt(arDate[0]);
            System.out.println("Arrival Month: " + dMonth);
        }
        if(arDate[1] != null){
            aDay = Integer.parseInt(arDate[1]);
            System.out.println("Arrival Day: " + aDay);
        }
        if(arDate[2] != null){
            aYear = Integer.parseInt(arDate[2]);
            System.out.println("Arrival Year: " + aYear);
        }




        if(flightId.getText() == null || flightId.getText().trim().isEmpty() ||
                flightName.getText() == null || flightName.getText().trim().isEmpty() ||
                price.getText() == null || price.getText().trim().isEmpty() ||
                sourceId.getValue() == null || destId.getValue() == null ||
                departDate.getEditor().getText() == null || departDate.getEditor().getText().trim().isEmpty() ||
                arrivalDate.getEditor().getText() == null || arrivalDate.getEditor().getText().trim().isEmpty() ||
                departTime.getEditor().getText() == null || arrivalTime.getEditor().getText() == null){

                showAlert("Empty Fields","Please fill in all fields");


            return false;
        }
        if(dYear == aYear){
            if(aMonth < dMonth){
                showAlert("Incorrect Months", "Arrival month cannot be before departure month");
                return false;
            }
        }
        if(dMonth == aMonth){
            if(aDay < dDay){
                showAlert("Incorrect days", "Arrival day cannot be before departure day");
                return false;
            }
        }
        if(dMonth == aMonth){
            if(aDay < dDay){
                showAlert("Incorrect days", "Arrival day cannot be before departure day");
                return false;
            }
        }
        if(dAMPM.equals("AM")){
            showAlert("Incorrect Time", "S.E.G. Air Lines flights depart only after 12:00 PM");
            return false;
        }
        if(dHour == aHour && dMinute == aMinute){
           if(dAMPM.equals(aAMPM)){
               showAlert("Incorrect Time", "A Flight can not depart and arrive at same exact time");
               return false;
           }
        }


        return true;

    }


    public void showAlert(String message1, String message2){

        Alert fieldAlert = new Alert(Alert.AlertType.WARNING);
        fieldAlert.setTitle(message1);
        fieldAlert.setHeaderText(null);
        fieldAlert.setContentText(message2);
        fieldAlert.showAndWait();
    }


    public String getMonth(int monthNum){

        String month = " ";

        switch(monthNum){
            case 1:
                month = "January";
                break;
            case 2:
                month = "February";
                break;
            case 3:
                month = "March";
                break;
            case  4:
                month = "April";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "June";
                break;
            case 7:
                month = "July";
                break;
            case 8:
                month = "August";
                break;
            case 9:
                month = "September";
                break;
            case 10:
                month = "October";
                break;
            case 11:
                month = "November";
                break;
            case 12:
                month = "December";
                break;
        }

        System.out.println("The month of report is: " + month);

        return month;
    }

    public void setMonthlyRep(boolean monthlyRep, String weekReport, LocalDate localDate){
        this.weekReport = weekReport;
        this.monthlyRep = monthlyRep;
        this.localDate = localDate;
        System.out.println("Monthly rep boolean is : " + this.monthlyRep);
        System.out.println("Weekly rep String is : " + weekReport);

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



    //Below Promotions are applied


    @FXML
    public void fFPromo() {

        ArrayList<String> fFlierPassengers = new ArrayList();
        GregorianCalendar gCal = new GregorianCalendar();

        int currentMonth = gCal.get(Calendar.MONTH) + 1;
        int day = gCal.get(Calendar.DATE);

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Frequent Flier");
        alert.setHeaderText(null);
        alert.setContentText("Frequent Flyer Promotion Enabled For Following Month");
        alert.showAndWait();

        try{
            //String fFlier = "SELECT owner, count(*) FROM Ticket GROUP BY owner HAVING count(*) > 2 AND owner IS NOT NULL ";
            //String applyPromo = "UPDATE passenger SET ffPromo = ? WHERE userName IN (SELECT owner, count(*) FROM Ticket GROUP BY owner HAVING count(*) > 2 AND owner IS NOT NULL )";
            String fFlier = "SELECT owner FROM Ticket GROUP BY owner HAVING count(*) > 2 AND owner IS NOT NULL";
            String applyPromo = "UPDATE passenger SET ffPromo = ? WHERE userName IN (SELECT owner FROM Ticket GROUP BY owner HAVING count(*) > 2 AND owner IS NOT NULL )";
            String addPromo  = "INSERT INTO Promotion(promoName, startMonth, startDay, endMonth, endDay) VALUES(?,?,?,?,?)";
            Connection conn = DBConnect.getConnection();
            PreparedStatement pr = conn.prepareStatement(fFlier);
            ResultSet rs = pr.executeQuery();

            while(rs.next()){
                fFlierPassengers.add(rs.getString(1));
            }

            PreparedStatement pr1 = conn.prepareStatement(applyPromo);
            pr1.setInt(1, 1);
            pr1.execute();


            PreparedStatement pr2 = conn.prepareStatement(addPromo);
            pr2.setString(1, "FrequentFlier");
            pr2.setInt(2, currentMonth);
            pr2.setInt(3, day);
            if(currentMonth == 12){
                pr2.setInt(4, 1);
            }else{
                pr2.setInt(4,currentMonth+1);
            }
            pr2.setInt(5, day);
            pr2.execute();

            conn.close();
        }catch(SQLException err){
            System.out.println("Error: " + err);
        }

        for(String owners: fFlierPassengers){
            System.out.println("Owners who qualify for Frequent Flier: " + owners);
        }

    }
    @FXML
    public void fBPromo() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Free Bag");
        alert.setHeaderText(null);
        alert.setContentText("Free Bag Promotion Enabled For Following Month");
        alert.showAndWait();

    }

    @FXML
    public void registerPassenger(ActionEvent actionEvent) throws IOException{
        FXMLLoader loader =  new FXMLLoader(getClass().getResource("../fxml/UserRegistration.fxml"));
        Parent register = loader.load();
        UserRegistrationController uc = loader.getController();
        uc.setAdmReg(true);

        Scene registerScene = new Scene(register);

        Stage regWindow = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        regWindow.setScene(registerScene);
        regWindow.show();
    }
}
